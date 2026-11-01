package dev.inmo.kslog.build.release

import org.jetbrains.amper.plugins.ExecutionAvoidance
import org.jetbrains.amper.plugins.TaskAction
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.deleteIfExists
import kotlin.io.path.isRegularFile
import kotlin.io.path.pathString
import kotlin.io.path.writeText

@TaskAction(ExecutionAvoidance.Disabled)
fun githubRelease(
    projectRootDir: String,
    settings: GitHubReleaseSettings,
) {
    val root = Path.of(projectRootDir).toAbsolutePath().normalize()
    val changelog = root.resolve(settings.changelogFile).normalize()
    require(changelog.startsWith(root) && changelog.isRegularFile()) {
        "Changelog must be an existing file inside the module: $changelog"
    }

    val releasePreflight = root.resolve("verification/check_release_consistency.py")
    require(releasePreflight.isRegularFile()) { "Missing release consistency preflight: $releasePreflight" }
    runCommand(root, listOf("python3", releasePreflight.pathString))

    val parser = root.resolve("changelog_parser.sh")
    require(parser.isRegularFile()) { "Missing changelog parser: $parser" }
    val notes = runCommand(root, listOf("bash", parser.pathString, settings.version, changelog.pathString))
        .trim()
    require(notes.isNotEmpty()) { "No CHANGELOG.md section found for ${settings.version}" }

    val tag = "${settings.tagPrefix}${settings.version}"
    val targetCommitish = settings.targetCommitish.ifBlank { settings.version }
    val repository = "${settings.owner}/${settings.repository}"
    if (System.getenv("KSLOG_GITHUB_RELEASE_DRY_RUN").toBoolean()) {
        println("Dry run: GitHub release $repository tag=$tag target=$targetCommitish title=${settings.version}")
        println(notes)
        return
    }

    val token = System.getenv("GITHUB_RELEASE_TOKEN")
        ?: System.getenv("GH_TOKEN")
        ?: System.getenv("GITHUB_TOKEN")
        ?: error("Set GITHUB_RELEASE_TOKEN, GH_TOKEN, or GITHUB_TOKEN before creating a release")

    val notesFile = Files.createTempFile("kslog-release-notes-", ".md")
    try {
        notesFile.writeText(notes)
        val process = ProcessBuilder(
            "gh",
            "release",
            "create",
            tag,
            "--repo",
            repository,
            "--title",
            settings.version,
            "--target",
            targetCommitish,
            "--notes-file",
            notesFile.pathString,
        )
            .directory(root.toFile())
            .redirectErrorStream(true)
        process.environment()["GH_TOKEN"] = token
        val started = process.start()
        val output = started.inputStream.bufferedReader().use { it.readText() }
        val exitCode = started.waitFor()
        check(exitCode == 0) { "gh release create failed with exit code $exitCode:\n$output" }
        print(output)
    } finally {
        notesFile.deleteIfExists()
    }
}

private fun runCommand(workingDirectory: Path, command: List<String>): String {
    val process = ProcessBuilder(command)
        .directory(workingDirectory.toFile())
        .redirectErrorStream(true)
        .start()
    val output = process.inputStream.bufferedReader().use { it.readText() }
    val exitCode = process.waitFor()
    check(exitCode == 0) { "Command failed with exit code $exitCode: ${command.first()}\n$output" }
    return output
}
