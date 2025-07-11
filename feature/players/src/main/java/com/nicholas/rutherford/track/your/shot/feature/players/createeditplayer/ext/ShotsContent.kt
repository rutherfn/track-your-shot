package com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer.ext

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.your.shot.AppColors
import com.nicholas.rutherford.track.your.shot.TrackYourShotTheme
import com.nicholas.rutherford.track.your.shot.base.resources.Colors
import com.nicholas.rutherford.track.your.shot.base.resources.R
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.data.room.response.ShotLogged
import com.nicholas.rutherford.track.your.shot.helper.extensions.parseDateValueToString
import com.nicholas.rutherford.track.your.shot.helper.ui.Padding
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles

/**
 * Displays logged shots and pending shots in separate sections,
 * along with a button to log new shots.
 *
 * Shows an empty state UI when both lists are empty.
 *
 * @param shotList List of confirmed logged shots.
 * @param pendingShotList List of pending shots awaiting confirmation.
 * @param hintLogNewShotText Hint text displayed in the empty state.
 * @param onLogShotsClicked Callback triggered when the "Log Shots" button is clicked.
 * @param onViewShotClicked Callback triggered when a logged shot card is clicked.
 *        Provides the shot type and shot ID.
 * @param onPendingShotClicked Callback triggered when a pending shot card is clicked.
 *        Provides the shot type and shot ID.
 */
@Composable
fun ColumnScope.ShotsContent(
    shotList: List<ShotLogged>,
    pendingShotList: List<ShotLogged>,
    hintLogNewShotText: String,
    onLogShotsClicked: () -> Unit,
    onViewShotClicked: (shotType: Int, shotId: Int) -> Unit,
    onPendingShotClicked: (shotType: Int, shotId: Int) -> Unit
) {
    if (shotList.isEmpty() && pendingShotList.isEmpty()) {
        ShotContentEmptyState(
            hintLogNewShotText = hintLogNewShotText,
            onLogShotsClicked = onLogShotsClicked
        )
        return
    }

    if (shotList.isNotEmpty()) {
        Text(
            text = stringResource(id = R.string.shots),
            style = TextStyles.smallBold,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(top = 16.dp, bottom = 8.dp)
        )

        shotList.forEach { shot ->
            LoggedShotCard(
                shot = shot,
                onViewShotClicked = onViewShotClicked
            )
        }
    }

    if (pendingShotList.isNotEmpty()) {
        Text(
            text = stringResource(id = R.string.pending_shots),
            style = TextStyles.smallBold,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(top = 24.dp, bottom = 8.dp)
        )

        pendingShotList.forEach { shot ->
            LoggedShotCard(
                shot = shot,
                onViewShotClicked = onPendingShotClicked
            )
        }
    }

    LogNewShotButton(onLogShotsClicked = onLogShotsClicked)
}

/**
 * Card displaying information about a logged shot.
 *
 * Shows the shot name and the time the shot was logged.
 * Clickable to allow viewing detailed shot info.
 *
 * @param shot The [ShotLogged] data to display.
 * @param onViewShotClicked Callback invoked when this card is clicked,
 *        passing the shot's type and ID.
 */
@Composable
private fun LoggedShotCard(
    shot: ShotLogged,
    onViewShotClicked: (shotType: Int, shotId: Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onViewShotClicked(shot.shotType, shot.id) },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = AppColors.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = shot.shotName,
                    style = TextStyles.smallBold,
                    color = AppColors.Black
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = parseDateValueToString(shot.shotsLoggedMillisecondsValue),
                    style = TextStyles.body,
                    color = AppColors.Black.copy(alpha = 0.6f)
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = AppColors.Black.copy(alpha = 0.5f)
            )
        }
    }
}

/**
 * Button centered within its container to trigger logging new shots.
 *
 * @param onLogShotsClicked Callback invoked when the button is clicked.
 */
