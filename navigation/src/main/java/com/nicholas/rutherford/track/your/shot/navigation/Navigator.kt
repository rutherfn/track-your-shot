package com.nicholas.rutherford.track.your.shot.navigation

import com.nicholas.rutherford.track.your.shot.data.shared.InputInfo
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.datepicker.DatePickerInfo
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress
import kotlinx.coroutines.flow.StateFlow

interface Navigator {
    val alertActions: StateFlow<Alert?>
    val appSettingsActions: StateFlow<Boolean?>
    val datePickerActions: StateFlow<DatePickerInfo?>
    val emailActions: StateFlow<Boolean?>
    val finishActions: StateFlow<Boolean?>
    val inputInfoActions: StateFlow<InputInfo?>
    val navActions: StateFlow<NavigationAction?>
    val popRouteActions: StateFlow<String?>
    val progressActions: StateFlow<Progress?>
    val navigationDrawerAction: StateFlow<Boolean?>
    val urlAction: StateFlow<String?>

    fun alert(alertAction: Alert?)
    fun appSettings(appSettingsAction: Boolean?)
    fun datePicker(datePickerAction: DatePickerInfo?)
    fun emailAction(emailAction: Boolean?)
    fun finish(finishAction: Boolean?)
    fun inputInfo(inputInfoAction: InputInfo?)
    fun navigate(navigationAction: NavigationAction?)
    fun pop(popRouteAction: String?)
    fun progress(progressAction: Progress?)
    fun showNavigationDrawer(navigationDrawerAction: Boolean?)
    fun url(url: String?)
}
