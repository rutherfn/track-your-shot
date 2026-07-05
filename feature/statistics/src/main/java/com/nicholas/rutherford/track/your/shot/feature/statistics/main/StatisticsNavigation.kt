package com.nicholas.rutherford.track.your.shot.feature.statistics.main

import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert

/**
 * Created by Nicholas Rutherford, last edited on 2026-07-02
 *
 * Defines navigation actions available from the statistics screen.
 */
interface StatisticsNavigation {
    fun alert(alert: Alert)
    fun openNavigationDrawer()
    fun navigateToPlayerStatistics(playerId: Int)
}
