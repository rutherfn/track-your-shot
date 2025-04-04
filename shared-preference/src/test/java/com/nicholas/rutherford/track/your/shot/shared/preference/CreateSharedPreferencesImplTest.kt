package com.nicholas.rutherford.track.your.shot.shared.preference

import android.content.SharedPreferences
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import com.nicholas.rutherford.track.your.shot.shared.preference.create.CreateSharedPreferencesImpl
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class CreateSharedPreferencesImplTest {

    private lateinit var createSharedPreferencesImpl: CreateSharedPreferencesImpl

    private var sharedPreferences = mockk<SharedPreferences>(relaxed = true)
    private var editor = mockk<SharedPreferences.Editor>(relaxed = true)

    fun init() {
        createSharedPreferencesImpl = CreateSharedPreferencesImpl(editor = editor)
    }

    @BeforeEach
    fun beforeEach() {
        init()
    }

    @Nested
    inner class Editors {

        @BeforeEach
        fun beforeEach() {
            mockkStatic(SharedPreferences::class)
            mockkStatic(SharedPreferences.Editor::class)
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `createAppHasLaunchedPreference should call editor putBoolean and apply`() =
            runTest {
                val defaultValue = true

                coEvery { sharedPreferences.edit() } returns editor

                init()

                createSharedPreferencesImpl.createAppHasLaunchedPreference(value = defaultValue)

                verify {
                    editor.putBoolean(Constants.Preferences.APP_HAS_LAUNCHED, defaultValue)
                }
                verify { editor.apply() }
            }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `createShouldUpdateLoggedInPlayerListPreference should call editor putBoolean and apply`() =
        runTest {
            val defaultValue = true

            coEvery { sharedPreferences.edit() } returns editor

            init()

            createSharedPreferencesImpl.createShouldUpdateLoggedInPlayerListPreference(value = defaultValue)

            verify {
                editor.putBoolean(Constants.Preferences.SHOULD_UPDATE_LOGGED_IN_PLAYER_LIST, defaultValue)
            }
            verify { editor.apply() }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `createShouldUpdateLoggedInDeclaredShotListPreference should call editor putBoolean and apply`() =
        runTest {
            val defaultValue = true

            coEvery { sharedPreferences.edit() } returns editor

            init()

            createSharedPreferencesImpl.createShouldUpdateLoggedInDeclaredShotListPreference(value = defaultValue)

            verify {
                editor.putBoolean(Constants.Preferences.SHOULD_UPDATE_LOGGED_IN_DECLARED_SHOT_LIST, defaultValue)
            }
            verify { editor.apply() }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `createShouldShowTermsAndConditionsPreference should call editor putBoolean and apply`() =
        runTest {
            val defaultValue = true

            coEvery { sharedPreferences.edit() } returns editor

            init()

            createSharedPreferencesImpl.createShouldShowTermsAndConditionsPreference(value = defaultValue)

            verify {
                editor.putBoolean(Constants.Preferences.SHOULD_SHOW_TERM_AND_CONDITIONS, defaultValue)
            }
            verify { editor.apply() }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `createHasAccountAuthenticatedAccount should call editor putBoolean and apply`() =
        runTest {
            val defaultValue = true

            coEvery { sharedPreferences.edit() } returns editor

            init()

            createSharedPreferencesImpl.createHasAuthenticatedAccount(value = defaultValue)

            verify {
                editor.putBoolean(Constants.Preferences.HAS_AUTHENTICATED_ACCOUNT, defaultValue)
            }
            verify { editor.apply() }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `createIsLoggedIn should call editor putBoolean and apply`() =
        runTest {
            val defaultValue = true

            coEvery { sharedPreferences.edit() } returns editor

            init()

            createSharedPreferencesImpl.createIsLoggedIn(value = defaultValue)

            verify {
                editor.putBoolean(Constants.Preferences.IS_LOGGED_IN, defaultValue)
            }
            verify { editor.apply() }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `createPlayerFilterName should call editor putString and apply`() =
        runTest {
            val defaultValue = "value"

            coEvery { sharedPreferences.edit() } returns editor

            init()

            createSharedPreferencesImpl.createPlayerFilterName(value = defaultValue)

            verify {
                editor.putString(Constants.Preferences.PLAYER_FILTER_NAME, defaultValue)
            }
            verify { editor.apply() }
        }
}
