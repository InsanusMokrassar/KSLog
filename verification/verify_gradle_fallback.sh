#!/usr/bin/env bash
set -euo pipefail

root_dir=$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)
base_commit=${KSLOG_MIGRATION_BASE_COMMIT:-0b9fb30862c7319bb228039ee33eef9a0ddef95c}
work_dir=$(mktemp -d /tmp/kslog-gradle-verifier.XXXXXX)

cleanup() {
    if [[ "${KSLOG_KEEP_VERIFICATION_TEMP:-false}" == "true" ]]; then
        printf 'verification workspace retained at %s\n' "$work_dir"
    else
        rm -rf -- "$work_dir"
    fi
}
trap cleanup EXIT

for command_name in git tar python3; do
    command -v "$command_name" >/dev/null || {
        printf 'required command is unavailable: %s\n' "$command_name" >&2
        exit 1
    }
done

git -C "$root_dir" cat-file -e "$base_commit^{commit}"

baseline_dir="$work_dir/baseline"
baseline_repo="$work_dir/baseline-m2"
current_repo="$work_dir/current-m2"
gradle_home="$work_dir/gradle-home"
gnupg_home="$work_dir/gnupg"
archive="$work_dir/baseline.tar"
mkdir -p "$baseline_dir" "$baseline_repo" "$current_repo" "$gradle_home" "$gnupg_home"
chmod 700 "$gnupg_home"

git -C "$root_dir" archive --format=tar --output="$archive" "$base_commit"
tar -xf "$archive" -C "$baseline_dir"

cache_source=${KSLOG_GRADLE_CACHE_SOURCE:-${GRADLE_USER_HOME:-${HOME}/.gradle}}
for cache_entry in caches wrapper; do
    if [[ -d "$cache_source/$cache_entry" ]]; then
        ln -s "$cache_source/$cache_entry" "$gradle_home/$cache_entry"
    fi
done

if [[ -e "$gradle_home/gradle.properties" || -e "$gradle_home/init.d" ]]; then
    printf 'isolated Gradle home unexpectedly contains user configuration\n' >&2
    exit 1
fi

run_publication() {
    local project_dir=$1
    local repository_dir=$2
    local log_file=$3

    (
        cd "$project_dir"
        unset \
            GITHUB_RELEASE_TOKEN GH_TOKEN GITHUB_TOKEN \
            INMONEXUS_USER INMONEXUS_PASSWORD \
            SONATYPE_USER SONATYPE_PASSWORD PUBLISHING_TYPE \
            KOTLIN_TOOLCHAIN_SIGNING_KEY KOTLIN_TOOLCHAIN_SIGNING_KEY_PASSPHRASE \
            GRADLE_OPTS JAVA_TOOL_OPTIONS _JAVA_OPTIONS
        export GRADLE_USER_HOME="$gradle_home"
        export GNUPGHOME="$gnupg_home"
        ./gradlew --no-daemon \
            -Dmaven.repo.local="$repository_dir" \
            clean publishToMavenLocal
    ) | tee "$log_file"

    if find "$repository_dir" -type f -name '*.asc' -print -quit | grep -q .; then
        printf 'isolated publication unexpectedly produced signatures: %s\n' "$repository_dir" >&2
        exit 1
    fi
    if grep -Eq '^> Task :sign[A-Z]' "$log_file"; then
        printf 'isolated publication unexpectedly executed a signing task\n' >&2
        exit 1
    fi
}

printf 'Publishing untouched baseline %s to an isolated repository\n' "$base_commit"
run_publication "$baseline_dir" "$baseline_repo" "$work_dir/baseline-publish.log"

printf 'Publishing post-move Gradle fallback to an isolated repository\n'
run_publication "$root_dir" "$current_repo" "$work_dir/current-publish.log"

python3 "$root_dir/verification/verify_gradle_publication.py" \
    --project-root "$root_dir" \
    --base-commit "$base_commit" \
    --baseline-repository "$baseline_repo" \
    --current-repository "$current_repo"

printf 'Compiling the committed consumer probe against the post-move publication\n'
(
    unset \
        GITHUB_RELEASE_TOKEN GH_TOKEN GITHUB_TOKEN \
        INMONEXUS_USER INMONEXUS_PASSWORD \
        SONATYPE_USER SONATYPE_PASSWORD PUBLISHING_TYPE \
        KOTLIN_TOOLCHAIN_SIGNING_KEY KOTLIN_TOOLCHAIN_SIGNING_KEY_PASSPHRASE \
        GRADLE_OPTS JAVA_TOOL_OPTIONS _JAVA_OPTIONS
    export GRADLE_USER_HOME="$gradle_home"
    export GNUPGHOME="$gnupg_home"
    export KSLOG_TEST_REPOSITORY="$current_repo"
    export KSLOG_CONSUMER_BUILD_DIR="$work_dir/consumer-build"
    "$root_dir/gradlew" --no-daemon \
        --project-dir "$root_dir/verification/consumer" \
        --project-cache-dir "$work_dir/consumer-project-cache" \
        assemble
) | tee "$work_dir/consumer.log"

printf 'Gradle fallback verification: PASS baseline=%s primaryFiles=46 consumer=PASS\n' "$base_commit"
