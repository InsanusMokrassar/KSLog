/*
 * Copyright 2000-2025 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

/*
 * KSLog modification notice: adapted from JetBrains Kotlin Toolchain v0.13.0
 * build-sources/dokka. KSLog adds versioned KMP documentation settings and
 * project-specific source-link defaults. See ../README.md for provenance.
 */

package dev.inmo.kslog.build.dokka

import org.jetbrains.amper.plugins.Configurable

@Configurable
interface DokkaSettings {
    val moduleName: String get() = "KSLog"
    val moduleVersion: String
    val sourceLinkBaseUrl: String get() = "https://github.com/InsanusMokrassar/KSLog/blob/master/"
    val sourceLinkLineSuffix: String get() = "#L"
    val skipDeprecated: Boolean get() = true
}
