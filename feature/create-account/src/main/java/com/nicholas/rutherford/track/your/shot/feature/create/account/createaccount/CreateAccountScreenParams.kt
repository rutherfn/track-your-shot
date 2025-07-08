package com.nicholas.rutherford.track.your.shot.feature.create.account.createaccount

/**
 * Holds the UI state and event callbacks for the Create Account screen.
 *
 * @property state Current UI state holding input field values.
 * @property onUsernameValueChanged Callback triggered when the username input changes.
 * @property onEmailValueChanged Callback triggered when the email input changes.
 * @property onPasswordValueChanged Callback triggered when the password input changes.
 * @property onCreateAccountButtonClicked Callback triggered when the user taps the create account button.
 * @property onBackButtonClicked Callback triggered when the back navigation is requested.
 */
data class CreateAccountScreenParams(
    val state: CreateAccountState,
    val onUsernameValueChanged: (newUsername: String) -> Unit,
    val onEmailValueChanged: (newEmail: String) -> Unit,
    val onPasswordValueChanged: (newPassword: String) -> Unit,
    val onCreateAccountButtonClicked: () -> Unit,
    val onBackButtonClicked: () -> Unit
)
