package com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer

import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.your.shot.navigation.NavigationActions
import com.nicholas.rutherford.track.your.shot.navigation.NavigationDestinations
import com.nicholas.rutherford.track.your.shot.navigation.Navigator

class CreateEditPlayerNavigationImpl(private val navigator: Navigator) : CreateEditPlayerNavigation {
    override fun alert(alert: Alert) = navigator.alert(alertAction = alert)

    override fun appSettings() = navigator.appSettings(appSettingsAction = true)
    override fun navigateToSelectShot(
        isExistingPlayer: Boolean,
        playerId: Int
    ) = navigator.navigate(navigationAction = NavigationActions.CreateEditPlayer.selectShot(
        isExistingPlayer = isExistingPlayer,
        playerId = playerId
    )
    )
    override fun disableProgress() = navigator.progress(progressAction = null)
    override fun enableProgress(progress: Progress) = navigator.progress(progressAction = progress)
    override fun pop() = navigator.pop(popRouteAction = NavigationDestinations.PLAYERS_LIST_SCREEN)
}
