package com.nicholas.rutherford.track.your.shot.feature.forgot.password

import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Defines navigation actions available from the forgot password screen.
 */
interface ForgotPasswordNavigation {
    fun alert(alert: Alert)
    fun disableProgress()
    fun enableProgress(progress: Progress)
    fun pop()
}
