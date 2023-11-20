package com.nicholas.rutherford.track.your.shot.feature.players

import android.app.Application
import androidx.lifecycle.ViewModel
import com.nicholas.rutherford.track.your.shot.data.room.repository.ActiveUserRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.Player
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.alert.AlertConfirmAndDismissButton
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.your.shot.feature.splash.StringsIds
import com.nicholas.rutherford.track.your.shot.firebase.core.delete.DeleteFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.firebase.core.read.ReadFirebaseUserInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlayersListViewModel(
    private val application: Application,
    private val scope: CoroutineScope,
    private val navigation: PlayersListNavigation,
    private val deleteFirebaseUserInfo: DeleteFirebaseUserInfo
    private val activeUserRepository: ActiveUserRepository,
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

    fun deletePlayer(player: Player) {
        navigation.enableProgress(progress = Progress())
        scope.launch {
            activeUserRepository.fetchActiveUser()?.firebaseAccountInfoKey.let { accountInfoKey ->

                playerRepository.deletePlayer(player = player)
            }
        }
    }

    fun onDeletePlayerClicked(player: Player) {
        val fullPlayerName = "${player.firstName} ${player.lastName}"
        navigation.alert(
            alert = Alert(
                title = application.getString(StringsIds.deleteX, fullPlayerName),
                confirmButton = AlertConfirmAndDismissButton(
                    buttonText = application.getString(StringsIds.yes),
                    onButtonClicked = { deletePlayer(player = player) }
                ),
                dismissButton = AlertConfirmAndDismissButton(
                    buttonText = application.getString(StringsIds.no)
                ),
                description =application.getString(StringsIds.areYouCertainYouWishToRemoveX, fullPlayerName)
            )
        )
    }
}
