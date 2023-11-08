package com.nicholas.rutherford.track.my.shot

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavOptions
import com.nicholas.rutherford.track.my.shot.base.resources.R
import com.nicholas.rutherford.track.my.shot.navigation.DrawerScreens

@Composable
fun DrawerContent(
    screens: List<DrawerScreens>,
    modifier: Modifier = Modifier,
    onDestinationClicked: (route: String, navOptions: NavOptions) -> Unit
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

            screens.forEach { screen ->
                Spacer(Modifier.height(24.dp))
                Text(
                    text = screen.title,
                    style = MaterialTheme.typography.h4,
                    modifier = Modifier.clickable {
                        onDestinationClicked(screen.route, screen.navOptions)
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun DrawerContentPreview() {
    val screens = listOf(DrawerScreens.PlayersList, DrawerScreens.Settings)

    Column(modifier = Modifier.background(AppColors.White)) {
        DrawerContent(
            screens = screens,
            onDestinationClicked = { _, _ -> }
        )
    }
}
