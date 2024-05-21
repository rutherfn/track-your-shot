package com.nicholas.rutherford.track.your.shot.feature.players.playerlist

import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.your.shot.helper.extensions.safeLet
import com.nicholas.rutherford.track.your.shot.navigation.NavigationActions
import com.nicholas.rutherford.track.your.shot.navigation.Navigator

class PlayersListNavigationImpl(
    private val navigator: Navigator
) : PlayersListNavigation {
    override fun alert(alert: Alert) = navigator.alert(alertAction = alert)
    override fun disableProgress() = navigator.progress(progressAction = null)
    override fun enableProgress(progress: Progress) = navigator.progress(progressAction = progress)
    override fun navigateToCreateEditPlayer(firstName: String?, lastName: String?) {
        safeLet(firstName, lastName) { first, last ->
            navigator.navigate(
                navigationAction = NavigationActions.PlayersList.createEditPlayerWithParams(
                    firstName = first,
                    lastName = last
                )
            )
        } ?: run {
            navigator.navigate(navigationAction = NavigationActions.PlayersList.createEditPlayer())
        }
    }
    override fun openNavigationDrawer() = navigator.showNavigationDrawer(navigationDrawerAction = true)
}
