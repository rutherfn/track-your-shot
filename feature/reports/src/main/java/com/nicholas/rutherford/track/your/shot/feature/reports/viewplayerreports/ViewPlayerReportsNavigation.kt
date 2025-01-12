package com.nicholas.rutherford.track.your.shot.feature.reports.viewplayerreports

import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress

interface ViewPlayerReportsNavigation {
    fun alert(alert: Alert)
    fun disableProgress()
    fun enableProgress(progress: Progress)
    fun pop()
}
