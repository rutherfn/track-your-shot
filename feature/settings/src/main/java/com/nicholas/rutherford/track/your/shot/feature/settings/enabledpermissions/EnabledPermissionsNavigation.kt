package com.nicholas.rutherford.track.your.shot.feature.settings.enabledpermissions

import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert

interface EnabledPermissionsNavigation {
    fun alert(alert: Alert)
    fun appSettings()
    fun pop()
}
