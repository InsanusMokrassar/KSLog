package dev.inmo.kslog.common

import java.util.logging.Level
import java.util.logging.Logger

fun KSLog(
    defaultTag: String,
    logger: Logger,
    filter: MessageFilter = { _, _, _, _ -> true },
    messageFormatter: MessageFormatter = defaultMessageFormatter
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

fun KSLog(
    defaultTag: String,
    logger: Logger,
    levels: Iterable<LogLevel>,
    messageFormatter: MessageFormatter = defaultMessageFormatter
): KSLog {
    val levels = levels.toSet()
    return KSLog (defaultTag, logger, { l, _, _, _ ->
        l in levels
    }, messageFormatter)
}

fun KSLog(
    defaultTag: String,
    logger: Logger,
    firstLevel: LogLevel,
    secondLevel: LogLevel,
    vararg otherLevels: LogLevel,
    messageFormatter: MessageFormatter = defaultMessageFormatter,
): KSLog = KSLog (defaultTag, logger, setOf(firstLevel, secondLevel, *otherLevels), messageFormatter)

fun KSLog(
    defaultTag: String,
    logger: Logger,
    minLoggingLevel: LogLevel = LogLevel.values().first(),
    messageFormatter: MessageFormatter = defaultMessageFormatter
): KSLog = KSLog (defaultTag, logger, { l, _, _, _ ->
    minLoggingLevel.ordinal <= l.ordinal
}, messageFormatter)

actual fun KSLog(
    defaultTag: String,
    filter: MessageFilter,
    messageFormatter: MessageFormatter
): KSLog = KSLog(defaultTag, Logger.getLogger("KSLog"), filter, messageFormatter)
