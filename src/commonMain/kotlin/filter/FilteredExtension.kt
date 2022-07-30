package dev.inmo.kslog.common.filter

import dev.inmo.kslog.common.KSLog
import dev.inmo.kslog.common.MessageFilter

fun KSLog.filtered(
    filter: MessageFilter
) = FilterKSLog(this, filter)
