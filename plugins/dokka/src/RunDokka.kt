/*
 * Copyright 2000-2025 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

/*
 * KSLog modification notice: adapted from JetBrains Kotlin Toolchain v0.13.0
 * build-sources/dokka. KSLog adds KMP source sets, HTML/GFM entry points, and
 * project-confined, symlink-safe generated-output cleanup. See ../README.md.
 */

package dev.inmo.kslog.build.dokka

import kotlinx.serialization.json.Json
import org.jetbrains.amper.plugins.Classpath
import org.jetbrains.amper.plugins.ExecutionAvoidance
import org.jetbrains.amper.plugins.Input
import org.jetbrains.amper.plugins.Output
import org.jetbrains.amper.plugins.TaskAction
import java.nio.file.Files
import java.nio.file.LinkOption
import java.nio.file.Path
import kotlin.concurrent.thread
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.createDirectories
import kotlin.io.path.deleteIfExists
import kotlin.io.path.deleteRecursively
import kotlin.io.path.exists
import kotlin.io.path.pathString
import kotlin.io.path.writeText

@TaskAction(ExecutionAvoidance.Disabled)
fun dokkaHtml(
    projectRootDir: String,
    moduleRootDir: String,
    @Input moduleCompileClasspath: Classpath,
    @Input runtimeClasspath: Classpath,
    @Input pluginsClasspath: Classpath,
    @Output outputDir: Path,
    settings: DokkaSettings,
) = runDokka(
    projectRootDir,
    moduleRootDir,
    moduleCompileClasspath,
    runtimeClasspath,
    pluginsClasspath,
    outputDir,
    settings,
)

@TaskAction(ExecutionAvoidance.Disabled)
fun dokkaGfm(
    projectRootDir: String,
    moduleRootDir: String,
    @Input moduleCompileClasspath: Classpath,
    @Input runtimeClasspath: Classpath,
    @Input pluginsClasspath: Classpath,
    @Output outputDir: Path,
    settings: DokkaSettings,
) = runDokka(
    projectRootDir,
    moduleRootDir,
    moduleCompileClasspath,
    runtimeClasspath,
    pluginsClasspath,
    outputDir,
    settings,
)

