package com.nicholas.rutherford.track.your.shot.feature.reports.reportlist

import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.navigation.NavigationActions
import com.nicholas.rutherford.track.your.shot.navigation.Navigator

class ReportListNavigationImpl(private val navigator: Navigator) : ReportListNavigation {
    override fun alert(alert: Alert) = navigator.alert(alertAction = alert)

    override fun openNavigationDrawer() = navigator.showNavigationDrawer(navigationDrawerAction = true)

    override fun navigateToCreateReport() = navigator.navigate(navigationAction = NavigationActions.ReportList.createReport())

    override fun navigateToViewPlayerReports() = navigator.navigate(navigationAction = NavigationActions.ReportList.viewPlayersReports())
}
