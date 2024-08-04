package com.nicholas.rutherford.track.your.shot.feature.settings.accountinfo

import com.nicholas.rutherford.track.your.shot.feature.splash.StringsIds

data class AccountInfoState(
    val shouldEditAccountInfoDetails: Boolean = false,
    val toolbarTitleId: Int = StringsIds.accountInfo,
    val email: String = "",
    val username: String = ""
)
