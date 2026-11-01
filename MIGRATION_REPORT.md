# KSLog Kotlin Toolchain migration report

Date: 2026-11-02
Base commit: `0b9fb30862c7319bb228039ee33eef9a0ddef95c`
Kotlin Toolchain: `v0.13.0` source commit
`abc7a5f7579e9ef267107782048c76777f0bf932`

## Result

KSLog now has a checked-in, release-pinned Kotlin Toolchain project that loads,
builds every platform supported by Toolchain 0.13.0, runs real JVM tests,
generates both documentation formats, can publish signed KMP artifacts through
the configured `mavenLocal` destination, and is consumable by Gradle KMP and
Android projects. Migration validation redirects Maven Local to a temporary
repository; the destination itself is not inherently isolated. Toolchain Nexus
credentials/repository configuration is absent and resolved Maven Central
publishing is disabled.

This is **not yet a publication-compatible full cutover**. Toolchain 0.13.0
cannot represent `linuxArm32Hfp` and hard-codes the WasmJS artifact suffix as
`-wasmjs` instead of KSLog's released `-wasm-js`. The Gradle build is therefore
retained, mapped to the moved sources, and validated as the complete target and
publication fallback. No remote artifact, documentation, tag, or release was
published during this migration.

## Implemented layout

- `kotlin` and `kotlin.bat` preserve the official 0.13.0 wrapper content. Git
  attributes enforce LF for `kotlin`/Kotlin sources and CRLF for `kotlin.bat`.
  The wrappers select CLI `0.13.0-dev-4444` with the official pinned
  distribution SHA-256.
- `project.yaml` registers the stable `kslog` library module and two local build
  plugin modules. The stable directory name prevents compiler module identity
  from depending on the checkout/worktree directory name.
- `kslog/module.yaml` declares Android, JS, JVM, WasmJS, Linux x64/ARM64, and
  MinGW x64, plus Android/JDK/Kotlin settings and full publication metadata.
- The 20 production files moved byte-for-byte into `kslog/src*` Toolchain
  source roots. `build.gradle` maps the retained Gradle source sets to those
  roots, including Linux ARM32.
- Compiler module identities are pinned to the existing published identities:
  JVM/Android `META-INF/dev.inmo_kslog.kotlin_module`, leaf KLIB
  `unique_name=dev.inmo:kslog`, common metadata `kslog_commonMain`, and native
  metadata `dev.inmo:kslog_nativeMain`/`kslog_nativeMain`.
- `kslog/test@jvm/CallbackKSLogTest.kt` adds two focused tests covering exact
  callback forwarding and rejected lazy-message evaluation.
- `plugins/dokka` adapts the official Apache-2.0 Toolchain Dokka plugin for
  KSLog's KMP graph and Dokka 2.2.0 HTML/GFM output. Each adapted Apache source
  carries a prominent KSLog modification/provenance notice, and cleanup rejects
  symbolic-link ancestors and requires real-path containment. Platform analysis
  receives JVM/common, JS, and WasmJS standard-library classpaths. Because the
  plugin API only exposes the JVM module classpath, the documented JS/WasmJS
  prebuild materializes the transformed Common and matching platform KLIBs for
  deterministic discovery. Generation
  fails before deployment if unresolved-type placeholders remain.
- `plugins/github-release` provides an explicit `gh release create` command and
  a non-network dry-run mode. It runs the committed version consistency
  preflight first; no workflow invokes it.
- `DEVELOPMENT.md` documents Toolchain use, the Gradle fallback, isolated local
  publication, disabled Toolchain remotes, the retained Gradle release path,
  and release gating.
- `verification/` contains portable release/parser/wrapper checks plus the
  baseline/current Gradle publication, source-identity, public-signature, and
  consumer verifier used by CI.

## Target and test matrix

