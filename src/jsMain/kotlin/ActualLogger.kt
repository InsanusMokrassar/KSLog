package dev.inmo.kslog.common

actual fun KSLog(
    defaultTag: String,
    filter: MessageFilter,
    messageFormatter: MessageFormatter
): KSLog = KSLog { l, t, m, e ->
    if (!filter(l, t, m, e)) return@KSLog
    val text = messageFormatter(l, t?:defaultTag, m, e)
    val args = e ?.let {
        arrayOf(text, e)
    } ?: arrayOf(text)
    when (l) {
        LogLevel.DEBUG -> console.log(*args)
        LogLevel.VERBOSE,
        LogLevel.INFO -> console.info(*args)
        LogLevel.WARNING -> console.warn(*args)
        LogLevel.ERROR,
        LogLevel.ASSERT -> console.error(*args)
    }
}
