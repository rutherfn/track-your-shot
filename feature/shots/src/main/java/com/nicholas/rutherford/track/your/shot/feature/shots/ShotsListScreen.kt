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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
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
import com.nicholas.rutherford.track.your.shot.compose.components.Content
import com.nicholas.rutherford.track.your.shot.data.shared.appbar.AppBar
import com.nicholas.rutherford.track.your.shot.helper.extensions.toTimestampString
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles
import java.util.Date

@Composable
fun ShotsListScreen(params: ShotsListScreenParams) {
    val isShotListEmpty = params.state.shotList.isEmpty()

    Content(
        ui = {
            if (!isShotListEmpty) {
                ShotsList(params = params)
            } else {
                AddShotEmptyState()
            }
        },
        appBar = AppBar(
            toolbarTitle = stringResource(id = R.string.shots),
            shouldShowMiddleContentAppBar = true,
            onIconButtonClicked = {
                params.onToolbarMenuClicked.invoke()
            },
            onSecondaryIconButtonClicked = {
                // todo user should be taken where they can filter there shots screen
            }
        ),
        secondaryImageVector = Icons.Filled.FilterAlt,
        secondaryImageEnabled = !isShotListEmpty
    )
}

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

@Composable
private fun ShotsList(params: ShotsListScreenParams) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(params.state.shotList) { shot ->
            ShotItem(shot = shot, onShotItemClicked = params.onShotItemClicked)
        }
    }
}

@Composable
private fun ShotItem(
    shot: ShotLoggedWithPlayer,
    onShotItemClicked: (ShotLoggedWithPlayer) -> Unit
) {
    Card(
        modifier = Modifier
            .background(AppColors.White)
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                onShotItemClicked.invoke(shot)
            },
        elevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = shot.shotLogged.shotName,
                style = TextStyles.bodyBold,
                textAlign = TextAlign.Start,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = stringResource(id = StringsIds.shotTakenByX, shot.playerName),
                style = TextStyles.bodySmall,
                textAlign = TextAlign.Start,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = stringResource(id = StringsIds.shotTakenOnX, Date(shot.shotLogged.shotsAttemptedMillisecondsValue).toTimestampString()),
                style = TextStyles.bodySmall,
                textAlign = TextAlign.Start,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
