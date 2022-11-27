package com.nicholas.rutherford.track.my.shot.feature.forgot.password

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ForgotPasswordViewModel : ViewModel() {

    private val forgotPasswordMutableStateFlow = MutableStateFlow(value = ForgotPasswordState(email = null))
    val forgotPasswordStateFlow = forgotPasswordMutableStateFlow.asStateFlow()

    internal fun onSendPasswordResetButtonClicked() {
        // todo: business logic
    }

    internal fun onEmailValueChanged(newEmail: String) {
        forgotPasswordMutableStateFlow.value = forgotPasswordMutableStateFlow.value.copy(email = newEmail)
    }
}
