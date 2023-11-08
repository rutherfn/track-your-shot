package com.nicholas.rutherford.track.my.shot.navigation

import com.nicholas.rutherford.track.my.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.my.shot.data.shared.progress.Progress
import kotlinx.coroutines.flow.StateFlow

interface Navigator {
    val alertActions: StateFlow<Alert?>
    val emailActions: StateFlow<Boolean?>
    val finishActions: StateFlow<Boolean?>
    val navActions: StateFlow<NavigationAction?>
    val popRouteActions: StateFlow<String?>
    val progressActions: StateFlow<Progress?>
    val navigationDrawerAction: StateFlow<Boolean?>

    fun alert(alertAction: Alert?)
    fun emailAction(emailAction: Boolean?)
    fun finish(finishAction: Boolean?)
    fun navigate(navigationAction: NavigationAction?)
    fun pop(popRouteAction: String?)
    fun progress(progressAction: Progress?)
    fun showNavigationDrawer(navigationDrawerAction: Boolean?)
}
