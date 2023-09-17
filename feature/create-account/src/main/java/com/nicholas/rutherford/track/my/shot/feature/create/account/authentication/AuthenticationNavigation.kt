package com.nicholas.rutherford.track.my.shot.feature.create.account.authentication

import com.nicholas.rutherford.track.my.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.my.shot.data.shared.progress.Progress

interface AuthenticationNavigation {
    fun alert(alert: Alert)
    fun disableProgress()
    fun enableProgress(progress: Progress)
    fun navigateToPlayersList()
    fun openEmail()
    fun finish()
}
