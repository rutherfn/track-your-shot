package com.nicholas.rutherford.track.my.shot.navigation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NavigatorImpl : Navigator {
    private val _navActions: MutableStateFlow<NavigationAction?> = MutableStateFlow(null)

    override val navActions: StateFlow<NavigationAction?> = _navActions.asStateFlow()

    override fun navigate(navigationAction: NavigationAction?) = _navActions.update { navigationAction }
}
