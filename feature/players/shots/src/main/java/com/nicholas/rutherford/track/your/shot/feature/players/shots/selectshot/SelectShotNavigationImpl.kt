package com.nicholas.rutherford.track.your.shot.feature.players.shots.selectshot

import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import com.nicholas.rutherford.track.your.shot.navigation.NavigationActions
import com.nicholas.rutherford.track.your.shot.navigation.NavigationDestinations
import com.nicholas.rutherford.track.your.shot.navigation.Navigator

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Implementation of [SelectShotNavigation] using a [Navigator] to perform
 * navigation via predefined [NavigationActions].
 */
class SelectShotNavigationImpl(private val navigator: Navigator) : SelectShotNavigation {
    override fun alert(alert: Alert) = navigator.alert(alertAction = alert)
    override fun popFromCreatePlayer() = navigator.pop(popRouteAction = Constants.POP_DEFAULT_ACTION)

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
