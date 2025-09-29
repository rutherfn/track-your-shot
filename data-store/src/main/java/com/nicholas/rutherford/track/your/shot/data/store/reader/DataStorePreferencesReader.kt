package com.nicholas.rutherford.track.your.shot.data.store.reader

import kotlinx.coroutines.flow.Flow

/**
 * Created by Nicholas Rutherford, last edited on 2025-10-27
 *
 * Interface defining operations for reading preference values from DataStore.
 * This interface provides methods to retrieve various user-related flags and data
 * from persistent storage using Android DataStore.
 */
interface DataStorePreferencesReader {
    fun readAppHasBeenLaunchedFlow(): Flow<Boolean>
    fun readShouldShowTermsAndConditionsFlow(): Flow<Boolean>
    fun readIsLoggedInFlow(): Flow<Boolean>
    fun readPlayerFilterNameFlow(): Flow<String>
}
