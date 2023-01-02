package com.nicholas.rutherford.track.my.shot.feature.create.account

import com.nicholas.rutherford.track.my.shot.data.shared.alert.Alert

interface CreateAccountNavigation {
    fun alert(alert: Alert)
    fun pop()
}
