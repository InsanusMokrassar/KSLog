package dev.inmo.kslog.common


enum class LogLevel {
    VERBOSE,
    INFO,
    WARNING,
    ERROR,
    ASSERT,
    DEBUG
}

interface KSLog {
    fun performLog(level: LogLevel, tag: String?, message: String, throwable: Throwable?)
    companion object : KSLog {
        private var defaultLogger: KSLog? = null
        var default: KSLog
            get() {
                return defaultLogger ?: KSLog("app").also {
                    defaultLogger = it
                }
            }
            set(value) {
                defaultLogger = value
            }
        override fun performLog(level: LogLevel, tag: String?, message: String, throwable: Throwable?) = default.performLog(level, tag, message, throwable)
    }
}


operator fun KSLog.invoke(performLogCallback: (level: LogLevel, tag: String?, message: String, throwable: Throwable?) -> Unit) = CallbackKSLog(performLogCallback)

class CallbackKSLog(
    private val performLogCallback: (level: LogLevel, tag: String?, message: String, throwable: Throwable?) -> Unit
) : KSLog {
    override fun performLog(level: LogLevel, tag: String?, message: String, throwable: Throwable?) = performLogCallback(level, tag, message, throwable)
}


expect fun KSLog(
    defaultTag: String,
    filter: (l: LogLevel, t: String, m: String, Throwable?) -> Boolean
) : KSLog

fun KSLog(
    defaultTag: String,
    levels: Iterable<LogLevel>
): KSLog {
    val levels = levels.toSet()
    return KSLog (defaultTag) { l, _, _, _ ->
        l in levels
    }
}

inline fun KSLog(
    defaultTag: String,
    firstLevel: LogLevel,
    secondLevel: LogLevel,
    vararg otherLevels: LogLevel
): KSLog = KSLog(defaultTag, setOf(firstLevel, secondLevel, *otherLevels))

fun KSLog(
    defaultTag: String,
    minLoggingLevel: LogLevel = LogLevel.VERBOSE
): KSLog = KSLog (defaultTag) { l, _, _, _ ->
    minLoggingLevel.ordinal <= l.ordinal
}
