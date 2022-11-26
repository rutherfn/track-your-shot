package com.nicholas.rutherford.track.my.shot.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

const val SPLASH_DELAY_IN_MILLIS = 4000L
const val SPLASH_IMAGE_SCALE = 1f

// todo - remove this, this is here until we have create account working as expected
const val ALWAYS_LOGIN_TEST = true

class SplashViewModel(private val navigation: SplashNavigation) : ViewModel() {

    private val initializeSplashState = SplashState(
        backgroundColor = Colors.primaryColor,
        imageScale = SPLASH_IMAGE_SCALE,
        imageDrawableId = DrawablesIds.splash
    )

    private val splashStateMutableStateFlow = MutableStateFlow(value = initializeSplashState)
    val splashStateFlow = splashStateMutableStateFlow.asStateFlow()

    init {
        delayAndNavigateToHomeOrLogin()
    }

    private fun delayAndNavigateToHomeOrLogin() {
        viewModelScope.launch {
            delay(timeMillis = SPLASH_DELAY_IN_MILLIS)
            navigateLoginOrHome()
        }
    }

    private fun navigateLoginOrHome() {
        // todo - determine if the user is signed in or not via auth0
        // todo - if they arent, navigate them to login
        // todo - if they are, navigate them to home
        if (ALWAYS_LOGIN_TEST) {
            navigation.navigateToLogin()
        } else {
            navigation.navigateToHome()
        }
    }
}
