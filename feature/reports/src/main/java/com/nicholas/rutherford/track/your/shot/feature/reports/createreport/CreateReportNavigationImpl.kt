package com.nicholas.rutherford.track.your.shot.feature.reports.createreport

import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import com.nicholas.rutherford.track.your.shot.navigation.NavigationActions
import com.nicholas.rutherford.track.your.shot.navigation.Navigator

/**
 * Implementation of [CreateReportNavigation] using a [Navigator] to perform
 * navigation via predefined [NavigationActions].
 */
class CreateReportNavigationImpl(private val navigator: Navigator) : CreateReportNavigation {
    override fun alert(alert: Alert) = navigator.alert(alertAction = alert)
    override fun pop() = navigator.pop(popRouteAction = Constants.POP_DEFAULT_ACTION)
    override fun disableProgress() = navigator.progress(progressAction = null)
    override fun enableProgress(progress: Progress) = navigator.progress(progressAction = progress)
}
