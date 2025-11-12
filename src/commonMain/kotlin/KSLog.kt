package dev.inmo.kslog.common

import dev.inmo.kslog.common.filter.filtered
import dev.inmo.kslog.common.utils.plus


/**
 * Enumeration of all available log levels ordered from least to most severe
 * 
 * Used to categorize and filter log messages by importance
 */
enum class LogLevel {
    /** Trace level - most detailed logging, typically for tracing code execution */
    TRACE,
    /** Debug level - detailed information useful for debugging */
    DEBUG,
    /** Verbose level - detailed informational messages */
    VERBOSE,
    /** Info level - general informational messages about application state */
    INFO,
    /** Warning level - potentially harmful situations */
    WARNING,
    /** Error level - error events that might still allow the application to continue */
    ERROR,
    /** Assert level - severe errors that should never happen */
    ASSERT,
}

/**
 * Base interface for any logger in KSLog framework
 *
 * This is the core logging interface that all logger implementations must implement.
 * It provides multiple overloads for logging messages with various combinations of parameters.
 * 
 * The interface supports:
 * - Synchronous and suspendable logging
 * - Lazy message evaluation via lambda builders
 * - Optional tags for categorizing logs
 * - Exception/throwable logging
 * - All standard log levels (TRACE, DEBUG, VERBOSE, INFO, WARNING, ERROR, ASSERT)
 * 
 * @see default Default logger instance
 * @see DefaultKSLog Default implementation
 * @see FilterKSLog Logger with filtering capabilities
 * @see TypedKSLog Logger with type-based routing
 */
interface KSLog {
    /**
     * The core logging method that must be implemented by all logger implementations
     * 
     * All other [performLog] overloads will eventually call this method by default.
     * 
     * @param level The severity level of the log message
     * @param tag Optional tag to categorize or identify the source of the log
     * @param message The message to be logged (can be any object, will be converted to string)
     * @param throwable Optional exception or throwable to be logged alongside the message
     */
    fun performLog(level: LogLevel, tag: String?, message: Any, throwable: Throwable?)

    /**
     * Logs a message without a tag
     * 
     * Convenience method that calls the main [performLog] with `tag` set to `null`
     * 
     * @param level The severity level of the log message
     * @param message The message to be logged
     * @param throwable Optional exception or throwable to be logged
     */
    fun performLog(level: LogLevel, message: Any, throwable: Throwable?) = performLog(level, null, message, throwable)
    /**
     * Logs a message with lazy evaluation of the message content
     * 
     * The [messageBuilder] lambda will only be executed if the message is actually logged.
     * This is useful for expensive message construction that should be avoided when the
     * message would be filtered out.
     * 
     * This method should be overridden in logger implementations that support lazy evaluation
     * (like [dev.inmo.kslog.common.filter.FilterKSLog]).
     * 
     * @param level The severity level of the log message
     * @param tag Optional tag to categorize the log
     * @param throwable Optional exception or throwable to be logged
     * @param messageBuilder Lambda that builds the message only when needed
     */
    fun performLog(
        level: LogLevel,
        tag: String?,
        throwable: Throwable?,
        messageBuilder: () -> Any
    ) = performLog(level, tag, messageBuilder(), throwable)

