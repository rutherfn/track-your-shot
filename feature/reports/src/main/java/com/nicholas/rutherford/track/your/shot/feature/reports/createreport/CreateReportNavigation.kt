package com.nicholas.rutherford.track.your.shot.feature.reports.createreport

import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Defines navigation actions available from the create report screen
 */
interface CreateReportNavigation {
    fun alert(alert: Alert)
    fun pop()
    fun navigateToReportList()
    fun disableProgress()
    fun enableProgress(progress: Progress)
}
