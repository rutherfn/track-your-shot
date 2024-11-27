package com.nicholas.rutherford.track.your.shot.feature.settings.enabledpermissions

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.nicholas.rutherford.track.your.shot.compose.components.Content
import com.nicholas.rutherford.track.your.shot.compose.components.SwitchCard
import com.nicholas.rutherford.track.your.shot.data.shared.appbar.AppBar
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.helper.extensions.hasCameraPermissionEnabled
import com.nicholas.rutherford.track.your.shot.helper.extensions.hasReadImagePermissionEnabled
import com.nicholas.rutherford.track.your.shot.helper.extensions.readMediaImagesOrExternalStoragePermission
import com.nicholas.rutherford.track.your.shot.helper.ui.Padding

@Composable
fun EnabledPermissionsScreen(params: EnabledPermissionsParams) {
    Content(
        ui = { EnabledPermissionsContent(params = params) },
        appBar = AppBar(
            toolbarTitle = stringResource(id = StringsIds.enabledPermissions),
            onIconButtonClicked = { params.onToolbarMenuClicked.invoke() }
        ),
        imageVector = Icons.Filled.ArrowBack
    )
}

@Composable
fun EnabledPermissionsContent(params: EnabledPermissionsParams) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val context = LocalContext.current
    var hasMediaImagesOrExternalStorage by remember { mutableStateOf(hasReadImagePermissionEnabled(context = context)) }
    var hasCameraPermission by remember { mutableStateOf(hasCameraPermissionEnabled(context = context)) }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            hasCameraPermission = isGranted
        }
    )
    val readStorageOrMediaImagesPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            hasMediaImagesOrExternalStorage = isGranted
        }
    )

    DisposableEffect(lifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                hasMediaImagesOrExternalStorage = hasReadImagePermissionEnabled(context = context)
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
        Spacer(modifier = Modifier.height(Padding.sixteen))

        SwitchCard(
            state = hasMediaImagesOrExternalStorage,
            title = stringResource(id = StringsIds.readMediaImagesPermission),
            onSwitchChanged = { switch ->
                if (switch) {
                    readStorageOrMediaImagesPermissionLauncher.launch(readMediaImagesOrExternalStoragePermission())
                } else {
                    params.onSwitchChangedToTurnOffPermission.invoke()
                }
            }
        )

        Spacer(modifier = Modifier.height(Padding.eight))

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
