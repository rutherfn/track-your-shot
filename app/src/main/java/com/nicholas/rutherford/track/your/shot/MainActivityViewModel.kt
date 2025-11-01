package com.nicholas.rutherford.track.your.shot

import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.base.vm.BaseViewModel
import com.nicholas.rutherford.track.your.shot.base.vm.FlowCollectionTrigger
import com.nicholas.rutherford.track.your.shot.data.store.reader.DataStorePreferencesReader
import com.nicholas.rutherford.track.your.shot.feature.settings.debugtoggle.DebugToggleScreen
import com.nicholas.rutherford.track.your.shot.helper.account.AccountManager
import com.nicholas.rutherford.track.your.shot.helper.network.Network
import com.nicholas.rutherford.track.your.shot.navigation.DrawerAction
import com.nicholas.rutherford.track.your.shot.navigation.LogoutAction
import com.nicholas.rutherford.track.your.shot.navigation.PlayersListAction
import com.nicholas.rutherford.track.your.shot.navigation.ReportingAction
import com.nicholas.rutherford.track.your.shot.navigation.SettingsAction
import com.nicholas.rutherford.track.your.shot.navigation.ShotsAction
import com.nicholas.rutherford.track.your.shot.navigation.VoiceCommandsAction
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
 * It also goes in And collects feature toggles that get set from the [DebugToggleScreen]
 *
 * @property network Handles network-related operations like connectivity status.
 * @property scope The coroutine scope used for managing coroutines.
 * @property accountManager Handles account-related actions like logout.
 * @property dataStorePreferenceReader Reads data store preferences collected values
 */
class MainActivityViewModel(
    network: Network,
    private val scope: CoroutineScope,
    private val accountManager: AccountManager,
    private val dataStorePreferenceReader: DataStorePreferencesReader
) : BaseViewModel() {

    // Override to provide the injected scope
    override fun getScope(): CoroutineScope? = scope

    // Override to start collecting flows in onStart
    override fun getFlowCollectionTrigger(): FlowCollectionTrigger = FlowCollectionTrigger.INIT

    internal var isVoiceToggleEnabled = false

    /**
     * A state flow representing whether the device is currently connected to the network.
     * The flow starts sharing while there are active subscribers and stops after
     * [IS_CONNECTED_TIMEOUT_MILLIS] milliseconds without subscribers.
     */
    val isConnected = network
        .isConnected
        .stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(IS_CONNECTED_TIMEOUT_MILLIS),
            initialValue = false
        )

    init {
        collectReadVoiceToggledDebugEnabledFlow()
    }

    /**
     * Collects the read voice toggled debug enabled flow from the [dataStorePreferenceReader]
     */
    private fun collectReadVoiceToggledDebugEnabledFlow() {
        collectFlow(flow = dataStorePreferenceReader.readVoiceToggledDebugEnabledFlow()) { enabled ->
            isVoiceToggleEnabled = enabled
        }
    }

    /**
     * Builds a list of [DrawerAction] for the drawer menu.
     *
     * @return A list of [DrawerAction].
     */
    fun buildDrawerActions(): List<DrawerAction> {
        val drawerActions: ArrayList<DrawerAction> = arrayListOf()

        drawerActions.add(PlayersListAction)
        drawerActions.add(ShotsAction)
        drawerActions.add(ReportingAction)
        drawerActions.add(SettingsAction)

        if (isVoiceToggleEnabled) {
            drawerActions.add(VoiceCommandsAction)
        }

        drawerActions.add(LogoutAction)

        return drawerActions
    }

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
