package com.nicholas.rutherford.track.your.shot.feature.login

/**
 * Holds UI state for the login screen.
 *
 * @param launcherDrawableId Optional drawable resource ID for the launcher icon.
 * @param email Current email input value.
 * @param password Current password input value.
 */
data class LoginState(
    val launcherDrawableId: Int? = null,
    val email: String? = null,
    val password: String? = null
)
