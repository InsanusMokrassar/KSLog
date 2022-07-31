package dev.inmo.kslog.common

import dev.inmo.kslog.common.filter.filtered
import dev.inmo.kslog.common.utils.plus


enum class LogLevel {
    DEBUG,
    VERBOSE,
    INFO,
    WARNING,
    ERROR,
    ASSERT,
}

interface KSLog {
    fun performLog(level: LogLevel, tag: String?, message: Any, throwable: Throwable?)
    fun performLog(level: LogLevel, message: Any, throwable: Throwable?) = performLog(level, null, message, throwable)
    fun performLog(
        level: LogLevel,
        tag: String?,
        throwable: Throwable?,
        messageBuilder: () -> Any
    ) = performLog(level, tag, messageBuilder(), throwable)
    suspend fun performLogS(
        level: LogLevel,
        tag: String?,
        throwable: Throwable?,
        messageBuilder: suspend () -> Any
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
        override fun performLog(level: LogLevel, tag: String?, message: Any, throwable: Throwable?) = default.performLog(level, tag, message, throwable)
        override fun performLog(level: LogLevel, message: Any, throwable: Throwable?) = default.performLog(level, message, throwable)
        override fun performLog(
            level: LogLevel,
            tag: String?,
            throwable: Throwable?,
            messageBuilder: () -> Any
        ) = default.performLog(level, tag, throwable, messageBuilder)
        override suspend fun performLogS(
            level: LogLevel,
            tag: String?,
            throwable: Throwable?,
            messageBuilder: suspend () -> Any
        ) = default.performLogS(level, tag, throwable, messageBuilder)
    }
}


operator fun KSLog.invoke(performLogCallback: (level: LogLevel, tag: String?, message: Any, throwable: Throwable?) -> Unit) = CallbackKSLog(performLogCallback)

internal expect val defaultLogging: (level: LogLevel, tag: String, message: Any, throwable: Throwable?) -> Unit

fun KSLog(
    defaultTag: String,
    messageFormatter: MessageFormatter = defaultMessageFormatter
): KSLog = DefaultKSLog(
    defaultTag,
    messageFormatter
)

@Deprecated("Filtering should be replaced with FilterKSLog")
fun KSLog(
    defaultTag: String,
    filter: MessageFilter,
    messageFormatter: MessageFormatter = defaultMessageFormatter
): KSLog = DefaultKSLog(
    defaultTag,
    filter,
    messageFormatter
)

fun KSLog(
    defaultTag: String,
    levels: Iterable<LogLevel>,
    messageFormatter: MessageFormatter = defaultMessageFormatter
): KSLog {
    val levels = levels.toSet()
    return KSLog (defaultTag, messageFormatter).filtered { l, _, _ ->
        l in levels
    }
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
    messageFormatter
).filtered { l, _, _ ->
    minLoggingLevel.ordinal <= l.ordinal
}

fun setDefaultKSLog(newDefault: KSLog) { KSLog.default = newDefault }
fun addDefaultKSLog(newDefault: KSLog) { KSLog.default = KSLog.default + newDefault }
