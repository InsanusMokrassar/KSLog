@file:Suppress("NOTHING_TO_INLINE")

package dev.inmo.kslog.common

inline fun KSLog.log(level: LogLevel, tag: String?, e: Throwable? = null, noinline messageBuilder: () -> String) = performLog(level, tag, e, messageBuilder)
suspend inline fun KSLog.logS(level: LogLevel, tag: String?, e: Throwable? = null, noinline messageBuilder: suspend () -> String) = performLogS(level, tag, e, messageBuilder)
inline fun KSLog.log(level: LogLevel, e: Throwable? = null, noinline messageBuilder: () -> String) = performLog(level, null, e, messageBuilder)
suspend inline fun KSLog.logS(level: LogLevel, e: Throwable? = null, noinline messageBuilder: suspend () -> String) = performLogS(level, null, e, messageBuilder)
inline fun KSLog.log(level: LogLevel, message: String, e: Throwable? = null) = performLog(level, message, e)
inline fun KSLog.log(level: LogLevel, tag: String, message: String, e: Throwable? = null) = performLog(level, tag, message, e)


inline fun KSLog.debug(e: Throwable? = null, noinline messageBuilder: () -> String) = log(LogLevel.DEBUG, null, e, messageBuilder)
inline fun KSLog.debug(tag: String?, e: Throwable? = null, noinline messageBuilder: () -> String) = log(LogLevel.DEBUG, tag, e, messageBuilder)
suspend inline fun KSLog.debugS(e: Throwable? = null, noinline messageBuilder: suspend () -> String) = logS(LogLevel.DEBUG, null, e, messageBuilder)
suspend inline fun KSLog.debugS(tag: String?, e: Throwable? = null, noinline messageBuilder: suspend () -> String) = logS(LogLevel.DEBUG, tag, e, messageBuilder)
inline fun KSLog.debug(message: String, e: Throwable? = null) = log(LogLevel.DEBUG, message, e)
inline fun KSLog.debug(tag: String, message: String, e: Throwable? = null) = log(LogLevel.DEBUG, tag, message, e)


inline fun KSLog.verbose(e: Throwable? = null, noinline messageBuilder: () -> String) = log(LogLevel.VERBOSE, null, e, messageBuilder)
inline fun KSLog.verbose(tag: String?, e: Throwable? = null, noinline messageBuilder: () -> String) = log(LogLevel.VERBOSE, tag, e, messageBuilder)
suspend inline fun KSLog.verboseS(e: Throwable? = null, noinline messageBuilder: suspend () -> String) = logS(LogLevel.VERBOSE, null, e, messageBuilder)
suspend inline fun KSLog.verboseS(tag: String?, e: Throwable? = null, noinline messageBuilder: suspend () -> String) = logS(LogLevel.VERBOSE, tag, e, messageBuilder)
inline fun KSLog.verbose(message: String, e: Throwable? = null) = log(LogLevel.VERBOSE, message, e)
inline fun KSLog.verbose(tag: String, message: String, e: Throwable? = null) = log(LogLevel.VERBOSE, tag, message, e)


inline fun KSLog.info(e: Throwable? = null, noinline messageBuilder: () -> String) = log(LogLevel.INFO, null, e, messageBuilder)
inline fun KSLog.info(tag: String?, e: Throwable? = null, noinline messageBuilder: () -> String) = log(LogLevel.INFO, tag, e, messageBuilder)
suspend inline fun KSLog.infoS(e: Throwable? = null, noinline messageBuilder: suspend () -> String) = logS(LogLevel.INFO, null, e, messageBuilder)
suspend inline fun KSLog.infoS(tag: String?, e: Throwable? = null, noinline messageBuilder: suspend () -> String) = logS(LogLevel.INFO, tag, e, messageBuilder)
inline fun KSLog.info(message: String, e: Throwable? = null) = log(LogLevel.INFO, message, e)
inline fun KSLog.info(tag: String, message: String, e: Throwable? = null) = log(LogLevel.INFO, tag, message, e)


