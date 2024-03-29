@file:Suppress("NOTHING_TO_INLINE")

package dev.inmo.kslog.common

inline fun KSLog.log(level: LogLevel, tag: String?, e: Throwable? = null, noinline messageBuilder: () -> Any) = performLog(level, tag, e, messageBuilder)
inline fun KSLog.log(level: LogLevel, tag: String, noinline messageBuilder: () -> Any) = performLog(level, tag, null, messageBuilder)
suspend inline fun KSLog.logS(level: LogLevel, tag: String?, e: Throwable? = null, noinline messageBuilder: suspend () -> Any) = performLogS(level, tag, e, messageBuilder)
suspend inline fun KSLog.logS(level: LogLevel, tag: String, noinline messageBuilder: suspend () -> Any) = performLogS(level, tag, null, messageBuilder)
inline fun KSLog.log(level: LogLevel, e: Throwable? = null, noinline messageBuilder: () -> Any) = performLog(level, null, e, messageBuilder)
suspend inline fun KSLog.logS(level: LogLevel, e: Throwable? = null, noinline messageBuilder: suspend () -> Any) = performLogS(level, null, e, messageBuilder)
inline fun KSLog.log(level: LogLevel, message: Any, e: Throwable?) = performLog(level, message, e)
inline fun KSLog.log(level: LogLevel, message: String, e: Throwable) = log(level, message as Any, e)
inline fun KSLog.log(level: LogLevel, message: Any) = performLog(level, message, null)
inline fun KSLog.log(level: LogLevel, tag: String, message: Any, e: Throwable?) = performLog(level, tag, message, e)
inline fun KSLog.log(level: LogLevel, tag: String, message: Any) = performLog(level, tag, message, null)


inline fun KSLog.trace(e: Throwable? = null, noinline messageBuilder: () -> Any) = log(LogLevel.TRACE, null, e, messageBuilder)
inline fun KSLog.trace(tag: String?, e: Throwable? = null, noinline messageBuilder: () -> Any) = log(LogLevel.TRACE, tag, e, messageBuilder)
inline fun KSLog.trace(tag: String, noinline messageBuilder: () -> Any) = trace(tag, null, messageBuilder)
suspend inline fun KSLog.traceS(e: Throwable? = null, noinline messageBuilder: suspend () -> Any) = logS(LogLevel.TRACE, null, e, messageBuilder)
suspend inline fun KSLog.traceS(tag: String?, e: Throwable? = null, noinline messageBuilder: suspend () -> Any) = logS(LogLevel.TRACE, tag, e, messageBuilder)
suspend inline fun KSLog.traceS(tag: String, noinline messageBuilder: suspend () -> Any) = traceS(tag, null, messageBuilder)
inline fun KSLog.trace(message: Any, e: Throwable?) = log(LogLevel.TRACE, message, e)
inline fun KSLog.trace(message: String, e: Throwable) = trace(message as Any, e)
inline fun KSLog.trace(message: Any) = trace(message, null)
inline fun KSLog.trace(tag: String, message: Any, e: Throwable?) = log(LogLevel.TRACE, tag, message, e)
inline fun KSLog.trace(tag: String, message: Any) = trace(tag, message, null)


inline fun KSLog.debug(e: Throwable? = null, noinline messageBuilder: () -> Any) = log(LogLevel.DEBUG, null, e, messageBuilder)
inline fun KSLog.debug(tag: String?, e: Throwable? = null, noinline messageBuilder: () -> Any) = log(LogLevel.DEBUG, tag, e, messageBuilder)
inline fun KSLog.debug(tag: String, noinline messageBuilder: () -> Any) = debug(tag, null, messageBuilder)
suspend inline fun KSLog.debugS(e: Throwable? = null, noinline messageBuilder: suspend () -> Any) = logS(LogLevel.DEBUG, null, e, messageBuilder)
suspend inline fun KSLog.debugS(tag: String?, e: Throwable? = null, noinline messageBuilder: suspend () -> Any) = logS(LogLevel.DEBUG, tag, e, messageBuilder)
suspend inline fun KSLog.debugS(tag: String, noinline messageBuilder: suspend () -> Any) = debugS(tag, null, messageBuilder)
inline fun KSLog.debug(message: Any, e: Throwable?) = log(LogLevel.DEBUG, message, e)
inline fun KSLog.debug(message: String, e: Throwable) = debug(message as Any, e)
inline fun KSLog.debug(message: Any) = debug(message, null)
inline fun KSLog.debug(tag: String, message: Any, e: Throwable?) = log(LogLevel.DEBUG, tag, message, e)
inline fun KSLog.debug(tag: String, message: Any) = debug(tag, message, null)


