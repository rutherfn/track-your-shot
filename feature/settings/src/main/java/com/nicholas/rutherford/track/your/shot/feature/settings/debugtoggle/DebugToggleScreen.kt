package com.nicholas.rutherford.track.your.shot.feature.settings.debugtoggle

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.nicholas.rutherford.track.your.shot.compose.components.SwitchCard
import com.nicholas.rutherford.track.your.shot.helper.ui.Padding

/**
 * Created by Nicholas Rutherford, last edited on 2025-09-29
 *
 * Main composable screen for debug toggle settings. This screen provides a user interface
 * for toggling debug-related features such as voice commands and video upload functionality.
 * 
 * The screen includes back button handling and displays two toggle switches for:
 * - Voice command debugging
 * - Video upload debugging
 *
 * @param params [DebugToggleParams] containing the current state and callback functions
 *               for handling user interactions and navigation
 */
@Composable
fun DebugToggleScreen(params: DebugToggleParams) {
    BackHandler(enabled = true) { params.onToolbarMenuClicked.invoke() }
    DebugToggleContent(
        state = params.state,
        onVoiceDebugToggled = params.onVoiceDebugToggled,
        onVideoUploadDebugToggled = params.onVideoUploadDebugToggled
    )
}

/**
 * Created by Nicholas Rutherford, last edited on 2025-09-29
 *
 * Content composable for the debug toggle screen. This composable displays the actual
 * UI elements including the toggle switches for debug features.
 *
 * The content is scrollable and includes proper padding for a clean layout.
 *
 * @param state [DebugToggleState] containing the current state of both debug toggles
 * @param onVoiceDebugToggled Callback invoked when the voice debug toggle is changed
 * @param onVideoUploadDebugToggled Callback invoked when the video upload debug toggle is changed
 */
@Composable
fun DebugToggleContent(
    state: DebugToggleState,
    onVoiceDebugToggled: (value: Boolean) -> Unit,
    onVideoUploadDebugToggled: (value: Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(
                start = Padding.sixteen,
                end = Padding.sixteen,
                bottom = Padding.sixteen
            ),
        horizontalAlignment = Alignment.Start
    ) {
        SwitchCard(
            state = state.voiceToggledState,
            title = "Enabled Voice Commands",
            onSwitchChanged = onVoiceDebugToggled
        )
        SwitchCard(
            state = state.videoUploadToggleState,
            title = "Enabled Video Upload On Log Shot",
            onSwitchChanged = onVideoUploadDebugToggled
        )
    }
}

/** ------------------- PREVIEWS ------------------- **/

/**
 * Preview of the DebugToggleScreen with both toggles disabled.
 */
@Preview(showBackground = true)
@Composable
fun DebugToggleScreenPreview_BothDisabled() {
    DebugToggleScreen(
        params = DebugToggleParams(
            state = DebugToggleState(
                voiceToggledState = false,
                videoUploadToggleState = false
            ),
            onToolbarMenuClicked = {},
            onVoiceDebugToggled = {},
            onVideoUploadDebugToggled = {}
        )
    )
}

/**
 * Preview of the DebugToggleScreen with both toggles enabled.
 */
@Preview(showBackground = true)
@Composable
fun DebugToggleScreenPreview_BothEnabled() {
    DebugToggleScreen(
        params = DebugToggleParams(
            state = DebugToggleState(
                voiceToggledState = true,
                videoUploadToggleState = true
            ),
            onToolbarMenuClicked = {},
            onVoiceDebugToggled = {},
            onVideoUploadDebugToggled = {}
        )
    )
}

/**
 * Preview of the DebugToggleScreen with mixed toggle states.
 */
@Preview(showBackground = true)
@Composable
fun DebugToggleScreenPreview_MixedStates() {
    DebugToggleScreen(
        params = DebugToggleParams(
            state = DebugToggleState(
                voiceToggledState = true,
                videoUploadToggleState = false
            ),
            onToolbarMenuClicked = {},
            onVoiceDebugToggled = {},
            onVideoUploadDebugToggled = {}
        )
    )
}

/**
 * Preview of the DebugToggleContent composable with both toggles disabled.
 */
@Preview(showBackground = true)
@Composable
fun DebugToggleContentPreview_BothDisabled() {
    Column(modifier = Modifier.background(Color.White)) {
        DebugToggleContent(
            state = DebugToggleState(
                voiceToggledState = false,
                videoUploadToggleState = false
            ),
            onVoiceDebugToggled = {},
            onVideoUploadDebugToggled = {}
        )
    }
}

/**
 * Preview of the DebugToggleContent composable with both toggles enabled.
 */
@Preview(showBackground = true)
@Composable
fun DebugToggleContentPreview_BothEnabled() {
    Column(modifier = Modifier.background(Color.White)) {
        DebugToggleContent(
            state = DebugToggleState(
                voiceToggledState = true,
                videoUploadToggleState = true
            ),
            onVoiceDebugToggled = {},
            onVideoUploadDebugToggled = {}
        )
    }
}