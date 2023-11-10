package com.nicholas.rutherford.track.your.shot.shared.preference

import android.content.SharedPreferences
import com.nicholas.rutherford.track.your.shot.helper.constants.SharedPreferencesConstants
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
        fun `createAccountHasBeenCreatedPreference should call editor putBoolean and apply`() =
            runTest {
                val defaultValue = true

                coEvery {
                    sharedPreferences.edit()
                } returns editor

                init()

                createSharedPreferencesImpl.createAccountHasBeenCreatedPreference(value = defaultValue)

                verify {
                    editor.putBoolean(
                        SharedPreferencesConstants.Preferences.ACCOUNT_HAS_BEEN_CREATED,
                        defaultValue
                    )
                }
                verify { editor.apply() }
            }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `createUnverifiedEmailPreference should call editor putString and apply`() =
        runTest {
            val defaultValue = "emailtest@gmail.com"

            coEvery {
                sharedPreferences.edit()
            } returns editor

            init()

            createSharedPreferencesImpl.createUnverifiedEmailPreference(value = defaultValue)

            verify {
                editor.putString(
                    SharedPreferencesConstants.Preferences.UNVERIFIED_EMAIL,
                    defaultValue
                )
            }
            verify { editor.apply() }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `createUnverifiedUsernamePreference should call editor putString and apply`() =
        runTest {
            val defaultValue = "newUsername112"

            coEvery {
                sharedPreferences.edit()
            } returns editor

            init()

            createSharedPreferencesImpl.createUnverifiedUsernamePreference(value = defaultValue)

            verify {
                editor.putString(
                    SharedPreferencesConstants.Preferences.UNVERIFIED_USERNAME,
                    defaultValue
                )
            }
            verify { editor.apply() }
        }
}
