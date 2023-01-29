package com.nicholas.rutherford.track.my.shot.feature.create.account.authentication

import androidx.lifecycle.ViewModel
import com.nicholas.rutherford.track.my.shot.firebase.read.ReadFirebaseUserInfo
import com.nicholas.rutherford.track.my.shot.firebase.util.AuthenticationFirebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthenticationViewModel(
    private val readFirebaseUserInfo: ReadFirebaseUserInfo,
    private val navigation: AuthenticationNavigation,
    private val authenticationFirebase: AuthenticationFirebase
) : ViewModel() {

    private val authenticationMutableStateFlow = MutableStateFlow(value = AuthenticationState(test = ""))
    val authenticationStateFlow = authenticationMutableStateFlow.asStateFlow()

    fun onNavigateClose() {
    }

    fun onResume() {
        if (readFirebaseUserInfo.isEmailVerified) {
            println("attempt to actually create the user account via database")
        }
    }
}
