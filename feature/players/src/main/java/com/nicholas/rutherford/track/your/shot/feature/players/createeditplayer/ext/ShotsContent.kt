package com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer.ext

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.your.shot.AppColors
import com.nicholas.rutherford.track.your.shot.base.resources.R
import com.nicholas.rutherford.track.your.shot.data.room.response.ShotLogged
import com.nicholas.rutherford.track.your.shot.feature.splash.Colors
import com.nicholas.rutherford.track.your.shot.feature.splash.StringsIds
import com.nicholas.rutherford.track.your.shot.helper.extensions.parseDateValueToString
import com.nicholas.rutherford.track.your.shot.helper.ui.Padding
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles

@Composable
fun ColumnScope.ShotsContent(
    shotList: List<ShotLogged>,
    pendingShotList: List<ShotLogged>,
    hintLogNewShotText: String,
    onLogShotsClicked: () -> Unit,
    onViewShotClicked: (shotId: Int) -> Unit,
    onPendingShotClicked: (shotId: Int) -> Unit
) {
    if (shotList.isEmpty() && pendingShotList.isEmpty()) {
        ShotContentEmptyState(hintLogNewShotText = hintLogNewShotText, onLogShotsClicked = onLogShotsClicked)
    } else if (shotList.isNotEmpty()) {
        shotList.forEach { shot ->
            LoggedShot(
                shot = shot,
                onViewShotClicked = onViewShotClicked
            )
        }
    }

    if (pendingShotList.isNotEmpty()) {
        Text(
            text = "Pending Shots",
            style = TextStyles.small,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(top = Padding.twelve, start = Padding.four)
        )
        pendingShotList.forEach { shot -> PendingShot(shot = shot, onPendingShotClicked = onPendingShotClicked) }
    }
}

@Composable
private fun PendingShot(
    shot: ShotLogged,
    onPendingShotClicked: (shotId: Int) -> Unit
) {
    Card(
        modifier = Modifier
            .background(AppColors.White)
            .fillMaxWidth()
            .clickable { onPendingShotClicked.invoke(shot.id) }
            .padding(top = 4.dp, end = 4.dp),
        elevation = 2.dp
    ) {
        Column {
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { onPendingShotClicked.invoke(shot.id) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = StringsIds.shotNameX, shot.shotName),
                    style = TextStyles.body,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                IconButton(onClick = { onPendingShotClicked.invoke(shot.id) }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowForward,
                        contentDescription = ""
                    )
                }
            }
        }
    }
}

@Composable
private fun ColumnScope.LoggedShot(
    shot: ShotLogged,
    onViewShotClicked: (shotId: Int) -> Unit
) {
    Text(
        text = stringResource(id = R.string.shots),
        style = TextStyles.small,
        modifier = Modifier
            .align(Alignment.Start)
            .padding(top = Padding.twelve, start = Padding.four)
    )

    Card(
        modifier = Modifier
            .background(AppColors.White)
            .fillMaxWidth()
            .clickable { onViewShotClicked.invoke(shot.id) }
            .padding(
                top = 16.dp,
                end = 4.dp,
                bottom = 16.dp
            ),
        elevation = 2.dp
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.x_shot_last_logged_x, shot.shotName, parseDateValueToString(timeInMilliseconds = shot.shotsLoggedMillisecondsValue)),
                    style = TextStyles.body,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                IconButton(onClick = { onViewShotClicked.invoke(shot.id) }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowForward,
                        contentDescription = ""
                    )
                }
            }
        }
    }
}

@Composable
private fun ColumnScope.ShotContentEmptyState(hintLogNewShotText: String, onLogShotsClicked: () -> Unit) {
    Text(
        text = stringResource(id = R.string.log_shots),
        style = TextStyles.small,
        modifier = Modifier
            .align(Alignment.Start)
            .padding(top = Padding.twelve, start = Padding.four)
    )
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
                painter = painterResource(id = R.drawable.ic_basketball_log_shot_empty_state),
                contentDescription = null,
                modifier = Modifier.size(90.dp)
            )

            Text(
                text = stringResource(id = StringsIds.noCurrentShotsLoggedForPlayer),
                style = TextStyles.smallBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Text(
                text = hintLogNewShotText,
                style = TextStyles.small,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Button(
                onClick = { onLogShotsClicked.invoke() },
                shape = RoundedCornerShape(size = 50.dp),
                modifier = Modifier
                    .padding(vertical = Padding.twelve)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(backgroundColor = Colors.secondaryColor)
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
