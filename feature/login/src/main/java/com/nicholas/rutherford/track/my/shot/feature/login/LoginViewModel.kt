package com.nicholas.rutherford.track.my.shot.feature.login

import androidx.lifecycle.ViewModel
import com.nicholas.rutherford.track.my.shot.build.type.BuildType
import com.nicholas.rutherford.track.my.shot.feature.splash.DrawablesIds
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel(private val navigation: LoginNavigation, private val buildType: BuildType) : ViewModel() {

    private val loginMutableStateFlow = MutableStateFlow(
        value = LoginState(
            launcherDrawableId = null,
            username = null,
            password = null
        )
    )
    val loginStateFlow = loginMutableStateFlow.asStateFlow()

    init {
        updateLauncherDrawableIdState()
    }

    private fun updateLauncherDrawableIdState() {
        if (buildType.isDebug()) {
            loginMutableStateFlow.value = loginMutableStateFlow.value.copy(launcherDrawableId = DrawablesIds.launcherRoundTest)
        } else if (buildType.isStage()) {
            loginMutableStateFlow.value = loginMutableStateFlow.value.copy(launcherDrawableId = DrawablesIds.launcherRoundStage)
        } else if (buildType.isRelease()) {
            loginMutableStateFlow.value = loginMutableStateFlow.value.copy(launcherDrawableId = DrawablesIds.launcherRound)
        }
    }

    internal fun onLoginButtonClicked() {
    }

    internal fun onLoginClicked() = navigation.navigateToHome()

    internal fun onForgotPasswordClicked() = navigation.navigateToForgotPassword()

    internal fun onCreateAccountClicked() = navigation.navigateToCreateAccount()

    internal fun onUsernameValueChanged(newUsername: String) {
        loginMutableStateFlow.value = loginMutableStateFlow.value.copy(username = newUsername)
    }

    internal fun onPasswordValueChanged(newPassword: String) {
        loginMutableStateFlow.value = loginMutableStateFlow.value.copy(password = newPassword)
    }
}
