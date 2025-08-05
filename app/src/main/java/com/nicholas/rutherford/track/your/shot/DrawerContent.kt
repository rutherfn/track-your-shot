package com.nicholas.rutherford.track.your.shot

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavOptions
import com.nicholas.rutherford.track.your.shot.base.resources.R
import com.nicholas.rutherford.track.your.shot.helper.extensions.safeLet
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles
import com.nicholas.rutherford.track.your.shot.navigation.DrawerAction
import com.nicholas.rutherford.track.your.shot.navigation.PlayersListAction
import com.nicholas.rutherford.track.your.shot.navigation.ReportingAction
import com.nicholas.rutherford.track.your.shot.navigation.SettingsAction

@Composable
fun DrawerContent(
    actions: List<DrawerAction>,
    modifier: Modifier = Modifier,
    onDestinationClicked: (route: String, navOptions: NavOptions, titleId: Int) -> Unit?
) {
    TrackYourShotTheme {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(AppColors.White)
                .padding(horizontal = 24.dp, vertical = 32.dp)
        ) {
            Image(
                painter = painterResource(R.mipmap.ic_launcher_round),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(72.dp)
                    .align(Alignment.Start)
            )

            Spacer(Modifier.height(16.dp))
            HorizontalDivider()

            Spacer(Modifier.height(16.dp))

            Text(
                text = stringResource(id = R.string.track_your_shot),
                style = TextStyles.subLarge
            )
            Text(
                text = stringResource(id = R.string.by_nicholas_rutherford),
                style = TextStyles.smallBold
            )

            Spacer(Modifier.height(24.dp))

            Text(
                text = stringResource(id = R.string.menu),
                style = TextStyles.subLarge
            )

            Spacer(Modifier.height(12.dp))

            actions.forEach { action ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            safeLet(action.route, action.navOptions, action.titleId) { route, navOptions, titleId ->
                                onDestinationClicked(route, navOptions, titleId)
                            } ?: run {
                                onDestinationClicked("", NavOptions.Builder().build(), action.titleId)
                            }
                        }
                        .padding(vertical = 12.dp)
                ) {
                    Icon(
                        imageVector = action.imageVector,
                        contentDescription = null,
                        tint = LocalContentColor.current
                    )
                    Spacer(Modifier.width(16.dp))
                    Text(
                        text = stringResource(id = action.titleId),
                        style = TextStyles.small
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun DrawerContentPreview() {
    val actions = listOf(PlayersListAction, ReportingAction, SettingsAction)

    Column(modifier = Modifier.background(AppColors.White)) {
        DrawerContent(
            actions = actions,
            onDestinationClicked = { _, _, _ -> }
        )
    }
}
