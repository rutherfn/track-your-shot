package com.nicholas.rutherford.track.my.shot.feature.login

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel(private val navigation: LoginNavigation) : ViewModel() {

    private val initializeLoginState = LoginState(username = null, password = null)

    private val loginMutableStateFlow = MutableStateFlow(value = initializeLoginState)
    val loginStateFlow = loginMutableStateFlow.asStateFlow()

    internal fun onLoginClicked() = navigation.navigateToHome()

    internal fun onResetPasswordClicked() = navigation.navigateToResetPassword()

    internal fun onCreateAccountClicked() = navigation.navigateToCreateAccount()

    internal fun onUsernameValueChanged(newUsername: String) {
        loginMutableStateFlow.value = loginMutableStateFlow.value.copy(username = newUsername)
    }

    internal fun onPasswordValueChanged(newPassword: String) {
        loginMutableStateFlow.value = loginMutableStateFlow.value.copy(password = newPassword)
    }
}
