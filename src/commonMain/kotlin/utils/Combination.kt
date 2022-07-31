package dev.inmo.kslog.common.utils

import dev.inmo.kslog.common.CallbackKSLog
import dev.inmo.kslog.common.KSLog

infix operator fun KSLog.plus(other: KSLog) = CallbackKSLog { l, t, m, e ->
    this@plus.performLog(l, t, m, e)
    other.performLog(l, t, m, e)
}
