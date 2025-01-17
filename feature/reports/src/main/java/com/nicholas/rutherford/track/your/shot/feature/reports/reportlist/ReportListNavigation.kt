package com.nicholas.rutherford.track.your.shot.feature.reports.reportlist

import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress

interface ReportListNavigation {
    fun alert(alert: Alert)
    fun openNavigationDrawer()
    fun navigateToCreateReport()
    fun disableProgress()
    fun enableProgress(progress: Progress)
}
