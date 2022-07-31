package dev.inmo.kslog.common

typealias MessageFormatter = (l: LogLevel, t: String?, m: Any, Throwable?) -> String
@Suppress("NOTHING_TO_INLINE")
inline fun MessageFormatter(noinline formatter: MessageFormatter) = formatter
typealias MessageFilter = (l: LogLevel, t: String?, Throwable?) -> Boolean
val defaultMessageFormatter: MessageFormatter = { l, t, m, _ -> "[$l] ${t ?.let { "$it " } ?: ""}- $m" }

