package com.nicholas.rutherford.track.your.shot.navigation

import com.nicholas.rutherford.track.your.shot.data.shared.InputInfo
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.datepicker.DatePickerInfo
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.your.shot.data.shared.snackbar.SnackBarInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16.
 *
 * Implementation of the [Navigator] interface that acts as a centralized controller for
 * UI-related actions such as showing alerts, navigating, triggering dialogs, etc.
 *
 * This is designed to be injected and shared across the app, enabling
 * loosely-coupled, state-driven communication between view models and composables.
 */
class NavigatorImpl : Navigator {

    // Internal mutable state flows
    private val _alertActions = MutableStateFlow<Alert?>(value = null)
    private val _appSettingsActions = MutableStateFlow<Boolean?>(value = null)
    private val _datePickerActions = MutableStateFlow<DatePickerInfo?>(value = null)
    private val _emailActions = MutableStateFlow<Boolean?>(value = null)
    private val _emailDevActions = MutableStateFlow<String?>(value = null)
    private val _finishActions = MutableStateFlow<Boolean?>(value = null)
    private val _inputInfoActions = MutableStateFlow<InputInfo?>(value = null)
    private val _navActions = MutableStateFlow<NavigationAction?>(value = null)
    private val _popRouteActions = MutableStateFlow<String?>(value = null)
    private val _progressActions = MutableStateFlow<Progress?>(value = null)
    private val _snackBarActions = MutableStateFlow<SnackBarInfo?>(value = null)
    private val _navigationDrawerAction = MutableStateFlow<Boolean?>(value = null)
    private val _reviewActions = MutableStateFlow<Boolean?>(value = null)
    private val _urlAction = MutableStateFlow<String?>(value = null)

    // Exposed read-only flows for observers (e.g., UI)
    override val alertActions: StateFlow<Alert?> = _alertActions.asStateFlow()
    override val appSettingsActions: StateFlow<Boolean?> = _appSettingsActions.asStateFlow()
    override val datePickerActions: StateFlow<DatePickerInfo?> = _datePickerActions.asStateFlow()
    override val emailActions: StateFlow<Boolean?> = _emailActions.asStateFlow()
    override val emailDevActions: StateFlow<String?> = _emailDevActions.asStateFlow()
    override val finishActions: StateFlow<Boolean?> = _finishActions.asStateFlow()
    override val inputInfoActions: StateFlow<InputInfo?> = _inputInfoActions.asStateFlow()
    override val navActions: StateFlow<NavigationAction?> = _navActions.asStateFlow()
    override val popRouteActions: StateFlow<String?> = _popRouteActions.asStateFlow()
    override val progressActions: StateFlow<Progress?> = _progressActions.asStateFlow()
    override val navigationDrawerAction: StateFlow<Boolean?> = _navigationDrawerAction.asStateFlow()
    override val reviewActions: StateFlow<Boolean?> = _reviewActions.asStateFlow()
    override val snackBarActions: StateFlow<SnackBarInfo?> = _snackBarActions.asStateFlow()
    override val urlAction: StateFlow<String?> = _urlAction.asStateFlow()

    /** Triggers an alert dialog with the given [alertAction]. */
    override fun alert(alertAction: Alert?) = _alertActions.update { alertAction }

    /** Requests opening of the app's system settings. */
    override fun appSettings(appSettingsAction: Boolean?) = _appSettingsActions.update { appSettingsAction }

    /** Displays a date picker dialog with the given [datePickerAction] data. */
    override fun datePicker(datePickerAction: DatePickerInfo?) = _datePickerActions.update { datePickerAction }

    /** Opens the default email client with a generic message. */
    override fun emailAction(emailAction: Boolean?) = _emailActions.update { emailAction }

    /** Opens the email client to send a message to the developer. */
    override fun emailDevAction(emailDevAction: String?) = _emailDevActions.update { emailDevAction }

    /** Triggers input-related UI with the provided [inputInfoAction]. */
    override fun inputInfo(inputInfoAction: InputInfo?) = _inputInfoActions.update { inputInfoAction }

    /** Triggers a request to finish the current activity or screen. */
    override fun finish(finishAction: Boolean?) = _finishActions.update { finishAction }

    /** Initiates navigation to a new screen using [navigationAction]. */
    override fun navigate(navigationAction: NavigationAction?) = _navActions.update { navigationAction }

    /** Requests a pop (back) action to a specific route, if provided. */
    override fun pop(popRouteAction: String?) = _popRouteActions.update { popRouteAction }

    /** Triggers a progress indicator with optional details in [progressAction]. */
    override fun progress(progressAction: Progress?) = _progressActions.update { progressAction }

    /** Opens or closes the navigation drawer. */
    override fun showNavigationDrawer(navigationDrawerAction: Boolean?) = _navigationDrawerAction.update { navigationDrawerAction }

    /** Displays a snack bar with the given [snackBarInfo] data. */
    override fun snackBar(snackBarInfo: SnackBarInfo?) = _snackBarActions.update { snackBarInfo }

    /** Requests an in-app review flow. */
    override fun requestReview(reviewAction: Boolean?) = _reviewActions.update { reviewAction }

    /** Opens a given [url] in the user's browser. */
    override fun url(url: String?) = _urlAction.update { url }
}
