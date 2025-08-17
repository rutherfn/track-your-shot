package com.nicholas.rutherford.track.your.shot.feature.settings.enabledpermissions

import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Defines navigation actions available from the Enabled Permissions screen.
 */
interface EnabledPermissionsNavigation {
    fun alert(alert: Alert)
    fun appSettings()
    fun pop()
}
