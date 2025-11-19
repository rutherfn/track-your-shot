package com.nicholas.rutherford.track.your.shot.data.store.writer

import android.app.Application
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.nicholas.rutherford.track.your.shot.data.store.dataStore
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants

/**
 * Created by Nicholas Rutherford, last edited on 2025-10-27
 *
 * Implementation of [DataStorePreferencesWriter] responsible for saving values to DataStore.
 * This class writes various user-related flags and data into persistent storage using
 * Android DataStore preferences.
 *
 * @param application The [Application] instance used to access the DataStore.
 */
class DataStorePreferencesWriterImpl(private val application: Application) :
    DataStorePreferencesWriter {

    /** Saves whether the app has been launched before. */
    override suspend fun saveAppHasLaunched(value: Boolean) {
        application.dataStore.edit { preference ->
            preference[booleanPreferencesKey(Constants.Preferences.APP_HAS_LAUNCHED)] = value
        }
    }

    /** Saves whether the terms and conditions should be shown to the user. */
    override suspend fun saveShouldShowTermsAndConditions(value: Boolean) {
        application.dataStore.edit { preference ->
            preference[booleanPreferencesKey(Constants.Preferences.SHOULD_SHOW_TERM_AND_CONDITIONS)] = value
        }
    }

    /** Saves the logged-in state of the user. */
    override suspend fun saveIsLoggedIn(value: Boolean) {
        application.dataStore.edit { preference ->
            preference[booleanPreferencesKey(Constants.Preferences.IS_LOGGED_IN)] = value
        }
    }

    /** Saves the player filter name. */
    override suspend fun savePlayerFilterName(value: String) {
        application.dataStore.edit { preference ->
            preference[stringPreferencesKey(Constants.Preferences.PLAYER_FILTER_NAME)] = value
        }
    }

    /** Saves the voice toggled debug enabled state of the user. */
    override suspend fun saveVoiceToggledDebugEnabled(value: Boolean) {
        application.dataStore.edit { preference ->
            preference[booleanPreferencesKey(Constants.Preferences.VOICE_TOGGLED_DEBUG_ENABLED)] = value
        }
    }

    /** Saves the upload video toggled debug enabled state of the user. */
    override suspend fun saveUploadVideoToggledDebugEnabled(value: Boolean) {
        application.dataStore.edit { preference ->
            preference[booleanPreferencesKey(Constants.Preferences.UPLOAD_VIDEO_TOGGLED_DEBUG_ENABLED)] = value
        }
    }

    /** Saves the app launch count. */
    override suspend fun saveAppLaunchCount(value: Int) {
        application.dataStore.edit { preference ->
            preference[intPreferencesKey(Constants.Preferences.APP_LAUNCH_COUNT)] = value
        }
    }

    /** Saves the last review prompt date (timestamp in milliseconds). */
    override suspend fun saveLastReviewPromptDate(value: Long) {
        application.dataStore.edit { preference ->
            preference[longPreferencesKey(Constants.Preferences.LAST_REVIEW_PROMPT_DATE)] = value
        }
    }

    /** Saves whether the user has declined to review the app. */
    override suspend fun saveUserDeclinedReview(value: Boolean) {
        application.dataStore.edit { preference ->
            preference[booleanPreferencesKey(Constants.Preferences.USER_DECLINED_REVIEW)] = value
        }
    }
}
