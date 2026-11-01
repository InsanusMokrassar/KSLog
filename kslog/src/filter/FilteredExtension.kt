package dev.inmo.kslog.common.filter

import dev.inmo.kslog.common.KSLog
import dev.inmo.kslog.common.MessageFilter

/**
 * Creates a [FilterKSLog] that wraps this logger with a message filter
 * 
 * This extension function provides a convenient way to add filtering to any existing logger.
 * The resulting logger will only pass messages to this logger when the [filter] returns `true`.
 * 
 * Example:
 * ```kotlin
 * val logger = KSLog("app").filtered { level, tag, throwable ->
 *     level.ordinal >= LogLevel.INFO.ordinal  // Only log INFO and above
 * }
 * ```
 * 
 * @param filter Predicate function that receives (level, tag, throwable) and returns whether to log
 * @return A new [FilterKSLog] instance wrapping this logger
 * @see FilterKSLog
 */
fun KSLog.filtered(
    filter: MessageFilter
) = FilterKSLog(this, filter)
