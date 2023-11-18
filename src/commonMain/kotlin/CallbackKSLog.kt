package dev.inmo.kslog.common

import kotlin.jvm.JvmInline

typealias SimpleKSLogCallback = (level: LogLevel, tag: String?, message: Any, throwable: Throwable?) -> Unit

/**
 * Creates simple [KSLog] which will pass all incoming [performLog] calls to [performLogCallback]
 */
@JvmInline
value class CallbackKSLog(
    private val performLogCallback: SimpleKSLogCallback
) : KSLog {
    override fun performLog(level: LogLevel, tag: String?, message: Any, throwable: Throwable?) = performLogCallback(level, tag, message, throwable)
}

/**
 * Creating [CallbackKSLog] using [performLogCallback] as an argument for constructor
 */
fun KSLog(
    performLogCallback: SimpleKSLogCallback
) = CallbackKSLog(performLogCallback)
