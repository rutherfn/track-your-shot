package com.nicholas.rutherford.track.your.shot.koin

import android.app.Application
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import com.nicholas.rutherford.track.your.shot.shared.preference.create.CreateSharedPreferences
import com.nicholas.rutherford.track.your.shot.shared.preference.create.CreateSharedPreferencesImpl
import com.nicholas.rutherford.track.your.shot.shared.preference.read.ReadSharedPreferences
import com.nicholas.rutherford.track.your.shot.shared.preference.read.ReadSharedPreferencesImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Koin module for providing SharedPreferences and related helper classes.
 *
 * This module provides:
 * - The default SharedPreferences instance.
 * - The SharedPreferences editor.
 * - Helper classes for reading and writing SharedPreferences.
 */
object SharedPreferenceModule {

    /** Koin module definitions for SharedPreferences and related helpers. */
    val modules = module {
        /** Provides the SharedPreferences instance for the app. */
        single {
            getSharedPreferences(androidApplication())
        }

        /** Provides the SharedPreferences editor instance. */
        single<android.content.SharedPreferences.Editor> {
            getSharedPreferences(androidApplication()).edit()
        }

        /** Provides a helper for creating or updating SharedPreferences values. */
        single<CreateSharedPreferences> {
            CreateSharedPreferencesImpl(editor = get())
        }

        /** Provides a helper for reading SharedPreferences values. */
        single<ReadSharedPreferences> {
            ReadSharedPreferencesImpl(sharedPreferences = get())
        }
    }

    /**
     * Returns the SharedPreferences instance for the application.
     *
     * @param androidApplication The application instance used to get SharedPreferences.
     * @return The SharedPreferences instance with the name defined in Constants.
     */
    private fun getSharedPreferences(androidApplication: Application): android.content.SharedPreferences {
        return androidApplication.getSharedPreferences(
            Constants.Preferences.TRACK_MY_SHOT_PREFERENCES,
            android.content.Context.MODE_PRIVATE
        )
    }
}
