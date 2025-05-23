package com.nicholas.rutherford.track.your.shot

import androidx.lifecycle.ViewModel
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.helper.account.AccountManager
import com.nicholas.rutherford.track.your.shot.helper.network.Network
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

const val IS_CONNECTED_TIMEOUT_MILLIS = 5000L

class MainActivityViewModel(
    private val accountManager: AccountManager,
    scope: CoroutineScope,
    network: Network
) : ViewModel() {

    val isConnected = network
        .isConnected
        .stateIn(
            scope,
            SharingStarted.WhileSubscribed(IS_CONNECTED_TIMEOUT_MILLIS),
            false
        )

    fun logout(titleId: Int) {
        if (titleId == StringsIds.logout) {
            accountManager.logout()
        }
    }
}