inline fun KSLog.verbose(e: Throwable? = null, noinline messageBuilder: () -> Any) = log(LogLevel.VERBOSE, null, e, messageBuilder)
inline fun KSLog.verbose(tag: String?, e: Throwable? = null, noinline messageBuilder: () -> Any) = log(LogLevel.VERBOSE, tag, e, messageBuilder)
inline fun KSLog.verbose(tag: String, noinline messageBuilder: () -> Any) = verbose(tag, null, messageBuilder)
suspend inline fun KSLog.verboseS(e: Throwable? = null, noinline messageBuilder: suspend () -> Any) = logS(LogLevel.VERBOSE, null, e, messageBuilder)
suspend inline fun KSLog.verboseS(tag: String?, e: Throwable? = null, noinline messageBuilder: suspend () -> Any) = logS(LogLevel.VERBOSE, tag, e, messageBuilder)
suspend inline fun KSLog.verboseS(tag: String, noinline messageBuilder: suspend () -> Any) = verboseS(tag, null, messageBuilder)
inline fun KSLog.verbose(message: Any, e: Throwable?) = log(LogLevel.VERBOSE, message, e)
inline fun KSLog.verbose(message: String, e: Throwable) = verbose(message as Any, e)
inline fun KSLog.verbose(message: Any) = verbose(message, null)
inline fun KSLog.verbose(tag: String, message: Any, e: Throwable?) = log(LogLevel.VERBOSE, tag, message, e)
inline fun KSLog.verbose(tag: String, message: Any) = verbose(tag, message, null)


inline fun KSLog.info(e: Throwable? = null, noinline messageBuilder: () -> Any) = log(LogLevel.INFO, null, e, messageBuilder)
inline fun KSLog.info(tag: String?, e: Throwable? = null, noinline messageBuilder: () -> Any) = log(LogLevel.INFO, tag, e, messageBuilder)
inline fun KSLog.info(tag: String, noinline messageBuilder: () -> Any) = info(tag, null, messageBuilder)
suspend inline fun KSLog.infoS(e: Throwable? = null, noinline messageBuilder: suspend () -> Any) = logS(LogLevel.INFO, null, e, messageBuilder)
suspend inline fun KSLog.infoS(tag: String?, e: Throwable? = null, noinline messageBuilder: suspend () -> Any) = logS(LogLevel.INFO, tag, e, messageBuilder)
suspend inline fun KSLog.infoS(tag: String, noinline messageBuilder: suspend () -> Any) = infoS(tag, null, messageBuilder)
inline fun KSLog.info(message: Any, e: Throwable?) = log(LogLevel.INFO, message, e)
inline fun KSLog.info(message: String, e: Throwable) = info(message as Any, e)
inline fun KSLog.info(message: Any) = info(message, null)
inline fun KSLog.info(tag: String, message: Any, e: Throwable?) = log(LogLevel.INFO, tag, message, e)
inline fun KSLog.info(tag: String, message: Any) = info(tag, message, null)


inline fun KSLog.warning(e: Throwable? = null, noinline messageBuilder: () -> Any) = log(LogLevel.WARNING, null, e, messageBuilder)
inline fun KSLog.warning(tag: String?, e: Throwable? = null, noinline messageBuilder: () -> Any) = log(LogLevel.WARNING, tag, e, messageBuilder)
inline fun KSLog.warning(tag: String, noinline messageBuilder: () -> Any) = warning(tag, null, messageBuilder)
suspend inline fun KSLog.warningS(e: Throwable? = null, noinline messageBuilder: suspend () -> Any) = logS(LogLevel.WARNING, null, e, messageBuilder)
suspend inline fun KSLog.warningS(tag: String?, e: Throwable? = null, noinline messageBuilder: suspend () -> Any) = logS(LogLevel.WARNING, tag, e, messageBuilder)
suspend inline fun KSLog.warningS(tag: String, noinline messageBuilder: suspend () -> Any) = warningS(tag, null, messageBuilder)
inline fun KSLog.warning(message: Any, e: Throwable?) = log(LogLevel.WARNING, message, e)
inline fun KSLog.warning(message: String, e: Throwable) = warning(message as Any, e)
inline fun KSLog.warning(message: Any) = warning(message, null)
inline fun KSLog.warning(tag: String, message: Any, e: Throwable?) = log(LogLevel.WARNING, tag, message, e)
inline fun KSLog.warning(tag: String, message: Any) = warning(tag, message, null)


