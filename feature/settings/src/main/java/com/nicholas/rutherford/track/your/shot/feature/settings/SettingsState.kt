package com.nicholas.rutherford.track.your.shot.feature.settings

/**
 * Represents the UI state for the Settings screen.
 *
 * @property generalSettings A list of labels representing general settings options (e.g., terms, account info).
 * @property permissionSettings A list of labels representing permission-related settings (e.g., camera, media access).
 */
data class SettingsState(
    val generalSettings: List<String> = emptyList(),
    val permissionSettings: List<String> = emptyList()
)