@OptIn(ExperimentalPathApi::class)
private fun runDokka(
    projectRootDir: String,
    moduleRootDir: String,
    moduleCompileClasspath: Classpath,
    runtimeClasspath: Classpath,
    pluginsClasspath: Classpath,
    outputDir: Path,
    settings: DokkaSettings,
) {
    val normalizedRoot = Path.of(moduleRootDir).toRealPath()
    val normalizedOutput = prepareDokkaOutput(projectRootDir, outputDir)

    val commonId = SourceSetId(settings.moduleName, "commonMain")
    val sourceLink = SourceLink(
        localDirectory = normalizedRoot.pathString,
        remoteUrl = settings.sourceLinkBaseUrl,
        remoteLineSuffix = settings.sourceLinkLineSuffix,
    )
    val analysisClasspaths = resolveDokkaAnalysisClasspaths(moduleCompileClasspath.resolvedFiles)

    fun sourceSet(
        directoryName: String,
        sourceSetName: String,
        displayName: String,
        analysisPlatform: String,
        dependentOnCommon: Boolean,
        classpath: List<String> = emptyList(),
    ): DokkaSourceSet? {
        val sourceRoot = normalizedRoot.resolve(directoryName)
        if (!sourceRoot.exists()) return null
        return DokkaSourceSet(
            sourceSetID = SourceSetId(settings.moduleName, sourceSetName),
            displayName = displayName,
            dependentSourceSets = if (dependentOnCommon) listOf(commonId) else emptyList(),
            classpath = classpath,
            sourceRoots = listOf(sourceRoot.pathString),
            skipDeprecated = settings.skipDeprecated,
            jdkVersion = if (displayName == "JVM") 8 else 17,
            analysisPlatform = analysisPlatform,
            sourceLinks = listOf(sourceLink),
        )
    }

    val androidClasspath = buildList {
        addAll(analysisClasspaths.jvm)
        val sdkRoot = System.getenv("ANDROID_HOME") ?: System.getenv("ANDROID_SDK_ROOT")
        if (sdkRoot != null) {
            val androidJar = Path.of(sdkRoot).resolve("platforms/android-37/android.jar")
            if (Files.isRegularFile(androidJar)) add(androidJar.pathString)
        }
    }

    val sourceSets = listOfNotNull(
        sourceSet(
            "src",
            "commonMain",
            "Common",
            "common",
            dependentOnCommon = false,
            classpath = analysisClasspaths.commonNative,
        ),
        sourceSet(
            "src@jvm",
            "jvmMain",
            "JVM",
            "jvm",
            dependentOnCommon = true,
            classpath = analysisClasspaths.jvm,
        ),
        sourceSet("src@android", "androidMain", "Android", "jvm", dependentOnCommon = true, classpath = androidClasspath),
        sourceSet("src@js", "jsMain", "JS", "js", dependentOnCommon = true, classpath = analysisClasspaths.js),
        sourceSet(
            "src@wasmJs",
            "wasmJsMain",
            "WasmJS",
            "js",
            dependentOnCommon = true,
            classpath = analysisClasspaths.wasmJs,
        ),
        sourceSet(
            "src@native",
            "nativeMain",
            "Native",
            "native",
            dependentOnCommon = true,
            classpath = analysisClasspaths.commonNative,
        ),
    )

    val configuration = DokkaConfiguration(
        moduleName = settings.moduleName,
        moduleVersion = settings.moduleVersion,
        outputDir = normalizedOutput.pathString,
        sourceSets = sourceSets,
        pluginsClasspath = pluginsClasspath.resolvedFiles.map { it.pathString },
        pluginsConfiguration = listOf(
            PluginConfiguration(
                fqPluginName = "org.jetbrains.dokka.base.DokkaBase",
                serializationFormat = "JSON",
                values = Json.encodeToString(DokkaBase(footerMessage = "KSLog ${settings.moduleVersion}")),
            )
        ),
    )

    val configurationFile = Files.createTempFile("kslog-dokka-", ".json")
    try {
        configurationFile.writeText(Json.encodeToString(configuration))
        val cliJar = runtimeClasspath.resolvedFiles.single()
        val commandLine = listOf(
            ProcessHandle.current().info().command().orElse("java"),
            "-jar",
            cliJar.pathString,
            configurationFile.pathString,
        )
        val process = ProcessBuilder(commandLine)
            .directory(normalizedRoot.toFile())
            .start()
        val capture = listOf(
            thread { process.inputStream.copyTo(System.out) },
            thread { process.errorStream.copyTo(System.err) },
        )
        val exitCode = process.waitFor()
        capture.forEach { it.join() }
        check(exitCode == 0) { "Dokka terminated with exit code $exitCode" }
        requireNoUnresolvedTypes(normalizedOutput)
    } finally {
        configurationFile.deleteIfExists()
    }
}

internal data class DokkaAnalysisClasspaths(
    val commonNative: List<String>,
    val jvm: List<String>,
    val js: List<String>,
    val wasmJs: List<String>,
)

internal fun resolveDokkaAnalysisClasspaths(moduleClasspath: List<Path>): DokkaAnalysisClasspaths {
    val kotlinStdlib = checkNotNull(
        moduleClasspath.firstOrNull { path ->
            val version = path.parent?.fileName?.toString()
            version != null &&
                path.parent?.parent?.fileName?.toString() == "kotlin-stdlib" &&
                path.fileName?.toString() == "kotlin-stdlib-$version.jar"
        }
    ) {
        "Cannot locate the Kotlin JVM stdlib in the module compile classpath; " +
            "it is required to discover the matching JS and WasmJS analysis KLIBs"
    }
    val versionDirectory = checkNotNull(kotlinStdlib.parent)
    val version = versionDirectory.fileName.toString()
    val kotlinGroupDirectory = checkNotNull(versionDirectory.parent?.parent)
    val mavenCacheDirectory = checkNotNull(kotlinGroupDirectory.parent?.parent?.parent)
    check(mavenCacheDirectory.fileName.toString() == ".m2.cache") {
        "Unexpected Kotlin Toolchain Maven cache layout for $kotlinStdlib"
    }
    val transformedStdlibDirectory = checkNotNull(mavenCacheDirectory.parent)
        .resolve("kotlin/kotlinTransformedMetadataLibraries/org.jetbrains.kotlin/kotlin-stdlib/$version")
    val commonStdlib = if (Files.isDirectory(transformedStdlibDirectory)) {
        Files.walk(transformedStdlibDirectory, 2).use { paths ->
            paths
                .filter { Files.isRegularFile(it) }
                .filter { it.fileName.toString() == "kotlin-stdlib-commonMain-$version.klib" }
                .sorted()
                .findFirst()
                .orElse(null)
        }
    } else {
        null
    }

    fun platformStdlib(artifactId: String): Path = kotlinGroupDirectory
        .resolve(artifactId)
        .resolve(version)
        .resolve("$artifactId-$version.klib")

    val jsStdlib = platformStdlib("kotlin-stdlib-js")
    val wasmJsStdlib = platformStdlib("kotlin-stdlib-wasm-js")
    val missing = listOfNotNull(jsStdlib, wasmJsStdlib).filterNot(Files::isRegularFile)
    check(commonStdlib != null && missing.isEmpty()) {
        "Missing Kotlin analysis KLIB(s) for Dokka below $transformedStdlibDirectory or in " +
            "${missing.joinToString()}. " +
            "Run `./kotlin build -m kslog -p js -p wasmJs` before documentation generation."
    }

    return DokkaAnalysisClasspaths(
        commonNative = listOf(commonStdlib.pathString),
        jvm = moduleClasspath.map { it.pathString },
        js = listOf(jsStdlib.pathString),
        wasmJs = listOf(wasmJsStdlib.pathString),
    )
}