@Composable
private fun LogNewShotButton(onLogShotsClicked: () -> Unit) {
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
                onClick = { onLogShotsClicked.invoke() },
                shape = RoundedCornerShape(size = 50.dp),
                modifier = Modifier
                    .padding(vertical = Padding.twelve)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Colors.secondaryColor)
            ) {
                Text(
                    text = "Log Shots",
                    style = TextStyles.smallBold,
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
            }
        }
    }
}

/**
 * Empty state UI shown when there are no logged or pending shots.
 *
 * Displays a header, an illustration, explanatory text, and a button to log shots.
 *
 * @param hintLogNewShotText Hint text to guide the user about logging shots.
 * @param onLogShotsClicked Callback invoked when the log shots button is clicked.
 */
@Composable
private fun ColumnScope.ShotContentEmptyState(
    hintLogNewShotText: String,
    onLogShotsClicked: () -> Unit
) {
    Text(
        text = stringResource(id = R.string.log_shots),
        style = TextStyles.smallBold,
        modifier = Modifier
            .align(Alignment.Start)
            .padding(top = 16.dp, bottom = 8.dp)
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = AppColors.White)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(20.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_basketball_log_shot_empty_state),
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .padding(bottom = 16.dp)
            )

            Text(
                text = stringResource(id = StringsIds.noCurrentShotsLoggedForPlayer),
                style = TextStyles.body,
                textAlign = TextAlign.Center,
                color = AppColors.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = hintLogNewShotText,
                style = TextStyles.body,
                textAlign = TextAlign.Center,
                color = AppColors.Black.copy(alpha = 0.7f),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Button(
                onClick = onLogShotsClicked,
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Colors.secondaryColor)
            ) {
                Text(
                    text = stringResource(id = R.string.log_shots),
                    style = TextStyles.smallBold,
                    color = Color.White
                )
            }
        }
    }
}

@Preview(
    name = "ShotsContent - Empty State",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun ShotsContentEmptyPreview() {
    TrackYourShotTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(AppColors.White)
                .padding(
                    start = Padding.twenty,
                    end = Padding.twenty,
                    bottom = Padding.twenty
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ShotsContent(
                shotList = emptyList(),
                pendingShotList = emptyList(),
                hintLogNewShotText = "Log your first shot to begin tracking progress.",
                onLogShotsClicked = {},
                onViewShotClicked = { _, _ -> },
                onPendingShotClicked = { _, _ -> }
            )
        }
    }
}

@Preview(
    name = "ShotsContent - With Logged Shots",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun ShotsContentWithLoggedShotsPreview() {
    TrackYourShotTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            ShotsContent(
                shotList = listOf(
                    ShotLogged(
                        id = 1,
                        shotName = "3PT Corner",
                        shotsLoggedMillisecondsValue = System.currentTimeMillis(),
                        shotType = 0,
                        shotsAttempted = 4,
                        shotsMade = 2,
                        shotsMissed = 2,
                        shotsMadePercentValue = 50.0,
                        shotsMissedPercentValue = 50.0,
                        isPending = false,
                        shotsAttemptedMillisecondsValue = 0
                    )
                ),
                pendingShotList = emptyList(),
                hintLogNewShotText = "",
                onLogShotsClicked = {},
                onViewShotClicked = { _, _ -> },
                onPendingShotClicked = { _, _ -> }
            )
        }
    }
}

@Preview(
    name = "ShotsContent - With Pending Shots",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun ShotsContentWithPendingShotsPreview() {
    TrackYourShotTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            ShotsContent(
                shotList = emptyList(),
                pendingShotList = listOf(
                    ShotLogged(
                        id = 2,
                        shotName = "Midrange Jumper",
                        shotsLoggedMillisecondsValue = System.currentTimeMillis(),
                        shotType = 1,
                        shotsAttempted = 4,
                        shotsMade = 2,
                        shotsMissed = 2,
                        shotsMadePercentValue = 50.0,
                        shotsMissedPercentValue = 50.0,
                        isPending = false,
                        shotsAttemptedMillisecondsValue = 0
                    )
                ),
                hintLogNewShotText = "",
                onLogShotsClicked = {},
                onViewShotClicked = { _, _ -> },
                onPendingShotClicked = { _, _ -> }
            )
        }
    }
}
