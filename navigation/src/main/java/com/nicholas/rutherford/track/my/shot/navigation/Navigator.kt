package com.nicholas.rutherford.track.my.shot.navigation

import com.nicholas.rutherford.track.my.shot.data.shared.alert.Alert
import kotlinx.coroutines.flow.StateFlow

interface Navigator {
    val alertActions: StateFlow<Alert?>
    val navActions: StateFlow<NavigationAction?>
    val popRouteActions: StateFlow<String?>

    fun alert(alertAction: Alert?)
    fun navigate(navigationAction: NavigationAction?)
    fun pop(popRouteAction: String?)
}
