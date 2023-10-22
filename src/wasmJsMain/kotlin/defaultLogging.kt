package dev.inmo.kslog.common

internal actual val defaultLogging: (level: LogLevel, tag: String, message: Any, throwable: Throwable?) -> Unit
    get() = printlnLogging
