#!/usr/bin/env python3
"""Compare post-move Gradle publication/API output with the untouched base."""

from __future__ import annotations

import argparse
import hashlib
import os
from pathlib import Path
import shutil
import subprocess
import tempfile
import xml.etree.ElementTree as ET
import zipfile


EXPECTED_ARTIFACT_IDS = {
    "kslog",
    "kslog-android",
    "kslog-js",
    "kslog-jvm",
    "kslog-linuxarm32hfp",
    "kslog-linuxarm64",
    "kslog-linuxx64",
    "kslog-mingwx64",
    "kslog-wasm-js",
}
EXPECTED_VERSION = "2.1.0"
SOURCE_ROOTS = {
    "kslog/src": "src/commonMain/kotlin",
    "kslog/src@android": "src/androidMain/kotlin",
    "kslog/src@js": "src/jsMain/kotlin",
    "kslog/src@jvm": "src/jvmMain/kotlin",
    "kslog/src@native": "src/nativeMain/kotlin",
    "kslog/src@wasmJs": "src/wasmJsMain/kotlin",
}


def fail(message: str) -> None:
    raise SystemExit(f"Gradle fallback verification failed: {message}")


def primary_files(repository: Path) -> dict[str, Path]:
    group_root = repository / "dev/inmo"
    if not group_root.is_dir():
        fail(f"missing publication group directory: {group_root}")
    ignored_names = {"maven-metadata-local.xml", "_remote.repositories"}
    ignored_suffixes = (".asc", ".md5", ".sha1", ".sha256", ".sha512", ".lastUpdated")
    result = {}
    for path in sorted(group_root.rglob("*")):
        if not path.is_file() or path.name in ignored_names or path.name.endswith(ignored_suffixes):
            continue
        result[path.relative_to(group_root).as_posix()] = path
    return result


def zip_payload(path: Path) -> dict[str, str]:
    with zipfile.ZipFile(path) as archive:
        return {
            info.filename: hashlib.sha256(archive.read(info)).hexdigest()
            for info in archive.infolist()
            if not info.is_dir() and info.filename != "META-INF/MANIFEST.MF"
        }


def xml_text(element: ET.Element, child: str) -> str:
    found = element.find(f"{{*}}{child}")
    return "" if found is None or found.text is None else found.text.strip()


def pom_model(path: Path) -> dict[str, object]:
    root = ET.parse(path).getroot()

    def records(container: str, record: str, fields: tuple[str, ...]) -> tuple[tuple[str, ...], ...]:
        parent = root.find(f"{{*}}{container}")
        if parent is None:
            return ()
        return tuple(
            sorted(tuple(xml_text(item, field) for field in fields) for item in parent.findall(f"{{*}}{record}"))
        )

    scm = root.find("{*}scm")
    return {
        "groupId": xml_text(root, "groupId"),
        "artifactId": xml_text(root, "artifactId"),
        "version": xml_text(root, "version"),
        "packaging": xml_text(root, "packaging"),
        "name": xml_text(root, "name"),
        "description": xml_text(root, "description"),
        "url": xml_text(root, "url"),
        "licenses": records("licenses", "license", ("name", "url")),
        "developers": records("developers", "developer", ("id", "name", "email")),
        "scm": ()
        if scm is None
        else (
            xml_text(scm, "url"),
            xml_text(scm, "connection"),
            xml_text(scm, "developerConnection"),
        ),
        "dependencies": records(
            "dependencies",
            "dependency",
            ("groupId", "artifactId", "version", "scope", "type", "classifier", "optional"),
        ),
    }


def validate_required_pom(path: Path, model: dict[str, object]) -> None:
    expected_artifact_id = path.parent.parent.name
    required = {
        "groupId": "dev.inmo",
        "artifactId": expected_artifact_id,
        "version": EXPECTED_VERSION,
        "name": "kslog",
        "description": "Simple logging library for kotlin",
        "url": "https://github.com/InsanusMokrassar/KSLog",
        "licenses": (("MIT License", "https://opensource.org/licenses/MIT"),),
        "developers": (
            ("Akkihi", "Akkihi", "akkihiguy@gmail.com"),
            ("InsanusMokrassar", "InsanusMokrassar", "ovsyannikov.alexey95@gmail.com"),
        ),
        "scm": (
            "https://github.com/InsanusMokrassar/KSLog.git",
            "",
            "scm:git:[fetch=]https://github.com/InsanusMokrassar/KSLog.git[push=]https://github.com/InsanusMokrassar/KSLog.git",
        ),
    }
    for field, expected in required.items():
        if model[field] != expected:
            fail(f"required POM metadata mismatch in {path}: {field}={model[field]!r}")


def compare_publications(baseline_repository: Path, current_repository: Path) -> None:
    baseline = primary_files(baseline_repository)
    current = primary_files(current_repository)
    if len(baseline) != 46 or len(current) != 46:
        fail(f"expected 46 primary files, got baseline={len(baseline)} current={len(current)}")
    if baseline.keys() != current.keys():
        missing = sorted(baseline.keys() - current.keys())
        added = sorted(current.keys() - baseline.keys())
        fail(f"publication inventory differs; missing={missing}, added={added}")

    artifact_ids = {relative.split("/", 1)[0] for relative in baseline}
    if artifact_ids != EXPECTED_ARTIFACT_IDS:
        fail(f"published artifact IDs differ: {sorted(artifact_ids)}")

    source_jars = [relative for relative in baseline if relative.endswith("-sources.jar")]
    if len(source_jars) != 9:
        fail(f"expected 9 source JARs, found {len(source_jars)}")
    for relative in source_jars:
        if zip_payload(baseline[relative]) != zip_payload(current[relative]):
            fail(f"source JAR payload differs: {relative}")

    pom_paths = [relative for relative in baseline if relative.endswith(".pom")]
    if len(pom_paths) != 9:
        fail(f"expected 9 POMs, found {len(pom_paths)}")
    for relative in pom_paths:
        baseline_model = pom_model(baseline[relative])
        current_model = pom_model(current[relative])
        validate_required_pom(baseline[relative], baseline_model)
        validate_required_pom(current[relative], current_model)
        if baseline_model != current_model:
            fail(f"POM model differs: {relative}")

    print("publication inventory: PASS primaryFiles=46 coordinates=9 sourceJars=9 poms=9")


