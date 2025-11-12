package dev.inmo.kslog.common

import android.util.Log

/**
 * Android platform implementation of the default logger
 *
 * Uses Android's `[android.util.Log]` as the logging backend, mapping KSLog levels
 * to Android log levels:
 * - [LogLevel.TRACE] → [Log.d] (debug)
 * - [LogLevel.DEBUG] → [Log.d] (debug)
 * - [LogLevel.VERBOSE] → [Log.v] (verbose)
 * - [LogLevel.INFO] → [Log.i] (info)
 * - [LogLevel.WARNING] → [Log.w] (warning)
 * - [LogLevel.ERROR] → [Log.e] (error)
 * - [LogLevel.ASSERT] → [Log.wtf] (what a terrible failure)
 *
 * Logs can be viewed using `adb logcat` or Android Studio's Logcat window.
 */
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