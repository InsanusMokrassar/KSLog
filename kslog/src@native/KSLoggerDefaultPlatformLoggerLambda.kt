package dev.inmo.kslog.common

/**
 * Native platform implementation of the default logger
 *
 * Uses [printlnLogging] which formats and outputs logs using `[println]`.
 * Logs are written to standard output.
 *
 * @see printlnLogging
 */
actual var KSLoggerDefaultPlatformLoggerLambda: (level: LogLevel, tag: String, message: Any, throwable: Throwable?) -> Unit =
    printlnLogging