package dev.inmo.kslog.common

fun KSLog(
    messageFormatter: (l: LogLevel, t: String?, m: String, Throwable?) -> String,
    filter: (l: LogLevel, t: String?, m: String, Throwable?) -> Boolean
) = KSLog { l, t, m, e ->
    if (!filter(l, t, m, e)) return@KSLog
    val text = messageFormatter(l,t,m,e)
    when (l) {
        LogLevel.VERBOSE,
        LogLevel.INFO -> console.info(text, e)
        LogLevel.WARNING -> console.warn(text, e)
        LogLevel.ERROR,
        LogLevel.ASSERT -> console.error(text, e)
        LogLevel.DEBUG -> console.log(text, e)
    }
}

actual fun KSLog(
    defaultTag: String,
    filter: (l: LogLevel, t: String, m: String, Throwable?) -> Boolean
): KSLog {
    return KSLog(
        { l, t, m, _ -> "[$l] ${t ?: defaultTag} - $m" },
        { l, t, m, e -> filter(l, t ?: defaultTag, m, e) }
    )
}
