package com.nicholas.rutherford.track.my.shot

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    @DisplayName("Test with Custom Display Name in nested class")
    internal fun testInNestedClass() {
        assertEquals("LOL", "LO" + "L")
    }
}
