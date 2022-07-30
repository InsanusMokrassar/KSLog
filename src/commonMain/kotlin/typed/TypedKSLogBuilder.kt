package dev.inmo.kslog.common.typed

import dev.inmo.kslog.common.*
import kotlin.reflect.KClass

class TypedKSLogBuilder(
    private val preset: Map<KClass<*>?, KSLog> = emptyMap()
) {
    private val loggers = mutableMapOf<KClass<*>?, KSLog>().apply {
        putAll(preset)
    }

    fun on(kClass: KClass<*>?, with: KSLog) = loggers.put(kClass, with)
    fun on(kClass: KClass<*>?, with: SimpleKSLogCallback) = loggers.put(kClass, CallbackKSLog(with))

    fun default(with: KSLog) = on(null, with)
    fun default(with: SimpleKSLogCallback) = on(null, with)

    inline fun <reified T: Any> on(with: KSLog) = on(T::class, with)
    inline fun <reified T: Any> on(noinline with: SimpleKSLogCallback) = on(T::class, with)

    fun build() = TypedKSLog(loggers.toMap())
}

inline fun buildTypedLogger(
    preset: Map<KClass<*>?, KSLog> = emptyMap(),
    block: TypedKSLogBuilder.() -> Unit
) = TypedKSLogBuilder(preset).apply(block).build()
