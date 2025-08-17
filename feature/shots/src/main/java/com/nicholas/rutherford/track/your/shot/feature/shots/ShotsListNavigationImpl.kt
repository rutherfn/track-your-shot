package com.nicholas.rutherford.track.your.shot.feature.shots

import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.navigation.NavigationActions
import com.nicholas.rutherford.track.your.shot.navigation.NavigationDestinations
import com.nicholas.rutherford.track.your.shot.navigation.Navigator

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Implementation of [ShotsListNavigation] using a [Navigator] to perform
 * navigation via predefined [NavigationActions].
 */
class ShotsListNavigationImpl(private val navigator: Navigator) : ShotsListNavigation {
    override fun alert(alert: Alert) = navigator.alert(alertAction = alert)
    override fun navigateToLogShot(
        isExistingPlayer: Boolean,
        playerId: Int,
        shotType: Int,
        shotId: Int,
        viewCurrentExistingShot: Boolean,
        viewCurrentPendingShot: Boolean,
        fromShotList: Boolean
    ) = navigator.navigate(
        navigationAction = NavigationActions.ShotsList.logShot(
            isExistingPlayer = isExistingPlayer,
            shotType = shotType,
            playerId = playerId,
            shotId = shotId,
            viewCurrentExistingShot = viewCurrentExistingShot,
            viewCurrentPendingShot = viewCurrentPendingShot,
            fromShotList = fromShotList
        )
    )
    override fun openNavigationDrawer() = navigator.showNavigationDrawer(navigationDrawerAction = true)
    override fun popToPlayerList() = navigator.pop(popRouteAction = NavigationDestinations.PLAYERS_LIST_SCREEN)
}