internal fun requireNoUnresolvedTypes(outputDir: Path) {
    val unresolvedFiles = Files.walk(outputDir).use { paths ->
        paths
            .filter { Files.isRegularFile(it) }
            .filter { path ->
                path.fileName.toString().substringAfterLast('.', "") in setOf("html", "json", "md")
            }
            .filter { Files.readString(it).contains("Error class: unknown class") }
            .map { outputDir.relativize(it).pathString }
            .sorted()
            .toList()
    }
    check(unresolvedFiles.isEmpty()) {
        "Dokka generated unresolved type placeholders in ${unresolvedFiles.size} file(s): " +
            unresolvedFiles.joinToString(limit = 10)
    }
}

@OptIn(ExperimentalPathApi::class)
internal fun prepareDokkaOutput(projectRootDir: String, outputDir: Path): Path {
    val configuredProjectRoot = Path.of(projectRootDir).toAbsolutePath().normalize()
    require(Files.isDirectory(configuredProjectRoot, LinkOption.NOFOLLOW_LINKS)) {
        "Project root must be a real directory, not a symbolic link: $configuredProjectRoot"
    }

    val configuredGeneratedRoot = configuredProjectRoot.resolve("build/dokka").normalize()
    val configuredOutput = outputDir.toAbsolutePath().normalize()
    require(configuredOutput.parent == configuredGeneratedRoot) {
        "Refusing to clean Dokka output outside a direct child of $configuredGeneratedRoot: $configuredOutput"
    }

    val realProjectRoot = configuredProjectRoot.toRealPath()
    val relativeOutput = configuredProjectRoot.relativize(configuredOutput)
    val generatedRoot = realProjectRoot.resolve("build/dokka").normalize()
    val output = realProjectRoot.resolve(relativeOutput).normalize()
    require(output.parent == generatedRoot) {
        "Resolved Dokka output escaped $generatedRoot: $output"
    }

    requireNoSymbolicLinkAncestors(realProjectRoot, output)
    Files.createDirectories(generatedRoot)
    requireNoSymbolicLinkAncestors(realProjectRoot, output)

    val realGeneratedRoot = generatedRoot.toRealPath()
    require(realGeneratedRoot.startsWith(realProjectRoot)) {
        "Resolved Dokka root escaped the project: $realGeneratedRoot"
    }

    if (Files.exists(output, LinkOption.NOFOLLOW_LINKS)) {
        requireNoSymbolicLinkAncestors(realProjectRoot, output)
        val realOutput = output.toRealPath()
        require(realOutput.parent == realGeneratedRoot) {
            "Resolved Dokka output escaped $realGeneratedRoot: $realOutput"
        }
        output.deleteRecursively()
    }

    output.createDirectories()
    requireNoSymbolicLinkAncestors(realProjectRoot, output)
    val createdOutput = output.toRealPath()
    require(createdOutput.parent == realGeneratedRoot) {
        "Created Dokka output escaped $realGeneratedRoot: $createdOutput"
    }
    return output
}

private fun requireNoSymbolicLinkAncestors(root: Path, target: Path) {
    require(target.startsWith(root)) { "Path is outside the project root: $target" }
    var current = root
    for (component in root.relativize(target)) {
        current = current.resolve(component)
        require(!Files.isSymbolicLink(current)) {
            "Refusing Dokka cleanup through symbolic link: $current"
        }
    }
}
