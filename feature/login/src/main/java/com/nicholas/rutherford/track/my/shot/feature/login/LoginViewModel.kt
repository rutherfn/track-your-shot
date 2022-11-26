package com.nicholas.rutherford.track.my.shot.feature.login

import androidx.lifecycle.ViewModel
import com.nicholas.rutherford.track.my.shot.feature.splash.StringsIds
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel(private val navigation: LoginNavigation) : ViewModel() {

    internal val initializeLoginState = LoginState(
        clickMeToCreateAccountId = StringsIds.clickMeToCreateAccount
    )

    private val loginMutableStateFlow = MutableStateFlow(value = initializeLoginState)
    val loginStateFlow = loginMutableStateFlow.asStateFlow()

    internal fun onButtonClickedTest() = navigation.navigateToHome()
}
