# KSLog Dokka Toolchain plugin

This local plugin is adapted from JetBrains Kotlin Toolchain's
`build-sources/dokka` plugin at release `v0.13.0`, commit
`abc7a5f7579e9ef267107782048c76777f0bf932`.

Upstream source: <https://github.com/JetBrains/kotlin-toolchain/tree/abc7a5f7579e9ef267107782048c76777f0bf932/build-sources/dokka>

Changes made for KSLog:

- models the KMP common/JVM/Android/JS/WasmJS/native source-set graph;
- uses Dokka 2.2.0 to match the pre-migration build;
- provides separate `dokkaHtml` and `dokkaGfm` commands;
- writes to the preserved `build/dokka/html` and `build/dokka/gfm` paths;
- preserves deprecated-member filtering and GitHub source links;
- supplies the resolved JVM classpath plus transformed Common and matching
  JS/WasmJS standard-library KLIBs materialized by the documented prebuild;
- fails generation if Dokka emits an unresolved-type placeholder, preventing
  the deployment workflow from uploading incomplete API signatures.

The legacy Gradle `DOKKA_PATH` override remains Gradle-only. The Toolchain
plugin intentionally accepts only output directories below `build/dokka`,
rejects symbolic-link ancestors, and verifies real-path containment before
cleaning generated output.

The adapted Kotlin files retain the upstream Apache-2.0 header. See
`LICENSE.txt` in this directory.
