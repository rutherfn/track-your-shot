package com.nicholas.rutherford.track.your.shot.helper.extensions

import com.nicholas.rutherford.track.your.shot.feature.splash.StringsIds
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class ExtensionsTest {

    @Nested
    inner class SafeLet {

        @Nested
        inner class WithTwoValues {

            @Test
            fun `with two non null params should return expected result`() {
                val result = safeLet("Test1", "Test2") { value1, value2 ->
                    "Here is the values $value1 $value2"
                }

                Assertions.assertEquals(result, "Here is the values Test1 Test2")
            }

            @Test
            fun `when one param is null should return expected result`() {
                val test2: String? = null
                val result = safeLet("Test1", test2) { value1, value2 ->
                    "Here is the values $value1 $value2"
                }

                Assertions.assertNull(result)
            }
        }

        @Nested
        inner class WithThreeValues {

            @Test
            fun `when three non null params should return expected result`() {
                val result = safeLet("Test1", "Test2", "Test3") { value1, value2, value3 ->
                    "Here is the values $value1 $value2 $value3"
                }

                Assertions.assertEquals(result, "Here is the values Test1 Test2 Test3")
            }

            @Test
            fun `when one param is null should return expected result`() {
                val test3: String? = null
                val result = safeLet("Test1", "Test2", test3) { value1, value2, value3 ->
                    "Here is the values $value1 $value2 $value3"
                }

                Assertions.assertNull(result)
            }
        }

        @Nested
        inner class WithFourValues {

            @Test
            fun `when four non null params should return expected result`() {
                val result = safeLet("Test1", "Test2", "Test3", "Test4") { value1, value2, value3, value4 ->
                    "Here is the values $value1 $value2 $value3 $value4"
                }

                Assertions.assertEquals(result, "Here is the values Test1 Test2 Test3 Test4")
            }

            @Test
            fun `when one param is null should return expected result`() {
                val test4: String? = null
                val result = safeLet("Test1", "Test2", "Test3", test4) { value1, value2, value3, value4 ->
                    "Here is the values $value1 $value2 $value3 $value4"
                }

                Assertions.assertNull(result)
            }
        }

        @Nested
        inner class WithFiveValues {

            @Test
            fun `when five non null params should return expected result`() {
                val result = safeLet("Test1", "Test2", "Test3", "Test4", "Test5") { value1, value2, value3, value4, value5 ->
                    "Here is the values $value1 $value2 $value3 $value4 $value5"
                }

                Assertions.assertEquals(result, "Here is the values Test1 Test2 Test3 Test4 Test5")
            }

            @Test
            fun `when one param is null should return expected result`() {
                val test5: String? = null
                val result = safeLet("Test1", "Test2", "Test3", "Test4", test5) { value1, value2, value3, value4, value5 ->
                    "Here is the values $value1 $value2 $value3 $value4 $value5"
                }

                Assertions.assertNull(result)
            }
        }
    }

    @Nested
    inner class ToPlayerPositionAbvId {

        @Test
        fun `when value is 0 should return point guard value`() {
            val playerPosition = 0
            Assertions.assertEquals(playerPosition.toPlayerPositionAbvId(), StringsIds.pg)
        }

        @Test
        fun `when value is 1 should return shooting guard value`() {
            val playerPosition = 1
            Assertions.assertEquals(playerPosition.toPlayerPositionAbvId(), StringsIds.sg)
        }

        @Test
        fun `when value is 2 should return small forward value`() {
            val playerPosition = 2
            Assertions.assertEquals(playerPosition.toPlayerPositionAbvId(), StringsIds.sf)
        }

        @Test
        fun `when value is 3 should return power forward value`() {
            val playerPosition = 3
            Assertions.assertEquals(playerPosition.toPlayerPositionAbvId(), StringsIds.pf)
        }

        @Test
        fun `when value is 4 should return center value`() {
            val playerPosition = 4
            Assertions.assertEquals(playerPosition.toPlayerPositionAbvId(), StringsIds.center)
        }

        @Test
        fun `when value does not contain ay positions should return null`() {
            val playerPosition = 5
            Assertions.assertEquals(playerPosition.toPlayerPositionAbvId(), null)
        }
    }
}