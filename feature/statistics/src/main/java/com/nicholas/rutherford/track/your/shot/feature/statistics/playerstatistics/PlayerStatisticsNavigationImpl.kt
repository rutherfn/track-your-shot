package com.nicholas.rutherford.track.your.shot.feature.statistics.playerstatistics

import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import com.nicholas.rutherford.track.your.shot.navigation.NavigationActions
import com.nicholas.rutherford.track.your.shot.navigation.Navigator

/**
 * Created by Nicholas Rutherford, last edited on 2026-07-05
 *
 * Implementation of [PlayerStatisticsNavigation] using a [Navigator] to perform
 * navigation via predefined [NavigationActions].
 */
class PlayerStatisticsNavigationImpl(private val navigator: Navigator) : PlayerStatisticsNavigation {
    override fun pop() = navigator.pop(popRouteAction = Constants.POP_DEFAULT_ACTION)
}
