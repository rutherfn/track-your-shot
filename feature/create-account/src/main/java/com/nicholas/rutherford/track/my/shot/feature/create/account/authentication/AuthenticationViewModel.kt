package com.nicholas.rutherford.track.my.shot.feature.create.account.authentication

import androidx.lifecycle.ViewModel
import com.nicholas.rutherford.track.my.shot.firebase.util.AuthenticationFirebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthenticationViewModel(
    private val navigation: AuthenticationNavigation,
    private val authenticationFirebase: AuthenticationFirebase
) : ViewModel() {

    private val authenticationMutableStateFlow = MutableStateFlow(value = AuthenticationState(test = ""))
    val authenticationStateFlow = authenticationMutableStateFlow.asStateFlow()
}
