package com.nicholas.rutherford.track.your.shot.feature.settings

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Represents the UI state for the Settings screen.
 *
 * @property generalSettings A list of labels representing general settings options (e.g., terms, account info).
 * @property permissionSettings A list of labels representing permission-related settings (e.g., camera, media access)
 * @property debugSettings A list of labels representing debug-related settings (e.g., delete account, firebase viewer).
 */
data class SettingsState(
    val generalSettings: List<String> = emptyList(),
    val permissionSettings: List<String> = emptyList(),
    val debugSettings: List<String> = emptyList()
)
