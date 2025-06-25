package com.nicholas.rutherford.track.your.shot.compose.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.your.shot.AppColors
import com.nicholas.rutherford.track.your.shot.TrackMyShotTheme
import com.nicholas.rutherford.track.your.shot.base.resources.R
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.data.shared.appbar.AppBar
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles

/**
 * Default Content with optional back [TopAppBar]. Used for default content views inside of [Composable]
 * It uses [TrackMyShotTheme] to set theming of the content block
 *
 * @param ui used to set body of the interface below the optional [TopAppBar] via a [Composable]
 * @param appBar optional param that is responsible for creating a [TopAppBar] with set properties if not null
 * @param imageVector optional param that will set a new default [ImageVector] if not null to the [TopAppBar]
 * @param [imageVector] optional param that will set a [ImageVector] to the [appBar] if not null
 * If the [imageVector] is null then go ahead and set a preset image vector
 * @param [secondaryImageVector] optional param that will set a [ImageVector] to the right inside of the [appBar] if not null
 * @param [secondaryImageEnabled] optional param that will set if [secondaryImageVector] [Icon] will be enabled or nbot
 * @param invokeFunctionOnInit optional param that will invoke a function on the [Content] function invoke state
 */
@Composable
fun Content(
    ui: @Composable () -> Unit,
    appBar: AppBar? = null,
    imageVector: ImageVector? = null,
    secondaryImageVector: ImageVector? = null,
    secondaryImageEnabled: Boolean? = null,
    invokeFunctionOnInit: (() -> Unit?)? = null,
    secondaryIconTint: Color = AppColors.White
) {
    TrackMyShotTheme {
        Column(
           // modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            invokeFunctionOnInit?.invoke()

            if (appBar != null) {
                ConditionalTopAppBar(
                    appBar = appBar,
                    imageVector = imageVector,
                    secondaryImageVector = secondaryImageVector,
                    secondaryImageEnabled = secondaryImageEnabled,
                    secondaryIconTint = secondaryIconTint
                )
            }

            ui.invoke()
        }
    }
}

@Composable
private fun AddNewPlayerEmptyState() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_basketball_player_empty_state),
                contentDescription = null,
                modifier = Modifier.size(120.dp)
            )

            Text(
                text = stringResource(id = StringsIds.noCurrentPlayersAdded),
                style = TextStyles.medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Text(
                text = stringResource(id = StringsIds.hintAddNewPlayer),
                style = TextStyles.smallBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}

@Preview
@Composable
fun ContentPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.White)
            .verticalScroll(rememberScrollState())
            .padding(14.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Content(
            ui = {
                Text(text = "Test")
            }
        )
    }
}
