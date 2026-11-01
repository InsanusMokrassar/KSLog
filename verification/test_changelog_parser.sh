#!/usr/bin/env bash
set -euo pipefail

root_dir=$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)
fixture_dir=$(mktemp -d /tmp/kslog-changelog-parser.XXXXXX)
trap 'rm -rf -- "$fixture_dir"' EXIT

cat > "$fixture_dir/CHANGELOG.md" <<'EOF'
# Changelog

## 2x1x0

regex lookalike must not match

## 2.1.0

literal section

## 2.0.0

older section
EOF

actual=$(bash "$root_dir/changelog_parser.sh" 2.1.0 "$fixture_dir/CHANGELOG.md")
if [[ "$actual" != $'\nliteral section' ]]; then
    printf 'unexpected parser output:\n%s\n' "$actual" >&2
    exit 1
fi

stdin_actual=$(bash "$root_dir/changelog_parser.sh" 2.1.0 < "$fixture_dir/CHANGELOG.md")
if [[ "$stdin_actual" != "$actual" ]]; then
    printf 'stdin/file parser behavior differs\n' >&2
    exit 1
fi

if bash "$root_dir/changelog_parser.sh" 9.9.9 "$fixture_dir/CHANGELOG.md" >/dev/null 2>&1; then
    printf 'missing literal heading unexpectedly succeeded\n' >&2
    exit 1
fi

printf 'changelog parser: PASS literal heading selected; regex lookalike rejected\n'