inline fun KSLog.error(e: Throwable? = null, noinline messageBuilder: () -> Any) = log(LogLevel.ERROR, null, e, messageBuilder)
inline fun KSLog.error(tag: String?, e: Throwable? = null, noinline messageBuilder: () -> Any) = log(LogLevel.ERROR, tag, e, messageBuilder)
inline fun KSLog.error(tag: String, noinline messageBuilder: () -> Any) = error(tag, null, messageBuilder)
suspend inline fun KSLog.errorS(e: Throwable? = null, noinline messageBuilder: suspend () -> Any) = logS(LogLevel.ERROR, null, e, messageBuilder)
suspend inline fun KSLog.errorS(tag: String?, e: Throwable? = null, noinline messageBuilder: suspend () -> Any) = logS(LogLevel.ERROR, tag, e, messageBuilder)
suspend inline fun KSLog.errorS(tag: String, noinline messageBuilder: suspend () -> Any) = errorS(tag, null, messageBuilder)
inline fun KSLog.error(message: Any, e: Throwable?) = log(LogLevel.ERROR, message, e)
inline fun KSLog.error(message: String, e: Throwable) = error(message as Any, e)
inline fun KSLog.error(message: Any) = error(message, null)
inline fun KSLog.error(tag: String, message: Any, e: Throwable?) = log(LogLevel.ERROR, tag, message, e)
inline fun KSLog.error(tag: String, message: Any) = error(tag, message, null)


inline fun KSLog.assert(e: Throwable? = null, noinline messageBuilder: () -> Any) = log(LogLevel.ASSERT, null, e, messageBuilder)
inline fun KSLog.assert(tag: String?, e: Throwable? = null, noinline messageBuilder: () -> Any) = log(LogLevel.ASSERT, tag, e, messageBuilder)
inline fun KSLog.assert(tag: String, noinline messageBuilder: () -> Any) = assert(tag, null, messageBuilder)
suspend inline fun KSLog.assertS(e: Throwable? = null, noinline messageBuilder: suspend () -> Any) = logS(LogLevel.ASSERT, null, e, messageBuilder)
suspend inline fun KSLog.assertS(tag: String?, e: Throwable? = null, noinline messageBuilder: suspend () -> Any) = logS(LogLevel.ASSERT, tag, e, messageBuilder)
suspend inline fun KSLog.assertS(tag: String, noinline messageBuilder: suspend () -> Any) = assertS(tag, null, messageBuilder)
inline fun KSLog.assert(message: Any, e: Throwable?) = log(LogLevel.ASSERT, message, e)
inline fun KSLog.assert(message: String, e: Throwable) = assert(message as Any, e)
inline fun KSLog.assert(message: Any) = assert(message, null)
inline fun KSLog.assert(tag: String, message: Any, e: Throwable?) = log(LogLevel.ASSERT, tag, message, e)
inline fun KSLog.assert(tag: String, message: Any) = assert(tag, message, null)

// ----- Short names


inline fun KSLog.l(level: LogLevel, tag: String?, e: Throwable? = null, noinline messageBuilder: () -> Any) = log(level, tag, e, messageBuilder)
inline fun KSLog.l(level: LogLevel, tag: String, noinline messageBuilder: () -> Any) = l(level, tag, null, messageBuilder)
inline fun KSLog.l(level: LogLevel, e: Throwable? = null, noinline messageBuilder: () -> Any) = log(level, e, messageBuilder)
suspend inline fun KSLog.lS(level: LogLevel, tag: String?, e: Throwable? = null, noinline messageBuilder: suspend () -> Any) = logS(level, tag, e, messageBuilder)
suspend inline fun KSLog.lS(level: LogLevel, tag: String, noinline messageBuilder: suspend () -> Any) = lS(level, tag, null, messageBuilder)
suspend inline fun KSLog.lS(level: LogLevel, e: Throwable? = null, noinline messageBuilder: suspend () -> Any) = logS(level, e, messageBuilder)
inline fun KSLog.l(level: LogLevel, message: Any, e: Throwable?) = log(level, message, e)
inline fun KSLog.l(level: LogLevel, message: String, e: Throwable) = l(level, message as Any, e)
inline fun KSLog.l(level: LogLevel, message: Any) = log(level, message, null)
inline fun KSLog.l(level: LogLevel, tag: String, message: Any, e: Throwable?) = log(level, tag, message, e)
inline fun KSLog.l(level: LogLevel, tag: String, message: Any) = log(level, tag, message, null)


