package dev.inmo.kslog.common

import dev.inmo.kslog.common.filter.filtered
import java.util.logging.Level
import java.util.logging.Logger

/**
 * Default Java Logger instance used by KSLog on JVM platform
 *
 * Initialized lazily with the name "KSLog"
 */
internal val defaultKSLogLogger by lazy {
    Logger.getLogger("KSLog")
}

/**
 * Extension function to log with KSLog levels using Java Logger
 *
 * Maps KSLog [LogLevel] to Java [Level]:
 * - [LogLevel.TRACE] → [Level.FINEST]
 * - [LogLevel.DEBUG] → [Level.FINER]
 * - [LogLevel.VERBOSE] → [Level.FINE]
 * - [LogLevel.INFO] → [Level.INFO]
 * - [LogLevel.WARNING] → [Level.WARNING]
 * - [LogLevel.ERROR] → [Level.SEVERE]
 * - [LogLevel.ASSERT] → [Level.SEVERE]
 *
 * @param l KSLog log level
 * @param m Message string
 * @param e Optional throwable
 */
internal fun Logger.doLog(
    l: LogLevel, m: String, e: Throwable?
) = log(
    when(l) {
        LogLevel.TRACE -> Level.FINEST
        LogLevel.DEBUG -> Level.FINER
        LogLevel.VERBOSE -> Level.FINE
        LogLevel.INFO -> Level.INFO
        LogLevel.WARNING -> Level.WARNING
        LogLevel.ERROR -> Level.SEVERE
        LogLevel.ASSERT -> Level.SEVERE
    },
    m,
    e
)

@Deprecated("Filtering should be replaced with FilterKSLog")
fun KSLog(
    defaultTag: String,
    logger: Logger,
    filter: MessageFilter,
    messageFormatter: MessageFormatter = defaultMessageFormatter
) = KSLog { l, t, m, e ->
    if (!filter(l, t, e)) return@KSLog
    val text = messageFormatter(l,t ?: defaultTag,m.toString(),e)
    logger.doLog(l, text, e)
}

/**
 * Creates a KSLog that uses a specific Java [Logger] instance
 * 
 * @param defaultTag Default tag to use when tag is not specified
 * @param logger The Java Logger instance to use for logging
 * @param messageFormatter Formatter for log messages (defaults to [defaultMessageFormatter])
 * @return A new [CallbackKSLog] instance wrapping the Java logger
 */
fun KSLog(
    defaultTag: String,
    logger: Logger,
    messageFormatter: MessageFormatter = defaultMessageFormatter
) = KSLog { l, t, m, e ->
    val text = messageFormatter(l,t ?: defaultTag,m.toString(),e)
    logger.doLog(l, text, e)
}

/**
 * Creates a filtered KSLog using a Java [Logger] that only logs specific levels
 * 
 * @param defaultTag Default tag to use when tag is not specified
 * @param logger The Java Logger instance to use for logging
 * @param levels Collection of log levels to include
 * @param messageFormatter Formatter for log messages (defaults to [defaultMessageFormatter])
 * @return A new filtered [KSLog] instance
 */
fun KSLog(
    defaultTag: String,
    logger: Logger,
    levels: Iterable<LogLevel>,
    messageFormatter: MessageFormatter = defaultMessageFormatter
): KSLog {
    val levelsSet = levels.toSet()
    return KSLog (defaultTag, logger, messageFormatter).filtered { l, _, _ -> l in levelsSet }
}

/**
 * Creates a filtered KSLog using a Java [Logger] with specific log levels
 * 
 * @param defaultTag Default tag to use when tag is not specified
 * @param logger The Java Logger instance to use for logging
 * @param firstLevel First log level to include
 * @param secondLevel Second log level to include
 * @param otherLevels Additional log levels to include
 * @param messageFormatter Formatter for log messages (defaults to [defaultMessageFormatter])
 * @return A new filtered [KSLog] instance
 */
fun KSLog(
    defaultTag: String,
    logger: Logger,
    firstLevel: LogLevel,
    secondLevel: LogLevel,
    vararg otherLevels: LogLevel,
    messageFormatter: MessageFormatter = defaultMessageFormatter,
): KSLog = KSLog (defaultTag, logger, setOf(firstLevel, secondLevel, *otherLevels), messageFormatter)

/**
 * Creates a filtered KSLog using a Java [Logger] with a minimum log level
 * 
 * Only logs messages at or above the specified minimum level.
 * 
 * @param defaultTag Default tag to use when tag is not specified
 * @param logger The Java Logger instance to use for logging
 * @param minLoggingLevel Minimum log level to include (defaults to TRACE - the first level)
 * @param messageFormatter Formatter for log messages (defaults to [defaultMessageFormatter])
 * @return A new filtered [KSLog] instance
 */
fun KSLog(
    defaultTag: String,
    logger: Logger,
    minLoggingLevel: LogLevel = LogLevel.values().first(),
    messageFormatter: MessageFormatter = defaultMessageFormatter
): KSLog = KSLog (defaultTag, logger, messageFormatter).filtered { l, _, _ -> minLoggingLevel.ordinal <= l.ordinal }
