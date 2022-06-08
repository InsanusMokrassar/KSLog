package dev.inmo.kslog.common

fun KSJSLog(
    defaultTag: String,
    messageFormatter: MessageFormatter = defaultMessageFormatter,
    filter: (l: LogLevel, t: String?, m: String, Throwable?) -> Boolean
) = KSLog { l, t, m, e ->
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

fun KSJSLog(
    defaultTag: String,
    messageFormatter: MessageFormatter = defaultMessageFormatter,
    levels: Iterable<LogLevel>
): KSLog {
    val levels = levels.toSet()
    return KSJSLog (defaultTag, messageFormatter) { l, _, _, _ ->
        l in levels
    }
}

fun KSJSLog(
    defaultTag: String,
    messageFormatter: MessageFormatter = defaultMessageFormatter,
    firstLevel: LogLevel,
    secondLevel: LogLevel,
    vararg otherLevels: LogLevel
): KSLog = KSJSLog (defaultTag, messageFormatter, setOf(firstLevel, secondLevel, *otherLevels))

fun KSJSLog(
    defaultTag: String,
    messageFormatter: MessageFormatter = defaultMessageFormatter,
    minLoggingLevel: LogLevel = LogLevel.values().first()
): KSLog = KSJSLog (defaultTag, messageFormatter) { l, _, _, _ ->
    minLoggingLevel.ordinal <= l.ordinal
}
