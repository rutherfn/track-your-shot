package com.nicholas.rutherford.track.your.shot.feature.statistics.playerstatistics

import com.nicholas.rutherford.track.your.shot.compose.components.charts.PlayerShotsBreakdownLineChartEntry
import com.nicholas.rutherford.track.your.shot.compose.components.charts.PlayerShotsBreakdownLineChartInfo
import com.nicholas.rutherford.track.your.shot.data.room.response.Player
import com.nicholas.rutherford.track.your.shot.helper.extensions.parseDateValueToString

/**
 * Created by Nicholas Rutherford, last edited on 2026-07-05
 *
 * Maps finalized logged shots for this player into chart entries sorted by date.
 */
fun Player.toLoggedShotEntries(): List<PlayerLoggedShotEntry> {
    return shotsLoggedList
        .filterNot { shotLogged -> shotLogged.isPending }
        .sortedBy { shotLogged -> shotLogged.shotsLoggedMillisecondsValue }
        .map { shotLogged ->
            PlayerLoggedShotEntry(
                loggedDateLabel = parseDateValueToString(shotLogged.shotsLoggedMillisecondsValue),
                loggedDateMillis = shotLogged.shotsLoggedMillisecondsValue,
                shotName = shotLogged.shotName,
                madePercentage = shotLogged.shotsMadePercentValue,
                missedPercentage = shotLogged.shotsMissedPercentValue
            )
        }
}

/**
 * Builds date filter options from logged shot entries with an all-dates option first.
 */
fun List<PlayerLoggedShotEntry>.buildDateFilterOptions(allLabel: String): List<String> {
    val loggedDates = map { entry -> entry.loggedDateLabel }.distinct()
    return listOf(allLabel) + loggedDates
}

/**
 * Filters logged shot entries by the selected date filter option.
 */
fun List<PlayerLoggedShotEntry>.filterByDate(
    selectedFilter: String,
    allLabel: String
): List<PlayerLoggedShotEntry> {
    return if (selectedFilter.isEmpty() || selectedFilter == allLabel) {
        this
    } else {
        filter { entry -> entry.loggedDateLabel == selectedFilter }
    }
}

/**
 * Converts logged shot entries into line chart data.
 */
fun List<PlayerLoggedShotEntry>.toLineChartEntries(): List<PlayerShotsBreakdownLineChartEntry> {
    return map { entry ->
        PlayerShotsBreakdownLineChartEntry(
            sessionLabel = entry.shotName,
            madePercentage = entry.madePercentage,
            missedPercentage = entry.missedPercentage
        )
    }
}

/**
 * Builds line chart info from logged shot entries.
 */
fun List<PlayerLoggedShotEntry>.toLineChartInfo(
    madeLabel: String,
    missedLabel: String
): PlayerShotsBreakdownLineChartInfo? {
    if (isEmpty()) {
        return null
    }

    return PlayerShotsBreakdownLineChartInfo(
        madeLabel = madeLabel,
        missedLabel = missedLabel,
        entries = toLineChartEntries()
    )
}
