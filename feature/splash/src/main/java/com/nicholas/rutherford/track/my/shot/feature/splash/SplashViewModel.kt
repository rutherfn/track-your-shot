package com.nicholas.rutherford.track.my.shot.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nicholas.rutherford.track.my.shot.firebase.read.ReadFirebaseUserInfo
import com.nicholas.rutherford.track.my.shot.shared.preference.read.ReadSharedPreferences
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

const val SPLASH_DELAY_IN_MILLIS = 4000L
const val SPLASH_IMAGE_SCALE = 1f

class SplashViewModel(
    private val readSharedPreferences: ReadSharedPreferences,
    private val navigation: SplashNavigation,
    private val readFirebaseUserInfo: ReadFirebaseUserInfo
) : ViewModel() {

    private val initializeSplashState = SplashState(
        backgroundColor = Colors.primaryColor,
        imageScale = SPLASH_IMAGE_SCALE,
        imageDrawableId = DrawablesIds.splash
    )

    private val splashStateMutableStateFlow = MutableStateFlow(value = initializeSplashState)
    val splashStateFlow = splashStateMutableStateFlow.asStateFlow()

    init { navigateToHomeLoginOrAuthentication() }

    private fun navigateToHomeLoginOrAuthentication() {
        if (readFirebaseUserInfo.isLoggedIn) {
            if (readFirebaseUserInfo.isEmailVerified && readSharedPreferences.accountHasBeenCreated()) {
                delayAndNavigateToHomeOrLogin()
            } else {
                navigation.navigateToAuthentication()
            }
        } else {
            delayAndNavigateToHomeOrLogin()
        }
    }

    private fun delayAndNavigateToHomeOrLogin() {
        viewModelScope.launch {
            delay(timeMillis = SPLASH_DELAY_IN_MILLIS)
            navigateToLoginOrHome()
        }
    }

    private fun navigateToLoginOrHome() {
        if (readFirebaseUserInfo.isLoggedIn) {
            navigation.navigateToHome()
        } else {
            navigation.navigateToLogin()
        }
    }
}
