package dev.inmo.kslog.common.typed

import dev.inmo.kslog.common.*
import kotlin.reflect.KClass

/**
 * Builder for creating [TypedKSLog] instances with a fluent API
 * 
 * Provides convenient methods for configuring type-based routing of log messages.
 * Supports both explicit [KClass] specifications and reified type parameters.
 * 
 * @param preset Optional initial map of type-to-logger associations to start with
 * 
 * @see buildTypedLogger DSL function for convenient usage
 */
class TypedKSLogBuilder(
    private val preset: Map<KClass<*>?, KSLog> = emptyMap()
) {
    private val loggers = mutableMapOf<KClass<*>?, KSLog>().apply {
        putAll(preset)
    }

    /**
     * Associates a specific class with a logger
     * 
     * @param kClass The class to route, or `null` for the default/fallback logger
     * @param with The logger to use for this class
     */
    fun on(kClass: KClass<*>?, with: KSLog) = loggers.put(kClass, with)

    /**
     * Associates a specific class with a callback-based logger
     * 
     * @param kClass The class to route, or `null` for the default/fallback logger
     * @param with The callback function to handle logs for this class
     */
    fun on(kClass: KClass<*>?, with: SimpleKSLogCallback) = loggers.put(kClass, CallbackKSLog(with))

    /**
     * Sets the default/fallback logger for messages of unregistered types
     * 
     * @param with The logger to use as default
     */
    fun default(with: KSLog) = on(null, with)

    /**
     * Sets the default/fallback logger using a callback function
     * 
     * @param with The callback function to handle logs from unregistered types
     */
    fun default(with: SimpleKSLogCallback) = on(null, with)

    /**
     * Associates a specific type with a logger using reified type parameter
     * 
     * Example: `on<MyEvent>(customLogger)`
     * 
     * @param T The type to route
     * @param with The logger to use for this type
     */
    inline fun <reified T: Any> on(with: KSLog) = on(T::class, with)

    /**
     * Associates a specific type with a callback-based logger using reified type parameter
     * 
     * Example: `on<MyEvent> { level, tag, message, error -> ... }`
     * 
     * @param T The type to route
     * @param with The callback function to handle logs for this type
     */
    inline fun <reified T: Any> on(noinline with: SimpleKSLogCallback) = on(T::class, with)

    /**
     * Builds the [TypedKSLog] instance with the configured type-to-logger associations
     */
    fun build() = TypedKSLog(loggers.toMap())
}

/**
 * DSL function for building a [TypedKSLog] with a fluent configuration block
 * 
 * Provides a convenient way to create typed loggers with type-to-logger associations.
 * 
 * Example:
 * ```kotlin
 * val logger = buildTypedLogger {
 *     on<ErrorEvent>(errorLogger)
 *     on<WarningEvent>(warningLogger)
 *     default(defaultLogger)
 * }
 * ```
 * 
 * @param preset Optional initial map of type-to-logger associations
 * @param block Configuration block for specifying type-to-logger associations
 * @return A new [TypedKSLog] instance
 * @see TypedKSLogBuilder
 */
inline fun buildTypedLogger(
    preset: Map<KClass<*>?, KSLog> = emptyMap(),
    block: TypedKSLogBuilder.() -> Unit
) = TypedKSLogBuilder(preset).apply(block).build()
