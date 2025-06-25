package com.nicholas.rutherford.track.your.shot.feature.login

import com.nicholas.rutherford.track.your.shot.data.shared.appbar.AppBar
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
