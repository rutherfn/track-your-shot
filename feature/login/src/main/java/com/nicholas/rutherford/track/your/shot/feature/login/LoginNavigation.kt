package com.nicholas.rutherford.track.your.shot.feature.login

import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress

interface LoginNavigation {
    fun alert(alert: Alert)
    fun disableProgress()
    fun enableProgress(progress: Progress)
    fun navigateToPlayersList()
    fun navigateToForgotPassword()
    fun navigateToCreateAccount()
}
