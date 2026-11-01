# KSLog GitHub release Toolchain plugin

Kotlin Toolchain 0.13.0 has no built-in GitHub release task. This local plugin
keeps the existing explicit release capability as a narrow wrapper around the
standard `gh release create` CLI.

It extracts the matching version section from `CHANGELOG.md`, preserves the
legacy tag/title/target conventions, accepts the existing
`GITHUB_RELEASE_TOKEN` variable plus standard `GH_TOKEN`/`GITHUB_TOKEN`, and
supports a non-network `KSLOG_GITHUB_RELEASE_DRY_RUN=true` validation mode. It
runs the committed release/version consistency preflight before rendering or
creating a release. No CI workflow invokes it.

`gh release create` does not modify local git state, but GitHub may create the
remote tag at `--target` when the requested tag does not already exist. A real
invocation therefore mutates both the remote release and potentially the remote
tag; dry-run mode performs neither action.
