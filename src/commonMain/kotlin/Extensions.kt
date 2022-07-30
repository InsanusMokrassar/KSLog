@file:Suppress("NOTHING_TO_INLINE")

package dev.inmo.kslog.common

inline fun KSLog.log(level: LogLevel, tag: String?, e: Throwable? = null, noinline messageBuilder: () -> Any) = performLog(level, tag, e, messageBuilder)
suspend inline fun KSLog.logS(level: LogLevel, tag: String?, e: Throwable? = null, noinline messageBuilder: suspend () -> Any) = performLogS(level, tag, e, messageBuilder)
inline fun KSLog.log(level: LogLevel, e: Throwable? = null, noinline messageBuilder: () -> Any) = performLog(level, null, e, messageBuilder)
suspend inline fun KSLog.logS(level: LogLevel, e: Throwable? = null, noinline messageBuilder: suspend () -> Any) = performLogS(level, null, e, messageBuilder)
inline fun KSLog.log(level: LogLevel, message: Any, e: Throwable? = null) = performLog(level, message, e)
inline fun KSLog.log(level: LogLevel, tag: String, message: Any, e: Throwable? = null) = performLog(level, tag, message, e)


inline fun KSLog.debug(e: Throwable? = null, noinline messageBuilder: () -> Any) = log(LogLevel.DEBUG, null, e, messageBuilder)
inline fun KSLog.debug(tag: String?, e: Throwable? = null, noinline messageBuilder: () -> Any) = log(LogLevel.DEBUG, tag, e, messageBuilder)
suspend inline fun KSLog.debugS(e: Throwable? = null, noinline messageBuilder: suspend () -> Any) = logS(LogLevel.DEBUG, null, e, messageBuilder)
suspend inline fun KSLog.debugS(tag: String?, e: Throwable? = null, noinline messageBuilder: suspend () -> Any) = logS(LogLevel.DEBUG, tag, e, messageBuilder)
inline fun KSLog.debug(message: Any, e: Throwable? = null) = log(LogLevel.DEBUG, message, e)
inline fun KSLog.debug(tag: String, message: Any, e: Throwable? = null) = log(LogLevel.DEBUG, tag, message, e)


inline fun KSLog.verbose(e: Throwable? = null, noinline messageBuilder: () -> Any) = log(LogLevel.VERBOSE, null, e, messageBuilder)
inline fun KSLog.verbose(tag: String?, e: Throwable? = null, noinline messageBuilder: () -> Any) = log(LogLevel.VERBOSE, tag, e, messageBuilder)
suspend inline fun KSLog.verboseS(e: Throwable? = null, noinline messageBuilder: suspend () -> Any) = logS(LogLevel.VERBOSE, null, e, messageBuilder)
suspend inline fun KSLog.verboseS(tag: String?, e: Throwable? = null, noinline messageBuilder: suspend () -> Any) = logS(LogLevel.VERBOSE, tag, e, messageBuilder)
inline fun KSLog.verbose(message: Any, e: Throwable? = null) = log(LogLevel.VERBOSE, message, e)
inline fun KSLog.verbose(tag: String, message: Any, e: Throwable? = null) = log(LogLevel.VERBOSE, tag, message, e)


inline fun KSLog.info(e: Throwable? = null, noinline messageBuilder: () -> Any) = log(LogLevel.INFO, null, e, messageBuilder)
inline fun KSLog.info(tag: String?, e: Throwable? = null, noinline messageBuilder: () -> Any) = log(LogLevel.INFO, tag, e, messageBuilder)
suspend inline fun KSLog.infoS(e: Throwable? = null, noinline messageBuilder: suspend () -> Any) = logS(LogLevel.INFO, null, e, messageBuilder)
suspend inline fun KSLog.infoS(tag: String?, e: Throwable? = null, noinline messageBuilder: suspend () -> Any) = logS(LogLevel.INFO, tag, e, messageBuilder)
inline fun KSLog.info(message: Any, e: Throwable? = null) = log(LogLevel.INFO, message, e)
inline fun KSLog.info(tag: String, message: Any, e: Throwable? = null) = log(LogLevel.INFO, tag, message, e)


