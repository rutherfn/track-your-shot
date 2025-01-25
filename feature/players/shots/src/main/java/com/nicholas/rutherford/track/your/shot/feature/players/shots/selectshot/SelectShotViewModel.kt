package com.nicholas.rutherford.track.your.shot.feature.players.shots.selectshot

import android.app.Application
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.base.vm.BaseViewModel
import com.nicholas.rutherford.track.your.shot.data.room.repository.DeclaredShotRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.PendingPlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.DeclaredShot
import com.nicholas.rutherford.track.your.shot.data.room.response.Player
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.alert.AlertConfirmAndDismissButton
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import com.nicholas.rutherford.track.your.shot.helper.extensions.safeLet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

const val UPDATING_DECLARED_SHOT_LIST_DELAY = 400L

class SelectShotViewModel(
    private val application: Application,
    private val scope: CoroutineScope,
    private val navigation: SelectShotNavigation,
    private val declaredShotRepository: DeclaredShotRepository,
    private val playerRepository: PlayerRepository,
    private val pendingPlayerRepository: PendingPlayerRepository
) : BaseViewModel() {

    internal var currentDeclaredShotArrayList: ArrayList<DeclaredShot> = arrayListOf()

    internal val selectShotMutableStateFlow = MutableStateFlow(value = SelectShotState())
    val selectShotStateFlow = selectShotMutableStateFlow.asStateFlow()

    internal var isExistingPlayer: Boolean? = null
    internal var playerId: Int? = null

    override fun onNavigatedTo() {
        super.onNavigatedTo()
        fetchDeclaredShotsAndUpdateState()
    }

    fun updateIsExistingPlayerAndPlayerId(isExistingPlayerArgument: Boolean?, playerIdArgument: Int?) {
        this.isExistingPlayer = isExistingPlayerArgument
        this.playerId = playerIdArgument
    }

    internal fun fetchDeclaredShotsAndUpdateState(shouldDelay: Boolean = false) {
        scope.launch {
            currentDeclaredShotArrayList.clear()
            if (shouldDelay) {
                delay(UPDATING_DECLARED_SHOT_LIST_DELAY)
            }
            currentDeclaredShotArrayList.addAll(declaredShotRepository.fetchAllDeclaredShots())
            selectShotMutableStateFlow.update { state ->
                state.copy(declaredShotList = currentDeclaredShotArrayList)
            }
        }
    }

    fun onSearchValueChanged(newSearchQuery: String) {
        currentDeclaredShotArrayList.clear()

        scope.launch {
            declaredShotRepository.fetchDeclaredShotsBySearchQuery(searchQuery = newSearchQuery).forEach { declaredShot ->
                currentDeclaredShotArrayList.add(declaredShot)
            }
            selectShotMutableStateFlow.update { state ->
                state.copy(declaredShotList = currentDeclaredShotArrayList)
            }
        }
    }

    fun onCancelIconClicked(query: String) {
        if (query.isNotEmpty()) {
            currentDeclaredShotArrayList.clear()
            fetchDeclaredShotsAndUpdateState()
        }
    }

    fun onBackButtonClicked() {
        if (isExistingPlayer == false) {
            navigation.popFromCreatePlayer()
        } else {
            navigation.popFromEditPlayer()
        }
    }

    internal fun moreInfoAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.selectingAShot),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.gotIt)
            ),
            description = application.getString(StringsIds.chooseAShotToLogInfoDescription)
        )
    }

    fun onHelpIconClicked() = navigation.alert(alert = moreInfoAlert())

    internal fun determineShotId(player: Player): Int {
        return if (player.shotsLoggedList.isNotEmpty()) {
            player.shotsLoggedList.size
        } else {
            Constants.DEFAULT_SHOT_ID
        }
    }

    internal suspend fun loggedShotId(isExistingPlayer: Boolean, playerId: Int): Int {
        return if (isExistingPlayer) {
            playerRepository.fetchPlayerById(playerId)?.let { player ->
                determineShotId(player = player)
            } ?: Constants.DEFAULT_SHOT_ID
        } else {
            pendingPlayerRepository.fetchPlayerById(id = playerId)?.let { player ->
                determineShotId(player = player)
            } ?: Constants.DEFAULT_SHOT_ID
        }
    }

    fun onDeclaredShotItemClicked(shotType: Int) {
        safeLet(isExistingPlayer, playerId) { isExisting, id ->
            scope.launch {
                navigation.navigateToLogShot(
                    isExistingPlayer = isExisting,
                    playerId = id,
                    shotType = shotType,
                    shotId = loggedShotId(isExistingPlayer = isExisting, playerId = id),
                    viewCurrentExistingShot = false,
                    viewCurrentPendingShot = false
                )
            }
        }
    }
}
