package dev.inmo.kslog.common

val Any.logTag
    get() = this::class.simpleName ?: error("Unable to retrieve log tag")
val Any.logger: KSLog
    get() = TagLogger(logTag)

fun taggedLogger(tagBase: Any): KSLog = tagBase.logger
