package dev.inmo.kslog.common

import kotlin.jvm.JvmInline

@JvmInline
value class TagLogger(val tag: String) : KSLog {
    override fun performLog(level: LogLevel, tag: String?, message: String, throwable: Throwable?) {
        KSLog.performLog(level, tag ?: this.tag, message, throwable)
    }
}
