package com.nicholas.rutherford.track.your.shot.feature.forgot.password

/**
 * Holds the UI state and event callbacks for the Forgot Password screen.
 *
 * @property state Current UI state including the email input.
 * @property onEmailValueChanged Callback triggered when the email input changes.
 * @property onSendPasswordResetButtonClicked Callback triggered when the user requests to send a password reset email.
 * @property onBackButtonClicked Callback triggered when the user navigates back from the screen.
 */
data class ForgotPasswordScreenParams(
    val state: ForgotPasswordState,
    val onEmailValueChanged: (newEmail: String) -> Unit,
    val onSendPasswordResetButtonClicked: (newEmail: String?) -> Unit,
    val onBackButtonClicked: () -> Unit
)

