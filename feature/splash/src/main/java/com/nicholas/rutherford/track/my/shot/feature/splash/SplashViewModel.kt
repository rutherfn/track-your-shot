package com.nicholas.rutherford.track.my.shot.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nicholas.rutherford.track.my.shot.data.room.repository.ActiveUserRepository
import com.nicholas.rutherford.track.my.shot.firebase.read.ReadFirebaseUserInfo
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

const val SPLASH_DELAY_IN_MILLIS = 4000L
const val SPLASH_IMAGE_SCALE = 1f

class SplashViewModel(
    private val navigation: SplashNavigation,
    private val readFirebaseUserInfo: ReadFirebaseUserInfo,
    private val activeUserRepository: ActiveUserRepository
) : ViewModel() {

    fun navigateToHomeLoginOrAuthentication() {
        viewModelScope.launch {
            readFirebaseUserInfo.isLoggedInFlow()
                .combine(readFirebaseUserInfo.isEmailVerifiedFlow()) { isLoggedIn, isEmailVerified ->
                    val activeUser = activeUserRepository.fetchActiveUser()
                    if (isLoggedIn) {
                        if (isEmailVerified && activeUser != null && activeUser.accountHasBeenCreated) {
                            delayAndNavigateToHomeOrLogin(isLoggedIn = true, email = activeUser.email)
                        } else {
                            activeUser?.let { user ->
                                delayAndNavigateToAuthentication(
                                    username = user.username,
                                    email = user.email
                                )
                            }
                        }
                    } else {
                        delayAndNavigateToHomeOrLogin(isLoggedIn = false, email = activeUser?.email)
                    }
                }.collectLatest {}
        }
    }

    private suspend fun delayAndNavigateToHomeOrLogin(isLoggedIn: Boolean, email: String?) {
        delay(timeMillis = SPLASH_DELAY_IN_MILLIS)
        navigateToLoginOrHome(isLoggedIn = isLoggedIn, email = email)
    }

    private suspend fun delayAndNavigateToAuthentication(username: String, email: String) {
        delay(timeMillis = SPLASH_DELAY_IN_MILLIS)
        navigation.navigateToAuthentication(
            username = username,
            email = email
        )
    }

    private fun navigateToLoginOrHome(isLoggedIn: Boolean, email: String?) {
        if (isLoggedIn) {
            email?.let {
                navigation.navigateToHome(email = it)
            } ?: navigation.navigateToLogin()
        } else {
            navigation.navigateToLogin()
        }
    }
}
