package dev.inmo.kslog.common

/**
 * Function type for formatting log messages
 * 
 * @param l The log level
 * @param t Optional tag for the log message
 * @param m The message object to be logged
 * @param Throwable Optional exception/throwable to be logged
 * @return Formatted string representation of the log message
 */
typealias MessageFormatter = (l: LogLevel, t: String?, m: Any, Throwable?) -> String

/**
 * Helper function to create a [MessageFormatter] from a lambda
 */
inline fun MessageFormatter(noinline formatter: MessageFormatter) = formatter

/**
 * Function type for filtering log messages
 * 
 * @param l The log level
 * @param t Optional tag for the log message
 * @param Throwable Optional exception/throwable associated with the log
 * @return `true` if the message should be logged, `false` to filter it out
 */
typealias MessageFilter = (l: LogLevel, t: String?, Throwable?) -> Boolean

/**
 * Default message formatter that formats logs as: `[LEVEL] tag - message`
 * 
 * Example output: `[LogLevel.INFO] MyTag - Hello world`
 */
val defaultMessageFormatter: MessageFormatter = { l, t, m, _ -> "[$l] ${t ?.let { "$it " } ?: ""}- $m" }

/**
 * Message formatter that includes stack trace of exceptions in the output
 * 
 * Formats logs as: `[LEVEL] tag - message: stacktrace`
 * 
 * Example output: `[LogLevel.ERROR] MyTag - Error occurred: java.lang.Exception: ...`
 */
val defaultMessageFormatterWithErrorPrint: MessageFormatter = { l, t, m, e -> "[$l] ${t ?.let { "$it " } ?: ""}- $m${e ?.let { ": ${it.stackTraceToString()}" } ?: ""}" }