| Target | Toolchain result | Runtime/test result | Contract notes |
|---|---|---|---|
| JVM | PASS | 2 tests found, 2 started, 2 passed | Bytecode major 52; 25-class list and public JVM method/field signatures from `javap -public -s -constants` match Gradle |
| Android | PASS | No device tests in the project; Android consumer compilation PASS | Bytecode major 61; 24-class list and public JVM method/field signatures match Gradle; minSdk 21, compileSdk/build tools 37 |
| JS | PASS | No test sources; Toolchain 0.13.0 exposes no browser/Node runner selection for a library | Gradle fallback keeps browser + Node declarations |
| WasmJS | PASS | No test sources; Toolchain 0.13.0 exposes no Node/d8 selection and only has browser test support | Gradle fallback keeps browser + Node + d8 declarations |
| linuxX64 | PASS | No test sources | Host-compilable KLIB |
| linuxArm64 | PASS | Cross-compiled; not runnable on this x64 host | KLIB publication produced |
| mingwX64 | PASS | Cross-compiled; not runnable on this Linux host | KLIB publication produced |
| linuxArm32Hfp | **UNSUPPORTED** | Gradle clean build compiles it; no tests | Absent from Toolchain 0.13.0 `Platform`; minimal model probe fails |

`./kotlin check` did not mistake empty platform suites for passing tests: the
report only claims the two JVM tests. Gradle reports JS, WasmJS, Android, and
native tests as `NO-SOURCE` or skipped, while still compiling all declared
targets, including Linux ARM32.

## Verification evidence

The commands below are reproducible from the repository root. Temporary build
logs and repositories are intentionally not part of the published report.

### Model, build, checks, and compatibility

| Command | Result |
|---|---|
| `./kotlin show modules` | PASS; modules `dokka`, `github-release`, `kslog` |
| `./kotlin show settings -m kslog` plus `./kotlin show tasks` | PASS; `mavenCentral.enabled: false`, only local publish repository configured, no Nexus/Central task references |
| `./kotlin show commands` / `show checks` | PASS; Dokka/release commands and builtin tests enumerated |
| `./kotlin clean && ./kotlin build` | PASS; all seven Toolchain platforms plus both plugin modules |
| `./kotlin check` | PASS; KSLog JVM tests 2/2 plus Dokka guard/classpath tests 4/4 |
| `./kotlin test -m dokka -p jvm` | PASS 4/4; transformed/platform classpath mapping, unresolved-output rejection, symlink containment, and scoped cleanup |
| `./gradlew --no-daemon clean build` | PASS; complete legacy build, including Linux ARM32 |
| `bash verification/verify_gradle_fallback.sh` | PASS; isolated baseline/current Gradle publication and committed consumer probe |
| `python3 verification/check_release_consistency.py` and parser/wrapper checks | PASS; four version copies, literal changelog heading, LF/CRLF attributes |
| Linux ARM32 minimal model probe | Expected failure: `Product platforms list should not be empty` at `linuxArm32Hfp` |
| GitHub YAML, actionlint 1.7.7, shell syntax, `git diff --check` | PASS |
| `ast-index rebuild` | PASS after final edits |

The Toolchain emits the existing insignificant inline warning. It also warns
that `-module-name`/`-Xshort-module-name` are passed more than once because the
built-in checkout-derived names are intentionally overridden; inspection of
the output JAR/KLIB manifests proves the last values match the released Gradle
module identities.

### Source and ABI preservation

- All 20 production source files match the base commit byte-for-byte after
  their moves.
- The JVM 25-class and Android 24-class lists match; public JVM method/field
  signature dumps from `javap -public -s -constants` have zero differences.
- Gradle/Toolchain bytecode majors are JVM 52/52 and Android 61/61.
- Published JVM, common/native metadata, and supported leaf KLIB module
  identities match Gradle.

These checks are deliberately narrow. Source identity, consumer compilation,
JVM class/signature comparison, and inspected module names are not a complete
Kotlin metadata or KLIB ABI proof. Full cutover remains blocked and requires a
dedicated metadata/KLIB compatibility review in addition to resolving the two
upstream capability gaps.

### Documentation and release command

| Command | Result |
|---|---|
| `./kotlin build -m kslog -p js -p wasmJs` | PASS; materializes the Common/JS/WasmJS analysis KLIBs required by the pinned plugin API |
| `./kotlin do dokkaHtml` | PASS; 156 files at `build/dokka/html`, zero unresolved-type placeholders |
| `./kotlin do dokkaGfm` | PASS; 101 files at `build/dokka/gfm`, zero unresolved-type placeholders |
| Output/link/platform inspection | PASS; canonical source links and Common/JVM/Android/JS/WasmJS/Native tabs |
| `KSLOG_GITHUB_RELEASE_DRY_RUN=true ./kotlin do githubRelease` | PASS; consistency preflight and `v2.1.0` payload, no network mutation |

