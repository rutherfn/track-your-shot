package com.nicholas.rutherford.track.your.shot.feature.players.createplayer

import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.your.shot.navigation.NavigationActions
import com.nicholas.rutherford.track.your.shot.navigation.NavigationDestinations
import com.nicholas.rutherford.track.your.shot.navigation.Navigator

class CreatePlayerNavigationImpl(
    private val navigator: Navigator
) : CreatePlayerNavigation {
    override fun alert(alert: Alert) = navigator.alert(alertAction = alert)

    override fun appSettings() = navigator.appSettings(appSettingsAction = true)
    override fun disableProgress() = navigator.progress(progressAction = null)
    override fun enableProgress(progress: Progress) = navigator.progress(progressAction = progress)
    override fun pop() = navigator.pop(popRouteAction = NavigationDestinations.PLAYERS_LIST_SCREEN)
}
