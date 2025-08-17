package com.nicholas.rutherford.track.your.shot.feature.settings.enabledpermissions

import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import com.nicholas.rutherford.track.your.shot.navigation.NavigationActions
import com.nicholas.rutherford.track.your.shot.navigation.Navigator

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Implementation of [EnabledPermissionsNavigation] using a [Navigator] to perform
 * navigation via predefined [NavigationActions].
 */
class EnabledPermissionsNavigationImpl(private val navigator: Navigator) : EnabledPermissionsNavigation {
    override fun alert(alert: Alert) = navigator.alert(alertAction = alert)
    override fun appSettings() = navigator.appSettings(appSettingsAction = true)
    override fun pop() = navigator.pop(popRouteAction = Constants.POP_DEFAULT_ACTION)
}