The legacy `DOKKA_PATH` override is intentionally not ported. It remains
available only through Gradle because Toolchain cleanup is restricted to direct
children of `build/dokka`.

### Isolated publication and consumers

Toolchain publication used a temporary Maven repository selected with the JVM
`maven.repo.local` override, `-m kslog`, and a generated throwaway signing key.
The configured destination is named `mavenLocal` and would use the normal Maven
Local repository without that override. The temporary private key was deleted
after verification, and the normal Maven Local repository was not written.

| Check | Result |
|---|---|
| `./kotlin publish -m kslog mavenLocal` | PASS in an isolated repository |
| Toolchain artifact/signature inventory | 40 primary files and 40 detached signatures; 40/40 signatures verified with the throwaway public key |
| Toolchain required POM metadata comparison | PASS for root and every supported platform artifact |
| Gradle KMP consumer compile | PASS for JVM, JS, WasmJS, linuxX64, linuxArm64, mingwX64 |
| Gradle Android KMP consumer compile | PASS |
| `bash verification/verify_gradle_fallback.sh` | PASS; untouched-base and post-move Gradle outputs each contain the same 46 primary files, nine source JARs, and nine required-metadata POMs; no signing task/key/config is used |

Produced IDs are `kslog`, `kslog-android`, `kslog-js`, `kslog-jvm`,
`kslog-linuxarm64`, `kslog-linuxx64`, `kslog-mingwx64`, and `kslog-wasmjs`.
Root Gradle module metadata, Kotlin Tooling metadata, and sources are present.
Platform Toolchain publications contain placeholder Javadoc JARs, while the
root Toolchain Javadoc JAR is absent. The committed Gradle verifier uses a fresh
Gradle user home with only optional `caches`/`wrapper` links, an empty GnuPG
home, sanitized credential/signing environment variables, and separate Maven
repositories; it neither reads user Gradle signing configuration nor writes to
the normal Maven local repository.

## Exact parity blockers and differences

1. **Linux ARM32:** `linuxArm32Hfp` is absent from the pinned 0.13.0
   `Platform` enum and schema. Adding it to `product.platforms` is rejected at
   model load, before compilation or publication can be customized.
2. **Wasm artifact coordinate:** built-in KMP publication maps `wasmJs` to
   `kslog-wasmjs`; there is no per-platform artifact-suffix override. KSLog's
   released coordinate is `kslog-wasm-js`.
3. **Runtime declarations:** JS browser/Node and WasmJS browser/Node/d8 runner
   selection are not exposed for this Toolchain library model. Compilation and
   publication work; those execution contracts stay in Gradle.
4. **POM shape:** required name/description/URL/developers/license/SCM URL and
   developer connection are preserved. Toolchain additionally generates the
   SCM `connection`. Its root KMP POM uses `<packaging>pom</packaging>` and a
   `kslog-jvm` compile dependency, while Gradle's root POM carries a Kotlin
   stdlib runtime dependency. The Toolchain Android POM additionally declares
   `kotlin-stdlib-jdk7` and `kotlin-stdlib-jdk8`; its JS POM omits Gradle's
   `kotlin-dom-api-compat`; and its Linux ARM64, Linux x64, and MinGW POMs move
   the Kotlin stdlib dependency from compile to runtime scope. Gradle module
   metadata consumer selection and the committed consumers passed, but direct
   POM consumers can observe these dependency/scope changes, so this is not
   publication-shape parity and requires review before full cutover.
5. **Root Javadoc artifact:** the 46-file Gradle publication has
   `kslog-2.1.0-javadoc.jar`; the 40-file Toolchain publication omits it. This is
   the sixth missing primary file in addition to the five Linux ARM32 files.
   Toolchain platform Javadoc JARs contain a manifest plus a 78-byte `README.md`
   placeholder, while Gradle's Javadoc JARs contain only the manifest.
