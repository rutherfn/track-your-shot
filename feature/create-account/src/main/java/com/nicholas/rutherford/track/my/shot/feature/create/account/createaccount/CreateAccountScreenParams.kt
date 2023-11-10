package com.nicholas.rutherford.track.your.shot.feature.create.account.createaccount

data class CreateAccountScreenParams(
    val state: CreateAccountState,
    val onUsernameValueChanged: (newUsername: String) -> Unit,
    val onEmailValueChanged: (newEmail: String) -> Unit,
    val onPasswordValueChanged: (newPassword: String) -> Unit,
    val onCreateAccountButtonClicked: () -> Unit,
    val onBackButtonClicked: () -> Unit
)
