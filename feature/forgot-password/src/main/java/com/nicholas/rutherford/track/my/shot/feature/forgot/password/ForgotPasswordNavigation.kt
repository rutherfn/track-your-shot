package com.nicholas.rutherford.track.my.shot.feature.forgot.password

import com.nicholas.rutherford.track.my.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.my.shot.data.shared.progress.Progress

interface ForgotPasswordNavigation {
    fun alert(alert: Alert)
    fun disableProgress()
    fun enableProgress(progress: Progress)
    fun pop()
}
