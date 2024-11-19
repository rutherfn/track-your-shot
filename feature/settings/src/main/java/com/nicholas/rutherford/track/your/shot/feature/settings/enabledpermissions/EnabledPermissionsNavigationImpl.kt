package com.nicholas.rutherford.track.your.shot.feature.settings.enabledpermissions

import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import com.nicholas.rutherford.track.your.shot.navigation.Navigator

class EnabledPermissionsNavigationImpl(private val navigator: Navigator) : EnabledPermissionsNavigation {
    override fun alert(alert: Alert) = navigator.alert(alertAction = alert)
    override fun appSettings() = navigator.appSettings(appSettingsAction = true)
    override fun pop() = navigator.pop(popRouteAction = Constants.POP_DEFAULT_ACTION)
}
