# Developing KSLog

## Prerequisites

- JDK 17
- Android SDK platform 37 and build tools 37.0.0
  (`sdkmanager` package IDs: `platforms;android-37.0` and `build-tools;37.0.0`;
  the library's `compileSdk` remains 37)
- Git
- Bash and Python 3 for the committed compatibility verifiers

The checked-in `kotlin` and `kotlin.bat` wrappers download the pinned Kotlin
Toolchain distribution into the normal user cache. No global Toolchain install
is required.

## Build and test

```shell
./kotlin show modules
./kotlin clean
./kotlin build
./kotlin check
```

Build one supported platform with `./kotlin build -p <platform>`, where the
declared platforms are `android`, `js`, `jvm`, `linuxArm64`, `linuxX64`,
`mingwX64`, and `wasmJs`. Run the real JVM tests directly with:

```shell
./kotlin test -p jvm
```

Kotlin Toolchain 0.13.0 does not expose `linuxArm32Hfp`. Until that target and
the existing `kslog-wasm-js` artifact name can be represented, the Gradle build
remains the full-contract fallback:

```shell
./gradlew --no-daemon build
./gradlew --no-daemon compileKotlinLinuxArm32Hfp
```

Do not remove the Gradle path merely because the supported Toolchain targets
compile. See `MIGRATION_REPORT.md` for the exact capability gaps and validation
matrix.

Run the release/version preflight independently with:

```shell
python3 verification/check_release_consistency.py
bash verification/test_changelog_parser.sh
```

CI and the explicit GitHub release command run the same consistency check.

## Documentation

```shell
./kotlin build -m kslog -p js -p wasmJs
./kotlin do dokkaHtml
./kotlin do dokkaGfm
```

The outputs are `build/dokka/html` and `build/dokka/gfm`. CI only deploys HTML
documentation from `master`; pull requests and other branch pushes never deploy
documentation. Toolchain 0.13.0 exposes only the JVM module classpath to local
plugins, so the platform build first materializes the transformed Common plus
matching JS and WasmJS standard-library KLIBs in the Toolchain cache. The Dokka
plugin discovers those KLIBs from the resolved JVM stdlib version and fails with
the prebuild command above if any one is missing.

The legacy Gradle-only `DOKKA_PATH` property/environment override is not
implemented by the Toolchain plugin. Toolchain documentation is deliberately
restricted to generated directories under `build/dokka` so cleanup cannot
target an arbitrary external path. Use the retained Gradle path when that
legacy override is required.

## Local publication and compatibility testing

Publication is enabled for the KSLog module, while the two local build-plugin
modules are intentionally not publishable. Select the library module explicitly:

```shell
isolated_repo="$(mktemp -d)"
KOTLIN_CLI_JAVA_OPTIONS="-Dmaven.repo.local=$isolated_repo" \
  KOTLIN_TOOLCHAIN_SIGNING_KEY="$(gpg --armor --export-secret-keys YOUR_KEY_ID)" \
  ./kotlin publish -m kslog mavenLocal
```

Use a throwaway key for local validation. Never use the command above without
the isolated Maven path for version 2.1.0, because it would overwrite the normal
local copy of that released version.

The retained Gradle publication can be checked without credentials or signing
keys against an archive of the exact migration base:

```shell
bash verification/verify_gradle_fallback.sh
```

The verifier creates isolated Gradle user homes and Maven repositories, checks
the baseline/current 46-file publication inventories, source JARs, required POM
metadata, source bytes, JVM/Android public method and field signatures, module
identities, and compiles the committed consumer probe. It never writes to the
normal Maven local repository.

## Remote publication and release

The validation workflow used for pushes and pull requests never publishes
packages or creates tags/releases. The separate `master`-only KDocs workflow
intentionally deploys generated HTML to the `kdocs` branch after a read-only
generation job succeeds. Toolchain remote package publication is disabled in
the resolved model: the module exposes only `mavenLocal`, has no Inmo Nexus
repository/credentials, and does not enable Maven Central. `mavenLocal` is not
inherently isolated; without the `-Dmaven.repo.local=...` JVM override shown
above it writes to the user's normal Maven Local repository. There is no
configured Toolchain destination that uploads packages remotely.

Do not re-enable Toolchain Nexus or Central publication until both the missing
Linux ARM32 target and the `kslog-wasm-js` coordinate have compatible
implementations and the complete publication diff has been reviewed again.

The retained Gradle build is the only full-target remote publication path. Its
existing release tasks remain explicit maintainer operations and are not run by
CI:

```shell
INMONEXUS_USER=... INMONEXUS_PASSWORD=... \
  ./gradlew --no-daemon publishAllPublicationsToInmoNexusRepository

SONATYPE_USER=... SONATYPE_PASSWORD=... \
  ./gradlew --no-daemon publishAllPublicationsToSonatypeRepository
```

Those commands require the existing Gradle/GPG release configuration and cause
real uploads. They are documented only to preserve the fallback release
contract; do not run them as migration validation.

Preview the GitHub release payload without network mutation:

```shell
KSLOG_GITHUB_RELEASE_DRY_RUN=true ./kotlin do githubRelease
```

A real release requires `GITHUB_RELEASE_TOKEN`, `GH_TOKEN`, or `GITHUB_TOKEN`
and an explicit `./kotlin do githubRelease` invocation. The command first runs
the release/version consistency preflight, then delegates to the standard
`gh release create` CLI. It may create the remote tag when that tag is absent;
it does not alter local git state. Validation CI never invokes it.
