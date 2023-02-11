package com.nicholas.rutherford.track.my.shot.feature.create.account.authentication

import com.nicholas.rutherford.track.my.shot.data.shared.alert.Alert

interface AuthenticationNavigation {
    fun alert(alert: Alert)

    fun openEmail()
    fun finish()
}
