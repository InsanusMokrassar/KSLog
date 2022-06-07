@file:Suppress("NOTHING_TO_INLINE")

package dev.inmo.kslog.common

inline fun KSLog.log(level: LogLevel, tag: String? = null, e: Throwable? = null, messageBuilder: () -> String) = performLog(level, tag, messageBuilder(), e)
inline fun KSLog.log(level: LogLevel, message: String, e: Throwable? = null) = log(level, null, e) { message }
inline fun KSLog.log(level: LogLevel, tag: String, message: String, e: Throwable? = null) = log(level, tag, e) { message }


inline fun KSLog.verbose(tag: String? = null, e: Throwable? = null, messageBuilder: () -> String) = log(LogLevel.VERBOSE, tag, e, messageBuilder)
inline fun KSLog.verbose(message: String, e: Throwable? = null) = log(LogLevel.VERBOSE, message, e)
inline fun KSLog.verbose(tag: String, message: String, e: Throwable? = null) = log(LogLevel.VERBOSE, tag, message, e)


inline fun KSLog.info(tag: String? = null, e: Throwable? = null, messageBuilder: () -> String) = log(LogLevel.INFO, tag, e, messageBuilder)
inline fun KSLog.info(message: String, e: Throwable? = null) = log(LogLevel.INFO, message, e)
inline fun KSLog.info(tag: String, message: String, e: Throwable? = null) = log(LogLevel.INFO, tag, message, e)


inline fun KSLog.warning(tag: String? = null, e: Throwable? = null, messageBuilder: () -> String) = log(LogLevel.WARNING, tag, e, messageBuilder)
inline fun KSLog.warning(message: String, e: Throwable? = null) = log(LogLevel.WARNING, message, e)
inline fun KSLog.warning(tag: String, message: String, e: Throwable? = null) = log(LogLevel.WARNING, tag, message, e)


inline fun KSLog.error(tag: String? = null, e: Throwable? = null, messageBuilder: () -> String) = log(LogLevel.ERROR, tag, e, messageBuilder)
inline fun KSLog.error(message: String, e: Throwable? = null) = log(LogLevel.ERROR, message, e)
inline fun KSLog.error(tag: String, message: String, e: Throwable? = null) = log(LogLevel.ERROR, tag, message, e)


inline fun KSLog.assert(tag: String? = null, e: Throwable? = null, messageBuilder: () -> String) = log(LogLevel.ASSERT, tag, e, messageBuilder)
inline fun KSLog.assert(message: String, e: Throwable? = null) = log(LogLevel.ASSERT, message, e)
inline fun KSLog.assert(tag: String, message: String, e: Throwable? = null) = log(LogLevel.ASSERT, tag, message, e)


inline fun KSLog.debug(tag: String? = null, e: Throwable? = null, messageBuilder: () -> String) = log(LogLevel.DEBUG, tag, e, messageBuilder)
inline fun KSLog.debug(message: String, e: Throwable? = null) = log(LogLevel.DEBUG, message, e)
inline fun KSLog.debug(tag: String, message: String, e: Throwable? = null) = log(LogLevel.DEBUG, tag, message, e)

// ----- Short names


inline fun KSLog.l(level: LogLevel, tag: String? = null, e: Throwable? = null, messageBuilder: () -> String) = log(level, tag, e, messageBuilder)
inline fun KSLog.l(level: LogLevel, message: String, e: Throwable? = null) = log(level, message, e)
inline fun KSLog.l(level: LogLevel, tag: String, message: String, e: Throwable? = null) = log(level, tag, message, e)

inline fun KSLog.v(tag: String? = null, e: Throwable? = null, messageBuilder: () -> String) = verbose(tag, e, messageBuilder)
inline fun KSLog.v(message: String, e: Throwable? = null) = verbose(message, e)
inline fun KSLog.v(tag: String, message: String, e: Throwable? = null) = verbose(tag, message, e)


inline fun KSLog.i(tag: String? = null, e: Throwable? = null, messageBuilder: () -> String) = info(tag, e, messageBuilder)
inline fun KSLog.i(message: String, e: Throwable? = null) = info(message, e)
inline fun KSLog.i(tag: String, message: String, e: Throwable? = null) = info(tag, message, e)


inline fun KSLog.w(tag: String? = null, e: Throwable? = null, messageBuilder: () -> String) = warning(tag, e, messageBuilder)
inline fun KSLog.w(message: String, e: Throwable? = null) = warning(message, e)
inline fun KSLog.w(tag: String, message: String, e: Throwable? = null) = warning(tag, message, e)


inline fun KSLog.e(tag: String? = null, e: Throwable? = null, messageBuilder: () -> String) = error(tag, e, messageBuilder)
inline fun KSLog.e(message: String, e: Throwable? = null) = error(message, e)
inline fun KSLog.e(tag: String, message: String, e: Throwable? = null) = error(tag, message, e)


inline fun KSLog.wtf(tag: String? = null, e: Throwable? = null, messageBuilder: () -> String) = assert(tag, e, messageBuilder)
inline fun KSLog.wtf(message: String, e: Throwable? = null) = assert(message, e)
inline fun KSLog.wtf(tag: String, message: String, e: Throwable? = null) = assert(tag, message, e)


inline fun KSLog.d(tag: String? = null, e: Throwable? = null, messageBuilder: () -> String) = debug(tag, e, messageBuilder)
inline fun KSLog.d(message: String, e: Throwable? = null) = debug(message, e)
inline fun KSLog.d(tag: String, message: String, e: Throwable? = null) = debug(tag, message, e)
