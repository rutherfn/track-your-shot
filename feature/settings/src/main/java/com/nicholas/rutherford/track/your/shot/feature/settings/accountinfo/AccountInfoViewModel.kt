package com.nicholas.rutherford.track.your.shot.feature.settings.accountinfo

import com.nicholas.rutherford.track.your.shot.base.vm.BaseViewModel

class AccountInfoViewModel(private val navigation: AccountInfoNavigation) : BaseViewModel() {

    fun onToolbarMenuClicked() = navigation.pop()
}
