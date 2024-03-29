package dev.inmo.kslog.common.filter

import dev.inmo.kslog.common.*

/**
 * In its [performLog]/[performLogS] methods do logging only in cases when [messageFilter] returns true for incoming
 * parameters
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
