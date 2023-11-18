package dev.inmo.kslog.common

internal actual val defaultLogging: (level: LogLevel, tag: String, message: Any, throwable: Throwable?) -> Unit = { l, t, m, e ->
    val args = e ?.let {
        arrayOf(m, e)
    } ?: arrayOf(m)
    when (l) {
        LogLevel.TRACE,
        LogLevel.DEBUG -> console.log(*args)
        LogLevel.VERBOSE,
        LogLevel.INFO -> console.info(*args)
        LogLevel.WARNING -> console.warn(*args)
        LogLevel.ERROR,
        LogLevel.ASSERT -> console.error(*args)
    }
}
