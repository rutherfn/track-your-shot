package com.nicholas.rutherford.track.my.shot.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nicholas.rutherford.track.my.shot.firebase.read.ReadFirebaseUserInfo
import com.nicholas.rutherford.track.my.shot.helper.extensions.safeLet
import com.nicholas.rutherford.track.my.shot.shared.preference.read.ReadSharedPreferences
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

const val SPLASH_DELAY_IN_MILLIS = 4000L
const val SPLASH_IMAGE_SCALE = 1f

class SplashViewModel(
    private val readSharedPreferences: ReadSharedPreferences,
    private val navigation: SplashNavigation,
    private val readFirebaseUserInfo: ReadFirebaseUserInfo
) : ViewModel() {

    init {
        viewModelScope.launch {
            navigateToHomeLoginOrAuthentication()
        }
    }

    private suspend fun navigateToHomeLoginOrAuthentication() {
        readFirebaseUserInfo.isLoggedInFlow().combine(readFirebaseUserInfo.isEmailVerifiedFlow()) { isLoggedIn, isEmailVerified ->
            if (isLoggedIn) {
                if (isEmailVerified && readSharedPreferences.accountHasBeenCreated()) {
                    delayAndNavigateToHomeOrLogin(isLoggedIn = true)
                } else {
                    safeLet(
                        readSharedPreferences.unverifiedUsername(),
                        readSharedPreferences.unverifiedEmail()
                    ) { username, email ->
                        navigation.navigateToAuthentication(username = username, email = email)
                    }
                }
            } else {
                delayAndNavigateToHomeOrLogin(isLoggedIn = false)
            }
        }.collectLatest {}
    }

    private suspend fun delayAndNavigateToHomeOrLogin(isLoggedIn: Boolean) {
        delay(timeMillis = SPLASH_DELAY_IN_MILLIS)
        navigateToLoginOrHome(isLoggedIn = isLoggedIn)
    }

    private fun navigateToLoginOrHome(isLoggedIn: Boolean) {
        if (isLoggedIn) {
            navigation.navigateToHome()
        } else {
            navigation.navigateToLogin()
        }
    }
}
