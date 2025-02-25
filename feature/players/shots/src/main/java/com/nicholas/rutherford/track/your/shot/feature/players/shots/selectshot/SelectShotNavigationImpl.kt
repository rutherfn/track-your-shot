package com.nicholas.rutherford.track.your.shot.feature.players.shots.selectshot

import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.navigation.NavigationActions
import com.nicholas.rutherford.track.your.shot.navigation.NavigationDestinations
import com.nicholas.rutherford.track.your.shot.navigation.Navigator

class SelectShotNavigationImpl(private val navigator: Navigator) : SelectShotNavigation {
    override fun alert(alert: Alert) = navigator.alert(alertAction = alert)
    override fun popFromCreatePlayer() = navigator.pop(popRouteAction = NavigationDestinations.CREATE_EDIT_PLAYER_SCREEN)

    override fun popFromEditPlayer() = navigator.pop(popRouteAction = NavigationDestinations.CREATE_EDIT_PLAYER_SCREEN_WITH_PARAMS)

    override fun navigateToLogShot(
        isExistingPlayer: Boolean,
        playerId: Int,
        shotType: Int,
        shotId: Int,
        viewCurrentExistingShot: Boolean,
        viewCurrentPendingShot: Boolean,
        fromShotList: Boolean
    ) {
        navigator.navigate(
            navigationAction = NavigationActions.SelectShot.logShot(
                isExistingPlayer = isExistingPlayer,
                playerId = playerId,
                shotType = shotType,
                shotId = shotId,
                viewCurrentExistingShot = viewCurrentExistingShot,
                viewCurrentPendingShot = viewCurrentPendingShot,
                fromShotList = fromShotList
            )
        )
    }
}
