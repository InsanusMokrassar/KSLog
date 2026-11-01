#!/usr/bin/env python3
"""Fail when KSLog's duplicated release version settings diverge."""

from __future__ import annotations

from pathlib import Path
import sys


ROOT = Path(__file__).resolve().parents[1]


def fail(message: str) -> None:
    raise SystemExit(f"release consistency check failed: {message}")


def gradle_property(name: str) -> str:
    for raw_line in (ROOT / "gradle.properties").read_text(encoding="utf-8").splitlines():
        line = raw_line.strip()
        if not line or line.startswith("#") or "=" not in line:
            continue
        key, value = line.split("=", 1)
        if key.strip() == name:
            return value.strip()
    fail(f"missing {name!r} in gradle.properties")


def yaml_scalar(path: tuple[str, ...]) -> str:
    stack: list[tuple[int, str]] = []
    for raw_line in (ROOT / "kslog/module.yaml").read_text(encoding="utf-8").splitlines():
        if not raw_line.strip() or raw_line.lstrip().startswith("#"):
            continue
        indent = len(raw_line) - len(raw_line.lstrip(" "))
        content = raw_line.strip()
        if ":" not in content or content.startswith("-"):
            continue
        key, value = content.split(":", 1)
        while stack and stack[-1][0] >= indent:
            stack.pop()
        candidate = tuple(item[1] for item in stack) + (key,)
        value = value.strip().strip('"\'')
        if candidate == path:
            if not value:
                fail(f"empty YAML scalar at {'.'.join(path)}")
            return value
        if not value:
            stack.append((indent, key))
    fail(f"missing YAML scalar at {'.'.join(path)}")


def main() -> None:
    version = gradle_property("version")
    copies = {
        "plugins.dokka.moduleVersion": yaml_scalar(("plugins", "dokka", "moduleVersion")),
        "plugins.github-release.version": yaml_scalar(("plugins", "github-release", "version")),
        "plugins.github-release.targetCommitish": yaml_scalar(
            ("plugins", "github-release", "targetCommitish")
        ),
        "settings.publishing.version": yaml_scalar(("settings", "publishing", "version")),
    }
    mismatches = {name: value for name, value in copies.items() if value != version}
    if mismatches:
        rendered = ", ".join(f"{name}={value!r}" for name, value in mismatches.items())
        fail(f"gradle.properties version is {version!r}, but {rendered}")

    expected_heading = f"## {version}"
    headings = [
        (index, line)
        for index, line in enumerate(
            (ROOT / "CHANGELOG.md").read_text(encoding="utf-8").splitlines(), start=1
        )
        if line == expected_heading
    ]
    if len(headings) != 1:
        fail(f"expected exactly one literal {expected_heading!r} heading, found {len(headings)}")

    print(
        f"release consistency: PASS version={version} "
        f"copies={len(copies)} changelogLine={headings[0][0]}"
    )


if __name__ == "__main__":
    try:
        main()
    except (OSError, UnicodeError) as error:
        fail(str(error))
