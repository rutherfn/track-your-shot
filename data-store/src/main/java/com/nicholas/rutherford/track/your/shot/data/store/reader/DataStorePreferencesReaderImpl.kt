package com.nicholas.rutherford.track.your.shot.data.store.reader

import android.app.Application
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.nicholas.rutherford.track.your.shot.data.store.dataStore
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Created by Nicholas Rutherford, last edited on 2025-10-27
 *
 * Implementation of [DataStorePreferencesReader] responsible for reading values from DataStore.
 * This class retrieves various user-related flags and data from persistent storage using
 * Android DataStore preferences.
 *
 * @param application The [Application] instance used to access the DataStore.
 */
class DataStorePreferencesReaderImpl(private val application: Application) : DataStorePreferencesReader {

    /** Reads whether the app has been launched before. */
    override fun readAppHasBeenLaunchedFlow(): Flow<Boolean> {
        return application.dataStore.data.map { preferences ->
            preferences[booleanPreferencesKey(Constants.Preferences.APP_HAS_LAUNCHED)] ?: false
        }
    }

    /** Reads whether the terms and conditions should be shown to the user. */
    override fun readShouldShowTermsAndConditionsFlow(): Flow<Boolean> {
        return application.dataStore.data.map { preferences ->
            preferences[booleanPreferencesKey(Constants.Preferences.SHOULD_SHOW_TERM_AND_CONDITIONS)] ?: false
        }
    }

    /** Reads the logged-in state of the user. */
    override fun readIsLoggedInFlow(): Flow<Boolean> {
        return application.dataStore.data.map { preferences ->
            preferences[booleanPreferencesKey(Constants.Preferences.IS_LOGGED_IN)] ?: false
        }
    }

    /** Reads the player filter name. */
    override fun readPlayerFilterNameFlow(): Flow<String> {
        return application.dataStore.data.map { preferences ->
            preferences[stringPreferencesKey(Constants.Preferences.PLAYER_FILTER_NAME)] ?: ""
        }
    }
}