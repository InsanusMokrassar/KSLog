package dev.inmo.kslog.common

import java.util.logging.Level

fun KSJVMLog(
    defaultTag: String,
    logger: java.util.logging.Logger,
    messageFormatter: MessageFormatter = defaultMessageFormatter,
    filter: (l: LogLevel, t: String?, m: String, Throwable?) -> Boolean
) = KSLog { l, t, m, e ->
    if (!filter(l, t, m, e)) return@KSLog
    val text = messageFormatter(l,t ?: defaultTag,m,e)
    logger.log(
        when(l) {
            LogLevel.DEBUG -> Level.FINEST
            LogLevel.VERBOSE -> Level.FINE
            LogLevel.INFO -> Level.INFO
            LogLevel.WARNING -> Level.WARNING
            LogLevel.ERROR -> Level.SEVERE
            LogLevel.ASSERT -> Level.SEVERE
        },
        text,
        e
    )
}

fun KSJVMLog(
    defaultTag: String,
    logger: java.util.logging.Logger,
    messageFormatter: MessageFormatter = defaultMessageFormatter,
    levels: Iterable<LogLevel>
): KSLog {
    val levels = levels.toSet()
    return KSJVMLog (defaultTag, logger, messageFormatter) { l, _, _, _ ->
        l in levels
    }
}

fun KSJVMLog(
    defaultTag: String,
    logger: java.util.logging.Logger,
    messageFormatter: MessageFormatter = defaultMessageFormatter,
    firstLevel: LogLevel,
    secondLevel: LogLevel,
    vararg otherLevels: LogLevel
): KSLog = KSJVMLog (defaultTag, logger, messageFormatter, setOf(firstLevel, secondLevel, *otherLevels))

fun KSJVMLog(
    defaultTag: String,
    logger: java.util.logging.Logger,
    messageFormatter: MessageFormatter = defaultMessageFormatter,
    minLoggingLevel: LogLevel = LogLevel.values().first()
): KSLog = KSJVMLog (defaultTag, logger, messageFormatter) { l, _, _, _ ->
    minLoggingLevel.ordinal <= l.ordinal
}
