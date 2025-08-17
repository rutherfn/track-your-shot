package com.nicholas.rutherford.track.your.shot.feature.reports.reportlist

import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Defines navigation actions available from the report list screen
 */
interface ReportListNavigation {
    fun alert(alert: Alert)
    fun openNavigationDrawer()
    fun navigateToCreateReport()
    fun navigateToUrl(url: String)
    fun disableProgress()
    fun enableProgress(progress: Progress)
}
