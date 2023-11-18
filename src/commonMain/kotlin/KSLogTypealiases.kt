package dev.inmo.kslog.common

typealias MessageFormatter = (l: LogLevel, t: String?, m: Any, Throwable?) -> String
inline fun MessageFormatter(noinline formatter: MessageFormatter) = formatter
typealias MessageFilter = (l: LogLevel, t: String?, Throwable?) -> Boolean
val defaultMessageFormatter: MessageFormatter = { l, t, m, _ -> "[$l] ${t ?.let { "$it " } ?: ""}- $m" }
val defaultMessageFormatterWithErrorPrint: MessageFormatter = { l, t, m, e -> "[$l] ${t ?.let { "$it " } ?: ""}- $m${e ?.let { ": ${it.stackTraceToString()}" } ?: ""}" }
