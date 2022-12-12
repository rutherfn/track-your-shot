package com.nicholas.rutherford.track.my.shot.navigation

import kotlinx.coroutines.flow.StateFlow

interface Navigator {
    val navActions: StateFlow<NavigationAction?>
    val popRouteActions: StateFlow<String?>

    fun navigate(navigationAction: NavigationAction?)
    fun pop(popRouteAction: String?)
    fun updatePopRouteActionToNull()
}
