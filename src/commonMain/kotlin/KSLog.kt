package dev.inmo.kslog.common

import dev.inmo.kslog.common.filter.filtered
import dev.inmo.kslog.common.utils.plus


enum class LogLevel {
    TRACE,
    DEBUG,
    VERBOSE,
    INFO,
    WARNING,
    ERROR,
    ASSERT,
}

/**
 * Base interface for any logger
 *
 * @see default
 *
 */
interface KSLog {
    /**
     * The only one function required to realization in any inheritor. All other [performLog] functions
     * will call this one by default
     */
    fun performLog(level: LogLevel, tag: String?, message: Any, throwable: Throwable?)

    /**
     * Calls default [performLog] with `tag` == `null`
     */
    fun performLog(level: LogLevel, message: Any, throwable: Throwable?) = performLog(level, null, message, throwable)
    /**
     * Calls default [performLog] with `message` built using [messageBuilder]. This method supposed to be overriden in
     * case when logger supports lazy-loaded messages (like [dev.inmo.kslog.common.filter.FilterKSLog])
     */
    fun performLog(
        level: LogLevel,
        tag: String?,
        throwable: Throwable?,
        messageBuilder: () -> Any
    ) = performLog(level, tag, messageBuilder(), throwable)

    /**
     * Suspendable variant of [performLog] with [messageBuilder]. Uses default [performLog] with `message` built using
     * [messageBuilder] by default
     */
    suspend fun performLogS(
        level: LogLevel,
        tag: String?,
        throwable: Throwable?,
        messageBuilder: suspend () -> Any
    ) = performLog(level, tag, messageBuilder(), throwable)


    companion object : KSLog {
        private var defaultLogger: KSLog? = null

        /**
         * Default logger used in case you are trying to use [KSLog] as a receiver for extensions like [info]
         */
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

internal val printlnLogging: (level: LogLevel, tag: String, message: Any, throwable: Throwable?) -> Unit = { l, t, m, e ->
    println(defaultMessageFormatter(l, t, m, e))
}

/**
 * Simple builder for [DefaultKSLog] logger based on [defaultTag]
 */
fun KSLog(
    defaultTag: String,
): KSLog = DefaultKSLog(defaultTag)

/**
 * Simple builder for [DefaultKSLog] logger based on [defaultTag] and [messageFormatter]
 */
fun KSLog(
    defaultTag: String,
    messageFormatter: MessageFormatter
): KSLog = DefaultKSLog(
    defaultTag,
    messageFormatter
)

@Deprecated("Filtering should be replaced with FilterKSLog")
fun KSLog(
    defaultTag: String,
    filter: MessageFilter,
    messageFormatter: MessageFormatter = defaultMessageFormatter
): KSLog = KSLog (
    defaultTag,
    messageFormatter
).filtered(filter)

/**
 * Building logger using [KSLog] builder based on [defaultTag] and [messageFormatter]. This logger will also filter
 * incoming levels: only levels passed in [levels] param will be logged
 */
fun KSLog(
    defaultTag: String,
    levels: Iterable<LogLevel>,
    messageFormatter: MessageFormatter = defaultMessageFormatter
): KSLog {
    val levelsSet = levels.toSet()
    return KSLog (defaultTag, messageFormatter).filtered { l, _, _ ->
        l in levelsSet
    }
}

/**
 * Building logger using [KSLog] builder based on [defaultTag] and [messageFormatter]. This logger will also filter
 * incoming levels: only levels passed in [firstLevel], [secondLevel] and [otherLevels] param will be logged
 */
fun KSLog(
    defaultTag: String,
    firstLevel: LogLevel,
    secondLevel: LogLevel,
    vararg otherLevels: LogLevel,
    messageFormatter: MessageFormatter = defaultMessageFormatter,
): KSLog = KSLog(defaultTag, setOf(firstLevel, secondLevel, *otherLevels), messageFormatter)

/**
 * Building logger using [KSLog] builder based on [defaultTag] and [messageFormatter]. This logger will also filter
 * incoming levels: only levels above [minLoggingLevel] will be logged
 */
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

/**
 * Setting [KSLog.default] logger to [newDefault]
 */
fun setDefaultKSLog(newDefault: KSLog) { KSLog.default = newDefault }
/**
 * Setting [KSLog.default] logger to new [CallbackKSLog] using [plus] operation
 */
fun addDefaultKSLog(newDefault: KSLog) {
    KSLog.default += newDefault
}
