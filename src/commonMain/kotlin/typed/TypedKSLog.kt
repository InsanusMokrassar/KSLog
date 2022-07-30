package dev.inmo.kslog.common.typed

import dev.inmo.kslog.common.*
import kotlin.reflect.KClass

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
