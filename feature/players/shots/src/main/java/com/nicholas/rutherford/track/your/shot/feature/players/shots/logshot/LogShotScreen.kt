package com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nicholas.rutherford.track.your.shot.AppColors
import com.nicholas.rutherford.track.your.shot.compose.components.Content
import com.nicholas.rutherford.track.your.shot.data.shared.appbar.AppBar
import com.nicholas.rutherford.track.your.shot.feature.splash.StringsIds
import com.nicholas.rutherford.track.your.shot.helper.ui.Padding
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles

@Composable
fun LogShotScreen(logShotParams: LogShotParams) {

    BackHandler(true) {
        logShotParams.onBackButtonClicked.invoke()
    }

    LaunchedEffect(Unit) {
        logShotParams.updateIsExistingPlayerAndPlayerId.invoke()
    }

    Content(
        ui = { LogShotContent(logShotParams = logShotParams) },
        appBar = AppBar(
            toolbarTitle = stringResource(id = StringsIds.logShot),
            shouldShowMiddleContentAppBar = false,
            shouldIncludeSpaceAfterDeclaration = false,
            shouldShowSecondaryButton = false,
            onIconButtonClicked = { logShotParams.onBackButtonClicked.invoke() }
        )
    )
}

@Composable
fun LogShotContent(logShotParams: LogShotParams) {
    ShotInfoContent(
        onDateShotsTakenClicked = logShotParams.onDateShotsTakenClicked
    )
}

@Composable
private fun ShotInfoContent(
    onDateShotsTakenClicked: () -> Unit
) {
    var isPlayerShotInfoExpanded by remember { mutableStateOf(value = false) }
    var playerShotInfoImageVector by remember { mutableStateOf(value = Icons.Filled.ExpandMore) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(
                start = Padding.twenty,
                end = Padding.twenty,
                bottom = Padding.twenty
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Card(
            modifier = Modifier
                .background(AppColors.White)
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 16.dp),
            elevation = 2.dp
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .fillMaxWidth()
                        .clickable {
                            isPlayerShotInfoExpanded = !isPlayerShotInfoExpanded
                            playerShotInfoImageVector = if (isPlayerShotInfoExpanded) {
                                Icons.Filled.ExpandLess
                            } else {
                                Icons.Filled.ExpandMore
                            }
                        }
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Hook Shot",
                        modifier = Modifier.padding(start = 4.dp),
                        style = TextStyles.smallBold
                    )
                    IconButton(
                        onClick = {
                            isPlayerShotInfoExpanded = !isPlayerShotInfoExpanded
                            playerShotInfoImageVector = if (isPlayerShotInfoExpanded) {
                                Icons.Filled.ExpandLess
                            } else {
                                Icons.Filled.ExpandMore
                            }
                        },
                        modifier = Modifier.padding(start = 4.dp),
                    ) {
                        Icon(
                            imageVector = playerShotInfoImageVector,
                            contentDescription = ""
                        )
                    }
                }

                if (isPlayerShotInfoExpanded) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Date Shots Logged",
                            modifier = Modifier.padding(start = 4.dp),
                            style = TextStyles.small.copy(fontSize = 16.sp)
                        )
                        Text(
                            text = "11/12/22",
                            modifier = Modifier.padding(start = 4.dp),
                            style = TextStyles.body,
                            color = AppColors.LightGray
                        )
                    }

                    Divider()

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                onDateShotsTakenClicked.invoke()
                            },
                        horizontalArrangement = Arrangement.SpaceBetween, // Aligns items with space between them
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Date Shots Taken",
                            style = TextStyles.small.copy(fontSize = 16.sp),
                            modifier = Modifier.padding(start = 4.dp)
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "-January 1, 2023",
                                style = TextStyles.body.copy(color = Color.Blue),
                                modifier = Modifier.padding(end = 8.dp)
                            )

                            Icon(
                                imageVector = Icons.Filled.CalendarToday,
                                contentDescription = "",
                                tint = Color.Blue,
                                modifier = Modifier
                                    .size(24.dp)
                                    .padding(end = 4.dp)
                            )
                        }
                    }


                }
            }
        }
    }
}
