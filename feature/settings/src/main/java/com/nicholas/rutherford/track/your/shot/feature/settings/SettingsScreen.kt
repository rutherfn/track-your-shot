package com.nicholas.rutherford.track.your.shot.feature.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Help
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.your.shot.AppColors
import com.nicholas.rutherford.track.your.shot.base.resources.R
import com.nicholas.rutherford.track.your.shot.compose.components.BaseRow
import com.nicholas.rutherford.track.your.shot.compose.components.Content
import com.nicholas.rutherford.track.your.shot.data.shared.appbar.AppBar
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.helper.ui.Padding
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles

@Composable
fun SettingsScreen(params: SettingsParams) {
    Content(
        ui = {
            SettingsContent(
                settingGeneralValues = params.state.generalSettings,
                settingPermissionsValues = params.state.permissionSettings,
                onSettingItemClicked = params.onSettingItemClicked
            )
        },
        appBar = AppBar(
            toolbarTitle = stringResource(id = R.string.settings),
            shouldShowMiddleContentAppBar = true,
            onIconButtonClicked = {
                params.onToolbarMenuClicked.invoke()
            }
        ),
        secondaryImageVector = Icons.Filled.Help
    )
}

@Composable
private fun SettingsContent(
    settingGeneralValues: List<String>,
    settingPermissionsValues: List<String>,
    onSettingItemClicked: (value: String) -> Unit
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
        Spacer(modifier = Modifier.height(Padding.sixteen))

        Text(
            text = stringResource(id = StringsIds.general),
            style = TextStyles.smallBold
        )

        Spacer(modifier = Modifier.height(Padding.eight))

        SettingsRowCard(
            values = settingGeneralValues,
            onSettingItemClicked = onSettingItemClicked
        )

        Spacer(modifier = Modifier.height(Padding.sixteen))

        Text(
            text = stringResource(id = StringsIds.permissions),
            style = TextStyles.smallBold
        )

        Spacer(modifier = Modifier.height(Padding.eight))

        SettingsRowCard(
            values = settingPermissionsValues,
            onSettingItemClicked = onSettingItemClicked
        )
    }
}

@Composable
private fun SettingsRowCard(
    values: List<String>,
    onSettingItemClicked: (value: String) -> Unit
) {
    Card(
        modifier = Modifier
            .background(AppColors.White)
            .fillMaxWidth()
            .padding(top = Padding.eight, bottom = Padding.eight),
        elevation = 2.dp
    ) {
        Column {
            values.forEach { value ->
                BaseRow(
                    title = value,
                    titleStyle = TextStyles.bodyBold,
                    onClicked = { onSettingItemClicked.invoke(value) },
                    imageVector = Icons.Filled.ChevronRight
                )
            }
        }
    }
}

@Preview
@Composable
fun SettingsScreenPreview() {
    Column(modifier = Modifier.background(Color.White)) {
        SettingsScreen(
            params = SettingsParams(
                onToolbarMenuClicked = {},
                onSettingItemClicked = {},
                state = SettingsState(
                    generalSettings = listOf(
                        stringResource(StringsIds.usingTheApp),
                        stringResource(StringsIds.termsConditions),
                        stringResource(StringsIds.accountInfo)
                    ),
                    permissionSettings = listOf(
                        stringResource(StringsIds.camera),
                        stringResource(StringsIds.readMediaStorage)
                    )
                )
            )
        )
    }
}
