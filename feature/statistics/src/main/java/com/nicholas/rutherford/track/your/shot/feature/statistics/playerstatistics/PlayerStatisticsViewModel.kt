package com.nicholas.rutherford.track.your.shot.feature.statistics.playerstatistics

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.SavedStateHandle
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.base.vm.BaseViewModel
import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerRepository
import com.nicholas.rutherford.track.your.shot.feature.statistics.toPlayerStatisticsSummary
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Created by Nicholas Rutherford, last edited on 2026-07-05
 *
 * ViewModel responsible for managing and exposing the state and user interactions
 * for the Player Statistics screen.
 *
 * @param savedStateHandle Provides saved state access to retrieve player id param.
 * @param application Application context used to access string resources.
 * @param scope Coroutine scope used to launch background operations.
 * @param navigation Interface to handle navigation events and alerts.
 * @param playerRepository Repository used to fetch player and shot information from local storage.
 */
class PlayerStatisticsViewModel(
    savedStateHandle: SavedStateHandle,
    private val application: Application,
    private val scope: CoroutineScope,
    private val navigation: PlayerStatisticsNavigation,
    private val playerRepository: PlayerRepository
) : BaseViewModel() {

    internal val playerStatisticsMutableStateFlow = MutableStateFlow(value = PlayerStatisticsState())
    val playerStatisticsStateFlow = playerStatisticsMutableStateFlow.asStateFlow()

    private val playerIdParam: Int? = savedStateHandle.get<Int>("playerIdParam")

    private val madeLabel: String
        get() = application.getString(StringsIds.make)

    private val missedLabel: String
        get() = application.getString(StringsIds.miss)

    init {
        updateStatisticsForPlayer()
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        updateStatisticsForPlayer()
    }

    /**
     * Loads player statistics and builds the initial chart and date filter options.
     */
    internal fun updateStatisticsForPlayer() {
        playerIdParam?.let { id ->
            scope.launch {
                val player = playerRepository.fetchPlayerById(id = id)

                if (player == null) {
                    navigation.pop()
                } else {
                    val allDatesFilterLabel = application.getString(StringsIds.all)
                    val loggedShotEntries = player.toLoggedShotEntries()

                    playerStatisticsMutableStateFlow.update { state ->
                        state.copy(
                            playerStatisticsSummary = player.toPlayerStatisticsSummary(),
                            loggedShotEntries = loggedShotEntries,
                            dateFilterOptions = loggedShotEntries.buildDateFilterOptions(
                                allLabel = allDatesFilterLabel
                            ),
                            selectedDateFilter = allDatesFilterLabel,
                            allDatesFilterLabel = allDatesFilterLabel,
                            chartInfo = loggedShotEntries.toLineChartInfo(
                                madeLabel = madeLabel,
                                missedLabel = missedLabel
                            ),
                            hasNoLoggedShots = loggedShotEntries.isEmpty()
                        )
                    }
                }
            }
        } ?: navigation.pop()
    }

    /**
     * Updates the selected date filter and refreshes the displayed line chart data.
     *
     * @param filter The selected date filter option.
     */
    fun onDateFilterSelected(filter: String) {
        playerStatisticsMutableStateFlow.update { state ->
            val filteredEntries = state.loggedShotEntries.filterByDate(
                selectedFilter = filter,
                allLabel = state.allDatesFilterLabel
            )

            state.copy(
                selectedDateFilter = filter,
                chartInfo = filteredEntries.toLineChartInfo(
                    madeLabel = madeLabel,
                    missedLabel = missedLabel
                )
            )
        }
    }

    fun onToolbarMenuClicked() = navigation.pop()
}
