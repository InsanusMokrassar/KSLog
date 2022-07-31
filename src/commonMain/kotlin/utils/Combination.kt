package dev.inmo.kslog.common.utils

import dev.inmo.kslog.common.CallbackKSLog
import dev.inmo.kslog.common.KSLog

infix operator fun KSLog.plus(other: KSLog) = CallbackKSLog { l, t, m, e ->
    val resultOfFirst = runCatching {
        this@plus.performLog(l, t, m, e)
    }
    val resultOfSecond = runCatching {
        other.performLog(l, t, m, e)
    }
    resultOfFirst.onFailure { throw it }
    resultOfSecond.onFailure { throw it }
}
