package com.nicholas.rutherford.track.your.shot.feature.settings.debugtoggle

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-29
 *
 * Parameters used to render the DebugToggleScreen UI.
 *
 * @param state The current state of the debug toggle screen.
 * @param onToolbarMenuClicked A callback to be invoked when the toolbar menu button is clicked.
 * @param onVoiceDebugToggled A callback to be invoked when the voice debug toggle is changed.
 * @param onVideoUploadDebugToggled A callback to be invoked when the video upload debug toggle is changed.
 */
data class DebugToggleParams(
    val state: DebugToggleState,
    val onToolbarMenuClicked: () -> Unit,
    val onVoiceDebugToggled: (value: Boolean) -> Unit,
    val onVideoUploadDebugToggled: (value: Boolean) -> Unit
)
