package com.nicholas.rutherford.track.my.shot.feature.login

import androidx.lifecycle.ViewModel

class LoginViewModel(private val navigation: LoginNavigation) : ViewModel() {

    internal fun onButtonClickedTest() = navigation.navigateToHome()
}
