package truth.simple.kmp.logger.common

fun Logger(
    messageFormatter: (l: LogLevel, m: String, t: String?, Throwable?) -> String,
    filter: (l: LogLevel, m: String, t: String?, Throwable?) -> Boolean
) = Logger { l, m, t, e ->
    if (!filter(l, m, t, e)) return@Logger
    val text = messageFormatter(l,m,t,e)
    when (l) {
        LogLevel.VERBOSE,
        LogLevel.INFO -> console.info(text, e)
        LogLevel.WARNING -> console.warn(text, e)
        LogLevel.ERROR,
        LogLevel.ASSERT -> console.error(text, e)
        LogLevel.DEBUG -> console.log(text, e)
    }

}

actual fun Logger(
    defaultTag: String,
    filter: (l: LogLevel, m: String, t: String, Throwable?) -> Boolean
): Logger {
    return Logger(
        { l, m, t, e -> "[$l] ${t ?: defaultTag} - $m" },
        { l, m, t, e -> filter(l, m, t ?: defaultTag, e) }
    )
}