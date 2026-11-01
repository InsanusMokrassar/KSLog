package dev.inmo.kslog.common.utils

import dev.inmo.kslog.common.CallbackKSLog
import dev.inmo.kslog.common.KSLog

/**
 * Combines two loggers into one that sends messages to both
 * 
 * Creates a new logger that will invoke [performLog] on both this logger and the [other] logger
 * for every log message. Both loggers will be called even if one fails.
 * 
 * Error handling:
 * - If this logger throws an exception, it will be rethrown after attempting to log to [other]
 * - If [other] throws an exception, it will also be rethrown
 * - If both throw exceptions, both will be rethrown (first this logger's, then other's)
 * 
 * Example:
 * ```kotlin
 * val combinedLogger = consoleLogger + fileLogger
 * combinedLogger.info("Message")  // Logs to both console and file
 * ```
 * 
 * @param other The second logger to combine with this one
 * @return A new [CallbackKSLog] that logs to both loggers
 * @see addDefaultKSLog Uses this operator to add loggers to the default logger
 */
infix operator fun KSLog.plus(other: KSLog) = CallbackKSLog { l, t, m, e ->
    val resultOfFirst = runCatching {
        this@plus.performLog(l, t, m, e)
    }
    val resultOfSecond = runCatching {
        other.performLog(l, t, m, e)
    }
    resultOfFirst.onFailure { throw it }
    resultOfSecond.onFailure { throw it }
}
