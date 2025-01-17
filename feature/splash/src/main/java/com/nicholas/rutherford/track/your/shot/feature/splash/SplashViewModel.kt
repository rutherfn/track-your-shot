package com.nicholas.rutherford.track.your.shot.feature.splash

import androidx.lifecycle.ViewModel
import com.nicholas.rutherford.track.your.shot.data.room.repository.ActiveUserRepository
import com.nicholas.rutherford.track.your.shot.firebase.core.read.ReadFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.helper.account.AccountManager
import com.nicholas.rutherford.track.your.shot.shared.preference.create.CreateSharedPreferences
import com.nicholas.rutherford.track.your.shot.shared.preference.read.ReadSharedPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class SplashViewModel(
    private val navigation: SplashNavigation,
    private val readFirebaseUserInfo: ReadFirebaseUserInfo,
    private val activeUserRepository: ActiveUserRepository,
    private val accountManager: AccountManager,
    private val readSharedPreferences: ReadSharedPreferences,
    private val createSharedPreferences: CreateSharedPreferences,
    private val scope: CoroutineScope
) : ViewModel() {

    internal fun checkIfAppHasBeenLaunchedBefore() {
        if (!readSharedPreferences.appHasBeenLaunched()) {
            accountManager.checkIfWeNeedToLogoutOnLaunch()
            createSharedPreferences.createAppHasLaunchedPreference(value = true)
        }
    }

    fun navigateToPlayersListLoginOrAuthentication() {
        checkIfAppHasBeenLaunchedBefore()

        scope.launch {
            combine(
                readFirebaseUserInfo.isEmailVerifiedFlow(),
                readFirebaseUserInfo.isLoggedInFlow()
            ) { isEmailVerified, isLoggedIn ->
                val activeUser = activeUserRepository.fetchActiveUser()

                if (isLoggedIn) {
                    if (isEmailVerified && activeUser != null && activeUser.accountHasBeenCreated) {
                        navigateToLoginOrPlayersList(isLoggedIn = true, email = activeUser.email)
                    } else {
                        activeUser?.let { user ->
                            navigation.navigateToAuthentication(
                                username = user.username,
                                email = user.email
                            )
                        }
                    }
                } else {
                    navigateToLoginOrPlayersList(isLoggedIn = false, email = activeUser?.email)
                }
            }.collectLatest { }
        }
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
