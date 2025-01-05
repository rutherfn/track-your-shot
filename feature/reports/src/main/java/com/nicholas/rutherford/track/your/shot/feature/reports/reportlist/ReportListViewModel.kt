package com.nicholas.rutherford.track.your.shot.feature.reports.reportlist

import android.app.Application
import androidx.lifecycle.ViewModel
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.getAllShots
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.alert.AlertConfirmAndDismissButton
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ReportListViewModel(
    private val application: Application,
    private val navigation: ReportListNavigation,
    private val playerRepository: PlayerRepository,
    private val scope: CoroutineScope
) : ViewModel() {

    internal val reportListMutableStateFlow = MutableStateFlow(value = ReportListState())
    val reportListStateFlow = reportListMutableStateFlow.asStateFlow()

    init {
        updateState()
    }

    private fun updateState() {
        reportListMutableStateFlow.update { reportListState ->
            reportListState.copy(reportInfoList = reportList())
        }
    }

    fun reportList(): List<String> {
        return listOf(
            application.getString(StringsIds.createPlayerReport),
            application.getString(StringsIds.viewCreatedReportTimes)
        )
    }

    fun onToolbarMenuClicked() = navigation.openNavigationDrawer()

    fun settingsHelpAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.reports),
            description = application.getString(StringsIds.reportsHelpDescription),
            confirmButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.gotIt)
            )
        )
    }

    fun noPlayersAddedAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.noPlayersCreated),
            description = application.getString(StringsIds.noPlayersCreatedDescription),
            confirmButton = AlertConfirmAndDismissButton(buttonText = application.getString(StringsIds.gotIt))
        )
    }

    fun noShotsAddedForPlayersAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.noShotsCreated),
            description = application.getString(StringsIds.noShotsCreatedDescription),
            confirmButton = AlertConfirmAndDismissButton(buttonText = application.getString(StringsIds.gotIt))
        )
    }

    fun onHelpClicked() = navigation.alert(alert = settingsHelpAlert())

    private suspend fun onCreatePlayerReportClicked() {
        val players = playerRepository.fetchAllPlayers()

        if (players.isEmpty()) {
            navigation.alert(alert = noPlayersAddedAlert())
        } else if (players.getAllShots().isEmpty()) {
            navigation.alert(alert = noShotsAddedForPlayersAlert())
        } else {
            navigation.navigateToCreateReport()
        }
    }

    fun onReportItemClicked(index: Int) {
        when (index) {
            Constants.CREATE_REPORT_INDEX -> {
                scope.launch { onCreatePlayerReportClicked() }
            }
            else -> navigation.navigateToViewCreatedReportTimes()
        }
    }
}
