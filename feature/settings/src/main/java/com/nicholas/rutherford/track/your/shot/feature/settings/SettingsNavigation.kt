package com.nicholas.rutherford.track.your.shot.feature.settings

import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert

interface SettingsNavigation {
    fun alert(alert: Alert)
    fun navigateToDeclaredShotsList()
    fun openNavigationDrawer()
    fun navigateToAccountInfoScreen(username: String, email: String)
    fun navigateToEnabledPermissions()
    fun navigateToPermissionEducationScreen()
    fun navigateToOnboardingEducationScreen()
    fun navigateToTermsConditions()
}
