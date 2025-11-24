package com.nicholas.rutherford.track.your.shot.helper.extensions.date

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DateExtImplTest {

    private lateinit var dateExt: DateExtImpl

    @BeforeEach
    fun setUp() {
        dateExt = DateExtImpl()
    }

    @Test
    fun `now should return current timestamp in milliseconds`() {
        val beforeTime = System.currentTimeMillis()
        val now = dateExt.now
        val afterTime = System.currentTimeMillis()

        Assertions.assertTrue(now >= beforeTime && now <= afterTime)
    }

    @Test
    fun `oneMinute should return 60000 milliseconds`() {
        Assertions.assertEquals(60000L, dateExt.oneMinute)
    }

    @Test
    fun `oneDay should return 86400000 milliseconds`() {
        Assertions.assertEquals(86400000L, dateExt.oneDay)
    }
}
