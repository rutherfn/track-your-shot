package com.nicholas.rutherford.track.your.shot.feature.settings

import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.your.shot.data.shared.snackbar.SnackBarInfo

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Defines navigation actions available from the Settings Screen.
 */
interface SettingsNavigation {
    fun alert(alert: Alert)
    fun navigateToDeclaredShotsList()
    fun openNavigationDrawer()
    fun navigateToAccountInfoScreen(username: String, email: String)
    fun navigateToEnabledPermissions()
    fun navigateToPermissionEducationScreen()
    fun navigateToOnboardingEducationScreen()
    fun navigateToTermsConditions()
    fun enableProgress(progress: Progress)
    fun disableProgress()
    fun snackBar(snackBarInfo: SnackBarInfo)
}
