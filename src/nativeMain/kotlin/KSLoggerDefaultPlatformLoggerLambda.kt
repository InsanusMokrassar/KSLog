package dev.inmo.kslog.common

actual var KSLoggerDefaultPlatformLoggerLambda: (level: LogLevel, tag: String, message: Any, throwable: Throwable?) -> Unit =
    printlnLogging