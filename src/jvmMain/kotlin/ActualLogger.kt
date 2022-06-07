package dev.inmo.kslog.common

import java.util.logging.Level

fun KSLog(
    logger: java.util.logging.Logger,
    filter: (l: LogLevel, t: String?, m: String, Throwable?) -> Boolean,
    messageFormatter: (l: LogLevel, t: String?, m: String, Throwable?) -> String
) = KSLog { l, t, m, e ->
    if (!filter(l, t, m, e)) return@KSLog
    val text = messageFormatter(l,t,m,e)
    logger.log(
        when(l) {
            LogLevel.VERBOSE -> Level.FINEST
            LogLevel.INFO -> Level.INFO
            LogLevel.WARNING -> Level.WARNING
            LogLevel.ERROR -> Level.SEVERE
            LogLevel.ASSERT -> Level.OFF
            LogLevel.DEBUG -> Level.ALL
        },
        text,
        e
    )
}

fun KSLog(
    defaultTag: String,
    logger: java.util.logging.Logger,
    filter: (l: LogLevel, t: String, m: String, Throwable?) -> Boolean = { _, _, _, _ -> true }
): KSLog {
    return KSLog(
        logger,
        { l, t, m, e -> filter(l, t ?: defaultTag, m, e) }
    ) { _, t, m, _ -> "${t ?: defaultTag} - $m" }
}

actual fun KSLog(
    defaultTag: String,
    filter: (l: LogLevel, t: String, m: String, Throwable?) -> Boolean
): KSLog {
    return KSLog(
        defaultTag,
        logger = java.util.logging.Logger.getAnonymousLogger(),
        filter = { l, t, m, e -> filter(l, t, m, e) }
    )
}
