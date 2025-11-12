package dev.inmo.kslog.common

/**
 * WebAssembly JavaScript platform implementation of the default logger
 *
 * Uses [printlnLogging] which formats and outputs logs using `[println]`.
 *
 * @see printlnLogging
 */
actual var KSLoggerDefaultPlatformLoggerLambda: (level: LogLevel, tag: String, message: Any, throwable: Throwable?) -> Unit =
    printlnLogging