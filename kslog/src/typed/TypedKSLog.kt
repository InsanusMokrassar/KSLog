package dev.inmo.kslog.common.typed

import dev.inmo.kslog.common.*
import kotlin.reflect.KClass

/**
 * A logger that routes messages to different loggers based on the message type
 * 
 * This logger uses the Kotlin class ([KClass]) of the log message to determine which logger to use.
 * This enables type-based routing of log messages, allowing different message types to be
 * handled by different loggers.
 * 
 * The routing works as follows:
 * 1. Extract the [KClass] from the incoming message object
 * 2. Look up the class in [typedLoggers] map
 * 3. If found, use that logger
 * 4. If not found, fall back to the logger with `null` key (if it exists)
 * 5. If no default logger exists, skip logging
 * 
 * Example use case: Route structured log events to different destinations based on their type.
 * 
 * @param typedLoggers Map from message class to logger. Use `null` key for the default/fallback logger
 * 
 * @see TypedKSLogBuilder Builder for creating typed loggers
 * @see buildTypedLogger DSL function for convenient creation
 */
class TypedKSLog(
    private val typedLoggers: Map<KClass<*>?, KSLog>
) : KSLog {
    override fun performLog(level: LogLevel, tag: String?, message: Any, throwable: Throwable?) {
        (typedLoggers[message::class] ?: typedLoggers[null]) ?.performLog(level, tag, message, throwable)
    }
    override fun performLog(level: LogLevel, message: Any, throwable: Throwable?) {
        (typedLoggers[message::class] ?: typedLoggers[null]) ?.performLog(level, message, throwable)
    }
    override fun performLog(level: LogLevel, tag: String?, throwable: Throwable?, messageBuilder: () -> Any) {
        val message = messageBuilder()
        (typedLoggers[message::class] ?: typedLoggers[null]) ?.performLog(level, tag, message, throwable)
    }
    override suspend fun performLogS(level: LogLevel, tag: String?, throwable: Throwable?, messageBuilder: suspend () -> Any) {
        val message = messageBuilder()
        (typedLoggers[message::class] ?: typedLoggers[null]) ?.performLog(level, tag, message, throwable)
    }
}
