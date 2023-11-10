package com.nicholas.rutherford.track.your.shot.feature.forgot.password

data class ForgotPasswordScreenParams(
    val state: ForgotPasswordState,
    val onEmailValueChanged: (newEmail: String) -> Unit,
    val onSendPasswordResetButtonClicked: (newEmail: String?) -> Unit,
    val onBackButtonClicked: () -> Unit
)
