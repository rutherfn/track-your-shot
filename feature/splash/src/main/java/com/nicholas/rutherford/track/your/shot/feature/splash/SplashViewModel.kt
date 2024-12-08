package com.nicholas.rutherford.track.your.shot.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nicholas.rutherford.track.your.shot.data.room.repository.ActiveUserRepository
import com.nicholas.rutherford.track.your.shot.firebase.core.read.ReadFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.helper.account.AccountManager
import com.nicholas.rutherford.track.your.shot.shared.preference.create.CreateSharedPreferences
import com.nicholas.rutherford.track.your.shot.shared.preference.read.ReadSharedPreferences
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

const val SPLASH_DELAY_IN_MILLIS = 4000L

class SplashViewModel(
    private val navigation: SplashNavigation,
    private val readFirebaseUserInfo: ReadFirebaseUserInfo,
    private val activeUserRepository: ActiveUserRepository,
    private val accountManager: AccountManager,
    private val readSharedPreferences: ReadSharedPreferences,
    private val createSharedPreferences: CreateSharedPreferences
) : ViewModel() {

    internal fun checkIfAppHasBeenLaunchedBefore() {
        if (!readSharedPreferences.appHasBeenLaunched()) {
            accountManager.checkIfWeNeedToLogoutOnLaunch()
            createSharedPreferences.createAppHasLaunchedPreference(value = true)
        }
    }

    fun navigateToPlayersListLoginOrAuthentication() {
        checkIfAppHasBeenLaunchedBefore()

        viewModelScope.launch {
            combine(
                readFirebaseUserInfo.isEmailVerifiedFlow(),
                readFirebaseUserInfo.isLoggedInFlow()
            ) { isEmailVerified, isLoggedIn ->
                val activeUser = activeUserRepository.fetchActiveUser()

                if (isLoggedIn) {
                    if (isEmailVerified && activeUser != null && activeUser.accountHasBeenCreated) {
                        delayAndNavigateToPlayersListOrLogin(isLoggedIn = true, email = activeUser.email)
                    } else {
                        activeUser?.let { user ->
                            delayAndNavigateToAuthentication(
                                username = user.username,
                                email = user.email
                            )
                        }
                    }
                } else {
                    delayAndNavigateToPlayersListOrLogin(isLoggedIn = false, email = activeUser?.email)
                }
            }.collectLatest { }
        }
    }

    private suspend fun delayAndNavigateToPlayersListOrLogin(isLoggedIn: Boolean, email: String?) {
        delay(timeMillis = SPLASH_DELAY_IN_MILLIS)
        navigateToLoginOrPlayersList(isLoggedIn = isLoggedIn, email = email)
    }

    private suspend fun delayAndNavigateToAuthentication(username: String, email: String) {
        delay(timeMillis = SPLASH_DELAY_IN_MILLIS)
        navigation.navigateToAuthentication(
            username = username,
            email = email
        )
    }

    private fun navigateToLoginOrPlayersList(isLoggedIn: Boolean, email: String?) {
        if (readSharedPreferences.shouldShowTermsAndConditions()) {
            navigation.navigateToTermsAndConditions()
        } else if (isLoggedIn) {
            email?.let {
                navigation.navigateToPlayersList()
            } ?: navigation.navigateToLogin()
        } else {
            navigation.navigateToLogin()
        }
    }
}