def verify_source_bytes(project_root: Path, base_commit: str) -> None:
    checked = 0
    for current_root, previous_root in SOURCE_ROOTS.items():
        for current_path in sorted((project_root / current_root).rglob("*.kt")):
            relative = current_path.relative_to(project_root / current_root)
            previous_path = f"{previous_root}/{relative.as_posix()}"
            completed = subprocess.run(
                ["git", "-C", str(project_root), "show", f"{base_commit}:{previous_path}"],
                check=False,
                stdout=subprocess.PIPE,
                stderr=subprocess.PIPE,
            )
            if completed.returncode != 0:
                fail(f"base source is missing: {previous_path}")
            if completed.stdout != current_path.read_bytes():
                fail(f"production source bytes changed: {current_path.relative_to(project_root)}")
            checked += 1
    if checked != 20:
        fail(f"expected 20 production sources, checked {checked}")
    print("production source identity: PASS files=20")


def javap_path() -> str:
    java_home = Path(os.environ.get("JAVA_HOME", ""))
    candidate = java_home / "bin/javap"
    if candidate.is_file():
        return str(candidate)
    found = shutil.which("javap")
    if found is None:
        fail("javap is unavailable; set JAVA_HOME to a JDK")
    return found


def public_api(archive_path: Path) -> tuple[tuple[str, ...], str, bytes]:
    with zipfile.ZipFile(archive_path) as archive:
        class_names = tuple(
            sorted(
                info.filename[:-6].replace("/", ".")
                for info in archive.infolist()
                if info.filename.endswith(".class") and not info.filename.startswith("META-INF/")
            )
        )
        module_entry = "META-INF/dev.inmo_kslog.kotlin_module"
        if module_entry not in archive.namelist():
            fail(f"missing Kotlin module identity in {archive_path}")
        module_bytes = archive.read(module_entry)

    output = []
    for class_name in class_names:
        completed = subprocess.run(
            [javap_path(), "-public", "-s", "-constants", "-classpath", str(archive_path), class_name],
            check=False,
            text=True,
            stdout=subprocess.PIPE,
            stderr=subprocess.PIPE,
        )
        if completed.returncode != 0:
            fail(f"javap failed for {class_name}: {completed.stderr.strip()}")
        output.append(completed.stdout)
    return class_names, "\n".join(output), module_bytes


def nested_classes_jar(aar: Path, destination: Path) -> Path:
    with zipfile.ZipFile(aar) as archive:
        try:
            classes = archive.read("classes.jar")
        except KeyError as error:
            fail(f"Gradle AAR lacks classes.jar: {aar}")
    destination.write_bytes(classes)
    return destination


def compare_public_jvm_signatures(baseline_repository: Path, current_repository: Path) -> None:
    group = Path("dev/inmo")
    jvm_relative = group / "kslog-jvm/2.1.0/kslog-jvm-2.1.0.jar"
    android_relative = group / "kslog-android/2.1.0/kslog-android-2.1.0.aar"

    baseline_jvm = public_api(baseline_repository / jvm_relative)
    current_jvm = public_api(current_repository / jvm_relative)
    if baseline_jvm != current_jvm:
        fail("JVM class list, public method/field signatures, or Kotlin module bytes differ")
    if len(current_jvm[0]) != 25:
        fail(f"expected 25 JVM classes, found {len(current_jvm[0])}")

    with tempfile.TemporaryDirectory(prefix="kslog-android-api-") as temporary:
        temporary_path = Path(temporary)
        baseline_android = public_api(
            nested_classes_jar(baseline_repository / android_relative, temporary_path / "baseline.jar")
        )
        current_android = public_api(
            nested_classes_jar(current_repository / android_relative, temporary_path / "current.jar")
        )
        if baseline_android != current_android:
            fail("Android class list, public method/field signatures, or Kotlin module bytes differ")
        if len(current_android[0]) != 24:
            fail(f"expected 24 Android classes, found {len(current_android[0])}")

    with zipfile.ZipFile(baseline_repository / android_relative) as baseline_aar:
        baseline_manifest = baseline_aar.read("AndroidManifest.xml")
    with zipfile.ZipFile(current_repository / android_relative) as current_aar:
        current_manifest = current_aar.read("AndroidManifest.xml")
    if baseline_manifest != current_manifest:
        fail("post-move Gradle AndroidManifest.xml differs from the untouched baseline")

    print("public JVM signatures: PASS jvmClasses=25 androidClasses=24 module=dev.inmo_kslog")


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser()
    parser.add_argument("--project-root", type=Path, required=True)
    parser.add_argument("--base-commit", required=True)
    parser.add_argument("--baseline-repository", type=Path, required=True)
    parser.add_argument("--current-repository", type=Path, required=True)
    return parser.parse_args()


def main() -> None:
    args = parse_args()
    compare_publications(args.baseline_repository, args.current_repository)
    verify_source_bytes(args.project_root, args.base_commit)
    compare_public_jvm_signatures(args.baseline_repository, args.current_repository)


if __name__ == "__main__":
    main()
