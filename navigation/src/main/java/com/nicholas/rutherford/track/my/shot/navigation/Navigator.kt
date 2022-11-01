package com.nicholas.rutherford.track.my.shot.navigation

import kotlinx.coroutines.flow.StateFlow

interface Navigator {
    val navActions: StateFlow<NavigationAction?>
    fun navigate(navigationAction: NavigationAction?)
}
