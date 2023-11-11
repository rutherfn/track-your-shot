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
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
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
import com.nicholas.rutherford.track.your.shot.navigation.SettingsAction

@Composable
fun DrawerContent(
    actions: List<DrawerAction>,
    modifier: Modifier = Modifier,
    onDestinationClicked: (route: String, navOptions: NavOptions) -> Unit?,
    onItemClicked: (titleId: Int) -> Unit?,
) {
    TrackMyShotTheme {
        Column(
            modifier
                .fillMaxSize()
                .padding(start = 24.dp, top = 48.dp)
        ) {
            Image(
                painterResource(R.mipmap.ic_launcher_round),
                contentDescription = "",
                contentScale = ContentScale.Fit
            )

            Spacer(Modifier.height(24.dp))
            Divider()

            Text(
                text = stringResource(id = R.string.track_your_shot),
                style = TextStyles.subLarge,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = stringResource(id = R.string.by_nicholas_rutherford),
                style = TextStyles.smallBold,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = stringResource(id = R.string.menu),
                style = TextStyles.subLarge,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(4.dp))

            actions.forEach { action ->
                Spacer(Modifier.height(12.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, end = 8.dp, bottom = 8.dp)
                        .clickable {
                            onItemClicked.invoke(action.titleId)
                            safeLet(action.route, action.navOptions) { route, navOptions ->
                                onDestinationClicked(route, navOptions)
                            }
                        }
                ) {
                    Icon(
                        imageVector = action.imageVector,
                        contentDescription = "Icon"
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
    val actions = listOf(PlayersListAction, SettingsAction)

    Column(modifier = Modifier.background(AppColors.White)) {
        DrawerContent(
            actions = actions,
            onDestinationClicked = { _, _ -> },
            onItemClicked = {}
        )
    }
}
