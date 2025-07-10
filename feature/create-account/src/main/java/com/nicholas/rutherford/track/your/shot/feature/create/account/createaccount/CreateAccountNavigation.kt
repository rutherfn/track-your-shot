package com.nicholas.rutherford.track.your.shot.feature.create.account.createaccount

import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress

/**
 * Defines navigation actions available from the create account screen.
 */
interface CreateAccountNavigation {
    fun alert(alert: Alert)
    fun disableProgress()
    fun enableProgress(progress: Progress)
    fun navigateToAuthentication(email: String, username: String)
    fun navigateToTermsAndConditions()
    fun pop()
}