inline fun KSLog.warning(e: Throwable? = null, noinline messageBuilder: () -> Any) = log(LogLevel.WARNING, null, e, messageBuilder)
inline fun KSLog.warning(tag: String?, e: Throwable? = null, noinline messageBuilder: () -> Any) = log(LogLevel.WARNING, tag, e, messageBuilder)
suspend inline fun KSLog.warningS(e: Throwable? = null, noinline messageBuilder: suspend () -> Any) = logS(LogLevel.WARNING, null, e, messageBuilder)
suspend inline fun KSLog.warningS(tag: String?, e: Throwable? = null, noinline messageBuilder: suspend () -> Any) = logS(LogLevel.WARNING, tag, e, messageBuilder)
inline fun KSLog.warning(message: Any, e: Throwable? = null) = log(LogLevel.WARNING, message, e)
inline fun KSLog.warning(tag: String, message: Any, e: Throwable? = null) = log(LogLevel.WARNING, tag, message, e)


inline fun KSLog.error(e: Throwable? = null, noinline messageBuilder: () -> Any) = log(LogLevel.ERROR, null, e, messageBuilder)
inline fun KSLog.error(tag: String?, e: Throwable? = null, noinline messageBuilder: () -> Any) = log(LogLevel.ERROR, tag, e, messageBuilder)
suspend inline fun KSLog.errorS(e: Throwable? = null, noinline messageBuilder: suspend () -> Any) = logS(LogLevel.ERROR, null, e, messageBuilder)
suspend inline fun KSLog.errorS(tag: String?, e: Throwable? = null, noinline messageBuilder: suspend () -> Any) = logS(LogLevel.ERROR, tag, e, messageBuilder)
inline fun KSLog.error(message: Any, e: Throwable? = null) = log(LogLevel.ERROR, message, e)
inline fun KSLog.error(tag: String, message: Any, e: Throwable? = null) = log(LogLevel.ERROR, tag, message, e)


inline fun KSLog.assert(e: Throwable? = null, noinline messageBuilder: () -> Any) = log(LogLevel.ASSERT, null, e, messageBuilder)
inline fun KSLog.assert(tag: String?, e: Throwable? = null, noinline messageBuilder: () -> Any) = log(LogLevel.ASSERT, tag, e, messageBuilder)
suspend inline fun KSLog.assertS(e: Throwable? = null, noinline messageBuilder: suspend () -> Any) = logS(LogLevel.ASSERT, null, e, messageBuilder)
suspend inline fun KSLog.assertS(tag: String?, e: Throwable? = null, noinline messageBuilder: suspend () -> Any) = logS(LogLevel.ASSERT, tag, e, messageBuilder)
inline fun KSLog.assert(message: Any, e: Throwable? = null) = log(LogLevel.ASSERT, message, e)
inline fun KSLog.assert(tag: String, message: Any, e: Throwable? = null) = log(LogLevel.ASSERT, tag, message, e)

// ----- Short names


inline fun KSLog.l(level: LogLevel, tag: String?, e: Throwable? = null, noinline messageBuilder: () -> Any) = log(level, tag, e, messageBuilder)
inline fun KSLog.l(level: LogLevel, e: Throwable? = null, noinline messageBuilder: () -> Any) = log(level, e, messageBuilder)
suspend inline fun KSLog.lS(level: LogLevel, tag: String?, e: Throwable? = null, noinline messageBuilder: suspend () -> Any) = logS(level, tag, e, messageBuilder)
suspend inline fun KSLog.lS(level: LogLevel, e: Throwable? = null, noinline messageBuilder: suspend () -> Any) = logS(level, e, messageBuilder)
inline fun KSLog.l(level: LogLevel, message: Any, e: Throwable? = null) = log(level, message, e)
inline fun KSLog.l(level: LogLevel, tag: String, message: Any, e: Throwable? = null) = log(level, tag, message, e)


inline fun KSLog.d(e: Throwable? = null, noinline messageBuilder: () -> Any) = debug(e, messageBuilder)
inline fun KSLog.d(tag: String?, e: Throwable? = null, noinline messageBuilder: () -> Any) = debug(tag, e, messageBuilder)
suspend inline fun KSLog.dS(e: Throwable? = null, noinline messageBuilder: suspend () -> Any) = debugS(e, messageBuilder)
suspend inline fun KSLog.dS(tag: String?, e: Throwable? = null, noinline messageBuilder: suspend () -> Any) = debugS(tag, e, messageBuilder)
inline fun KSLog.d(message: Any, e: Throwable? = null) = debug(message, e)
inline fun KSLog.d(tag: String, message: Any, e: Throwable? = null) = debug(tag, message, e)

