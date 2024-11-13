package com.nicholas.rutherford.track.your.shot.feature.settings

import com.nicholas.rutherford.track.your.shot.navigation.NavigationActions
import com.nicholas.rutherford.track.your.shot.navigation.Navigator

class SettingsNavigationImpl(private val navigator: Navigator) : SettingsNavigation {

    override fun openNavigationDrawer() = navigator.showNavigationDrawer(navigationDrawerAction = true)

    override fun navigateToEnabledPermissions() = navigator.navigate(navigationAction = NavigationActions.Settings.enabledPermissions())

    override fun navigateToPermissionEducationScreen() = navigator.navigate(navigationAction = NavigationActions.Settings.permissionEducation())

    override fun navigateToTermsConditions() = navigator.navigate(navigationAction = NavigationActions.Settings.termsConditions(isAcknowledgeConditions = false))
}
