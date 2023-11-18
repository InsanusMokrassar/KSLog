package dev.inmo.kslog.common

/**
 * Default logger for current platform. You may change it, but you should remember that it is global logger
 * used in [KSLog.default] by default
 */
expect var KSLoggerDefaultPlatformLoggerLambda: (level: LogLevel, tag: String, message: Any, throwable: Throwable?) -> Unit