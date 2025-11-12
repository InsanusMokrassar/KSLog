package dev.inmo.kslog.common

/**
 * JavaScript platform implementation of the default logger
 *
 * Uses browser/Node.js `console` API as the logging backend, mapping KSLog levels
 * to console methods:
 * - [LogLevel.TRACE] → console.trace() + console.debug()
 * - [LogLevel.DEBUG] → console.debug()
 * - [LogLevel.VERBOSE] → console.info()
 * - [LogLevel.INFO] → console.info()
 * - [LogLevel.WARNING] → console.warn()
 * - [LogLevel.ERROR] → console.error()
 * - [LogLevel.ASSERT] → console.error()
 *
 * Logs can be viewed in the browser's Developer Tools console or Node.js console output.
 *
 * **Note**: The tag parameter is ignored in JS implementation as console methods
 * don't support tagging natively.
 */
actual var KSLoggerDefaultPlatformLoggerLambda: (level: LogLevel, tag: String, message: Any, throwable: Throwable?) -> Unit = { l, _, m, e ->
    val args = e ?.let {
        arrayOf(m, e)
    } ?: arrayOf(m)
    when (l) {
        LogLevel.TRACE -> {
            console.trace()
            console.debug(*args)
        }
        LogLevel.DEBUG -> console.debug(*args)
        LogLevel.VERBOSE,
        LogLevel.INFO -> console.info(*args)
        LogLevel.WARNING -> console.warn(*args)
        LogLevel.ERROR -> console.error(*args)
        LogLevel.ASSERT -> console.error(*args)
    }
}