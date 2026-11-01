package dev.inmo.kslog.common

import dev.inmo.kslog.common.filter.filtered
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue

class CallbackKSLogTest {
    @Test
    fun forwardsExactArguments() {
        val expectedThrowable = IllegalStateException("boom")
        var observedLevel: LogLevel? = null
        var observedTag: String? = null
        var observedMessage: Any? = null
        var observedThrowable: Throwable? = null

        val logger = KSLog { level, tag, message, throwable ->
            observedLevel = level
            observedTag = tag
            observedMessage = message
            observedThrowable = throwable
        }

        logger.performLog(LogLevel.WARNING, "network", "retrying", expectedThrowable)

        assertEquals(LogLevel.WARNING, observedLevel)
        assertEquals("network", observedTag)
        assertEquals("retrying", observedMessage)
        assertSame(expectedThrowable, observedThrowable)
    }

    @Test
    fun filteredLoggerDoesNotBuildRejectedLazyMessage() {
        var callbackInvoked = false
        var messageBuilt = false
        val logger = KSLog { _, _, _, _ -> callbackInvoked = true }
            .filtered { level, _, _ -> level >= LogLevel.INFO }

        logger.performLog(LogLevel.DEBUG, "test", null) {
            messageBuilt = true
            "expensive"
        }

        assertFalse(callbackInvoked)
        assertFalse(messageBuilt)

        logger.performLog(LogLevel.ERROR, "test", null) {
            messageBuilt = true
            "needed"
        }

        assertTrue(callbackInvoked)
        assertTrue(messageBuilt)
    }
}
