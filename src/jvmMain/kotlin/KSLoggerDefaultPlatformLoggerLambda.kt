package dev.inmo.kslog.common

/**
 * JVM platform implementation of the default logger
 *
 * Uses `[java.util.logging.Logger]` as the logging backend, mapping KSLog levels
 * to Java logging levels.
 *
 * @see defaultKSLogLogger
 */
actual var KSLoggerDefaultPlatformLoggerLambda: (level: LogLevel, tag: String, message: Any, throwable: Throwable?) -> Unit = { l, t, m, e ->
    defaultKSLogLogger.doLog(l, m.toString(), e)
}