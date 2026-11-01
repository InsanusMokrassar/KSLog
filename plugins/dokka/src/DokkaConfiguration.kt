/*
 * Copyright 2000-2025 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

/*
 * KSLog modification notice: adapted from JetBrains Kotlin Toolchain v0.13.0
 * build-sources/dokka. KSLog extends the configuration model for its KMP
 * source-set graph and HTML/GFM plugin configuration. See ../README.md.
 */

package dev.inmo.kslog.build.dokka

import kotlinx.serialization.Serializable

@Serializable
internal data class DokkaConfiguration(
    val moduleName: String,
    val moduleVersion: String,
    val outputDir: String,
    val sourceSets: List<DokkaSourceSet>,
    val pluginsClasspath: List<String>,
    val pluginsConfiguration: List<PluginConfiguration>,
    val failOnWarning: Boolean = false,
)

@Serializable
internal data class SourceSetId(
    val scopeId: String,
    val sourceSetName: String,
)

@Serializable
internal data class DokkaSourceSet(
    val sourceSetID: SourceSetId,
    val displayName: String,
    val dependentSourceSets: List<SourceSetId>,
    val classpath: List<String>,
    val sourceRoots: List<String>,
    val documentedVisibilities: List<String> = listOf("PUBLIC"),
    val skipDeprecated: Boolean,
    val jdkVersion: Int,
    val languageVersion: String = "2.4",
    val apiVersion: String = "2.4",
    val analysisPlatform: String,
    val sourceLinks: List<SourceLink>,
)

@Serializable
internal data class SourceLink(
    val localDirectory: String,
    val remoteUrl: String,
    val remoteLineSuffix: String,
)

@Serializable
internal data class PluginConfiguration(
    val fqPluginName: String,
    val serializationFormat: String,
    val values: String,
)

@Serializable
internal data class DokkaBase(
    val footerMessage: String? = null,
)
