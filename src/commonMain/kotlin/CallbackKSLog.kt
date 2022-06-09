package dev.inmo.kslog.common

class CallbackKSLog(
    private val performLogCallback: (level: LogLevel, tag: String?, message: String, throwable: Throwable?) -> Unit
) : KSLog {
    override fun performLog(level: LogLevel, tag: String?, message: String, throwable: Throwable?) = performLogCallback(level, tag, message, throwable)
}
