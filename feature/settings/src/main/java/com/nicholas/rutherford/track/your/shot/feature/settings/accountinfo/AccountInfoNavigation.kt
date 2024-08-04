package com.nicholas.rutherford.track.your.shot.feature.settings.accountinfo

import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert

interface AccountInfoNavigation {
    fun alert(alert: Alert)
    fun disableProgress()
    fun enableProgress()
    fun pop()
}
