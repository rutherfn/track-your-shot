package com.nicholas.rutherford.track.your.shot.feature.create.account.createaccount

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Represents the UI state for the Create Account Screen
 *
 * @property username The entered username
 * @property email The entered email address
 * @property password The entered password
 */
data class CreateAccountState(
    val username: String? = null,
    val email: String? = null,
    val password: String? = null
)
