package com.nicholas.rutherford.track.your.shot.feature.login

import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Defines navigation actions available from the login screen.
 */
interface LoginNavigation {
    fun alert(alert: Alert)
    fun disableProgress()
    fun enableProgress(progress: Progress)
    fun navigateToPlayersList()
    fun navigateToForgotPassword()
    fun navigateToCreateAccount()
}
