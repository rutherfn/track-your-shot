package com.nicholas.rutherford.track.your.shot.feature.settings

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Parameters used to render the Settings screen UI.
 *
 * @property onToolbarMenuClicked Callback triggered when the toolbar menu icon is clicked.
 * @property onHelpClicked Callback triggered when the help icon is clicked.
 * @property onSettingItemClicked Callback triggered when a specific setting item is selected. The clicked item's value is passed as a parameter.
 * @property state The current state of the Settings screen, containing all settings data to be displayed.
 */
data class SettingsParams(
    val onToolbarMenuClicked: () -> Unit,
    val onHelpClicked: () -> Unit,
    val onSettingItemClicked: (value: String) -> Unit,
    val state: SettingsState
)
