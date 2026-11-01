#!/usr/bin/env python3
"""Verify wrapper checkout attributes and on-disk line endings."""

from pathlib import Path
import subprocess


ROOT = Path(__file__).resolve().parents[1]


def fail(message: str) -> None:
    raise SystemExit(f"wrapper line-ending check failed: {message}")


shell_bytes = (ROOT / "kotlin").read_bytes()
batch_bytes = (ROOT / "kotlin.bat").read_bytes()
if b"\r" in shell_bytes:
    fail("kotlin must contain LF line endings only")
if b"\r\n" not in batch_bytes or b"\n" in batch_bytes.replace(b"\r\n", b""):
    fail("kotlin.bat must contain CRLF line endings only")

completed = subprocess.run(
    ["git", "-C", str(ROOT), "check-attr", "text", "eol", "--", "kotlin", "kotlin.bat"],
    check=True,
    text=True,
    stdout=subprocess.PIPE,
)
attributes = {}
for line in completed.stdout.splitlines():
    path, attribute, value = line.split(": ", 2)
    attributes[(path, attribute)] = value

expected = {
    ("kotlin", "text"): "set",
    ("kotlin", "eol"): "lf",
    ("kotlin.bat", "text"): "set",
    ("kotlin.bat", "eol"): "crlf",
}
if attributes != expected:
    fail(f"unexpected attributes: {attributes}")

print("wrapper line endings: PASS kotlin=LF kotlin.bat=CRLF checkoutAttributes=PASS")
