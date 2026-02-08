package com.nicholas.rutherford.track.your.shot.room.response

import android.app.Application
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.data.room.response.HasShotsLoggedFilter
import com.nicholas.rutherford.track.your.shot.data.room.response.toHasShotsLoggedFilter
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class HasShotsLoggedFilterTest {

    private var application = mockk<Application>(relaxed = true)

    @Nested
    inner class TypesValidation {

        @Test
        fun `when hasShotsLoggedFilter is HasShots should contain hasShotsLoggedValue`() {
            val value = HasShotsLoggedFilter.HasShots.value

            Assertions.assertEquals(value, Constants.HAS_SHOTS_LOGGED_VALUE)
            Assertions.assertEquals(value, 1)
        }

        @Test
        fun `when hasShotsLoggedFilter is NoShots should contain noShotsLoggedValue`() {
            val value = HasShotsLoggedFilter.NoShots.value

            Assertions.assertEquals(value, Constants.NO_SHOTS_LOGGED_VALUE)
            Assertions.assertEquals(value, 0)
        }

        @Test
        fun `when hasShotsLoggedFilter is Both should contain bothShotsLoggedValue`() {
            val value = HasShotsLoggedFilter.Both.value

            Assertions.assertEquals(value, Constants.BOTH_SHOTS_LOGGED_VALUE)
            Assertions.assertEquals(value, 2)
        }

        @Test
        fun `when hasShotsLoggedFilter is None should contain noShotsLoggedFilterValue`() {
            val value = HasShotsLoggedFilter.None.value

            Assertions.assertEquals(value, Constants.NO_SHOTS_LOGGED_FILTER_VALUE)
            Assertions.assertEquals(value, 3)
        }
    }

    @Nested
    inner class FromValue {

        @Test
        fun `when value passed in is hasShotsLoggedValue should return HasShots`() {
            val result = HasShotsLoggedFilter.Companion.fromValue(value = Constants.HAS_SHOTS_LOGGED_VALUE)

            Assertions.assertEquals(result, HasShotsLoggedFilter.HasShots)
        }

        @Test
        fun `when value passed in is noShotsLoggedValue should return NoShots`() {
            val result = HasShotsLoggedFilter.Companion.fromValue(value = Constants.NO_SHOTS_LOGGED_VALUE)

            Assertions.assertEquals(result, HasShotsLoggedFilter.NoShots)
        }

        @Test
        fun `when value passed in is bothShotsLoggedValue should return Both`() {
            val result = HasShotsLoggedFilter.Companion.fromValue(value = Constants.BOTH_SHOTS_LOGGED_VALUE)

            Assertions.assertEquals(result, HasShotsLoggedFilter.Both)
        }

        @Test
        fun `when value passed in is noShotsLoggedFilterValue should return None`() {
            val result = HasShotsLoggedFilter.Companion.fromValue(value = Constants.NO_SHOTS_LOGGED_FILTER_VALUE)

            Assertions.assertEquals(result, HasShotsLoggedFilter.None)
        }

        @Test
        fun `when value passed in is null should return None`() {
            val result = HasShotsLoggedFilter.Companion.fromValue(value = null)

            Assertions.assertEquals(result, HasShotsLoggedFilter.None)
        }

        @Test
        fun `when value passed in is unknown value should return None`() {
            val result = HasShotsLoggedFilter.Companion.fromValue(value = 999)

            Assertions.assertEquals(result, HasShotsLoggedFilter.None)
        }
    }

    @Nested
    inner class ToLocalizedString {

        @BeforeEach
        fun beforeEach() {
            every { application.getString(StringsIds.hasShots) } returns "Has Shots"
            every { application.getString(StringsIds.noShots) } returns "No Shots"
            every { application.getString(StringsIds.both) } returns "Both"
        }

        @Test
        fun `when hasShotsLoggedFilter is HasShots should return hasShots string`() {
            val result = HasShotsLoggedFilter.HasShots.toLocalizedString(application = application)

            Assertions.assertEquals(result, "Has Shots")
        }

        @Test
        fun `when hasShotsLoggedFilter is NoShots should return noShots string`() {
            val result = HasShotsLoggedFilter.NoShots.toLocalizedString(application = application)

            Assertions.assertEquals(result, "No Shots")
        }

        @Test
        fun `when hasShotsLoggedFilter is Both should return both string`() {
            val result = HasShotsLoggedFilter.Both.toLocalizedString(application = application)

            Assertions.assertEquals(result, "Both")
        }

        @Test
        fun `when hasShotsLoggedFilter is None should return empty string`() {
            val result = HasShotsLoggedFilter.None.toLocalizedString(application = application)

            Assertions.assertEquals(result, "")
        }
    }

    @Nested
    inner class ToHasShotsLoggedFilter {

        @BeforeEach
        fun beforeEach() {
            every { application.getString(StringsIds.hasShots) } returns "Has Shots"
            every { application.getString(StringsIds.noShots) } returns "No Shots"
            every { application.getString(StringsIds.both) } returns "Both"
        }

        @Test
        fun `when string is hasShots should return HasShots`() {
            val result = "Has Shots".toHasShotsLoggedFilter(application = application)

            Assertions.assertEquals(result, HasShotsLoggedFilter.HasShots)
        }

        @Test
        fun `when string is noShots should return NoShots`() {
            val result = "No Shots".toHasShotsLoggedFilter(application = application)

            Assertions.assertEquals(result, HasShotsLoggedFilter.NoShots)
        }

        @Test
        fun `when string is both should return Both`() {
            val result = "Both".toHasShotsLoggedFilter(application = application)

            Assertions.assertEquals(result, HasShotsLoggedFilter.Both)
        }

        @Test
        fun `when string is empty should return None`() {
            val result = "".toHasShotsLoggedFilter(application = application)

            Assertions.assertEquals(result, HasShotsLoggedFilter.None)
        }

        @Test
        fun `when string is unknown value should return None`() {
            val result = "Unknown".toHasShotsLoggedFilter(application = application)

            Assertions.assertEquals(result, HasShotsLoggedFilter.None)
        }
    }
}