inline fun KSLog.warning(e: Throwable? = null, noinline messageBuilder: () -> String) = log(LogLevel.WARNING, null, e, messageBuilder)
inline fun KSLog.warning(tag: String?, e: Throwable? = null, noinline messageBuilder: () -> String) = log(LogLevel.WARNING, tag, e, messageBuilder)
suspend inline fun KSLog.warningS(e: Throwable? = null, noinline messageBuilder: suspend () -> String) = logS(LogLevel.WARNING, null, e, messageBuilder)
suspend inline fun KSLog.warningS(tag: String?, e: Throwable? = null, noinline messageBuilder: suspend () -> String) = logS(LogLevel.WARNING, tag, e, messageBuilder)
inline fun KSLog.warning(message: String, e: Throwable? = null) = log(LogLevel.WARNING, message, e)
inline fun KSLog.warning(tag: String, message: String, e: Throwable? = null) = log(LogLevel.WARNING, tag, message, e)


inline fun KSLog.error(e: Throwable? = null, noinline messageBuilder: () -> String) = log(LogLevel.ERROR, null, e, messageBuilder)
inline fun KSLog.error(tag: String?, e: Throwable? = null, noinline messageBuilder: () -> String) = log(LogLevel.ERROR, tag, e, messageBuilder)
suspend inline fun KSLog.errorS(e: Throwable? = null, noinline messageBuilder: suspend () -> String) = logS(LogLevel.ERROR, null, e, messageBuilder)
suspend inline fun KSLog.errorS(tag: String?, e: Throwable? = null, noinline messageBuilder: suspend () -> String) = logS(LogLevel.ERROR, tag, e, messageBuilder)
inline fun KSLog.error(message: String, e: Throwable? = null) = log(LogLevel.ERROR, message, e)
inline fun KSLog.error(tag: String, message: String, e: Throwable? = null) = log(LogLevel.ERROR, tag, message, e)


inline fun KSLog.assert(e: Throwable? = null, noinline messageBuilder: () -> String) = log(LogLevel.ASSERT, null, e, messageBuilder)
inline fun KSLog.assert(tag: String?, e: Throwable? = null, noinline messageBuilder: () -> String) = log(LogLevel.ASSERT, tag, e, messageBuilder)
suspend inline fun KSLog.assertS(e: Throwable? = null, noinline messageBuilder: suspend () -> String) = logS(LogLevel.ASSERT, null, e, messageBuilder)
suspend inline fun KSLog.assertS(tag: String?, e: Throwable? = null, noinline messageBuilder: suspend () -> String) = logS(LogLevel.ASSERT, tag, e, messageBuilder)
inline fun KSLog.assert(message: String, e: Throwable? = null) = log(LogLevel.ASSERT, message, e)
inline fun KSLog.assert(tag: String, message: String, e: Throwable? = null) = log(LogLevel.ASSERT, tag, message, e)

// ----- Short names


inline fun KSLog.l(level: LogLevel, tag: String?, e: Throwable? = null, noinline messageBuilder: () -> String) = log(level, tag, e, messageBuilder)
inline fun KSLog.l(level: LogLevel, e: Throwable? = null, noinline messageBuilder: () -> String) = log(level, e, messageBuilder)
suspend inline fun KSLog.lS(level: LogLevel, tag: String?, e: Throwable? = null, noinline messageBuilder: suspend () -> String) = logS(level, tag, e, messageBuilder)
suspend inline fun KSLog.lS(level: LogLevel, e: Throwable? = null, noinline messageBuilder: suspend () -> String) = logS(level, e, messageBuilder)
inline fun KSLog.l(level: LogLevel, message: String, e: Throwable? = null) = log(level, message, e)
inline fun KSLog.l(level: LogLevel, tag: String, message: String, e: Throwable? = null) = log(level, tag, message, e)


inline fun KSLog.d(e: Throwable? = null, noinline messageBuilder: () -> String) = debug(e, messageBuilder)
inline fun KSLog.d(tag: String?, e: Throwable? = null, noinline messageBuilder: () -> String) = debug(tag, e, messageBuilder)
suspend inline fun KSLog.dS(e: Throwable? = null, noinline messageBuilder: suspend () -> String) = debugS(e, messageBuilder)
suspend inline fun KSLog.dS(tag: String?, e: Throwable? = null, noinline messageBuilder: suspend () -> String) = debugS(tag, e, messageBuilder)
inline fun KSLog.d(message: String, e: Throwable? = null) = debug(message, e)
inline fun KSLog.d(tag: String, message: String, e: Throwable? = null) = debug(tag, message, e)

