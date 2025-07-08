package com.nicholas.rutherford.track.your.shot.feature.forgot.password

import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress

/**
 * Defines navigation actions available from the forgot password screen.
 */
interface ForgotPasswordNavigation {
    fun alert(alert: Alert)
    fun disableProgress()
    fun enableProgress(progress: Progress)
    fun pop()
}
