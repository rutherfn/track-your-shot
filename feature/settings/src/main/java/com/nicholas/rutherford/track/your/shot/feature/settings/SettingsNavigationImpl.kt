package com.nicholas.rutherford.track.your.shot.feature.settings

import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.your.shot.data.shared.snackbar.SnackBarInfo
import com.nicholas.rutherford.track.your.shot.navigation.NavigationActions
import com.nicholas.rutherford.track.your.shot.navigation.Navigator

/**
 * Created by Nicholas Rutherford, last edited on 2025-01-27
 *
 * Implementation of [SettingsNavigation] using a [Navigator] to perform
 * navigation via predefined [NavigationActions].
 *
 * This class handles all navigation actions from the Settings screen, including
 * navigation to various settings sub-screens, displaying alerts, progress indicators,
 * and snackBar messages.
 *
 * @param navigator The [Navigator] instance used to perform navigation actions.
 */
class SettingsNavigationImpl(private val navigator: Navigator) : SettingsNavigation {

    /** Displays an alert dialog with the specified alert information. */
    override fun alert(alert: Alert) = navigator.alert(alertAction = alert)

    /** Navigates to the declared shots list screen. */
    override fun navigateToDeclaredShotsList() = navigator.navigate(navigationAction = NavigationActions.Settings.declaredShotsList())

    /** Opens the navigation drawer. */
    override fun openNavigationDrawer() = navigator.showNavigationDrawer(navigationDrawerAction = true)

    /** Navigates to the account info screen with user details. */
    override fun navigateToAccountInfoScreen(username: String, email: String) = navigator.navigate(navigationAction = NavigationActions.Settings.accountInfo(username = username, email = email))

    /** Navigates to the debug toggles screen. */
    override fun navigateToDebugToggles() = navigator.navigate(navigationAction = NavigationActions.Settings.debugToggle())

    /** Navigates to the enabled permissions screen. */
    override fun navigateToEnabledPermissions() = navigator.navigate(navigationAction = NavigationActions.Settings.enabledPermissions())

    /** Navigates to the permission education screen. */
    override fun navigateToPermissionEducationScreen() = navigator.navigate(navigationAction = NavigationActions.Settings.permissionEducation())

    /** Navigates to the onboarding education screen. */
    override fun navigateToOnboardingEducationScreen() = navigator.navigate(navigationAction = NavigationActions.Settings.onboardingEducation(isFirstTimeLaunched = false))

    /** Navigates to the terms and conditions screen. */
    override fun navigateToTermsConditions() = navigator.navigate(navigationAction = NavigationActions.Settings.termsConditions(shouldAcceptTerms = false))

    /** Enables and displays a progress indicator. */
    override fun enableProgress(progress: Progress) = navigator.progress(progressAction = progress)

    /** Disables and hides the progress indicator. */
    override fun disableProgress() = navigator.progress(progressAction = null)

    /** Displays a snackBar with the specified information. */
    override fun snackBar(snackBarInfo: SnackBarInfo) = navigator.snackBar(snackBarInfo = snackBarInfo)

    /** Requests an in-app review flow. */
    override fun requestReview() = navigator.requestReview(reviewAction = true)
}
