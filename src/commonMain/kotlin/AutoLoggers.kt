package dev.inmo.kslog.common

val Any.logTag
    get() = this::class.simpleName ?: error("Unable to retrieve log tag")
val Any.logger
    get() = CallbackKSLog { l, t, m, e ->
        KSLog.default.performLog(l, t ?: logTag, m, e)
    }
