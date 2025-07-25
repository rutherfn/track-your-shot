package com.nicholas.rutherford.track.your.shot.feature.shots

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.your.shot.AppColors
import com.nicholas.rutherford.track.your.shot.base.resources.R
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.helper.extensions.toTimestampString
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles
import java.util.Date

/**
 * Displays the main screen for viewing a list of logged basketball shots.
 * If the shot list is empty, an empty state is shown encouraging users to add shots.
 *
 * @param params Contains the state and callback handlers for this screen.
 */
@Composable
fun ShotsListScreen(params: ShotsListScreenParams) {
    val isShotListEmpty = params.state.shotList.isEmpty()

    if (!isShotListEmpty) {
        ShotsList(params = params)
    } else {
        AddShotEmptyState()
    }
}

/**
 * Displays an empty state UI when no shots have been added.
 * Shows an image and messages encouraging the user to log a new shot.
 */
@Composable
private fun AddShotEmptyState() {
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
                painter = painterResource(id = R.drawable.ic_basketball_shot_empty_state),
                contentDescription = null,
                modifier = Modifier.size(120.dp)
            )

            Text(
                text = stringResource(id = StringsIds.noCurrentShotsAdded),
                style = TextStyles.medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Text(
                text = stringResource(id = StringsIds.hintAddNewShot),
                style = TextStyles.smallBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}

/**
 * Displays the list of logged shots using a [LazyColumn].
 *
 * @param params Contains the list of shots and click callback.
 */
@Composable
private fun ShotsList(params: ShotsListScreenParams) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(color = AppColors.White)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(params.state.shotList) { shot ->
            ShotItem(shot = shot, onShotItemClicked = params.onShotItemClicked)
        }
    }
}

/**
 * Displays a single shot entry in the list with basic information such as
 * the shot name, the player who took the shot, and the timestamp.
 *
 * @param shot The shot data with player info.
 * @param onShotItemClicked Callback triggered when the item is clicked.
 */
@Composable
private fun ShotItem(
    shot: ShotLoggedWithPlayer,
    onShotItemClicked: (ShotLoggedWithPlayer) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onShotItemClicked(shot) },
        colors = CardDefaults.cardColors(containerColor = AppColors.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = shot.shotLogged.shotName,
                style = TextStyles.bodyBold,
                textAlign = TextAlign.Start,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(height = 4.dp))

            Text(
                text = stringResource(id = StringsIds.shotTakenByX, shot.playerName),
                style = TextStyles.bodySmall,
                textAlign = TextAlign.Start,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = stringResource(
                    id = StringsIds.shotTakenOnX,
                    Date(shot.shotLogged.shotsAttemptedMillisecondsValue).toTimestampString()
                ),
                style = TextStyles.bodySmall,
                modifier = Modifier.padding(top = 4.dp),
                textAlign = TextAlign.Start,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

