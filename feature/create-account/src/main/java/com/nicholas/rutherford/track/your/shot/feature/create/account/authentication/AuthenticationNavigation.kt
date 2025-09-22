package com.nicholas.rutherford.track.your.shot.feature.create.account.authentication

import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress

interface AuthenticationNavigation {
    fun alert(alert: Alert)
    fun disableProgress()
    fun enableProgress(progress: Progress)
    fun navigateToTermsAndConditions()
    fun navigateToLogin()
    fun openEmail()
    fun finish()
}

// test
