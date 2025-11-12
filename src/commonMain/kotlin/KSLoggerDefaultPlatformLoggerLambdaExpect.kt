package dev.inmo.kslog.common

/**
 * Platform-specific default logging implementation
 *
 * This is an `expect` declaration that provides platform-specific logging behavior:
 * - **Android**: Uses `android.util.Log` (Log.d, Log.i, Log.w, Log.e, etc.)
 * - **JVM**: Uses `java.util.logging.Logger`
 * - **JS**: Uses browser `console` (console.debug, console.info, console.warn, console.error)
 * - **Native/WASM**: Uses `println` for output
 *
 * This lambda is used by [DefaultKSLog] as the default logging backend when no custom
 * logging function is provided.
 *
 * **Warning**: This is a global mutable variable. Changing it will affect all loggers
 * that use the default platform logger. It's primarily used by [KSLog.default].
 *
 * @see DefaultKSLog
 * @see KSLog.default
 */
expect var KSLoggerDefaultPlatformLoggerLambda: (level: LogLevel, tag: String, message: Any, throwable: Throwable?) -> Unit