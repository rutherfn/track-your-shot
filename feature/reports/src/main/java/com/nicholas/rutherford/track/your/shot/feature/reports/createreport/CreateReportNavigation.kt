package com.nicholas.rutherford.track.your.shot.feature.reports.createreport

import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert

interface CreateReportNavigation {
    fun alert(alert: Alert)
    fun pop()
}
