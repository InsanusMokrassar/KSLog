package dev.inmo.kslog.common

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