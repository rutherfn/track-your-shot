package com.nicholas.rutherford.track.your.shot

import androidx.lifecycle.ViewModel
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.helper.account.AccountManager
import com.nicholas.rutherford.track.your.shot.helper.network.Network
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

/**
 * Timeout for how long the connectivity state is considered active when there are no subscribers.
 */
const val IS_CONNECTED_TIMEOUT_MILLIS = 5000L

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * ViewModel for the [MainActivity] that manages network connectivity status
 * and user account actions such as logout.
 *
 * @property accountManager Handles account-related actions like logout.
 * @property isConnected A [kotlinx.coroutines.flow.StateFlow] that represents the current network connectivity status.
 *                     Observes the [Network.isConnected] flow and shares it within the given [CoroutineScope].
 */
class MainActivityViewModel(
    private val accountManager: AccountManager,
    scope: CoroutineScope,
    network: Network
) : ViewModel() {

    /**
     * A state flow representing whether the device is currently connected to the network.
     * The flow starts sharing while there are active subscribers and stops after
     * [IS_CONNECTED_TIMEOUT_MILLIS] milliseconds without subscribers.
     */
    val isConnected = network
        .isConnected
        .stateIn(
            scope,
            SharingStarted.WhileSubscribed(IS_CONNECTED_TIMEOUT_MILLIS),
            false
        )

    /**
     * Logs out the current user if the provided [titleId] matches the logout string resource.
     *
     * @param titleId The string resource ID used to determine if logout should be triggered.
     */
    fun logout(titleId: Int) {
        if (titleId == StringsIds.logout) {
            accountManager.logout()
        }
    }
}
