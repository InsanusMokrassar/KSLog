package dev.inmo.kslog.build.dokka

import java.nio.file.Files
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.createDirectories
import kotlin.io.path.deleteRecursively
import kotlin.io.path.exists
import kotlin.io.path.readText
import kotlin.io.path.writeText
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalPathApi::class)
class RunDokkaOutputGuardTest {
    @Test
    fun rejectsSymlinkedDokkaAncestorWithoutTouchingOutsideFiles() {
        val projectRoot = Files.createTempDirectory("kslog-dokka-project-")
        val outside = Files.createTempDirectory("kslog-dokka-outside-")
        val sentinel = outside.resolve("sentinel.txt")
        sentinel.writeText("preserve me")
        val build = projectRoot.resolve("build").createDirectories()
        val dokkaLink = build.resolve("dokka")
        Files.createSymbolicLink(dokkaLink, outside)

        try {
            val failure = assertFailsWith<IllegalArgumentException> {
                prepareDokkaOutput(projectRoot.toString(), dokkaLink.resolve("html"))
            }
            assertTrue(failure.message.orEmpty().contains("symbolic link"))
            assertEquals("preserve me", sentinel.readText())
        } finally {
            Files.deleteIfExists(dokkaLink)
            projectRoot.deleteRecursively()
            outside.deleteRecursively()
        }
    }

    @Test
    fun cleansOnlyTheSelectedGeneratedOutputDirectory() {
        val projectRoot = Files.createTempDirectory("kslog-dokka-project-")
        val html = projectRoot.resolve("build/dokka/html").createDirectories()
        val stale = html.resolve("stale.txt")
        stale.writeText("old")
        val gfmSentinel = projectRoot.resolve("build/dokka/gfm/sentinel.txt")
        gfmSentinel.parent.createDirectories()
        gfmSentinel.writeText("keep")

        try {
            val prepared = prepareDokkaOutput(projectRoot.toString(), html)
            assertEquals(html.toRealPath(), prepared.toRealPath())
            assertTrue(prepared.exists())
            assertFalse(stale.exists())
            assertEquals("keep", gfmSentinel.readText())
        } finally {
            projectRoot.deleteRecursively()
        }
    }

    @Test
    fun rejectsGeneratedDocumentationWithUnresolvedTypePlaceholders() {
        val output = Files.createTempDirectory("kslog-dokka-output-")
        val generatedPage = output.resolve("broken.md")
        generatedPage.writeText("fun example(): &lt;Error class: unknown class&gt;")

        try {
            val failure = assertFailsWith<IllegalStateException> {
                requireNoUnresolvedTypes(output)
            }
            assertTrue(failure.message.orEmpty().contains("broken.md"))
        } finally {
            output.deleteRecursively()
        }
    }

    @Test
    fun resolvesMatchingCommonJsAndWasmAnalysisKlibsFromTheToolchainCache() {
        val cache = Files.createTempDirectory("kslog-dokka-cache-")
        val kotlinGroup = cache.resolve(".m2.cache/org/jetbrains/kotlin")
        val version = "2.4.20"
        val kotlinStdlib = kotlinGroup
            .resolve("kotlin-stdlib/$version/kotlin-stdlib-$version.jar")
        val commonStdlib = cache.resolve(
            "kotlin/kotlinTransformedMetadataLibraries/org.jetbrains.kotlin/" +
                "kotlin-stdlib/$version/test-hash/kotlin-stdlib-commonMain-$version.klib"
        )
        val jsStdlib = kotlinGroup
            .resolve("kotlin-stdlib-js/$version/kotlin-stdlib-js-$version.klib")
        val wasmJsStdlib = kotlinGroup
            .resolve("kotlin-stdlib-wasm-js/$version/kotlin-stdlib-wasm-js-$version.klib")
        val dependency = cache.resolve("dependency.jar")
        listOf(kotlinStdlib, commonStdlib, jsStdlib, wasmJsStdlib).forEach {
            it.parent.createDirectories()
            it.writeText("fixture")
        }
        dependency.writeText("fixture")

        try {
            val classpaths = resolveDokkaAnalysisClasspaths(listOf(kotlinStdlib, dependency))
            assertEquals(listOf(commonStdlib.toString()), classpaths.commonNative)
            assertEquals(listOf(kotlinStdlib.toString(), dependency.toString()), classpaths.jvm)
            assertEquals(listOf(jsStdlib.toString()), classpaths.js)
            assertEquals(listOf(wasmJsStdlib.toString()), classpaths.wasmJs)
        } finally {
            cache.deleteRecursively()
        }
    }
}
