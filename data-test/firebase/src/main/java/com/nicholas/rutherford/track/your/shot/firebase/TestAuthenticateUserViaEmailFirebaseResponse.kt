package com.nicholas.rutherford.track.your.shot.firebase

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Utility class for creating test instances of [AuthenticateUserViaEmailFirebaseResponse].
 * Provides predefined test values for authentication results.
 */
class TestAuthenticateUserViaEmailFirebaseResponse {

    /**
     * Creates a test [AuthenticateUserViaEmailFirebaseResponse] instance with predefined values.
     *
     * @return a [AuthenticateUserViaEmailFirebaseResponse] populated with test authentication data.
     */
    fun create(): AuthenticateUserViaEmailFirebaseResponse {
        return AuthenticateUserViaEmailFirebaseResponse(
            isSuccessful = IS_SUCCESSFUL_AUTHENTICATED,
            isAlreadyAuthenticated = IS_ALREADY_AUTHENTICATED,
            isUserExist = IS_USER_EXIST
        )
    }
}

/** Predefined test value indicating that authentication was successful. */
const val IS_SUCCESSFUL_AUTHENTICATED = true

/** Predefined test value indicating whether the user is already authenticated. */
const val IS_ALREADY_AUTHENTICATED = false

/** Predefined test value indicating whether the user exists. */
const val IS_USER_EXIST = false
