package dev.inmo.kslog.common

import java.util.logging.Level
import java.util.logging.Logger

private val defaultKSLogLogger by lazy {
    Logger.getLogger("KSLog")
}
private fun Logger.doLog(
    l: LogLevel, t: String, m: String, e: Throwable?
) = log(
    when(l) {
        LogLevel.DEBUG -> Level.FINEST
        LogLevel.VERBOSE -> Level.FINE
        LogLevel.INFO -> Level.INFO
        LogLevel.WARNING -> Level.WARNING
        LogLevel.ERROR -> Level.SEVERE
        LogLevel.ASSERT -> Level.SEVERE
    },
    m,
    e
)
internal actual val defaultLogging: (level: LogLevel, tag: String, message: String, throwable: Throwable?) -> Unit = { l, t, m, e ->
    defaultKSLogLogger.doLog(l, t, m, e)
}

fun KSLog(
    defaultTag: String,
    logger: Logger,
    filter: MessageFilter = { _, _, _ -> true },
    messageFormatter: MessageFormatter = defaultMessageFormatter
) = KSLog { l, t, m, e ->
    if (!filter(l, t, e)) return@KSLog
    val text = messageFormatter(l,t ?: defaultTag,m,e)
    logger.doLog(l, t ?: defaultTag, text, e)
}

fun KSLog(
    defaultTag: String,
    logger: Logger,
    levels: Iterable<LogLevel>,
    messageFormatter: MessageFormatter = defaultMessageFormatter
): KSLog {
    val levels = levels.toSet()
    return KSLog (defaultTag, logger, { l, _, _ -> l in levels }, messageFormatter)
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
): KSLog = KSLog (defaultTag, logger, { l, _, _ -> minLoggingLevel.ordinal <= l.ordinal }, messageFormatter)