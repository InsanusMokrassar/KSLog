package dev.inmo.kslog.common

import dev.inmo.kslog.common.filter.filtered

/**
 * Logger based on [KSLoggerDefaultPlatformLoggerLambda] or [logging] parameter
 *
 * @param defaultTag will be used in case when `tag` parameter in [performLog] omitted
 * @param messageFormatter special formatter which creating [String] inside of [performLog] for each call. Defaults to
 * [defaultMessageFormatter]
 * @param logging target lambda which will be called with a result of [messageFormatter] logs formatting as a message
 * and tag as tag if not `null` and [defaultTag] otherwise
 */
class DefaultKSLog(
    private val defaultTag: String,
    private val messageFormatter: MessageFormatter = defaultMessageFormatter,
    private val logging: (level: LogLevel, tag: String, message: Any, throwable: Throwable?) -> Unit = KSLoggerDefaultPlatformLoggerLambda
) : KSLog {
    override fun performLog(level: LogLevel, tag: String?, message: Any, throwable: Throwable?) {
        val resultTag = tag ?: defaultTag

        val text = messageFormatter(level, resultTag, message, throwable)
        logging(level, resultTag, text, throwable)
    }

    override fun performLog(level: LogLevel, tag: String?, throwable: Throwable?, messageBuilder: () -> Any) {
        val resultTag = tag ?: defaultTag

        val text = messageFormatter(level, resultTag, messageBuilder(), throwable)
        logging(level, resultTag, text, throwable)
    }

    override suspend fun performLogS(
        level: LogLevel,
        tag: String?,
        throwable: Throwable?,
        messageBuilder: suspend () -> Any
    ) {
        val resultTag = tag ?: defaultTag

        val text = messageFormatter(level, resultTag, messageBuilder(), throwable)
        logging(level, resultTag, text, throwable)
    }
}

@Deprecated("Filtering should be replaced with FilterKSLog")
fun DefaultKSLog(
    defaultTag: String,
    filter: MessageFilter = { _, _, _ -> true },
    messageFormatter: MessageFormatter = defaultMessageFormatter,
    logging: (level: LogLevel, tag: String, message: Any, throwable: Throwable?) -> Unit = KSLoggerDefaultPlatformLoggerLambda
) = DefaultKSLog(
    defaultTag, messageFormatter, logging
).filtered(filter)
