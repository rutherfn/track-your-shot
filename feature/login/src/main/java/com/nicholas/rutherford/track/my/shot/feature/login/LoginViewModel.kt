package com.nicholas.rutherford.track.my.shot.feature.login

import androidx.lifecycle.ViewModel
import com.nicholas.rutherford.track.my.shot.build.type.BuildType
import com.nicholas.rutherford.track.my.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.my.shot.feature.splash.DrawablesIds
import com.nicholas.rutherford.track.my.shot.firebase.util.existinguser.ExistingUserFirebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest

class LoginViewModel(
    private val existingUserFirebase: ExistingUserFirebase,
    private val navigation: LoginNavigation,
    private val buildType: BuildType
) : ViewModel() {

    private val loginMutableStateFlow = MutableStateFlow(
        value = LoginState(
            launcherDrawableId = null,
            email = null,
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

    internal suspend fun onLoginButtonClicked(email: String, password: String) {
        if (email.isEmpty()) {

        }
        else if (password.isEmpty()) {

        } else {
            navigation.enableProgress(progress = Progress(onDismissClicked = {}))

            existingUserFirebase.logInFlow(email = email, password = password)
                .collectLatest { isSuccessful ->
                    if (isSuccessful) {
                        navigation.disableProgress()
                        navigation.navigateToHome()
                        // log the user in
                    } else {
                        navigation.disableProgress()
                        // show user a error
                    }
                }
        }
    }

    internal fun onLoginClicked() = navigation.navigateToHome()

    internal fun onForgotPasswordClicked() = navigation.navigateToForgotPassword()

    internal fun onCreateAccountClicked() = navigation.navigateToCreateAccount()

    internal fun onEmailValueChanged(newEmail: String) {
        loginMutableStateFlow.value = loginMutableStateFlow.value.copy(email = newEmail)
    }

    internal fun onPasswordValueChanged(newPassword: String) {
        loginMutableStateFlow.value = loginMutableStateFlow.value.copy(password = newPassword)
    }
}
