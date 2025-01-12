package com.nicholas.rutherford.track.your.shot.feature.reports.reportlist

import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert

interface ReportListNavigation {
    fun alert(alert: Alert)
    fun openNavigationDrawer()
    fun navigateToCreateReport()
    fun navigateToViewPlayerReports()
}
