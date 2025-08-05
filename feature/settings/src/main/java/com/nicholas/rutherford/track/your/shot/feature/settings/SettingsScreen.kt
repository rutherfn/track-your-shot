package com.nicholas.rutherford.track.your.shot.feature.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.your.shot.AppColors
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.compose.components.BaseRow
import com.nicholas.rutherford.track.your.shot.helper.ui.Padding
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles

/**
 * Displays the Settings screen which includes general settings and permission settings categorized.
 *
 * @param params Contains the current settings state and click callbacks.
 */
@Composable
fun SettingsScreen(params: SettingsParams) {
    SettingsContent(
        settingGeneralValues = params.state.generalSettings,
        settingPermissionsValues = params.state.permissionSettings,
        onSettingItemClicked = params.onSettingItemClicked
    )
}

/**
 * Displays the content of the settings screen including both general and permission sections.
 *
 * @param settingGeneralValues List of general setting labels.
 * @param settingPermissionsValues List of permission setting labels.
 * @param onSettingItemClicked Callback invoked when a setting item is clicked.
 */
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

/**
 * Displays a card containing a list of setting items.
 *
 * @param values A list of setting labels to display in a vertical list.
 * @param onSettingItemClicked Callback invoked when a setting item is selected.
 */
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
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AppColors.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
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

/**
 * Previews the Settings screen with sample data for both general and permission sections.
 */
@Preview
@Composable
fun SettingsScreenPreview() {
    Column(modifier = Modifier.background(Color.White)) {
        SettingsScreen(
            params = SettingsParams(
                onToolbarMenuClicked = {},
                onHelpClicked = {},
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

