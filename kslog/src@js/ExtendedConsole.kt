package dev.inmo.kslog.common

import kotlin.js.Console

/**
 * Extended Console interface that includes additional console methods
 * not present in Kotlin's standard Console interface
 *
 * Provides access to browser/Node.js console methods like `debug`, `trace`, and `assert`.
 */
external interface ExtendedConsole : Console {
    /**
     * Outputs a stack trace to the console
     */
    fun trace()

    /**
     * Outputs a debug message to the console
     *
     * @param o Objects to output
     */
    fun debug(vararg o: Any?)

    /**
     * Writes an error message to the console if the assertion is false
     *
     * @param assertion Boolean condition to test
     * @param o Objects to output if assertion is false
     */
    fun assert(assertion: Boolean, vararg o: Any?)
}

/**
 * Extension function to output debug messages to the console
 *
 * Maps to the browser's `console.debug()` method.
 *
 * [MDN Documentation](https://developer.mozilla.org/en-US/docs/Web/API/console/debug_static)
 *
 * @param args Objects to output
 */
fun Console.debug(vararg args: Any?) = unsafeCast<ExtendedConsole>().debug(*args)

/**
 * Extension function to write an error message if assertion fails
 *
 * Maps to the browser's `console.assert()` method.
 *
 * [MDN Documentation](https://developer.mozilla.org/en-US/docs/Web/API/console/assert_static)
 *
 * @param assertion Boolean condition to test
 * @param args Objects to output if assertion is false
 */
fun Console.assert(assertion: Boolean, vararg args: Any?) = unsafeCast<ExtendedConsole>().assert(assertion, *args)

/**
 * Extension function to output a stack trace to the console
 *
 * Maps to the browser's `console.trace()` method.
 *
 * [MDN Documentation](https://developer.mozilla.org/en-US/docs/Web/API/console/trace_static)
 */
fun Console.trace() = unsafeCast<ExtendedConsole>().trace()
