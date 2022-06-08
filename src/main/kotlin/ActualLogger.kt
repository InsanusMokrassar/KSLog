package dev.inmo.kslog.common

import android.util.Log

fun KSAndroidLog(
    defaultTag: String,
    filter: (l: LogLevel, t: String, m: String, Throwable?) -> Boolean
) = KSLog { l, t, m, e ->
    if (!filter(l, t ?: defaultTag, m, e)) return@KSLog
    val tag = t ?: defaultTag
    when(l) {
        LogLevel.VERBOSE -> Log.v(tag, m, e)
        LogLevel.INFO -> Log.i(tag, m, e)
        LogLevel.WARNING -> Log.w(tag, m, e)
        LogLevel.ERROR -> Log.e(tag, m, e)
        LogLevel.ASSERT -> Log.wtf(tag, m, e)
        LogLevel.DEBUG -> Log.d(tag, m, e)
    }
}

fun KSAndroidLog(
    defaultTag: String,
    levels: Iterable<LogLevel>
): KSLog {
    val levels = levels.toSet()
    return KSAndroidLog (defaultTag) { l, _, _, _ ->
        l in levels
    }
}

inline fun KSAndroidLog(
    defaultTag: String,
    firstLevel: LogLevel,
    secondLevel: LogLevel,
    vararg otherLevels: LogLevel
): KSLog = KSAndroidLog(defaultTag, setOf(firstLevel, secondLevel, *otherLevels))

fun KSAndroidLog(
    defaultTag: String,
    minLoggingLevel: LogLevel = LogLevel.VERBOSE
): KSLog = KSAndroidLog (defaultTag) { l, _, _, _ ->
    minLoggingLevel.ordinal <= l.ordinal
}
