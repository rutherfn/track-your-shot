package com.nicholas.rutherford.track.your.shot.feature.settings.accountinfo

import android.app.Application
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AccountInfoViewModel(
    private val application: Application,
    private val navigation: AccountInfoNavigation
) : ViewModel() {

    internal val accountInfoMutableStateFlow = MutableStateFlow(value = AccountInfoState())
    val accountInfoStateFlow = accountInfoMutableStateFlow.asStateFlow()

    fun onToolbarIconButtonClicked() = navigation.pop()

    fun onToolbarSecondaryIconButtonClicked() {
    }
}