inline fun KSLog.t(e: Throwable? = null, noinline messageBuilder: () -> Any) = trace(e, messageBuilder)
inline fun KSLog.t(tag: String?, e: Throwable? = null, noinline messageBuilder: () -> Any) = trace(tag, e, messageBuilder)
inline fun KSLog.t(tag: String, noinline messageBuilder: () -> Any) = t(tag, null, messageBuilder)
suspend inline fun KSLog.tS(e: Throwable? = null, noinline messageBuilder: suspend () -> Any) = traceS(e, messageBuilder)
suspend inline fun KSLog.tS(tag: String?, e: Throwable? = null, noinline messageBuilder: suspend () -> Any) = traceS(tag, e, messageBuilder)
suspend inline fun KSLog.tS(tag: String, noinline messageBuilder: suspend () -> Any) = tS(tag, null, messageBuilder)
inline fun KSLog.t(message: Any, e: Throwable?) = trace(message, e)
inline fun KSLog.t(message: String, e: Throwable) = t(message as Any, e)
inline fun KSLog.t(message: Any) = t(message, null)
inline fun KSLog.t(tag: String, message: Any, e: Throwable?) = trace(tag, message, e)
inline fun KSLog.t(tag: String, message: Any) = t(tag, message, null)


inline fun KSLog.d(e: Throwable? = null, noinline messageBuilder: () -> Any) = debug(e, messageBuilder)
inline fun KSLog.d(tag: String?, e: Throwable? = null, noinline messageBuilder: () -> Any) = debug(tag, e, messageBuilder)
inline fun KSLog.d(tag: String, noinline messageBuilder: () -> Any) = d(tag, null, messageBuilder)
suspend inline fun KSLog.dS(e: Throwable? = null, noinline messageBuilder: suspend () -> Any) = debugS(e, messageBuilder)
suspend inline fun KSLog.dS(tag: String?, e: Throwable? = null, noinline messageBuilder: suspend () -> Any) = debugS(tag, e, messageBuilder)
suspend inline fun KSLog.dS(tag: String, noinline messageBuilder: suspend () -> Any) = dS(tag, null, messageBuilder)
inline fun KSLog.d(message: Any, e: Throwable?) = debug(message, e)
inline fun KSLog.d(message: String, e: Throwable) = d(message as Any, e)
inline fun KSLog.d(message: Any) = d(message, null)
inline fun KSLog.d(tag: String, message: Any, e: Throwable?) = debug(tag, message, e)
inline fun KSLog.d(tag: String, message: Any) = d(tag, message, null)

inline fun KSLog.v(e: Throwable? = null, noinline messageBuilder: () -> Any) = verbose(e, messageBuilder)
inline fun KSLog.v(tag: String?, e: Throwable? = null, noinline messageBuilder: () -> Any) = verbose(tag, e, messageBuilder)
inline fun KSLog.v(tag: String, noinline messageBuilder: () -> Any) = v(tag, null, messageBuilder)
suspend inline fun KSLog.vS(e: Throwable? = null, noinline messageBuilder: suspend () -> Any) = verboseS(e, messageBuilder)
suspend inline fun KSLog.vS(tag: String?, e: Throwable? = null, noinline messageBuilder: suspend () -> Any) = verboseS(tag, e, messageBuilder)
suspend inline fun KSLog.vS(tag: String, noinline messageBuilder: suspend () -> Any) = vS(tag, null, messageBuilder)
inline fun KSLog.v(message: Any, e: Throwable?) = verbose(message, e)
inline fun KSLog.v(message: String, e: Throwable) = v(message as Any, e)
inline fun KSLog.v(message: Any) = v(message, null)
inline fun KSLog.v(tag: String, message: Any, e: Throwable?) = verbose(tag, message, e)
inline fun KSLog.v(tag: String, message: Any) = v(tag, message, null)


inline fun KSLog.i(e: Throwable? = null, noinline messageBuilder: () -> Any) = info(e, messageBuilder)
inline fun KSLog.i(tag: String?, e: Throwable? = null, noinline messageBuilder: () -> Any) = info(tag, e, messageBuilder)
inline fun KSLog.i(tag: String, noinline messageBuilder: () -> Any) = i(tag, null, messageBuilder)
suspend inline fun KSLog.iS(e: Throwable? = null, noinline messageBuilder: suspend () -> Any) = infoS(e, messageBuilder)
suspend inline fun KSLog.iS(tag: String?, e: Throwable? = null, noinline messageBuilder: suspend () -> Any) = infoS(tag, e, messageBuilder)
suspend inline fun KSLog.iS(tag: String, noinline messageBuilder: suspend () -> Any) = iS(tag, null, messageBuilder)
inline fun KSLog.i(message: Any, e: Throwable?) = info(message, e)
inline fun KSLog.i(message: String, e: Throwable) = i(message as Any, e)
inline fun KSLog.i(message: Any) = i(message, null)
inline fun KSLog.i(tag: String, message: Any, e: Throwable?) = info(tag, message, e)
inline fun KSLog.i(tag: String, message: Any) = i(tag, message, null)


