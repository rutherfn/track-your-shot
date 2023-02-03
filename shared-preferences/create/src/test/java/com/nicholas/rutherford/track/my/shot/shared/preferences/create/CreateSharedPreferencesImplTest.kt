package com.nicholas.rutherford.track.my.shot.shared.preferences.create

import android.content.SharedPreferences
import com.nicholas.rutherford.track.my.shot.helper.constants.SharedPreferencesConstants
import io.mockk.*
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
        createSharedPreferencesImpl = CreateSharedPreferencesImpl(editor = editor)
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

                coEvery { sharedPreferences.edit() } returns editor

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
}
