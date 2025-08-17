package com.nicholas.rutherford.track.your.shot.feature.settings

import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.navigation.NavigationActions
import com.nicholas.rutherford.track.your.shot.navigation.Navigator

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Implementation of [SettingsNavigation] using a [Navigator] to perform
 * navigation via predefined [NavigationActions].
 */
class SettingsNavigationImpl(private val navigator: Navigator) : SettingsNavigation {
    override fun alert(alert: Alert) = navigator.alert(alertAction = alert)

    override fun navigateToDeclaredShotsList() = navigator.navigate(navigationAction = NavigationActions.Settings.declaredShotsList())

    override fun openNavigationDrawer() = navigator.showNavigationDrawer(navigationDrawerAction = true)

    override fun navigateToAccountInfoScreen(username: String, email: String) = navigator.navigate(navigationAction = NavigationActions.Settings.accountInfo(username = username, email = email))

    override fun navigateToEnabledPermissions() = navigator.navigate(navigationAction = NavigationActions.Settings.enabledPermissions())

    override fun navigateToPermissionEducationScreen() = navigator.navigate(navigationAction = NavigationActions.Settings.permissionEducation())

    override fun navigateToOnboardingEducationScreen() = navigator.navigate(navigationAction = NavigationActions.Settings.onboardingEducation(isFirstTimeLaunched = false))

    override fun navigateToTermsConditions() = navigator.navigate(navigationAction = NavigationActions.Settings.termsConditions(shouldAcceptTerms = false))
}
