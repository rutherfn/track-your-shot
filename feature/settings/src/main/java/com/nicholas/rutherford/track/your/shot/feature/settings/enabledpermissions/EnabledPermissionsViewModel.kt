package com.nicholas.rutherford.track.your.shot.feature.settings.enabledpermissions

import androidx.lifecycle.ViewModel
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import com.nicholas.rutherford.track.your.shot.navigation.Navigator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class EnabledPermissionsViewModel(
    private val navigator: Navigator
) : ViewModel() {

    internal val enabledPermissionMutableStateFlow = MutableStateFlow(value = EnabledPermissionsState())
    val enabledPermissionStateFlow = enabledPermissionMutableStateFlow.asStateFlow()

    fun onBackButtonClicked() = navigator.pop(popRouteAction = Constants.POP_DEFAULT_ACTION)
}