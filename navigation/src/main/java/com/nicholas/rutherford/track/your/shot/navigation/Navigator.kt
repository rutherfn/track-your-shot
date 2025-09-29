package com.nicholas.rutherford.track.your.shot.navigation

import com.nicholas.rutherford.track.your.shot.data.shared.InputInfo
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.datepicker.DatePickerInfo
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.your.shot.data.shared.snackbar.SnackBarInfo
import kotlinx.coroutines.flow.StateFlow

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16.
 *
 * Defines a interface for navigation actions.
 * These actions are used to navigate to different screens in the app, and observed through a [StateFlow]
 * With then extension functions to update those values.
 */
interface Navigator {
    val alertActions: StateFlow<Alert?>
    val appSettingsActions: StateFlow<Boolean?>
    val datePickerActions: StateFlow<DatePickerInfo?>
    val emailActions: StateFlow<Boolean?>
    val emailDevActions: StateFlow<String?>
    val finishActions: StateFlow<Boolean?>
    val inputInfoActions: StateFlow<InputInfo?>
    val navActions: StateFlow<NavigationAction?>
    val popRouteActions: StateFlow<String?>
    val progressActions: StateFlow<Progress?>
    val navigationDrawerAction: StateFlow<Boolean?>
    val snackBarAction: StateFlow<SnackBarInfo>
    val urlAction: StateFlow<String?>

    fun alert(alertAction: Alert?)
    fun appSettings(appSettingsAction: Boolean?)
    fun datePicker(datePickerAction: DatePickerInfo?)
    fun emailAction(emailAction: Boolean?)
    fun emailDevAction(emailDevAction: String?)
    fun finish(finishAction: Boolean?)
    fun inputInfo(inputInfoAction: InputInfo?)
    fun navigate(navigationAction: NavigationAction?)
    fun pop(popRouteAction: String?)
    fun progress(progressAction: Progress?)
    fun showNavigationDrawer(navigationDrawerAction: Boolean?)
    fun url(url: String?)
}
