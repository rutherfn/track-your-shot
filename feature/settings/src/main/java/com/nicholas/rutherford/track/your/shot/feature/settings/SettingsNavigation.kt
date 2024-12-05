package com.nicholas.rutherford.track.your.shot.feature.settings

import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert

interface SettingsNavigation {
    fun alert(alert: Alert)
    fun openNavigationDrawer()
    fun navigateToEnabledPermissions()
    fun navigateToPermissionEducationScreen()
    fun navigateToOnboardingEducationScreen()
    fun navigateToTermsConditions()
}
