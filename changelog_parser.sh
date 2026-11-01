#!/bin/bash

function parse() {
    local version="$1"
    local line
    local found=""

    while IFS= read -r line; do
        if [[ "$line" == "## $version" ]]; then
            found="yes"
            break
        fi
    done

    if [[ -z "$found" ]]; then
        printf 'No literal changelog heading found for %s\n' "$version" >&2
        return 1
    fi

    while IFS= read -r line; do
        if [[ "$line" == \#* ]]; then
            break
        fi
        printf '%s\n' "$line"
    done
}

version="$1"
file="${2:-}"

if [ -n "$file" ]; then
  parse "$version" < "$file"
else
  parse "$version"
fi
