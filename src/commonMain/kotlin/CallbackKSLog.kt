package dev.inmo.kslog.common

typealias SimpleKSLogCallback = (level: LogLevel, tag: String?, message: Any, throwable: Throwable?) -> Unit

class CallbackKSLog(
    private val performLogCallback: SimpleKSLogCallback
) : KSLog {
    override fun performLog(level: LogLevel, tag: String?, message: Any, throwable: Throwable?) = performLogCallback(level, tag, message, throwable)
}

fun KSLog(
    performLogCallback: SimpleKSLogCallback
) = CallbackKSLog(performLogCallback)
