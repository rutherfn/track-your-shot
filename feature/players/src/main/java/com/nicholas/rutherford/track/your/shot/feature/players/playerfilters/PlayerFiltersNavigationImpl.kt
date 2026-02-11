package com.nicholas.rutherford.track.your.shot.feature.players.playerfilters

import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import com.nicholas.rutherford.track.your.shot.navigation.NavigationActions
import com.nicholas.rutherford.track.your.shot.navigation.Navigator

/**
 * Represents the UI state for the Player Filters screen.
 *
 * Implementation of [PlayerFiltersScreen] using a [Navigator] to perform
 * navigation via predefined [NavigationActions].
 */
class PlayerFiltersNavigationImpl(private val navigator: Navigator) : PlayerFiltersNavigation {
    override fun alert(alert: Alert) = navigator.alert(alertAction = alert)
    override fun disableProgress() = navigator.progress(progressAction = null)
    override fun enableProgress(progress: Progress) = navigator.progress(progressAction = progress)
    override fun pop() = navigator.pop(popRouteAction = Constants.POP_DEFAULT_ACTION)
}
