package com.nicholas.rutherford.track.your.shot.shared.preference

import android.content.SharedPreferences
import com.nicholas.rutherford.track.your.shot.helper.constants.SharedPreferencesConstants
import com.nicholas.rutherford.track.your.shot.shared.preference.read.ReadSharedPreferencesImpl
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class ReadSharedPreferencesImplTest {

    private lateinit var readSharedPreferencesImpl: ReadSharedPreferencesImpl

    private var sharedPreferences = mockk<SharedPreferences>(relaxed = true)

    fun init() {
        readSharedPreferencesImpl = ReadSharedPreferencesImpl(sharedPreferences = sharedPreferences)
    }

    @BeforeEach
    fun beforeEach() {
        init()
    }

    @Nested
    inner class AccountHasBeenCreated {

        @BeforeEach
        fun beforeEach() {
            mockkStatic(SharedPreferences::class)
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `should return true when getBoolean returns back true`() =
            runTest {
                coEvery {
                    sharedPreferences.getBoolean(
                        SharedPreferencesConstants.Preferences.ACCOUNT_HAS_BEEN_CREATED,
                        false
                    )
                } returns true

                init()

                Assertions.assertEquals(readSharedPreferencesImpl.accountHasBeenCreated(), true)
            }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `should return false when getBoolean returns back no value`() =
            runTest {
                init()

                Assertions.assertEquals(readSharedPreferencesImpl.accountHasBeenCreated(), false)
            }
    }

    @Nested
    inner class UnVerifiedEmail {

        @BeforeEach
        fun beforeEach() {
            mockkStatic(SharedPreferences::class)
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `should return null when getString returns back null`() =
            runTest {
                coEvery {
                    sharedPreferences.getString(
                        SharedPreferencesConstants.Preferences.UNVERIFIED_EMAIL,
                        null
                    )
                } returns null

                init()

                Assertions.assertEquals(readSharedPreferencesImpl.unverifiedEmail(), null)
            }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `should return null when getString returns back empty string`() =
            runTest {
                coEvery {
                    sharedPreferences.getString(
                        SharedPreferencesConstants.Preferences.UNVERIFIED_EMAIL,
                        null
                    )
                } returns ""

                init()

                Assertions.assertEquals(readSharedPreferencesImpl.unverifiedEmail(), null)
            }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `should return back actual data when getString returns back data`() =
            runTest {
                val testEmail = "test@email11.com"
                coEvery {
                    sharedPreferences.getString(
                        SharedPreferencesConstants.Preferences.UNVERIFIED_EMAIL,
                        null
                    )
                } returns testEmail

                init()

                Assertions.assertEquals(readSharedPreferencesImpl.unverifiedEmail(), testEmail)
            }
    }

    @Nested
    inner class UnVerifiedUsername {

        @BeforeEach
        fun beforeEach() {
            mockkStatic(SharedPreferences::class)
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `should return null when getString returns back null`() =
            runTest {
                coEvery {
                    sharedPreferences.getString(
                        SharedPreferencesConstants.Preferences.UNVERIFIED_USERNAME,
                        null
                    )
                } returns null

                init()

                Assertions.assertEquals(readSharedPreferencesImpl.unverifiedUsername(), null)
            }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `should return null when getString returns back empty string`() =
            runTest {
                coEvery {
                    sharedPreferences.getString(
                        SharedPreferencesConstants.Preferences.UNVERIFIED_USERNAME,
                        null
                    )
                } returns ""

                init()

                Assertions.assertEquals(readSharedPreferencesImpl.unverifiedUsername(), null)
            }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `should return back actual data when getString returns back data`() =
            runTest {
                val username = "testUsername"
                coEvery {
                    sharedPreferences.getString(
                        SharedPreferencesConstants.Preferences.UNVERIFIED_USERNAME,
                        null
                    )
                } returns username

                init()

                Assertions.assertEquals(readSharedPreferencesImpl.unverifiedUsername(), username)
            }
    }
}
