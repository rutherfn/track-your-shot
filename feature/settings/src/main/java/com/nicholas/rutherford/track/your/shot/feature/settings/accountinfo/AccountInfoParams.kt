package com.nicholas.rutherford.track.your.shot.feature.settings.accountinfo

data class AccountInfoParams(
    val onToolbarMenuClicked: () -> Unit,
    val usernameArgument: String,
    val emailArgument: String
)