package com.nicholas.rutherford.track.your.shot.feature.reports.createreport

import android.app.Application
import androidx.lifecycle.ViewModel
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.buildPlayersWithShots
import com.nicholas.rutherford.track.your.shot.data.room.response.fullName
import com.nicholas.rutherford.track.your.shot.data.room.response.sortedPlayers
import com.nicholas.rutherford.track.your.shot.notifications.Notifications
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateReportViewModel(
    private val application: Application,
    private val navigation: CreateReportNavigation,
    private val playerRepository: PlayerRepository,
    private val scope: CoroutineScope,
    private val notifications: Notifications
) : ViewModel() {

    internal val createReportMutableStateFlow = MutableStateFlow(value = CreateReportState())
    val createReportStateFlow = createReportMutableStateFlow.asStateFlow()

    fun onToolbarMenuClicked() = navigation.pop()

    fun updatePlayersState() {
        scope.launch {
            val sortedPlayers = playerRepository.fetchAllPlayers().buildPlayersWithShots().sortedPlayers()
            val playerOptions = sortedPlayers.map { it.fullName() }

            if (playerOptions.isNotEmpty()) {
                createReportMutableStateFlow.update { state ->
                    state.copy(playerOptions = playerOptions, selectedPlayer = sortedPlayers.first())
                }
            }
        }
    }

    fun showCreatePlayerReportNotification() {
        createReportMutableStateFlow.value.selectedPlayer?.let { playerState ->
            val playerFullName = playerState.fullName()

            notifications.buildPlayerReportNotification(
                title = application.getString(
                    StringsIds.xShotReportCreated,
                    playerFullName
                ),
                description = application.getString(
                    StringsIds.xShotReportCreatedDescription,
                    playerFullName
                )
            )
        }
    }
}
