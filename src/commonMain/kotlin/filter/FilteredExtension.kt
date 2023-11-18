package dev.inmo.kslog.common.filter

import dev.inmo.kslog.common.KSLog
import dev.inmo.kslog.common.MessageFilter

/**
 * Creates [FilterKSLog] with applying of [filter] to it
 */
fun KSLog.filtered(
    filter: MessageFilter
) = FilterKSLog(this, filter)
