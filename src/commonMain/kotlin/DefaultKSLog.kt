package dev.inmo.kslog.common

class DefaultKSLog(
    private val defaultTag: String,
    private val filter: MessageFilter = { _, _, _, _ -> true },
    private val messageFormatter: MessageFormatter = defaultMessageFormatter,
    private val logging: (level: LogLevel, tag: String, message: String, throwable: Throwable?) -> Unit
) : KSLog {
    override fun performLog(level: LogLevel, tag: String?, message: String, throwable: Throwable?) {
        TODO("Not yet implemented")
    }

    override fun performLog(level: LogLevel, tag: String?, throwable: Throwable?, messageBuilder: () -> String) {
        super.performLog(level, tag, throwable, messageBuilder)
    }

    override suspend fun performLog(
        level: LogLevel,
        tag: String?,
        throwable: Throwable?,
        messageBuilder: suspend () -> String
    ) {
        if (filter(level, tag, ))
        super.performLog(level, tag, throwable, messageBuilder)
    }
}
