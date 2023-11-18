package dev.inmo.kslog.common

import kotlin.js.Console

external interface ExtendedConsole : Console {
    fun trace()
    fun debug(vararg o: Any?)
    fun assert(vararg o: Any?)
}

/**
 * [https://developer.mozilla.org/en-US/docs/Web/API/console/debug_static](https://developer.mozilla.org/en-US/docs/Web/API/console/debug_static)
 */
fun Console.debug(vararg args: Any?) = unsafeCast<ExtendedConsole>().debug(*args)

/**
 * [https://developer.mozilla.org/en-US/docs/Web/APtraceI/console/assert_static](https://developer.mozilla.org/en-US/docs/Web/API/console/assert_static)
 */
fun Console.assert(vararg args: Any?) = unsafeCast<ExtendedConsole>().assert(*args)

/**
 * [https://developer.mozilla.org/en-US/docs/Web/API/console/trace_static](https://developer.mozilla.org/en-US/docs/Web/API/console/trace_static)
 */
fun Console.trace() = unsafeCast<ExtendedConsole>().trace()
