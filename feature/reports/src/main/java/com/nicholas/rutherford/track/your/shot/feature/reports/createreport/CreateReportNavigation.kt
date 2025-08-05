package com.nicholas.rutherford.track.your.shot.feature.reports.createreport

import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress

/**
 * Defines navigation actions available from the create report screen
 */
interface CreateReportNavigation {
    fun alert(alert: Alert)
    fun pop()
    fun disableProgress()
    fun enableProgress(progress: Progress)
}
