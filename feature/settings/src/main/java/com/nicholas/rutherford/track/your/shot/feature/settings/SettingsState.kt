package com.nicholas.rutherford.track.your.shot.feature.settings

data class SettingsState(
    val generalSettings: List<String> = emptyList(),
    val permissionSettings: List<String> = emptyList()
)
