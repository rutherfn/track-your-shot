package com.nicholas.rutherford.track.my.shot.feature.home

import androidx.lifecycle.ViewModel
import com.nicholas.rutherford.track.my.shot.data.room.repository.ActiveUserRepository
import com.nicholas.rutherford.track.my.shot.data.room.response.ActiveUser
import com.nicholas.rutherford.track.my.shot.firebase.read.ReadFirebaseUserInfo
import com.nicholas.rutherford.track.my.shot.firebase.util.existinguser.ExistingUserFirebase
import com.nicholas.rutherford.track.my.shot.helper.constants.Constants
import kotlinx.coroutines.flow.collectLatest

class HomeViewModel(
    private val navigation: HomeNavigation,
    private val existingUserFirebase: ExistingUserFirebase,
    private val activeUserRepository: ActiveUserRepository,
    private val readFirebaseUserInfo: ReadFirebaseUserInfo
) : ViewModel() {

    suspend fun collectAccountInfoFlowByEmail(email: String) {
        readFirebaseUserInfo.getAccountInfoFlowByEmail(email = email).collectLatest { accountInfoRealtimeResponse ->
            accountInfoRealtimeResponse?.let { response ->
                attemptToUpdateActiveUser(email = email, username = response.userName)
            }
        }
    }

    suspend fun attemptToUpdateActiveUser(email: String, username: String) {
        if (activeUserRepository.fetchActiveUser() == null) {
            activeUserRepository.createActiveUser(
                activeUser = ActiveUser(
                    id = Constants.ACTIVE_USER_ID,
                    accountHasBeenCreated = true,
                    email = email,
                    username = username
                )
            )
        }
    }
    fun navigateTest() {
        existingUserFirebase.logOut()
        navigation.navigateToLogin()
    }
}
