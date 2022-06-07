package dev.inmo.kslog.common


enum class LogLevel {
    VERBOSE,
    INFO,
    WARNING,
    ERROR,
    ASSERT,
    DEBUG
}

interface Logger {
    fun log (level: LogLevel, message: String, tag: String? = null, throwable: Throwable? = null)
    companion object : Logger {
        var DEFAULT: Logger = Logger("app")
        override fun log(level: LogLevel, message: String, tag: String?, throwable: Throwable?) = DEFAULT.log(level, message, tag, throwable)

        operator fun invoke (log: (level: LogLevel, message: String, tag: String?, throwable: Throwable?) -> Unit) = CallbackLogger(log)
    }
}

class CallbackLogger(
    private val logger: (level: LogLevel, message: String, tag: String?, throwable: Throwable?) -> Unit
) : Logger {
    override fun log(level: LogLevel, message: String, tag: String?, throwable: Throwable?) = logger(level, message, tag, throwable)
}


expect fun Logger(
    defaultTag: String,
    filter: (l: LogLevel, m: String, t: String, Throwable?) -> Boolean
) : Logger

fun Logger(
    defaultTag: String,
    levels: Set<LogLevel> = LogLevel.values().toSet()
): Logger = Logger (defaultTag) { l, _, _, _ ->
    l in levels
}
