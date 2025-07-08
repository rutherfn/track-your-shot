package com.nicholas.rutherford.track.your.shot.feature.login

import kotlinx.coroutines.CoroutineScope

/**
 * Parameters and callbacks needed by the login screen composable.
 *
 * @property state Current UI state including email, password, and launcher icon.
 * @property onEmailValueChanged Callback invoked when email input changes.
 * @property onPasswordValueChanged Callback invoked when password input changes.
 * @property onLoginButtonClicked Callback invoked when the login button is pressed.
 * @property onForgotPasswordClicked Callback invoked when the forgot password link is clicked.
 * @property onCreateAccountClicked Callback invoked when the create account link is clicked.
 */
data class LoginScreenParams(
    val state: LoginState,
    val onEmailValueChanged: (newEmail: String) -> Unit,
    val onPasswordValueChanged: (newPassword: String) -> Unit,
    val onLoginButtonClicked: () -> Unit,
    val onForgotPasswordClicked: () -> Unit,
    val onCreateAccountClicked: () -> Unit
)