    /**
     * Suspendable variant of [performLog] with lazy message evaluation
     * 
     * Similar to the non-suspending [performLog] with [messageBuilder], but allows the
     * message builder to be a suspending function. This enables logging with messages
     * that require async operations to construct.
     * 
     * By default, executes the suspending [messageBuilder] and calls the regular [performLog].
     * 
     * @param level The severity level of the log message
     * @param tag Optional tag to categorize the log
     * @param throwable Optional exception or throwable to be logged
     * @param messageBuilder Suspending lambda that builds the message only when needed
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
         * Default logger instance used when calling logging extensions without an explicit receiver
         * 
         * If not explicitly set, a default logger with tag "app" will be created automatically.
         * You can customize this by setting your own logger implementation.
         * 
         * @see setDefaultKSLog
         * @see addDefaultKSLog
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


/**
 * Creates a [CallbackKSLog] by invoking a [KSLog] instance with a callback function
 * 
 * This operator allows convenient logger creation syntax:
 * ```kotlin
 * val logger = KSLog { level, tag, message, throwable ->
 *     // Custom logging logic
 * }
 * ```
 * 
 * @param performLogCallback The callback function to handle log messages
 * @return A new [CallbackKSLog] instance
 */
operator fun KSLog.invoke(performLogCallback: (level: LogLevel, tag: String?, message: Any, throwable: Throwable?) -> Unit) = CallbackKSLog(performLogCallback)

/**
 * Simple logging implementation that uses `println` to output formatted log messages
 * 
 * Uses [defaultMessageFormatter] to format messages before outputting to standard output.
 * This is used as the default logging backend for Native and WASM platforms.
 * 
 * @see defaultMessageFormatter
 */
internal val printlnLogging: (level: LogLevel, tag: String, message: Any, throwable: Throwable?) -> Unit = { l, t, m, e ->
    println(defaultMessageFormatter(l, t, m, e))
}

/**
 * Creates a [DefaultKSLog] logger with the specified default tag
 * 
 * @param defaultTag The default tag to use for all log messages from this logger
 * @return A new [DefaultKSLog] instance
 */
fun KSLog(
    defaultTag: String,
): KSLog = DefaultKSLog(defaultTag)

/**
 * Creates a [DefaultKSLog] logger with a custom message formatter
 * 
 * @param defaultTag The default tag to use for all log messages
 * @param messageFormatter Custom formatter for converting log parameters to strings
 * @return A new [DefaultKSLog] instance
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
 * Creates a filtered [KSLog] that only logs messages at specific levels
 * 
 * Builds a logger that will only process log messages whose level is included in the [levels] collection.
 * All other log levels will be filtered out.
 * 
 * @param defaultTag The default tag to use for all log messages
 * @param levels Collection of log levels that should be logged
 * @param messageFormatter Custom formatter for converting log parameters to strings (defaults to [defaultMessageFormatter])
 * @return A new filtered [KSLog] instance
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
 * Creates a filtered [KSLog] that only logs messages at the specified levels
 * 
 * Convenience overload that accepts individual log levels as parameters.
 * 
 * @param defaultTag The default tag to use for all log messages
 * @param firstLevel First log level to include
 * @param secondLevel Second log level to include
 * @param otherLevels Additional log levels to include
 * @param messageFormatter Custom formatter for converting log parameters to strings (defaults to [defaultMessageFormatter])
 * @return A new filtered [KSLog] instance
 */
fun KSLog(
    defaultTag: String,
    firstLevel: LogLevel,
    secondLevel: LogLevel,
    vararg otherLevels: LogLevel,
    messageFormatter: MessageFormatter = defaultMessageFormatter,
): KSLog = KSLog(defaultTag, setOf(firstLevel, secondLevel, *otherLevels), messageFormatter)

/**
 * Creates a filtered [KSLog] that only logs messages at or above a minimum severity level
 * 
 * Uses the ordinal values of [LogLevel] enum to determine which messages to log.
 * For example, if [minLoggingLevel] is INFO, then INFO, WARNING, ERROR, and ASSERT will be logged,
 * while TRACE, DEBUG, and VERBOSE will be filtered out.
 * 
 * @param defaultTag The default tag to use for all log messages
 * @param minLoggingLevel Minimum log level to include (inclusive)
 * @param messageFormatter Custom formatter for converting log parameters to strings (defaults to [defaultMessageFormatter])
 * @return A new filtered [KSLog] instance
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
 * Sets the [KSLog.default] logger to a new instance
 * 
 * This replaces the current default logger completely.
 * 
 * @param newDefault The new default logger to use
 */
fun setDefaultKSLog(newDefault: KSLog) { KSLog.default = newDefault }

/**
 * Adds an additional logger to the [KSLog.default] logger chain
 * 
 * Uses the [plus] operator to combine the current default logger with [newDefault],
 * so that log messages will be sent to both loggers.
 * 
 * @param newDefault Additional logger to add to the default logger chain
 * @see dev.inmo.kslog.common.utils.plus
 */
fun addDefaultKSLog(newDefault: KSLog) {
    KSLog.default += newDefault
}
