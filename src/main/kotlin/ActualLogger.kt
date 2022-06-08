package dev.inmo.kslog.common

import android.util.Log

actual fun KSLog(
    defaultTag: String,
    filter: MessageFilter,
    messageFormatter: MessageFormatter
): KSLog = KSLog { l, t, m, e ->
    if (!filter(l, t ?: defaultTag, m, e)) return@KSLog
    val tag = t ?: defaultTag
    val text = messageFormatter(l, tag, m, e)
    when(l) {
        LogLevel.DEBUG -> Log.d(tag, text, e)
        LogLevel.VERBOSE -> Log.v(tag, text, e)
        LogLevel.INFO -> Log.i(tag, text, e)
        LogLevel.WARNING -> Log.w(tag, text, e)
        LogLevel.ERROR -> Log.e(tag, text, e)
        LogLevel.ASSERT -> Log.wtf(tag, text, e)
    }
}
