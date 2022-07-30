package dev.inmo.kslog.common

class DefaultKSLog(
    private val defaultTag: String,
    val filter: MessageFilter = { _, _, _ -> true },
    private val messageFormatter: MessageFormatter = defaultMessageFormatter,
    private val logging: (level: LogLevel, tag: String, message: Any, throwable: Throwable?) -> Unit = defaultLogging
) : KSLog {
    override fun performLog(level: LogLevel, tag: String?, message: Any, throwable: Throwable?) {
        val tag = tag ?: defaultTag

        if (filter(level, tag, throwable)) {
            val text = messageFormatter(level, tag, message, throwable)
            logging(level, tag, text, throwable)
        }
    }

    override fun performLog(level: LogLevel, tag: String?, throwable: Throwable?, messageBuilder: () -> Any) {
        val tag = tag ?: defaultTag

        if (filter(level, tag, throwable)) {
            val text = messageFormatter(level, tag, messageBuilder(), throwable)
            logging(level, tag, text, throwable)
        }
    }

    override suspend fun performLogS(
        level: LogLevel,
        tag: String?,
        throwable: Throwable?,
        messageBuilder: suspend () -> Any
    ) {
        val tag = tag ?: defaultTag

        if (filter(level, tag, throwable)) {
            val text = messageFormatter(level, tag, messageBuilder(), throwable)
            logging(level, tag, text, throwable)
        }
    }
}
