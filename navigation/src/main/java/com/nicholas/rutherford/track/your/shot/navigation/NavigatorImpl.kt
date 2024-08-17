package com.nicholas.rutherford.track.your.shot.navigation

import com.nicholas.rutherford.track.your.shot.data.shared.InputInfo
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.datepicker.DatePickerInfo
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NavigatorImpl : Navigator {
    private val _alertActions: MutableStateFlow<Alert?> = MutableStateFlow(value = null)
    private val _appSettingsActions: MutableStateFlow<Boolean?> = MutableStateFlow(value = null)
    private val _datePickerActions: MutableStateFlow<DatePickerInfo?> = MutableStateFlow(value = null)
    private val _emailActions: MutableStateFlow<Boolean?> = MutableStateFlow(value = null)
    private val _finishActions: MutableStateFlow<Boolean?> = MutableStateFlow(value = null)
    private val _inputInfoActions: MutableStateFlow<InputInfo?> = MutableStateFlow(value = null)
    private val _navActions: MutableStateFlow<NavigationAction?> = MutableStateFlow(value = null)
    private val _popRouteActions: MutableStateFlow<String?> = MutableStateFlow(value = null)
    private val _progressActions: MutableStateFlow<Progress?> = MutableStateFlow(value = null)
    private val _navigationDrawerAction: MutableStateFlow<Boolean?> = MutableStateFlow(value = null)

    override val alertActions: StateFlow<Alert?> = _alertActions.asStateFlow()
    override val appSettingsActions: StateFlow<Boolean?> = _appSettingsActions.asStateFlow()
    override val datePickerActions: StateFlow<DatePickerInfo?> = _datePickerActions.asStateFlow()
    override val emailActions: StateFlow<Boolean?> = _emailActions.asStateFlow()
    override val finishActions: StateFlow<Boolean?> = _finishActions.asStateFlow()
    override val inputInfoActions: StateFlow<InputInfo?> = _inputInfoActions.asStateFlow()
    override val popRouteActions: StateFlow<String?> = _popRouteActions.asStateFlow()
    override val navActions: StateFlow<NavigationAction?> = _navActions.asStateFlow()
    override val progressActions: StateFlow<Progress?> = _progressActions.asStateFlow()
    override val navigationDrawerAction: StateFlow<Boolean?> = _navigationDrawerAction.asStateFlow()

    override fun alert(alertAction: Alert?) = _alertActions.update { alertAction }

    override fun appSettings(appSettingsAction: Boolean?) = _appSettingsActions.update { appSettingsAction }

    override fun datePicker(datePickerAction: DatePickerInfo?) = _datePickerActions.update { datePickerAction }

    override fun emailAction(emailAction: Boolean?) = _emailActions.update { emailAction }
    override fun inputInfo(inputInfoAction: InputInfo?) = _inputInfoActions.update { inputInfoAction }

    override fun finish(finishAction: Boolean?) = _finishActions.update { finishAction }

    override fun navigate(navigationAction: NavigationAction?) = _navActions.update { navigationAction }

    override fun pop(popRouteAction: String?) {
        _popRouteActions.update { popRouteAction }
    }

    override fun progress(progressAction: Progress?) = _progressActions.update { progressAction }

    override fun showNavigationDrawer(navigationDrawerAction: Boolean?) = _navigationDrawerAction.update { navigationDrawerAction }
}
