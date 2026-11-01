# KSLog Gradle to Kotlin Toolchain migration plan

This inventory was written before adding any Kotlin Toolchain YAML. It records the
Gradle contract at commit `0b9fb30862c7319bb228039ee33eef9a0ddef95c` and is the
checklist for the migration.

## Pinned inputs

- Kotlin Toolchain release: `v0.13.0`, source commit
  `abc7a5f7579e9ef267107782048c76777f0bf932`.
- Official wrapper from that tag: CLI distribution `0.13.0-dev-4444`, SHA-256
  `13c2448146ac6dba0fa3a4e5d367dab73d8cfd7400bab348bebe905c165adecf`.
- Kotlin: `2.4.20` (set explicitly even though the release defaults to it).
- Dokka: `2.2.0`, source commit
  `656ca46fbbd676d872b0bd383042fc12ae7adcdd`.
- Migration guidance: the four official Kotlin agent skills installed from
  `Kotlin/kotlin-agent-skills@087f5689c1934135ed58718cdf524525e452b7e9`.

The pinned Toolchain documentation and source confirm that nested module
templates are supported in 0.13.0. This resolves the contradictory statement in
the core agent skill, although this one-module project does not need a template
hierarchy.

## Existing project contract

### Source and API

- One Kotlin Multiplatform library with 20 production Kotlin files and no tests.
- Source sets: `commonMain`, `jvmMain`, `androidMain`, `jsMain`, `wasmJsMain`,
  and `nativeMain`.
- Production source bytes must remain unchanged. Moving source roots to the
  stable `kslog` Toolchain module's KMP layout is allowed; legacy Gradle
  source-set paths will be mapped to those directories while the compatibility
  fallback remains.
- Public package/API/behavior must not change.

### Targets and execution environments

| Existing declaration | Required Toolchain mapping | Status in Toolchain 0.13.0 |
|---|---|---|
| JVM, bytecode 8 | `jvm`, target-specific `-jvm-target=1.8` | Supported; verified class major 52 |
| Android, bytecode 17 | `android`, target-specific `-jvm-target=17` | Supported; verified class major 61 |
| Android min/compile/build tools 21/37/37.0.0 | `settings.android` | Supported |
| JS IR, browser + Node.js | `js` | Library compilation/publication supported; runtime selection is not configurable |
| WasmJS, browser + Node.js + d8 | `wasmJs` | Library compilation/publication and browser tests supported; Node.js/d8 runners are not exposed |
| linuxX64 | `linuxX64` | Supported |
| linuxArm64 | `linuxArm64` | Supported (cross-compilation on this host) |
| linuxArm32Hfp | none | **Blocked:** not present in the 0.13.0 platform schema |
| mingwX64 | `mingwX64` | Supported (cross-compilation on this host) |
| Shared native implementation | `src@native` | Supported |

The pinned `Platform` enum omits `linuxArm32Hfp`; a minimal model-load probe
reports an empty effective platform list at that value. The probe remains
outside the active project model and its result is recorded in
`MIGRATION_REPORT.md`.

Toolchain 0.13.0 does not allow `settings.jvm.release` to vary by platform, so
the common value is unset and the JVM/Android compiler targets are supplied as
platform-specific Kotlin arguments. Inspected output proves the required
bytecode levels.

### Build plugins and custom logic

| Gradle plugin/feature | Decision | Replacement/justification |
|---|---|---|
| Kotlin Multiplatform | Native | `product.type: kmp/lib` and explicit platforms |
| Android KMP library plugin | Native | Android platform in the KMP product |
| Maven Publish | Native for isolated local validation | `settings.publishing` plus the sole publish repository `mavenLocal`; Toolchain remotes stay disabled while parity is blocked |
| NMCP aggregation/Central Portal | Retained in Gradle only | Toolchain `mavenCentral` remains at its disabled default; the complete Gradle fallback preserves the existing remote path |
| Signing plugin/GPG command | Native | `signArtifacts` and Toolchain signing environment variables |
| Dokka Gradle plugin | Local plugin | Vendor/extend the official Toolchain Dokka build plugin; add KMP source sets, HTML and GFM commands |
| GitHub release Gradle plugin | Local plugin using standard `gh` CLI | Explicit `githubRelease` command, dry-run support, no automatic release |
| Ben Manes versions plugin | Drop | Dependency maintenance only; Renovate remains |
| Dexcount | Drop from Toolchain path | No library/API/publication output; the Toolchain has no native equivalent |
| `javadocsJar` | Partial native publication behavior | Toolchain 0.13.0 publishes placeholder Javadoc JARs for platform artifacts but omits the root `kslog-2.1.0-javadoc.jar` |
| `changelog_parser.sh` | Preserve | Used by the GitHub release command |

