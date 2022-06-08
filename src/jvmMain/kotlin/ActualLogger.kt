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
    minLoggingLevel: LogLevel = LogLevel.VERBOSE
): KSLog = KSJVMLog (defaultTag, logger, messageFormatter) { l, _, _, _ ->
    minLoggingLevel.ordinal <= l.ordinal
}
