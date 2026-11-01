package dev.inmo.kslog.common.filter

import dev.inmo.kslog.common.*

/**
 * A logger that filters messages before passing them to a fallback logger
 * 
 * This logger implementation applies a [messageFilter] predicate to all incoming log requests.
 * Only when the filter returns `true` will the message be passed to the [fallbackLogger].
 * 
 * This enables:
 * - Log level filtering (e.g., only log ERROR and above)
 * - Tag-based filtering (e.g., only log from specific components)
 * - Conditional logging based on exception presence
 * - Lazy message evaluation - messages are only constructed if they pass the filter
 * 
 * @param fallbackLogger The underlying logger that will receive filtered messages
 * @param messageFilter Predicate function that determines which messages to log
 * 
 * @see filtered Extension function for convenient filter creation
 */
class FilterKSLog(
    private val fallbackLogger: KSLog,
    private val messageFilter: MessageFilter
) : KSLog {
    override fun performLog(level: LogLevel, tag: String?, message: Any, throwable: Throwable?) {
        if (messageFilter(level, tag, throwable)) {
            fallbackLogger.performLog(level, tag, message, throwable)
        }
    }

    override fun performLog(level: LogLevel, message: Any, throwable: Throwable?) {
        if (messageFilter(level, null, throwable)) {
            fallbackLogger.performLog(level, message, throwable)
        }
    }

    override fun performLog(level: LogLevel, tag: String?, throwable: Throwable?, messageBuilder: () -> Any) {
        if (messageFilter(level, tag, throwable)) {
            fallbackLogger.performLog(level, tag, throwable, messageBuilder)
        }
    }

    override suspend fun performLogS(level: LogLevel, tag: String?, throwable: Throwable?, messageBuilder: suspend () -> Any) {
        if (messageFilter(level, tag, throwable)) {
            fallbackLogger.performLogS(level, tag, throwable, messageBuilder)
        }
    }
}
