package com.nicholas.rutherford.track.my.shot.feature.create.account.authentication

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthenticationViewModel(
    private val navigation: AuthenticationNavigation
) : ViewModel() {

    private val authenticationMutableStateFlow = MutableStateFlow(value = AuthenticationState(test = ""))
    val authenticationStateFlow = authenticationMutableStateFlow.asStateFlow()
}
