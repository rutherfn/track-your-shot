package com.nicholas.rutherford.track.your.shot.feature.statistics

import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.navigation.NavigationActions
import com.nicholas.rutherford.track.your.shot.navigation.Navigator

/**
 * Created by Nicholas Rutherford, last edited on 2026-07-02
 *
 * Implementation of [StatisticsNavigation] using a [Navigator] to perform
 * navigation via predefined [NavigationActions].
 */
class StatisticsNavigationImpl(private val navigator: Navigator) : StatisticsNavigation {
    override fun alert(alert: Alert) = navigator.alert(alertAction = alert)
    override fun openNavigationDrawer() = navigator.showNavigationDrawer(navigationDrawerAction = true)

    // todo -> Wire up player detailed statistics destination when that screen is added.
    override fun navigateToPlayerDetailedStatistics(playerName: String) = Unit
}
