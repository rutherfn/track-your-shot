package com.nicholas.rutherford.track.your.shot.data.store.reader

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nicholas.rutherford.track.your.shot.data.store.dataStore
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DataStorePreferencesReaderImplTest {

    private lateinit var context: Context
    private lateinit var reader: DataStorePreferencesReaderImpl

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext<Application>()
        reader = DataStorePreferencesReaderImpl(application = context as Application)
    }

    @Test
    fun readAppHasBeenLaunchedFlow() = runBlocking {
        val expectedValue = true
        val appHasLaunchedKey = booleanPreferencesKey(Constants.Preferences.APP_HAS_LAUNCHED)

        context.dataStore.edit { preferences ->
            preferences[appHasLaunchedKey] = expectedValue
        }

        assertEquals(expectedValue, reader.readAppHasBeenLaunchedFlow().first())
    }

    @Test
    fun readShouldShowTermsAndConditionsFlow() = runBlocking {
        val expectedValue = true
        val shouldShowTermsAndConditionsKey = booleanPreferencesKey(Constants.Preferences.SHOULD_SHOW_TERM_AND_CONDITIONS)

        context.dataStore.edit { preferences ->
            preferences[shouldShowTermsAndConditionsKey] = expectedValue
        }

        assertEquals(expectedValue, reader.readShouldShowTermsAndConditionsFlow().first())
    }

    @Test
    fun readIsLoggedInFlow() = runBlocking {
        val expectedValue = true
        val isLoggedInKey = booleanPreferencesKey(Constants.Preferences.IS_LOGGED_IN)

        context.dataStore.edit { preferences ->
            preferences[isLoggedInKey] = expectedValue
        }

        assertEquals(expectedValue, reader.readIsLoggedInFlow().first())
    }

    @Test
    fun readPlayerFilterNameFlow() = runBlocking {
        val expectedValue = "filterName"
        val playerFilterNameKey = stringPreferencesKey(Constants.Preferences.PLAYER_FILTER_NAME)

        context.dataStore.edit { preferences ->
            preferences[playerFilterNameKey] = expectedValue
        }

        assertEquals(expectedValue, reader.readPlayerFilterNameFlow().first())
    }

    @Test
    fun readVoiceToggledDebugEnabledFlow() = runBlocking {
        val expectedValue = true
        val voiceToggledDebugEnabledKey = booleanPreferencesKey(Constants.Preferences.VOICE_TOGGLED_DEBUG_ENABLED)

        context.dataStore.edit { preferences ->
            preferences[voiceToggledDebugEnabledKey] = expectedValue
        }

        assertEquals(expectedValue, reader.readVoiceToggledDebugEnabledFlow().first())
    }

    @Test
    fun readUploadVideoToggledDebugEnabled() = runBlocking {
        val expectedValue = true
        val uploadVideoToggledDebugEnabledKey = booleanPreferencesKey(Constants.Preferences.UPLOAD_VIDEO_TOGGLED_DEBUG_ENABLED)

        context.dataStore.edit { preferences ->
            preferences[uploadVideoToggledDebugEnabledKey] = expectedValue
        }

        assertEquals(expectedValue, reader.readUploadVideoToggledDebugEnabled().first())
    }
}
