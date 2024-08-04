package com.nicholas.rutherford.track.your.shot.feature.settings.accountinfo

import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import com.nicholas.rutherford.track.your.shot.navigation.Navigator

class AccountInfoNavigationImpl(private val navigator: Navigator) : AccountInfoNavigation {
    override fun alert(alert: Alert) = navigator.alert(alertAction = alert)
    override fun disableProgress() = navigator.progress(progressAction = null)
    override fun enableProgress() = navigator.progress(progressAction = Progress())
    override fun pop() = navigator.pop(popRouteAction = Constants.POP_DEFAULT_ACTION)
}
