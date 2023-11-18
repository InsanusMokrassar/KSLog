package dev.inmo.kslog.common

/**
 * Creating base tag using class simple name of receiver
 *
 * @throws IllegalStateException If there is no opportunity to take simple name of receiver class
 */
val Any.logTag
    get() = this::class.simpleName ?: error("Unable to retrieve log tag")

/**
 * Creating [TagLogger] with [logTag] as base tag
 */
val Any.logger: KSLog
    get() = TagLogger(logTag)

/**
 * Creating [TagLogger] using [logger] extension property with [tagBase] as `this`
 */
fun taggedLogger(tagBase: Any): KSLog = tagBase.logger
