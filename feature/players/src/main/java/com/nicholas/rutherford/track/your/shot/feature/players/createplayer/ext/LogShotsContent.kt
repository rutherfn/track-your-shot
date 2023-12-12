package com.nicholas.rutherford.track.your.shot.feature.players.createplayer.ext

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
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
import com.nicholas.rutherford.track.your.shot.feature.splash.Colors
import com.nicholas.rutherford.track.your.shot.feature.splash.StringsIds
import com.nicholas.rutherford.track.your.shot.helper.ui.Padding
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles

@Composable
fun ColumnScope.LogShotsContent(
    isEmpty: Boolean = true,
    firstName: String,
    lastName: String
) {
    val hintLogNewShot = if (firstName.isNotEmpty() && lastName.isNotEmpty()) {
        stringResource(id = R.string.hint_log_new_shots_for_player_x, "$firstName $lastName")
    } else if (firstName.isNotEmpty()) {
        stringResource(id = R.string.hint_log_new_shots_for_player_x, firstName)
    } else if (lastName.isNotEmpty()) {
        stringResource(id = R.string.hint_log_new_shots_for_player_x, lastName)
    } else {
        stringResource(id = StringsIds.hintLogNewShots)
    }

    Text(
        text = stringResource(id = R.string.log_shots),
        style = TextStyles.small,
        modifier = Modifier
            .align(Alignment.Start)
            .padding(top = Padding.twelve, start = Padding.four)
    )

    if (isEmpty) {
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
                    text = hintLogNewShot,
                    style = TextStyles.small,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                Button(
                    onClick = {
                        // todo -> take user to list to log new shots
                    },
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
    } else {
        // todo -> show list of shots users logged
    }
}
