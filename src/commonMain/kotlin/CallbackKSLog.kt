package dev.inmo.kslog.common

class CallbackKSLog(
    private val performLogCallback: (level: LogLevel, tag: String?, message: Any, throwable: Throwable?) -> Unit
) : KSLog {
    override fun performLog(level: LogLevel, tag: String?, message: Any, throwable: Throwable?) = performLogCallback(level, tag, message, throwable)
}
