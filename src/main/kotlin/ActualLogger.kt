package dev.inmo.kslog.common

import android.util.Log

actual fun KSLog(
    defaultTag: String,
    filter: (l: LogLevel, t: String, m: String, Throwable?) -> Boolean
): KSLog {
    return KSLog { l, t, m, e ->
        val tag = t ?: defaultTag
        if (!filter(l, t ?: defaultTag, m, e)) return@KSLog
        when(l) {
            LogLevel.VERBOSE -> Log.v(tag, m, e)
            LogLevel.INFO -> Log.i(tag, m, e)
            LogLevel.WARNING -> Log.w(tag, m, e)
            LogLevel.ERROR -> Log.e(tag, m, e)
            LogLevel.ASSERT -> Log.wtf(tag, m, e)
            LogLevel.DEBUG -> Log.d(tag, m, e)
        }
    }
}
