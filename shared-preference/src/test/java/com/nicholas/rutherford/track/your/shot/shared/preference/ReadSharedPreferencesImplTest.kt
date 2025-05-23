package com.nicholas.rutherford.track.your.shot.shared.preference

import android.content.SharedPreferences
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
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
    inner class AppHasBeenLaunched {

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
                        Constants.Preferences.APP_HAS_LAUNCHED,
                        false
                    )
                } returns true

                init()

                Assertions.assertEquals(readSharedPreferencesImpl.appHasBeenLaunched(), true)
            }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `should return false when getBoolean returns back no value`() =
            runTest {
                init()

                Assertions.assertEquals(readSharedPreferencesImpl.appHasBeenLaunched(), false)
            }
    }

    @Nested
    inner class ShouldUpdateLoggedInPlayerListState {

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
                        Constants.Preferences.SHOULD_UPDATE_LOGGED_IN_PLAYER_LIST,
                        false
                    )
                } returns true

                init()

                Assertions.assertEquals(readSharedPreferencesImpl.shouldUpdateLoggedInPlayerListState(), true)
            }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `should return false when getBoolean returns back no value`() =
            runTest {
                init()

                Assertions.assertEquals(readSharedPreferencesImpl.shouldUpdateLoggedInPlayerListState(), false)
            }
    }

    @Nested
    inner class ShouldUpdateLoggedInDeclaredShotListState {

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
                        Constants.Preferences.SHOULD_UPDATE_LOGGED_IN_DECLARED_SHOT_LIST,
                        false
                    )
                } returns true

                init()

                Assertions.assertEquals(readSharedPreferencesImpl.shouldUpdateLoggedInDeclaredShotListState(), true)
            }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `should return false when getBoolean returns back no value`() =
            runTest {
                init()

                Assertions.assertEquals(readSharedPreferencesImpl.shouldUpdateLoggedInDeclaredShotListState(), false)
            }
    }

    @Nested
    inner class IsLoggedIn {

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
                        Constants.Preferences.IS_LOGGED_IN,
                        false
                    )
                } returns true

                init()

                Assertions.assertEquals(readSharedPreferencesImpl.isLoggedIn(), true)
            }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `should return true when getBoolean returns back false`() =
            runTest {
                coEvery {
                    sharedPreferences.getBoolean(
                        Constants.Preferences.IS_LOGGED_IN,
                        false
                    )
                } returns false

                init()

                Assertions.assertEquals(readSharedPreferencesImpl.isLoggedIn(), false)
            }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `should return false when getBoolean returns back no value`() =
            runTest {
                init()

                Assertions.assertEquals(readSharedPreferencesImpl.isLoggedIn(), false)
            }
    }

    @Nested
    inner class PlayerFilterName {

        @BeforeEach
        fun beforeEach() {
            mockkStatic(SharedPreferences::class)
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `should return value when getString returns back a value`() =
            runTest {
                val value = "value"

                coEvery {
                    sharedPreferences.getString(
                        Constants.Preferences.PLAYER_FILTER_NAME,
                        ""
                    )
                } returns value

                init()

                Assertions.assertEquals(readSharedPreferencesImpl.playerFilterName(), value)
            }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `should return empty string when getString returns back no value`() =
            runTest {
                init()

                Assertions.assertEquals(readSharedPreferencesImpl.playerFilterName(), "")
            }
    }

    @Nested
    inner class DeclaredShotId {

        @BeforeEach
        fun beforeEach() {
            mockkStatic(SharedPreferences::class)
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `should return value when getInt returns back a value`() =
            runTest {
                val value = 2

                coEvery {
                    sharedPreferences.getInt(
                        Constants.Preferences.DECLARED_SHOT_ID,
                        -1
                    )
                } returns value

                init()

                Assertions.assertEquals(readSharedPreferencesImpl.declaredShotId(), value)
            }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `should return default value when getInt returns back no value`() =
            runTest {
                init()

                Assertions.assertEquals(readSharedPreferencesImpl.declaredShotId(), 0)
            }
    }

    @Nested
    inner class ShouldShowTermsAndConditions {

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
                        Constants.Preferences.SHOULD_SHOW_TERM_AND_CONDITIONS,
                        false
                    )
                } returns true

                init()

                Assertions.assertEquals(readSharedPreferencesImpl.shouldShowTermsAndConditions(), true)
            }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `should return true when getBoolean returns back false`() =
            runTest {
                coEvery {
                    sharedPreferences.getBoolean(
                        Constants.Preferences.SHOULD_SHOW_TERM_AND_CONDITIONS,
                        false
                    )
                } returns false

                init()

                Assertions.assertEquals(readSharedPreferencesImpl.shouldShowTermsAndConditions(), false)
            }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `should return false when getBoolean returns back no value`() =
            runTest {
                init()

                Assertions.assertEquals(readSharedPreferencesImpl.shouldShowTermsAndConditions(), false)
            }
    }
}
