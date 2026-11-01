package dev.inmo.kslog.build.release

import org.jetbrains.amper.plugins.Configurable

@Configurable
interface GitHubReleaseSettings {
    val owner: String
    val repository: String
    val version: String
    val tagPrefix: String get() = "v"
    val targetCommitish: String get() = ""
    val changelogFile: String get() = "CHANGELOG.md"
}
