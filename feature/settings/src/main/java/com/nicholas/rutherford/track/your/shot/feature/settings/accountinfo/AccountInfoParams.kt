package com.nicholas.rutherford.track.your.shot.feature.settings.accountinfo

/**
 * Parameters used to render the AccountInfoScreen UI.
 *
 * @property onToolbarMenuClicked Callback function triggered when the user interacts with the toolbar menu.
 * @property usernameArgument The username argument passed to the screen.
 * @property emailArgument The email argument passed to the screen.
 */
data class AccountInfoParams(
    val onToolbarMenuClicked: () -> Unit,
    val usernameArgument: String,
    val emailArgument: String
)
