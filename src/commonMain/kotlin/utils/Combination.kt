package dev.inmo.kslog.common.utils

import dev.inmo.kslog.common.CallbackKSLog
import dev.inmo.kslog.common.KSLog

/**
 * Will send [KSLog.performLog] of both [this] and [other] [KSLog] instances. In case when [this] will throw exception
 * result logger will rethrow it. After it, if [other] will throw exception - will also rethrow it
 */
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
