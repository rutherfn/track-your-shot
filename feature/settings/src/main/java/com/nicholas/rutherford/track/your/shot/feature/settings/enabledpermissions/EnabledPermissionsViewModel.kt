package com.nicholas.rutherford.track.your.shot.feature.settings.enabledpermissions

import androidx.lifecycle.ViewModel

class EnabledPermissionsViewModel(
    private val navigation: EnabledPermissionsNavigation
) : ViewModel() {

    fun onToolbarMenuClicked() = navigation.pop()
}