6. **Android manifest and AAR layout:** Gradle's manifest declares only
   `minSdkVersion=21`; Toolchain additionally emits `targetSdkVersion=37`.
   Gradle uses `classes.jar`; Toolchain uses
   `libs/kslog-jvm.jar`. Both are valid AAR layouts, the bytecode level and
   compared public JVM method/field signatures match, and an Android KMP
   consumer compiles successfully.
7. **JS manifest detail:** public KLIB identity matches
   `unique_name=dev.inmo:kslog`, but Toolchain's JS/Wasm KLIB does not include
   Gradle's `jsOutputName=kslog` manifest entry.
8. **Android resource exclusions:** `resourcePackaging` is not accepted for a
   Toolchain `kmp/lib`. The produced AAR contains only the manifest and library
   JAR, so none of the three legacy serialization metadata exclusions are
   currently present. Gradle keeps the exclusions for the fallback.
9. **Per-platform JVM release:** Toolchain 0.13.0 does not allow `jvm.release`
   to differ by platform. The module uses target-specific `-jvm-target=1.8`
   and `-jvm-target=17`; inspected bytecode proves majors 52 and 61.
10. **Documentation output override:** Gradle honors `DOKKA_PATH`; Toolchain
    intentionally does not because arbitrary external cleanup would weaken the
    generated-output containment guarantee.

Because items 1 and 2 change released target/coordinate contracts, remote
Toolchain publication must remain blocked until Toolchain gains the required
capabilities or an explicitly reviewed compatibility implementation is added.

## CI behavior

- `.github/workflows/build.yml` replaces the stale workflow that attempted
  nonexistent Gitea/GitHub-Packages Gradle tasks on every push. Pushes and pull
  requests now run release/parser/wrapper preflights, Toolchain build/check, the
  complete retained Gradle build, and the isolated publication/API/consumer
  verifier. Checkout fetches full history so the verifier's pinned migration
  base commit is available. It has read-only repository permissions and no
  secrets or remote publication commands.
- `.github/workflows/kdocs.yml` remains `master`-only. Its read-only generation
  job checks out with `persist-credentials: false`, materializes the platform
  analysis KLIBs, runs Toolchain/Dokka, and
  uploads the generated artifact. A separate write-enabled job downloads only
  that artifact and deploys it to `kdocs`; project/plugin code never runs with a
  write credential.
- All workflow actions are pinned to resolved commit SHAs.
- Remote CI execution is intentionally unverified until the branch is pushed.

## Remote-only checks not performed

- GitHub Actions on an actual hosted runner.
- Inmo Nexus upload.
- Maven Central bundle upload/validation/release.
- GitHub release creation.
- Documentation deployment to the `kdocs` branch.
- Android device tests and browser/Node/d8 tests (there are currently no such
  test sources); Linux ARM64 and MinGW runtime execution on matching hosts.

## Recommended pull request

Suggested title:

> build: add Kotlin Toolchain migration with compatibility fallback

Suggested body:

> ## Summary
> - add the pinned Kotlin Toolchain 0.13.0 wrappers and a stable `kslog` KMP module
> - migrate source roots byte-for-byte and retain a validated Gradle fallback
> - add real JVM tests, KMP-capable Dokka HTML/GFM tasks, and an explicit GitHub release command
> - replace stale package-publishing CI with non-publishing Toolchain and full Gradle validation
> - document isolated publication, release gating, and full verification evidence
>
> ## Validation
> - all Toolchain-supported targets build
> - JVM tests: 2/2 pass
> - retained Gradle clean build passes, including linuxArm32Hfp
> - JVM/Android class lists and public JVM method/field signature diffs are empty; bytecode remains 8/17
> - signed isolated KMP publication verified and consumed by Gradle KMP + Android projects
> - Dokka HTML/GFM and GitHub release dry-run pass
>
> ## Known blockers
> Toolchain 0.13.0 has no linuxArm32Hfp platform and publishes `-wasmjs` instead
> of the released `-wasm-js` coordinate. Therefore Gradle remains the publication
> fallback and remote Toolchain publication is not enabled. See
> `MIGRATION_REPORT.md` for the complete matrix and artifact differences.

This migration is ready only for a **draft PR**. Full Gradle removal is not
review-ready and remains blocked by Toolchain 0.13.0's missing Linux ARM32
platform and incompatible Wasm artifact ID; the narrow checks above do not
waive those requirements.
