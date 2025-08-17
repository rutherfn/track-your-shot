package com.nicholas.rutherford.track.your.shot.firebase.realtime

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Data class representing the user account information fetched from
 * Firebase Realtime Database. This class is used to deserialize the
 * JSON response from Firebase into a Kotlin object.
 *
 * @property userName The username associated with the account. Defaults to an empty string.
 * @property email The email address associated with the account. Defaults to an empty string.
 */
data class AccountInfoRealtimeResponse(
    val userName: String = "",
    val email: String = ""
)
