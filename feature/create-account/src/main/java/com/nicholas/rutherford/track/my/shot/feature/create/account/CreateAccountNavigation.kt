package com.nicholas.rutherford.track.my.shot.feature.create.account

import com.nicholas.rutherford.track.my.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.my.shot.data.shared.progress.Progress

interface CreateAccountNavigation {
    fun alert(alert: Alert)
    fun enableProgress(progress: Progress)
    fun disableProgress()
    fun pop()
}
