package com.nicholas.rutherford.track.your.shot.feature.settings

data class SettingsParams(
    val onToolbarMenuClicked: () -> Unit,
    val onSettingItemClicked: (value: String) -> Unit,
    val state: SettingsState
)