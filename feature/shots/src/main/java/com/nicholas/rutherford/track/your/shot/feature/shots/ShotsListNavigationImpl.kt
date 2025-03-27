package com.nicholas.rutherford.track.your.shot.feature.shots

import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import com.nicholas.rutherford.track.your.shot.navigation.NavigationActions
import com.nicholas.rutherford.track.your.shot.navigation.NavigationDestinations
import com.nicholas.rutherford.track.your.shot.navigation.Navigator

class ShotsListNavigationImpl(private val navigator: Navigator) : ShotsListNavigation {
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
