package com.nicholas.rutherford.track.your.shot.feature.reports.reportlist

import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.navigation.NavigationActions
import com.nicholas.rutherford.track.your.shot.navigation.Navigator

class ReportListNavigationImpl(private val navigator: Navigator) : ReportListNavigation {
    override fun alert(alert: Alert) = navigator.alert(alertAction = alert)

    override fun openNavigationDrawer() = navigator.showNavigationDrawer(navigationDrawerAction = true)

    override fun navigateToCreatePlayerReport() = navigator.navigate(navigationAction = NavigationActions.ReportList.createPlayerReport())

    override fun navigateToViewCreatedReportTimes() = navigator.navigate(navigationAction = NavigationActions.ReportList.viewReportTimes())
}
