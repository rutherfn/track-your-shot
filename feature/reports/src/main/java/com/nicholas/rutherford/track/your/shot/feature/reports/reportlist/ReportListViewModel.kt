package com.nicholas.rutherford.track.your.shot.feature.reports.reportlist

import android.app.Application
import androidx.lifecycle.ViewModel
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.alert.AlertConfirmAndDismissButton
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ReportListViewModel(
    private val application: Application,
    private val navigation: ReportListNavigation
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

    fun onHelpClicked() = navigation.alert(alert = settingsHelpAlert())

    fun onReportItemClicked(index: Int) {
        when (index) {
            Constants.CREATE_PLAYER_REPORT_INDEX -> navigation.navigateToCreatePlayerReport()
            else -> navigation.navigateToViewCreatedReportTimes()
        }
    }
}
