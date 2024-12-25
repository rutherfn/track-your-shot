package com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.ChevronRight
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nicholas.rutherford.track.your.shot.AppColors
import com.nicholas.rutherford.track.your.shot.base.resources.Colors
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.compose.components.BaseRow
import com.nicholas.rutherford.track.your.shot.compose.components.Content
import com.nicholas.rutherford.track.your.shot.data.shared.appbar.AppBar
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
            shouldShowSecondaryButton = true,
            onIconButtonClicked = { logShotParams.onBackButtonClicked.invoke() },
            onSecondaryIconButtonClicked = { logShotParams.onSaveClicked.invoke() }
        )
    )
}

@Composable
fun LogShotContent(logShotParams: LogShotParams) {
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
        ShotInfoContent(
            state = logShotParams.state,
            onDateShotsTakenClicked = logShotParams.onDateShotsTakenClicked,
            onShotsMadeClicked = logShotParams.onShotsMadeClicked,
            onShotsMissedClicked = logShotParams.onShotsMissedClicked
        )
        PlayerInfoContent(state = logShotParams.state)

        if (logShotParams.state.deleteShotButtonVisible) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(AppColors.White),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = { logShotParams.onDeleteShotClicked.invoke() },
                        shape = RoundedCornerShape(size = 50.dp),
                        modifier = Modifier
                            .padding(vertical = Padding.twelve)
                            .fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Colors.secondaryColor)
                    ) {
                        Text(
                            text = stringResource(id = StringsIds.deleteX, logShotParams.state.shotName),
                            style = TextStyles.smallBold,
                            textAlign = TextAlign.Center,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ShotInfoContent(
    state: LogShotState,
    onDateShotsTakenClicked: () -> Unit,
    onShotsMadeClicked: () -> Unit,
    onShotsMissedClicked: () -> Unit
) {
    var isPlayerShotInfoExpanded by remember { mutableStateOf(value = false) }
    var playerShotInfoImageVector by remember { mutableStateOf(value = Icons.Filled.ExpandMore) }

    Card(
        modifier = Modifier
            .background(AppColors.White)
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp),
        elevation = 2.dp
    ) {
        Column {
            BaseRow(
                title = state.shotName,
                imageVector = playerShotInfoImageVector,
                onClicked = {
                    isPlayerShotInfoExpanded = !isPlayerShotInfoExpanded
                    playerShotInfoImageVector = if (isPlayerShotInfoExpanded) {
                        Icons.Filled.ExpandLess
                    } else {
                        Icons.Filled.ExpandMore
                    }
                }
            )

            if (isPlayerShotInfoExpanded) {
                BaseRow(
                    title = stringResource(id = StringsIds.dateShotsLogged),
                    onClicked = null,
                    subText = state.shotsLoggedDateValue,
                    subTextColor = AppColors.LightGray,
                    titleStyle = TextStyles.small.copy(color = AppColors.LightGray, fontSize = 16.sp),
                    imageVector = null,
                    shouldShowDivider = true
                )

                BaseRow(
                    title = stringResource(id = StringsIds.dateShotsTaken),
                    onClicked = { onDateShotsTakenClicked.invoke() },
                    titleStyle = TextStyles.small.copy(fontSize = 16.sp),
                    iconTint = Color.Blue,
                    subText = state.shotsTakenDateValue,
                    imageVector = Icons.Filled.CalendarToday,
                    shouldShowDivider = true
                )

                BaseRow(
                    title = stringResource(id = StringsIds.shotsMade),
                    onClicked = { onShotsMadeClicked.invoke() },
                    titleStyle = TextStyles.small.copy(fontSize = 16.sp),
                    iconTint = Color.Blue,
                    subText = state.shotsMade.toString(),
                    imageVector = Icons.Filled.ChevronRight,
                    shouldShowDivider = true
                )

                BaseRow(
                    title = stringResource(id = StringsIds.shotsMissed),
                    onClicked = { onShotsMissedClicked.invoke() },
                    titleStyle = TextStyles.small.copy(fontSize = 16.sp),
                    iconTint = Color.Blue,
                    subText = state.shotsMissed.toString(),
                    imageVector = Icons.Filled.ChevronRight,
                    shouldShowDivider = true
                )

                BaseRow(
                    title = stringResource(id = StringsIds.shotsAttempted),
                    onClicked = null,
                    subText = state.shotsAttempted.toString(),
                    subTextColor = AppColors.LightGray,
                    titleStyle = TextStyles.small.copy(color = AppColors.LightGray, fontSize = 16.sp),
                    imageVector = null,
                    shouldShowDivider = true
                )

                BaseRow(
                    title = stringResource(id = StringsIds.shotsMadePercentage),
                    onClicked = null,
                    subText = state.shotsMadePercentValue,
                    subTextColor = AppColors.LightGray,
                    titleStyle = TextStyles.small.copy(color = AppColors.LightGray, fontSize = 16.sp),
                    imageVector = null,
                    shouldShowDivider = true
                )

                BaseRow(
                    title = stringResource(id = StringsIds.shotsMissedPercentage),
                    onClicked = null,
                    subText = state.shotsMissedPercentValue,
                    subTextColor = AppColors.LightGray,
                    titleStyle = TextStyles.small.copy(color = AppColors.LightGray, fontSize = 16.sp),
                    imageVector = null,
                    shouldShowDivider = true
                )
            }
        }
    }
}

@Composable
fun PlayerInfoContent(
    state: LogShotState
) {
    var isPlayerInfoExpanded by remember { mutableStateOf(value = false) }
    var playerInfoImageVector by remember { mutableStateOf(value = Icons.Filled.ExpandMore) }

    Card(
        modifier = Modifier
            .background(AppColors.White)
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp),
        elevation = 2.dp
    ) {
        Column {
            BaseRow(
                title = state.playerName,
                imageVector = playerInfoImageVector,
                onClicked = {
                    isPlayerInfoExpanded = !isPlayerInfoExpanded
                    playerInfoImageVector = if (isPlayerInfoExpanded) {
                        Icons.Filled.ExpandLess
                    } else {
                        Icons.Filled.ExpandMore
                    }
                }
            )

            if (isPlayerInfoExpanded) {
                val subTextId = if (state.playerPosition == 0) {
                    StringsIds.empty
                } else {
                    state.playerPosition
                }
                BaseRow(
                    title = stringResource(id = StringsIds.position),
                    onClicked = null,
                    subText = stringResource(id = subTextId),
                    subTextColor = AppColors.LightGray,
                    titleStyle = TextStyles.small.copy(color = AppColors.LightGray, fontSize = 16.sp),
                    imageVector = null,
                    shouldShowDivider = true
                )
            }
        }
    }
}
