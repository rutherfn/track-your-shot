package com.nicholas.rutherford.track.your.shot.data.store.writer

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
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
class DataStorePreferencesWriterImplTest {

    private lateinit var context: Context
    private lateinit var writer: DataStorePreferencesWriterImpl

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext<Application>()
        writer = DataStorePreferencesWriterImpl(application = context as Application)
    }

    @Test
    fun saveAppHsLaunched() = runBlocking {
        val expectedValue = true
        val appHasLaunchedKey = booleanPreferencesKey(Constants.Preferences.APP_HAS_LAUNCHED)

        writer.saveAppHasLaunched(expectedValue)

        assertEquals(expectedValue, context.dataStore.data.first()[appHasLaunchedKey])
    }

    @Test
    fun saveShouldShowTermsAndConditions() = runBlocking {
        val expectedValue = true
        val shouldShowTermsAndConditionsKey = booleanPreferencesKey(Constants.Preferences.SHOULD_SHOW_TERM_AND_CONDITIONS)

        writer.saveShouldShowTermsAndConditions(expectedValue)

        assertEquals(expectedValue, context.dataStore.data.first()[shouldShowTermsAndConditionsKey])
    }

    @Test
    fun aveIsLoggedIn() = runBlocking {
        val expectedValue = true
        val isLoggedInKey = booleanPreferencesKey(Constants.Preferences.IS_LOGGED_IN)

        writer.saveIsLoggedIn(expectedValue)

        assertEquals(expectedValue, context.dataStore.data.first()[isLoggedInKey])
    }

    @Test
    fun savePlayerFilterName() = runBlocking {
        val expectedValue = "filterName"
        val playerFilterNameKey = stringPreferencesKey(Constants.Preferences.PLAYER_FILTER_NAME)

        writer.savePlayerFilterName(expectedValue)

        assertEquals(expectedValue, context.dataStore.data.first()[playerFilterNameKey])
    }
}