package dev.inmo.kslog.verification

import dev.inmo.kslog.common.CallbackKSLog
import dev.inmo.kslog.common.KSLog
import dev.inmo.kslog.common.LogLevel

fun exercisePublishedApi(): String {
    var captured = ""
    val logger: KSLog = CallbackKSLog { level, tag, message, throwable ->
        captured = listOf(level, tag, message, throwable).joinToString("|")
    }
    logger.performLog(LogLevel.INFO, "consumer", "message", null)
    return captured
}
