package dev.inmo.kslog.common

typealias MessageFormatter = (l: LogLevel, t: String, m: String, Throwable?) -> String
typealias MessageFilter = (l: LogLevel, t: String?, m: String, Throwable?) -> Boolean
val defaultMessageFormatter: MessageFormatter = { l, t, m, _ -> "[$l] $t - $m" }

