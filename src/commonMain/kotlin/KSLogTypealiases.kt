package dev.inmo.kslog.common

typealias MessageFormatter = (l: LogLevel, t: String, m: Any, Throwable?) -> String
typealias MessageFilter = (l: LogLevel, t: String?, Throwable?) -> Boolean
val defaultMessageFormatter: MessageFormatter = { l, t, m, _ -> "[$l] $t - $m" }

