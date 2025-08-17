package com.nicholas.rutherford.track.your.shot.firebase.realtime

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Data class representing a Declared Shot along with its Firebase Realtime
 * Database key. This allows tracking of the unique key for operations
 * such as updates or deletions in Firebase.
 *
 * @property declaredShotFirebaseKey The unique Firebase key for this declared shot.
 * @property declaredShotRealtimeResponse The actual declared shot data retrieved from Firebase.
 */
data class DeclaredShotWithKeyRealtimeResponse(
    val declaredShotFirebaseKey: String,
    val declaredShotRealtimeResponse: DeclaredShotRealtimeResponse
)
