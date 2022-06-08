package dev.inmo.kslog.common

typealias MessageFormatter = (l: LogLevel, t: String, m: String, Throwable?) -> String
val defaultMessageFormatter: MessageFormatter = { l, t, m, _ -> "[$l] $t - $m" }