inline fun KSLog.w(e: Throwable? = null, noinline messageBuilder: () -> Any) = warning(e, messageBuilder)
inline fun KSLog.w(tag: String?, e: Throwable? = null, noinline messageBuilder: () -> Any) = warning(tag, e, messageBuilder)
inline fun KSLog.w(tag: String, noinline messageBuilder: () -> Any) = w(tag, null, messageBuilder)
suspend inline fun KSLog.wS(e: Throwable? = null, noinline messageBuilder: suspend () -> Any) = warningS(e, messageBuilder)
suspend inline fun KSLog.wS(tag: String?, e: Throwable? = null, noinline messageBuilder: suspend () -> Any) = warningS(tag, e, messageBuilder)
suspend inline fun KSLog.wS(tag: String, noinline messageBuilder: suspend () -> Any) = wS(tag, null, messageBuilder)
inline fun KSLog.w(message: Any, e: Throwable?) = warning(message, e)
inline fun KSLog.w(message: String, e: Throwable) = w(message as Any, e)
inline fun KSLog.w(message: Any) = w(message, null)
inline fun KSLog.w(tag: String, message: Any, e: Throwable?) = warning(tag, message, e)
inline fun KSLog.w(tag: String, message: Any) = w(tag, message, null)


inline fun KSLog.e(e: Throwable? = null, noinline messageBuilder: () -> Any) = error(e, messageBuilder)
inline fun KSLog.e(tag: String?, e: Throwable? = null, noinline messageBuilder: () -> Any) = error(tag, e, messageBuilder)
inline fun KSLog.e(tag: String, noinline messageBuilder: () -> Any) = e(tag, null, messageBuilder)
suspend inline fun KSLog.eS(e: Throwable? = null, noinline messageBuilder: suspend () -> Any) = errorS(e, messageBuilder)
suspend inline fun KSLog.eS(tag: String?, e: Throwable? = null, noinline messageBuilder: suspend () -> Any) = errorS(tag, e, messageBuilder)
suspend inline fun KSLog.eS(tag: String, noinline messageBuilder: suspend () -> Any) = eS(tag, null, messageBuilder)
inline fun KSLog.e(message: Any, e: Throwable?) = error(message, e)
inline fun KSLog.e(message: String, e: Throwable) = e(message as Any, e)
inline fun KSLog.e(message: Any) = e(message, null)
inline fun KSLog.e(tag: String, message: Any, e: Throwable?) = error(tag, message, e)
inline fun KSLog.e(tag: String, message: Any) = e(tag, message, null)


inline fun KSLog.wtf(e: Throwable? = null, noinline messageBuilder: () -> Any) = assert(e, messageBuilder)
inline fun KSLog.wtf(tag: String?, e: Throwable? = null, noinline messageBuilder: () -> Any) = assert(tag, e, messageBuilder)
inline fun KSLog.wtf(tag: String, noinline messageBuilder: () -> Any) = wtf(tag, null, messageBuilder)
suspend inline fun KSLog.wtfS(e: Throwable? = null, noinline messageBuilder: suspend () -> Any) = assertS(e, messageBuilder)
suspend inline fun KSLog.wtfS(tag: String?, e: Throwable? = null, noinline messageBuilder: suspend () -> Any) = assertS(tag, e, messageBuilder)
suspend inline fun KSLog.wtfS(tag: String, noinline messageBuilder: suspend () -> Any) = wtfS(tag, null, messageBuilder)
inline fun KSLog.wtf(message: Any, e: Throwable?) = assert(message, e)
inline fun KSLog.wtf(message: String, e: Throwable) = wtf(message as Any, e)
inline fun KSLog.wtf(message: Any) = wtf(message, null)
inline fun KSLog.wtf(tag: String, message: Any, e: Throwable?) = assert(tag, message, e)
inline fun KSLog.wtf(tag: String, message: Any) = wtf(tag, message, null)
