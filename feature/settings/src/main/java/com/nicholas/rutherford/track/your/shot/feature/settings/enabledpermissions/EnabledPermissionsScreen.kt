package com.nicholas.rutherford.track.your.shot.feature.settings.enabledpermissions

import android.Manifest
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.compose.components.SwitchCard
import com.nicholas.rutherford.track.your.shot.helper.extensions.hasCameraPermissionEnabled
import com.nicholas.rutherford.track.your.shot.helper.ui.Padding

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Displays the Enabled Permissions screen, where the user can enable or disable camera permission.
 *
 * @param params Contains the state and callback handlers for this screen.
 */
@Composable
fun EnabledPermissionsScreen(params: EnabledPermissionsParams) {
    BackHandler(enabled = true) { params.onToolbarMenuClicked.invoke() }
    EnabledPermissionsContent(params = params)
}

/**
 * Displays an toggle switch to enable or disable camera permission.
 * In the future if we decide to ask for more permissions this is where we can add them.
 */
@Composable
fun EnabledPermissionsContent(params: EnabledPermissionsParams) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val context = LocalContext.current
    var hasCameraPermission by remember { mutableStateOf(hasCameraPermissionEnabled(context = context)) }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            hasCameraPermission = isGranted
        }
    )
    DisposableEffect(lifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                hasCameraPermission = hasCameraPermissionEnabled(context = context)
            }
        }
        lifecycle.addObserver(observer)

        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

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
            state = hasCameraPermission,
            title = stringResource(id = StringsIds.cameraPermission),
            onSwitchChanged = { switch ->
                if (switch) {
                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                } else {
                    params.onSwitchChangedToTurnOffPermission.invoke()
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EnabledPermissionsScreenPreview() {
    EnabledPermissionsScreen(params = EnabledPermissionsParams(onToolbarMenuClicked = {}, onSwitchChangedToTurnOffPermission = {}, permissionNotGrantedForCameraAlert = {}))
}
