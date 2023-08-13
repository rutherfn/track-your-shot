package com.nicholas.rutherford.track.my.shot.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nicholas.rutherford.track.my.shot.account.info.realtime.AccountInfoRealtimeResponse
import com.nicholas.rutherford.track.my.shot.data.room.repository.ActiveUserRepository
import com.nicholas.rutherford.track.my.shot.data.room.repository.UserRepository
import com.nicholas.rutherford.track.my.shot.data.room.response.User
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
    private val activeUserRepository: ActiveUserRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    fun navigateToHomeLoginOrAuthentication() {
        viewModelScope.launch {
            readFirebaseUserInfo.isEmailVerifiedFlow()
                .combine(readFirebaseUserInfo.isLoggedInFlow()) { isEmailVerified, isLoggedIn ->
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
                }
                .combine(readFirebaseUserInfo.getAccountInfoListFlow()) { _, accountInfoRealtimeResponse ->
                    // todo -> check to see when was the last time we updated the firebase database
                    // todo -> if the date of now is within 4 hours and the user instance in room is not null
                    // we wouldn't need to check it, or i should say wouldn't need to update the table.
                    // however if its greater then 4 hours then update the room table
                    // Trello ticket:
                    attemptToUpdateDatabaseUsers(accountInfoRealtimeResponse = accountInfoRealtimeResponse)
                }
                .collectLatest {}
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

    private suspend fun attemptToUpdateDatabaseUsers(accountInfoRealtimeResponse: List<AccountInfoRealtimeResponse>?) {
        val userArrayList: ArrayList<User> = arrayListOf()

        accountInfoRealtimeResponse?.mapIndexed { index, accountInfo ->
            userArrayList.add(
                User(
                    id = index,
                    username = accountInfo.userName,
                    email = accountInfo.email
                )
            )
        }

        if (userRepository.fetchAllUsers().isNotEmpty()) {
            userRepository.deleteAllUsers()
        }

        if (userArrayList.isNotEmpty()) {
            userRepository.createUsers(userList = userArrayList.toList())
        }
    }
}
