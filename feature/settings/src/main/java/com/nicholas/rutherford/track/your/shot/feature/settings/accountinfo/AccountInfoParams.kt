package com.nicholas.rutherford.track.your.shot.feature.settings.accountinfo

data class AccountInfoParams(
    val onToolbarIconButtonClicked: () -> Unit,
    val onToolbarSecondaryIconButtonClicked: () -> Unit,
    val onNewEmailValueChanged: (email: String) -> Unit,
    val onConfirmNewEmailValueChanged: (email: String) -> Unit,
    val onNewUsernameValueChanged: (username: String) -> Unit,
    val onConfirmNewUsernameValueChanged: (username: String) -> Unit,
    val state: AccountInfoState
)