inline fun KSLog.v(e: Throwable? = null, noinline messageBuilder: () -> Any) = verbose(e, messageBuilder)
inline fun KSLog.v(tag: String?, e: Throwable? = null, noinline messageBuilder: () -> Any) = verbose(tag, e, messageBuilder)
suspend inline fun KSLog.vS(e: Throwable? = null, noinline messageBuilder: suspend () -> Any) = verboseS(e, messageBuilder)
suspend inline fun KSLog.vS(tag: String?, e: Throwable? = null, noinline messageBuilder: suspend () -> Any) = verboseS(tag, e, messageBuilder)
inline fun KSLog.v(message: Any, e: Throwable? = null) = verbose(message, e)
inline fun KSLog.v(tag: String, message: Any, e: Throwable? = null) = verbose(tag, message, e)


inline fun KSLog.i(e: Throwable? = null, noinline messageBuilder: () -> Any) = info(e, messageBuilder)
inline fun KSLog.i(tag: String?, e: Throwable? = null, noinline messageBuilder: () -> Any) = info(tag, e, messageBuilder)
suspend inline fun KSLog.iS(e: Throwable? = null, noinline messageBuilder: suspend () -> Any) = infoS(e, messageBuilder)
suspend inline fun KSLog.iS(tag: String?, e: Throwable? = null, noinline messageBuilder: suspend () -> Any) = infoS(tag, e, messageBuilder)
inline fun KSLog.i(message: Any, e: Throwable? = null) = info(message, e)
inline fun KSLog.i(tag: String, message: Any, e: Throwable? = null) = info(tag, message, e)


inline fun KSLog.w(e: Throwable? = null, noinline messageBuilder: () -> Any) = warning(e, messageBuilder)
inline fun KSLog.w(tag: String?, e: Throwable? = null, noinline messageBuilder: () -> Any) = warning(tag, e, messageBuilder)
suspend inline fun KSLog.wS(e: Throwable? = null, noinline messageBuilder: suspend () -> Any) = warningS(e, messageBuilder)
suspend inline fun KSLog.wS(tag: String?, e: Throwable? = null, noinline messageBuilder: suspend () -> Any) = warningS(tag, e, messageBuilder)
inline fun KSLog.w(message: Any, e: Throwable? = null) = warning(message, e)
inline fun KSLog.w(tag: String, message: Any, e: Throwable? = null) = warning(tag, message, e)


inline fun KSLog.e(e: Throwable? = null, noinline messageBuilder: () -> Any) = error(e, messageBuilder)
inline fun KSLog.e(tag: String?, e: Throwable? = null, noinline messageBuilder: () -> Any) = error(tag, e, messageBuilder)
suspend inline fun KSLog.eS(e: Throwable? = null, noinline messageBuilder: suspend () -> Any) = errorS(e, messageBuilder)
suspend inline fun KSLog.eS(tag: String?, e: Throwable? = null, noinline messageBuilder: suspend () -> Any) = errorS(tag, e, messageBuilder)
inline fun KSLog.e(message: Any, e: Throwable? = null) = error(message, e)
inline fun KSLog.e(tag: String, message: Any, e: Throwable? = null) = error(tag, message, e)


inline fun KSLog.wtf(e: Throwable? = null, noinline messageBuilder: () -> Any) = assert(e, messageBuilder)
inline fun KSLog.wtf(tag: String?, e: Throwable? = null, noinline messageBuilder: () -> Any) = assert(tag, e, messageBuilder)
suspend inline fun KSLog.wtfS(e: Throwable? = null, noinline messageBuilder: suspend () -> Any) = assertS(e, messageBuilder)
suspend inline fun KSLog.wtfS(tag: String?, e: Throwable? = null, noinline messageBuilder: suspend () -> Any) = assertS(tag, e, messageBuilder)
inline fun KSLog.wtf(message: Any, e: Throwable? = null) = assert(message, e)
inline fun KSLog.wtf(tag: String, message: Any, e: Throwable? = null) = assert(tag, message, e)
