package com.nicholas.rutherford.track.your.shot.feature.settings.accountinfo

data class AccountInfoParams(
    val onToolbarIconButtonClicked: () -> Unit,
    val onToolbarSecondaryIconButtonClicked: () -> Unit,
    val state: AccountInfoState
)