The official Toolchain Dokka plugin was reviewed first. It explicitly says it
does not support KMP and only exposes HTML. The local adaptation keeps its
Apache-2.0 headers and provenance, models the actual KSLog source-set graph, and
adds the official Dokka GFM plugin. No existing Toolchain/GitHub-release plugin
was found; `gh` is already installed and is the standard implementation used by
the thin release action.

### Publication contract

- Group/version/base artifact: `dev.inmo:kslog:2.1.0`.
- Existing variant artifact IDs:
  - `kslog-android`
  - `kslog-js`
  - `kslog-jvm`
  - `kslog-linuxarm32hfp`
  - `kslog-linuxarm64`
  - `kslog-linuxx64`
  - `kslog-mingwx64`
  - `kslog-wasm-js`
- Publish KMP root metadata, per-platform artifacts, sources JARs, and empty
  javadoc JARs.
- Preserve POM name/description/homepage, both developers, MIT license, and SCM
  URL/developer connection.
- Preserve complete Inmo Nexus/Maven Central/signing capability through the
  retained Gradle fallback and preserve the explicit GitHub release command.
  Toolchain remote repositories stay absent/disabled until parity is restored.
  Never run a remote publication/release as migration validation.
- Validate to an isolated Maven local directory via
  `-Dmaven.repo.local=<temporary path>`; never overwrite normal `~/.m2` artifacts.

Known 0.13.0 publication mismatch: built-in Toolchain publication derives the
Wasm artifact suffix from `wasmJs` as `-wasmjs`, while Kotlin Gradle publishes
`-wasm-js`. Together with the missing Linux ARM32 target, this prevents a
publication-compatible cutover. The legacy Gradle files therefore remain until
these two blockers are resolved; the Toolchain implementation is a reversible,
working supported-target migration rather than a claim of full parity.

The stable `kslog` module directory plus narrow compiler-identity overrides
preserve the existing JVM Kotlin module and KLIB unique/short names. Remaining
built-in POM, Javadoc-placeholder, AAR-layout, and JS manifest-shape differences
are itemized in `MIGRATION_REPORT.md`.

### CI and documentation contract

- Replace the stale push workflow that names nonexistent Gitea and
  GitHub-Packages Gradle tasks with build/check validation only.
- Run the complete retained Gradle build plus an isolated baseline/current
  local-publication, public-signature, and consumer verifier in validation CI.
- Push and pull-request validation must not publish artifacts, docs, tags, or
  releases.
- Keep documentation deployment master-only and change generation to the
  Toolchain `dokkaHtml` command with output `build/dokka/html`.
- Generate docs in a read-only job without persisted checkout credentials, then
  deploy the generated artifact in a separate write-enabled job.
- Keep `dokkaGfm` with output `build/dokka/gfm` for local/consumer workflows.
- Remote publication and GitHub release remain explicit manual commands and are
  documented, not automatically broadened.

## Implementation checklist

- [x] Add the checked-in 0.13.0 wrappers.
- [x] Add `project.yaml`, stable `kslog/module.yaml`, and extend the existing version catalog.
- [x] Move source roots byte-for-byte to Toolchain KMP layout.
- [x] Map legacy Gradle source sets to the moved roots while blockers remain.
- [x] Add focused JVM behavior tests so validation is not `NO-SOURCE`.
- [x] Vendor and adapt the official Dokka plugin with source/commit/license.
- [x] Add the narrow GitHub release plugin with a non-network dry-run.
- [x] Load the project model and enumerate modules, platforms, commands, and checks.
- [x] Clean-build every declared Toolchain-supported target.
- [x] Run real tests/checks and distinguish host-unrunnable targets.
- [x] Generate HTML and GFM docs at the preserved paths.
- [x] Publish Toolchain artifacts with an ephemeral signing key to an isolated
  Maven repository before disabling all Toolchain remote destinations.
- [x] Inspect artifacts, signatures, POMs, and Gradle module metadata.
- [x] Add a committed verifier that republishes the untouched base and the
  post-move Gradle fallback with isolated Gradle homes and no signing secrets.
- [x] Run Gradle KMP and Android consumer smoke tests against the isolated repository.
- [x] Compare production source hashes and JVM/Android public bytecode/API with Gradle.
- [x] Validate CI YAML, shell scripts, and `git diff --check`.
- [x] Rebuild `ast-index` after source moves and edits.
- [x] Record exact commands, results, logs, blockers, and remote-only checks in
  `MIGRATION_REPORT.md`.
