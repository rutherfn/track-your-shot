package com.nicholas.rutherford.track.your.shot.feature.settings.accountinfo

import androidx.lifecycle.ViewModel

class AccountInfoViewModel(private val navigation: AccountInfoNavigation) : ViewModel() {

    fun onToolbarMenuClicked() = navigation.pop()
}