package com.nicholas.rutherford.track.my.shot.feature.forgot.password

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ForgotPasswordViewModel(private val navigation: ForgotPasswordNavigation) : ViewModel() {

    private val forgotPasswordMutableStateFlow = MutableStateFlow(value = ForgotPasswordState(email = null))
    val forgotPasswordStateFlow = forgotPasswordMutableStateFlow.asStateFlow()

    fun onBackButtonClicked() = navigation.pop()

    internal fun onSendPasswordResetButtonClicked() {
        // todo
    }

    internal fun onEmailValueChanged(newEmail: String) {
        forgotPasswordMutableStateFlow.value = forgotPasswordMutableStateFlow.value.copy(email = newEmail)
    }
}
