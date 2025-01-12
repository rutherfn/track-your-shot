package com.nicholas.rutherford.track.your.shot.feature.reports.viewplayerreports

import androidx.lifecycle.ViewModel
import com.nicholas.rutherford.track.your.shot.data.room.repository.IndividualPlayerReportRepository
import com.nicholas.rutherford.track.your.shot.helper.account.AccountManager
import com.nicholas.rutherford.track.your.shot.helper.extensions.dataadditionupdates.DataAdditionUpdates
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ViewPlayerReportsViewModel(
    private val dataAdditionUpdates: DataAdditionUpdates,
    private val individualPlayerReportRepository: IndividualPlayerReportRepository,
    private val accountManager: AccountManager,
    private val scope: CoroutineScope
) : ViewModel() {

    val viewPlayerReportsMutableStateFlow = MutableStateFlow(value = ViewPlayerReportsState())
    val viewPlayerReportsStateFlow = viewPlayerReportsMutableStateFlow.asStateFlow()

    init {
        updateViewPlayerReportsState()
        collectNewReportHasBeenAddedSharedFlow()
    }

    fun collectNewReportHasBeenAddedSharedFlow() {
        scope.launch {
            dataAdditionUpdates.newReportHasBeenAddedSharedFlow.collectLatest { hasBeenAdded ->
                handleReportAdded(hasBeenAdded = hasBeenAdded)
            }
        }
    }

    fun handleReportAdded(hasBeenAdded: Boolean) {
        if (hasBeenAdded) {
            updateViewPlayerReportsState()
        }
    }

    fun updateViewPlayerReportsState() {
        scope.launch {
            val reports = individualPlayerReportRepository.fetchAllReports()

            viewPlayerReportsMutableStateFlow.update { it.copy(reports = reports) }
        }
    }
}