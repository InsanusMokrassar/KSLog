package truth.simple.kmp.logger.common

import java.util.logging.Level

actual fun Logger(
    defaultTag: String,
    filter: (l: LogLevel, m: String, t: String, Throwable?) -> Boolean
): Logger {
    val logger = java.util.logging.Logger.getLogger(defaultTag)
    return Logger { l, m, t, e ->
        if (!filter(l, m, t ?: defaultTag, e)) return@Logger
        val currentLogger = t ?.let { java.util.logging.Logger.getLogger(t) } ?: logger
        currentLogger.log(
            when(l) {
                LogLevel.VERBOSE -> Level.FINEST
                LogLevel.INFO -> Level.INFO
                LogLevel.WARNING -> Level.WARNING
                LogLevel.ERROR -> Level.SEVERE
                LogLevel.ASSERT -> Level.OFF
                LogLevel.DEBUG -> Level.ALL
            },
            m,
            e
        )
    }
}