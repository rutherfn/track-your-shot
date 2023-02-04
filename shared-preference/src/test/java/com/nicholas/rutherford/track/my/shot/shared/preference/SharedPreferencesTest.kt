package com.nicholas.rutherford.track.my.shot.shared.preference

import android.app.Application
import android.content.Context
import com.nicholas.rutherford.track.my.shot.helper.constants.SharedPreferencesConstants
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class SharedPreferencesTest {

    private lateinit var sharedPreferences: SharedPreferences

    private var application = mockk<Application>(relaxed = true)
    private var editor = mockk<android.content.SharedPreferences.Editor>(relaxed = true)

    fun init() {
        sharedPreferences = SharedPreferences(application = application)
    }

    @BeforeEach
    fun beforeEach() {
        init()
    }

    @Nested
    inner class AccountHasBeenCreated {

        @BeforeEach
        fun beforeEach() {
            mockkStatic(android.content.SharedPreferences::class)
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `should return true when getBoolean returns back true`() =
            runTest {
                coEvery {
                    application.getSharedPreferences(SharedPreferencesConstants.Core.TRACK_MY_SHOT_PREFERENCES, Context.MODE_PRIVATE).getBoolean(
                        SharedPreferencesConstants.Preferences.ACCOUNT_HAS_BEEN_CREATED,
                        false
                    )
                } returns true

                init()

                Assertions.assertEquals(sharedPreferences.accountHasBeenCreated(), true)
            }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `should return false when getBoolean returns back no value`() =
            runTest {
                init()

                Assertions.assertEquals(sharedPreferences.accountHasBeenCreated(), false)
            }
    }

    @Nested
    inner class Editors {

        @BeforeEach
        fun beforeEach() {
            mockkStatic(android.content.SharedPreferences::class)
            mockkStatic(android.content.SharedPreferences.Editor::class)
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `createAccountHasBeenCreatedPreference should call editor putBoolean and apply`() =
            runTest {
                val defaultValue = true

                coEvery {
                    application.getSharedPreferences(
                        SharedPreferencesConstants.Core.TRACK_MY_SHOT_PREFERENCES,
                        android.content.Context.MODE_PRIVATE
                    ).edit()
                } returns editor

                init()

                sharedPreferences.createAccountHasBeenCreatedPreference(value = defaultValue)

                verify {
                    editor.putBoolean(
                        SharedPreferencesConstants.Preferences.ACCOUNT_HAS_BEEN_CREATED,
                        defaultValue
                    )
                }
                verify { editor.apply() }
            }
    }
}
