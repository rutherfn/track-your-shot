package com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.nicholas.rutherford.track.your.shot.compose.components.NumericRowStepper
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Entry point composable for the Log Shot screen.
 *
 * Wraps [LogShotContent] and intercepts the system back button to call the provided
 * [LogShotParams.onBackButtonClicked] callback.
 *
 * @param logShotParams The state and callbacks necessary to render and interact with the screen.
 */
@Composable
fun LogShotScreen(logShotParams: LogShotParams) {
    BackHandler(true) { logShotParams.onBackButtonClicked() }
    LogShotContent(logShotParams = logShotParams)
}

/**
 * Main layout container for logging a shot.
 *
 * Displays both shot and player information with interactive controls to modify
 * shot counts, view details, and delete the shot (if applicable).
 *
 * @param logShotParams Contains UI state and all user interaction callbacks.
 */
@Composable
fun LogShotContent(logShotParams: LogShotParams) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ShotInfoContent(
            state = logShotParams.state,
            onDateShotsTakenClicked = logShotParams.onDateShotsTakenClicked,
            onShotsMadeUpwardClicked = logShotParams.onShotsMadeUpwardClicked,
            onShotsMadeDownwardClicked = logShotParams.onShotsMadeDownwardClicked,
            onShotsMissedUpwardClicked = logShotParams.onShotsMissedUpwardClicked,
            onShotsMissedDownwardClicked = logShotParams.onShotsMissedDownwardClicked
        )

        PlayerInfoContent(state = logShotParams.state)

        if (logShotParams.state.deleteShotButtonVisible) {
            Button(
                onClick = { logShotParams.onDeleteShotClicked() },
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Colors.secondaryColor)
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

/**
 * Displays a collapsible card with all shot-related data such as date, counts, and percentages.
 *
 * Provides interactive numeric steppers for shot made/missed values and shows derived metrics
 * such as attempts and percentages.
 *
 * @param state Current UI state for shot logging.
 * @param onDateShotsTakenClicked Callback when the shot taken date is clicked.
 * @param onShotsMadeUpwardClicked Called when incrementing made shots.
 * @param onShotsMadeDownwardClicked Called when decrementing made shots.
 * @param onShotsMissedUpwardClicked Called when incrementing missed shots.
 * @param onShotsMissedDownwardClicked Called when decrementing missed shots.
 */
@Composable
private fun ShotInfoContent(
    state: LogShotState,
    onDateShotsTakenClicked: () -> Unit,
    onShotsMadeUpwardClicked: (Int) -> Unit,
    onShotsMadeDownwardClicked: (Int) -> Unit,
    onShotsMissedUpwardClicked: (Int) -> Unit,
    onShotsMissedDownwardClicked: (Int) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.background(AppColors.White)) {
            BaseRow(
                title = state.shotName,
                imageVector = if (isExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                onClicked = { isExpanded = !isExpanded }
            )

            if (isExpanded) {
                BaseRow(
                    title = stringResource(id = StringsIds.dateShotsLogged),
                    subText = state.shotsLoggedDateValue,
                    subTextColor = AppColors.LightGray,
                    titleStyle = TextStyles.small.copy(color = AppColors.LightGray, fontSize = 16.sp),
                    shouldShowDivider = true
                )

                BaseRow(
                    title = stringResource(id = StringsIds.dateShotsTaken),
                    onClicked = onDateShotsTakenClicked,
                    subText = state.shotsTakenDateValue,
                    titleStyle = TextStyles.small.copy(fontSize = 16.sp),
                    iconTint = Color.Blue,
                    imageVector = Icons.Filled.CalendarToday,
                    shouldShowDivider = true
                )

                NumericRowStepper(
                    title = stringResource(id = StringsIds.shotsMade),
                    onUpwardClicked = onShotsMadeUpwardClicked,
                    onDownwardClicked = onShotsMadeDownwardClicked,
                    titleStyle = TextStyles.small.copy(fontSize = 16.sp),
                    shouldShowDivider = true,
                    defaultValue = state.shotsMade
                )

                NumericRowStepper(
                    title = stringResource(id = StringsIds.shotsMissed),
                    onUpwardClicked = onShotsMissedUpwardClicked,
                    onDownwardClicked = onShotsMissedDownwardClicked,
                    titleStyle = TextStyles.small.copy(fontSize = 16.sp),
                    shouldShowDivider = true,
                    defaultValue = state.shotsMissed
                )

                BaseRow(
                    title = stringResource(id = StringsIds.shotsAttempted),
                    subText = state.shotsAttempted.toString(),
                    subTextColor = AppColors.LightGray,
                    titleStyle = TextStyles.small.copy(color = AppColors.LightGray, fontSize = 16.sp),
                    shouldShowDivider = true
                )

                BaseRow(
                    title = stringResource(id = StringsIds.shotsMadePercentage),
                    subText = state.shotsMadePercentValue,
                    subTextColor = AppColors.LightGray,
                    titleStyle = TextStyles.small.copy(color = AppColors.LightGray, fontSize = 16.sp),
                    shouldShowDivider = true
                )

                BaseRow(
                    title = stringResource(id = StringsIds.shotsMissedPercentage),
                    subText = state.shotsMissedPercentValue,
                    subTextColor = AppColors.LightGray,
                    titleStyle = TextStyles.small.copy(color = AppColors.LightGray, fontSize = 16.sp),
                    shouldShowDivider = false
                )
            }
        }
    }
}

/**
 * Displays player information within a collapsible card.
 *
 * Initially shows only the player name. Expanding reveals their position,
 * if it's defined in the [LogShotState].
 *
 * @param state Current UI state for the shot being logged.
 */
@Composable
fun PlayerInfoContent(state: LogShotState) {
    var isExpanded by remember { mutableStateOf(false) }
    val icon = if (isExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppColors.White)
            .padding(vertical = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.background(AppColors.White)) {
            BaseRow(
                title = state.playerName,
                imageVector = icon,
                onClicked = { isExpanded = !isExpanded }
            )

            if (isExpanded) {
                val positionText = if (state.playerPosition == 0) "" else stringResource(id = state.playerPosition)
                BaseRow(
                    title = stringResource(id = StringsIds.position),
                    subText = positionText,
                    subTextColor = AppColors.LightGray,
                    titleStyle = TextStyles.small.copy(color = AppColors.LightGray, fontSize = 16.sp),
                    shouldShowDivider = false
                )
            }
        }
    }
}
