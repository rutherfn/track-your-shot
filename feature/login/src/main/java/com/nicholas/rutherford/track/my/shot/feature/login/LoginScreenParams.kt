package com.nicholas.rutherford.track.my.shot.feature.login

import kotlinx.coroutines.CoroutineScope

data class LoginScreenParams(
    val state: LoginState,
    val onEmailValueChanged: (newEmail: String) -> Unit,
    val onPasswordValueChanged: (newPassword: String) -> Unit,
    val onLoginButtonClicked: () -> Unit,
    val onForgotPasswordClicked: () -> Unit,
    val onCreateAccountClicked: () -> Unit,
    val coroutineScope: CoroutineScope
)
