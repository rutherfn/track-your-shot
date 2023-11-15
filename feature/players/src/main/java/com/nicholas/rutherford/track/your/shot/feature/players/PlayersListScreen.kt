package com.nicholas.rutherford.track.your.shot.feature.players

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.nicholas.rutherford.track.your.shot.AppColors
import com.nicholas.rutherford.track.your.shot.base.resources.R
import com.nicholas.rutherford.track.your.shot.compose.components.Content
import com.nicholas.rutherford.track.your.shot.data.room.response.Player
import com.nicholas.rutherford.track.your.shot.data.shared.appbar.AppBar
import com.nicholas.rutherford.track.your.shot.feature.splash.DrawablesIds
import com.nicholas.rutherford.track.your.shot.feature.splash.StringsIds
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles

@Composable
fun PlayersListScreen(playerListScreenParams: PlayersListScreenParams) {
    val isPlayerListEmpty = playerListScreenParams.state.playerList.isEmpty()
    Content(
        ui = {
            if (!isPlayerListEmpty) {
                LazyColumn {
                    items(playerListScreenParams.state.playerList) { player ->
                        PlayerItem(player = player)
                    }
                }
            } else {
                AddNewPlayerEmptyState()
            }
        },
        appBar = AppBar(
            toolbarTitle = stringResource(id = R.string.players),
            shouldShowMiddleContentAppBar = true,
            onIconButtonClicked = {
                playerListScreenParams.onToolbarMenuClicked.invoke()
            }
        ),
        invokeFunctionOnInit = {
            playerListScreenParams.updatePlayerListState.invoke()
        }
    )
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun PlayerItem(player: Player) {
    val imagePainter = if (!player.imageUrl.isNullOrEmpty()) {
        rememberImagePainter(data = player.imageUrl)
    } else {
        painterResource(id = DrawablesIds.launcherRound)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(.dp),
        elevation = 4.dp // Add elevation for a shadow effect
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = imagePainter,
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .padding(4.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = player.firstName,
                style = TextStyles.body,
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.weight(1f))

            IconButton(
                onClick = { /* Handle click */ }
            ) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = ""
                )
            }
        }

        Divider()
    }
}

@Composable
fun AddNewPlayerEmptyState() {
    Box(
        modifier = Modifier.fillMaxSize().background(AppColors.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_basketball_player_empty_state),
                contentDescription = null,
                modifier = Modifier.size(120.dp)
            )

            Text(
                text = stringResource(id = StringsIds.noCurrentPlayersAdded),
                style = TextStyles.medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Text(
                text = stringResource(id = StringsIds.hintAddNewPlayer),
                style = TextStyles.smallBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}
