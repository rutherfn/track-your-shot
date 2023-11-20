package com.nicholas.rutherford.track.your.shot.feature.players

import android.app.Application
import androidx.lifecycle.ViewModel
import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.Player
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.alert.AlertConfirmAndDismissButton
import com.nicholas.rutherford.track.your.shot.feature.splash.StringsIds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlayersListViewModel(
    private val application: Application,
    private val scope: CoroutineScope,
    private val navigation: PlayersListNavigation,
    private val playerRepository: PlayerRepository
) : ViewModel() {

    internal val playerListMutableStateFlow = MutableStateFlow(
        value = PlayersListState(
            playerList = emptyList()
        )
    )

    val playerListStateFlow = playerListMutableStateFlow.asStateFlow()

    fun updatePlayerListState() {
        scope.launch {
            playerListMutableStateFlow.value = PlayersListState(playerList = playerRepository.fetchAllPlayers())
        }
    }

    fun onToolbarMenuClicked() = navigation.openNavigationDrawer()

    fun onDeletePlayerClicked(player: Player) {
        val fullPlayerName = "${player.firstName} ${player.lastName}"
        navigation.alert(
            alert = Alert(
                title = application.getString(StringsIds.deleteX, fullPlayerName),
                confirmButton = AlertConfirmAndDismissButton(
                    buttonText = application.getString(StringsIds.yes),
                    onButtonClicked = {

                    }
                ),
                dismissButton = AlertConfirmAndDismissButton(
                    buttonText = application.getString(StringsIds.no)
                ),
                description =application.getString(StringsIds.areYouCertainYouWishToRemoveX, fullPlayerName)
            )
        )
    }
}
