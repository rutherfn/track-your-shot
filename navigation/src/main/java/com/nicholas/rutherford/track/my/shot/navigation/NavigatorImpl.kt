package com.nicholas.rutherford.track.my.shot.navigation

import com.nicholas.rutherford.track.my.shot.data.shared.alert.Alert
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NavigatorImpl : Navigator {
    private val _alertActions: MutableStateFlow<Alert?> = MutableStateFlow(null)
    private val _navActions: MutableStateFlow<NavigationAction?> = MutableStateFlow(null)
    private val _popRouteActions: MutableStateFlow<String?> = MutableStateFlow(null)

    override val alertActions: StateFlow<Alert?> = _alertActions.asStateFlow()
    override val popRouteActions: StateFlow<String?> = _popRouteActions.asStateFlow()
    override val navActions: StateFlow<NavigationAction?> = _navActions.asStateFlow()

    override fun alert(alertAction: Alert?) = _alertActions.update { alertAction }

    override fun navigate(navigationAction: NavigationAction?) = _navActions.update { navigationAction }

    override fun pop(popRouteAction: String?) = _popRouteActions.update { popRouteAction }
}
