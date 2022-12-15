package com.nicholas.rutherford.track.my.shot.feature.create.account

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CreateAccountViewModel(private val navigation: CreateAccountNavigation) : ViewModel() {

    private val createAccountMutableStateFlow = MutableStateFlow(
        value = CreateAccountState(
            username = null,
            email = null,
            password = null
        )
    )
    val createAccountStateFlow = createAccountMutableStateFlow.asStateFlow()

    fun onBackButtonClicked() = navigation.pop()

    fun onUsernameValueChanged(newUsername: String) {
        createAccountMutableStateFlow.value = createAccountStateFlow.value.copy(username = newUsername)
    }

    fun onEmailValueChanged(newEmail: String) {
        createAccountMutableStateFlow.value = createAccountStateFlow.value.copy(email = newEmail)
    }

    fun onPasswordValueChanged(newPassword: String) {
        createAccountMutableStateFlow.value = createAccountStateFlow.value.copy(password = newPassword)
    }
}
