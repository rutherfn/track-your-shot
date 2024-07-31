package com.nicholas.rutherford.track.your.shot.feature.settings

import androidx.lifecycle.ViewModel

class SettingsViewModel(
    private val navigation: SettingsNavigation
) : ViewModel() {

    fun onToolbarMenuClicked() = navigation.openNavigationDrawer()
}