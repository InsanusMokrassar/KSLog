@file:Suppress("NOTHING_TO_INLINE")

package dev.inmo.kslog.common

inline fun Logger.log(level: LogLevel, tag: String? = null, e: Throwable? = null, messageBuilder: () -> String) = invoke(level, tag, messageBuilder(), e)
inline fun Logger.log(level: LogLevel, message: String, e: Throwable? = null) = log(level, null, e) { message }
inline fun Logger.log(level: LogLevel, tag: String, message: String, e: Throwable? = null) = log(level, tag, e) { message }


inline fun Logger.verbose(tag: String? = null, e: Throwable? = null, messageBuilder: () -> String) = log(LogLevel.VERBOSE, tag, e, messageBuilder)
inline fun Logger.verbose(message: String, e: Throwable? = null) = log(LogLevel.VERBOSE, message, e)
inline fun Logger.verbose(tag: String, message: String, e: Throwable? = null) = log(LogLevel.VERBOSE, tag, message, e)


inline fun Logger.info(tag: String? = null, e: Throwable? = null, messageBuilder: () -> String) = log(LogLevel.INFO, tag, e, messageBuilder)
inline fun Logger.info(message: String, e: Throwable? = null) = log(LogLevel.INFO, message, e)
inline fun Logger.info(tag: String, message: String, e: Throwable? = null) = log(LogLevel.INFO, tag, message, e)


inline fun Logger.warning(tag: String? = null, e: Throwable? = null, messageBuilder: () -> String) = log(LogLevel.WARNING, tag, e, messageBuilder)
inline fun Logger.warning(message: String, e: Throwable? = null) = log(LogLevel.WARNING, message, e)
inline fun Logger.warning(tag: String, message: String, e: Throwable? = null) = log(LogLevel.WARNING, tag, message, e)


inline fun Logger.error(tag: String? = null, e: Throwable? = null, messageBuilder: () -> String) = log(LogLevel.ERROR, tag, e, messageBuilder)
inline fun Logger.error(message: String, e: Throwable? = null) = log(LogLevel.ERROR, message, e)
inline fun Logger.error(tag: String, message: String, e: Throwable? = null) = log(LogLevel.ERROR, tag, message, e)


inline fun Logger.assert(tag: String? = null, e: Throwable? = null, messageBuilder: () -> String) = log(LogLevel.ASSERT, tag, e, messageBuilder)
inline fun Logger.assert(message: String, e: Throwable? = null) = log(LogLevel.ASSERT, message, e)
inline fun Logger.assert(tag: String, message: String, e: Throwable? = null) = log(LogLevel.ASSERT, tag, message, e)


inline fun Logger.debug(tag: String? = null, e: Throwable? = null, messageBuilder: () -> String) = log(LogLevel.DEBUG, tag, e, messageBuilder)
inline fun Logger.debug(message: String, e: Throwable? = null) = log(LogLevel.DEBUG, message, e)
inline fun Logger.debug(tag: String, message: String, e: Throwable? = null) = log(LogLevel.DEBUG, tag, message, e)

// ----- Short names


inline fun Logger.l(level: LogLevel, tag: String? = null, e: Throwable? = null, messageBuilder: () -> String) = log(level, tag, e, messageBuilder)
inline fun Logger.l(level: LogLevel, message: String, e: Throwable? = null) = log(level, message, e)
inline fun Logger.l(level: LogLevel, tag: String, message: String, e: Throwable? = null) = log(level, tag, message, e)

inline fun Logger.v(tag: String? = null, e: Throwable? = null, messageBuilder: () -> String) = verbose(tag, e, messageBuilder)
inline fun Logger.v(message: String, e: Throwable? = null) = verbose(message, e)
inline fun Logger.v(tag: String, message: String, e: Throwable? = null) = verbose(tag, message, e)


inline fun Logger.i(tag: String? = null, e: Throwable? = null, messageBuilder: () -> String) = info(tag, e, messageBuilder)
inline fun Logger.i(message: String, e: Throwable? = null) = info(message, e)
inline fun Logger.i(tag: String, message: String, e: Throwable? = null) = info(tag, message, e)


inline fun Logger.w(tag: String? = null, e: Throwable? = null, messageBuilder: () -> String) = warning(tag, e, messageBuilder)
inline fun Logger.w(message: String, e: Throwable? = null) = warning(message, e)
inline fun Logger.w(tag: String, message: String, e: Throwable? = null) = warning(tag, message, e)


inline fun Logger.e(tag: String? = null, e: Throwable? = null, messageBuilder: () -> String) = error(tag, e, messageBuilder)
inline fun Logger.e(message: String, e: Throwable? = null) = error(message, e)
inline fun Logger.e(tag: String, message: String, e: Throwable? = null) = error(tag, message, e)


inline fun Logger.wtf(tag: String? = null, e: Throwable? = null, messageBuilder: () -> String) = assert(tag, e, messageBuilder)
inline fun Logger.wtf(message: String, e: Throwable? = null) = assert(message, e)
inline fun Logger.wtf(tag: String, message: String, e: Throwable? = null) = assert(tag, message, e)


inline fun Logger.d(tag: String? = null, e: Throwable? = null, messageBuilder: () -> String) = debug(tag, e, messageBuilder)
inline fun Logger.d(message: String, e: Throwable? = null) = debug(message, e)
inline fun Logger.d(tag: String, message: String, e: Throwable? = null) = debug(tag, message, e)
