package com.nicholas.rutherford.track.your.shot.data.store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = Constants.Preferences.TRACK_MY_SHOT_PREFERENCES)
