package com.nicholas.rutherford.track.my.shot.feature.splash

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nicholas.rutherford.track.my.shot.data.room.PendingUser
import com.nicholas.rutherford.track.my.shot.data.room.dao.PendingUserDao
import com.nicholas.rutherford.track.my.shot.firebase.read.ReadFirebaseUserInfo
import com.nicholas.rutherford.track.my.shot.helper.extensions.safeLet
import com.nicholas.rutherford.track.my.shot.shared.preference.read.ReadSharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val SPLASH_DELAY_IN_MILLIS = 4000L
const val SPLASH_IMAGE_SCALE = 1f

class SplashViewModel(
    private val readSharedPreferences: ReadSharedPreferences,
    private val navigation: SplashNavigation,
    private val readFirebaseUserInfo: ReadFirebaseUserInfo,
    private val application: Application,
    private val pendingUserDao: PendingUserDao
) : ViewModel() {

    init {
        insertPendingUser()
        retrievePendingUser()
//        pendingUserDao.getPendingUser()

        // appDatabase.pendingUserDao().getPendingUser()
    }

    fun insertPendingUser() {
        val pendingUser = PendingUser(
            id = 1,
            accountHasBeenCreated = true,
            unverifiedEmail = "example@example.com",
            unverifiedUsername = "JohnDoe"
        )
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                pendingUserDao.insert(pendingUser)
            }
        }
    }

    fun retrievePendingUser() {
        viewModelScope.launch {
            val pendingUser = withContext(Dispatchers.IO) {
                pendingUserDao.getPendingUser()
            }
            pendingUser?.let {
                println("Retrieved Pending User: $it")
            }
        }
    }

    fun navigateToHomeLoginOrAuthentication() {
        viewModelScope.launch {
            readFirebaseUserInfo.isLoggedInFlow()
                .combine(readFirebaseUserInfo.isEmailVerifiedFlow()) { isLoggedIn, isEmailVerified ->
                    if (isLoggedIn) {
                        if (isEmailVerified && readSharedPreferences.accountHasBeenCreated()) {
                            delayAndNavigateToHomeOrLogin(isLoggedIn = true)
                        } else {
                            safeLet(
                                readSharedPreferences.unverifiedUsername(),
                                readSharedPreferences.unverifiedEmail()
                            ) { username, email ->
                                delayAndNavigateToAuthentication(
                                    username = username,
                                    email = email
                                )
                            }
                        }
                    } else {
                        delayAndNavigateToHomeOrLogin(isLoggedIn = false)
                    }
                }.collectLatest {}
        }
    }

    private suspend fun delayAndNavigateToHomeOrLogin(isLoggedIn: Boolean) {
        delay(timeMillis = SPLASH_DELAY_IN_MILLIS)
        navigateToLoginOrHome(isLoggedIn = isLoggedIn)
    }

    private suspend fun delayAndNavigateToAuthentication(username: String, email: String) {
        delay(timeMillis = SPLASH_DELAY_IN_MILLIS)
        navigation.navigateToAuthentication(
            username = username,
            email = email
        )
    }

    private fun navigateToLoginOrHome(isLoggedIn: Boolean) {
        if (isLoggedIn) {
            navigation.navigateToHome()
        } else {
            navigation.navigateToLogin()
        }
    }
}
