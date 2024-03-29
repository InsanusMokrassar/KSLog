package dev.inmo.kslog.common

import android.util.Log

actual var KSLoggerDefaultPlatformLoggerLambda: (level: LogLevel, tag: String, message: Any, throwable: Throwable?) -> Unit = { l, t, m, e ->
    val messageString = m.toString()
    when(l) {
        LogLevel.TRACE -> Log.d(t, messageString, e)
        LogLevel.DEBUG -> Log.d(t, messageString, e)
        LogLevel.VERBOSE -> Log.v(t, messageString, e)
        LogLevel.INFO -> Log.i(t, messageString, e)
        LogLevel.WARNING -> Log.w(t, messageString, e)
        LogLevel.ERROR -> Log.e(t, messageString, e)
        LogLevel.ASSERT -> Log.wtf(t, messageString, e)
    }
}