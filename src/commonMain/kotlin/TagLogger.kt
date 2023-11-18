package dev.inmo.kslog.common

import kotlin.jvm.JvmInline

/**
 * Logger which will use [tag] as default one in cases when [performLog] have `null` tag
 */
@JvmInline
value class TagLogger(val tag: String) : KSLog {
    override fun performLog(level: LogLevel, tag: String?, message: Any, throwable: Throwable?) {
        KSLog.performLog(level, tag ?: this.tag, message, throwable)
    }
}
