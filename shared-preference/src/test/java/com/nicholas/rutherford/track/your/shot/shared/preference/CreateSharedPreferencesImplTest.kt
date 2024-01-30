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
        fun `createAppHasLaunchedPreference should call editor putBoolean and apply`() =
            runTest {
                val defaultValue = true

                coEvery { sharedPreferences.edit() } returns editor

                init()

                createSharedPreferencesImpl.createAppHasLaunchedPreference(value = defaultValue)

                verify {
                    editor.putBoolean(SharedPreferencesConstants.Preferences.APP_HAS_LAUNCHED, defaultValue)
                }
                verify { editor.apply() }
            }
    }
}
