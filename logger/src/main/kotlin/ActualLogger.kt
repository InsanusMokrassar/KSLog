package truth.simple.kmp.logger.common

import android.util.Log

actual fun Logger(
    defaultTag: String,
    filter: (l: LogLevel, m: String, t: String, Throwable?) -> Boolean
): Logger {
    return Logger { l, m, t, e ->
        val tag = t ?: defaultTag
        if (!filter(l, m, t ?: defaultTag, e)) return@Logger
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