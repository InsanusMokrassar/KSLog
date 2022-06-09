package dev.inmo.kslog.common


enum class LogLevel {
    DEBUG,
    VERBOSE,
    INFO,
    WARNING,
    ERROR,
    ASSERT,
}

interface KSLog {
    fun performLog(level: LogLevel, tag: String?, message: String, throwable: Throwable?)
    fun performLog(level: LogLevel, message: String, throwable: Throwable?) = performLog(level, null, message, throwable)
    fun performLog(
        level: LogLevel,
        tag: String?,
        throwable: Throwable?,
        messageBuilder: () -> String
    ) = performLog(level, tag, messageBuilder(), throwable)
    suspend fun performLog(
        level: LogLevel,
        tag: String?,
        throwable: Throwable?,
        messageBuilder: suspend () -> String
    ) = performLog(level, tag, messageBuilder(), throwable)
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

expect fun KSLog(
    defaultTag: String,
    filter: MessageFilter = { _, _, _, _ -> true },
    messageFormatter: MessageFormatter = defaultMessageFormatter
): KSLog

fun KSLog(
    defaultTag: String,
    levels: Iterable<LogLevel>,
    messageFormatter: MessageFormatter = defaultMessageFormatter
): KSLog {
    val levels = levels.toSet()
    return KSLog (defaultTag, { l, _, _, _ ->
        l in levels
    }, messageFormatter)
}

fun KSLog(
    defaultTag: String,
    firstLevel: LogLevel,
    secondLevel: LogLevel,
    vararg otherLevels: LogLevel,
    messageFormatter: MessageFormatter = defaultMessageFormatter,
): KSLog = KSLog(defaultTag, setOf(firstLevel, secondLevel, *otherLevels), messageFormatter)

fun KSLog(
    defaultTag: String,
    minLoggingLevel: LogLevel,
    messageFormatter: MessageFormatter = defaultMessageFormatter,
): KSLog = KSLog (
    defaultTag,
    { l, _, _, _ ->
        minLoggingLevel.ordinal <= l.ordinal
    },
    messageFormatter
)