inline fun KSLog.v(e: Throwable? = null, noinline messageBuilder: () -> String) = verbose(e, messageBuilder)
inline fun KSLog.v(tag: String?, e: Throwable? = null, noinline messageBuilder: () -> String) = verbose(tag, e, messageBuilder)
suspend inline fun KSLog.vS(e: Throwable? = null, noinline messageBuilder: suspend () -> String) = verboseS(e, messageBuilder)
suspend inline fun KSLog.vS(tag: String?, e: Throwable? = null, noinline messageBuilder: suspend () -> String) = verboseS(tag, e, messageBuilder)
inline fun KSLog.v(message: String, e: Throwable? = null) = verbose(message, e)
inline fun KSLog.v(tag: String, message: String, e: Throwable? = null) = verbose(tag, message, e)


inline fun KSLog.i(e: Throwable? = null, noinline messageBuilder: () -> String) = info(e, messageBuilder)
inline fun KSLog.i(tag: String?, e: Throwable? = null, noinline messageBuilder: () -> String) = info(tag, e, messageBuilder)
suspend inline fun KSLog.iS(e: Throwable? = null, noinline messageBuilder: suspend () -> String) = infoS(e, messageBuilder)
suspend inline fun KSLog.iS(tag: String?, e: Throwable? = null, noinline messageBuilder: suspend () -> String) = infoS(tag, e, messageBuilder)
inline fun KSLog.i(message: String, e: Throwable? = null) = info(message, e)
inline fun KSLog.i(tag: String, message: String, e: Throwable? = null) = info(tag, message, e)


inline fun KSLog.w(e: Throwable? = null, noinline messageBuilder: () -> String) = warning(e, messageBuilder)
inline fun KSLog.w(tag: String?, e: Throwable? = null, noinline messageBuilder: () -> String) = warning(tag, e, messageBuilder)
suspend inline fun KSLog.wS(e: Throwable? = null, noinline messageBuilder: suspend () -> String) = warningS(e, messageBuilder)
suspend inline fun KSLog.wS(tag: String?, e: Throwable? = null, noinline messageBuilder: suspend () -> String) = warningS(tag, e, messageBuilder)
inline fun KSLog.w(message: String, e: Throwable? = null) = warning(message, e)
inline fun KSLog.w(tag: String, message: String, e: Throwable? = null) = warning(tag, message, e)


inline fun KSLog.e(e: Throwable? = null, noinline messageBuilder: () -> String) = error(e, messageBuilder)
inline fun KSLog.e(tag: String?, e: Throwable? = null, noinline messageBuilder: () -> String) = error(tag, e, messageBuilder)
suspend inline fun KSLog.eS(e: Throwable? = null, noinline messageBuilder: suspend () -> String) = errorS(e, messageBuilder)
suspend inline fun KSLog.eS(tag: String?, e: Throwable? = null, noinline messageBuilder: suspend () -> String) = errorS(tag, e, messageBuilder)
inline fun KSLog.e(message: String, e: Throwable? = null) = error(message, e)
inline fun KSLog.e(tag: String, message: String, e: Throwable? = null) = error(tag, message, e)


inline fun KSLog.wtf(e: Throwable? = null, noinline messageBuilder: () -> String) = assert(e, messageBuilder)
inline fun KSLog.wtf(tag: String?, e: Throwable? = null, noinline messageBuilder: () -> String) = assert(tag, e, messageBuilder)
suspend inline fun KSLog.wtfS(e: Throwable? = null, noinline messageBuilder: suspend () -> String) = assertS(e, messageBuilder)
suspend inline fun KSLog.wtfS(tag: String?, e: Throwable? = null, noinline messageBuilder: suspend () -> String) = assertS(tag, e, messageBuilder)
inline fun KSLog.wtf(message: String, e: Throwable? = null) = assert(message, e)
inline fun KSLog.wtf(tag: String, message: String, e: Throwable? = null) = assert(tag, message, e)
