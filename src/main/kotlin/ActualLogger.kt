package dev.inmo.kslog.common

import android.util.Log


internal actual val defaultLogging: (level: LogLevel, tag: String, message: String, throwable: Throwable?) -> Unit = { l, t, m, e ->
    when(l) {
        LogLevel.DEBUG -> Log.d(t, m, e)
        LogLevel.VERBOSE -> Log.v(t, m, e)
        LogLevel.INFO -> Log.i(t, m, e)
        LogLevel.WARNING -> Log.w(t, m, e)
        LogLevel.ERROR -> Log.e(t, m, e)
        LogLevel.ASSERT -> Log.wtf(t, m, e)
    }
}
