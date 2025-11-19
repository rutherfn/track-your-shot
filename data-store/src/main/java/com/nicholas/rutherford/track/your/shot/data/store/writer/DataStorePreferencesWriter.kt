package com.nicholas.rutherford.track.your.shot.data.store.writer

/**
 * Created by Nicholas Rutherford, last edited on 2025-10-27
 *
 * Interface defining operations for writing preference values to DataStore.
 * This interface provides methods to save various user-related flags and data
 * into persistent storage using Android DataStore.
 */
interface DataStorePreferencesWriter {
    suspend fun saveAppHasLaunched(value: Boolean)
    suspend fun saveShouldShowTermsAndConditions(value: Boolean)
    suspend fun saveIsLoggedIn(value: Boolean)
    suspend fun savePlayerFilterName(value: String)
    suspend fun saveVoiceToggledDebugEnabled(value: Boolean)
    suspend fun saveUploadVideoToggledDebugEnabled(value: Boolean)
    suspend fun saveAppLaunchCount(value: Int)
    suspend fun saveLastReviewPromptDate(value: Long)
    suspend fun saveUserDeclinedReview(value: Boolean)
}
