package com.nicholas.rutherford.track.your.shot.feature.settings.enabledpermissions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.nicholas.rutherford.track.your.shot.compose.components.Content
import com.nicholas.rutherford.track.your.shot.compose.components.SwitchRow
import com.nicholas.rutherford.track.your.shot.data.shared.appbar.AppBar
import com.nicholas.rutherford.track.your.shot.feature.splash.StringsIds
import com.nicholas.rutherford.track.your.shot.helper.extensions.readMediaImagesOrExternalStoragePermission
import com.nicholas.rutherford.track.your.shot.helper.ui.Padding

@Composable
fun EnabledPermissionsScreen(params: EnabledPermissionsParams) {
    Content(
        ui = { EnabledPermissionsContent() },
        appBar = AppBar(
            toolbarTitle = stringResource(id = StringsIds.enabledPermissions),
            onIconButtonClicked = { params.onToolbarMenuClicked.invoke()  }
        ),
        imageVector = Icons.Filled.ArrowBack
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun EnabledPermissionsContent() {
    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)
    val mediaImagesOrExternalStorage = rememberPermissionState(permission = readMediaImagesOrExternalStoragePermission())

    var hasMediaImagesOrExternalStorage by remember { mutableStateOf(mediaImagesOrExternalStorage.status.isGranted) }
    var hasCameraPermission by remember { mutableStateOf(cameraPermissionState.status.isGranted) }

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

        SwitchRow(
            state = hasMediaImagesOrExternalStorage,
            title = stringResource(id = StringsIds.readMediaImagesPermission),
            onSwitchChanged = { value -> hasMediaImagesOrExternalStorage = value }
        )

        Spacer(modifier = Modifier.height(Padding.eight))

        SwitchRow(
            state = hasCameraPermission,
            title = stringResource(id = StringsIds.cameraPermission),
            onSwitchChanged = { value -> hasCameraPermission = value }
        )
    }
}