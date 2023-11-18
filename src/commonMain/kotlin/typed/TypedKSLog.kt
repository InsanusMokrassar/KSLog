package dev.inmo.kslog.common.typed

import dev.inmo.kslog.common.*
import kotlin.reflect.KClass

/**
 * Uses [typedLoggers] [Map] to determine, where incoming __message__s should be sent. If there is no [KClass] key for
 * incoming message in [typedLoggers], logger will use logger by `null` key if exists. If there is no default logger
 * (by `null` key), logging will be skipped
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
