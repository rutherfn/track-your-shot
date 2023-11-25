package com.nicholas.rutherford.track.your.shot.feature.players.playerlist

import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.your.shot.navigation.NavigationActions
import com.nicholas.rutherford.track.your.shot.navigation.Navigator

class PlayersListNavigationImpl(
    private val navigator: Navigator
) : PlayersListNavigation {
    override fun alert(alert: Alert) = navigator.alert(alertAction = alert)
    override fun disableProgress() = navigator.progress(progressAction = null)
    override fun enableProgress(progress: Progress) = navigator.progress(progressAction = progress)
    override fun navigateToCreatePlayer() = navigator.navigate(navigationAction = NavigationActions.PlayersList.createPlayer())
    override fun openNavigationDrawer() = navigator.showNavigationDrawer(navigationDrawerAction = true)
}
