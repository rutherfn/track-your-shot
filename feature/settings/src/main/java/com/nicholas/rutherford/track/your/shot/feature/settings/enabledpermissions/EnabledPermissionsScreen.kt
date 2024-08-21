package com.nicholas.rutherford.track.your.shot.feature.settings.enabledpermissions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.your.shot.AppColors
import com.nicholas.rutherford.track.your.shot.compose.components.Content
import com.nicholas.rutherford.track.your.shot.compose.components.SwitchRow
import com.nicholas.rutherford.track.your.shot.data.shared.appbar.AppBar
import com.nicholas.rutherford.track.your.shot.feature.splash.StringsIds
import com.nicholas.rutherford.track.your.shot.helper.ui.Padding

@Composable
fun EnabledPermissionsScreen(enabledPermissionsParams: EnabledPermissionsParams) {
    Content(
        ui = {
            EnabledPermissionsScreenUi(enabledPermissionsParams = enabledPermissionsParams)
        },
        appBar = AppBar(
            toolbarTitle = stringResource(id = StringsIds.enabledPermissions),
            shouldShowMiddleContentAppBar = false,
            shouldIncludeSpaceAfterDeclaration = false,
            shouldShowSecondaryButton = false,
            onIconButtonClicked = { enabledPermissionsParams.onBackButtonClicked.invoke() }
        )
    )
}

@Composable
private fun EnabledPermissionsScreenUi(enabledPermissionsParams: EnabledPermissionsParams) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .background(AppColors.White)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            elevation = 2.dp
        ) {
            Column {
            SwitchRow(
                enabled = false,
                title = stringResource(id = enabledPermissionsParams.state.mediaOrExternalStorageStringId),
                onSwitchChanged = {}
            )
                }
        }

        Card(
            modifier = Modifier
                .background(AppColors.White)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            elevation = 2.dp
        ) {
            Column {
                SwitchRow(
                    enabled = false,
                    title = stringResource(id = enabledPermissionsParams.state.cameraPermissionStringId),
                    onSwitchChanged = {}
                )
            }
        }
    }
}

@Preview
@Composable
private fun EnabledPermissionsScreenPreview() {
    Column(modifier = Modifier.background(Color.White)) {
        EnabledPermissionsScreen(
            enabledPermissionsParams = EnabledPermissionsParams(
                onBackButtonClicked = {},
                state = EnabledPermissionsState()
            )
        )
    }
}