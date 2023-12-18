package com.nicholas.rutherford.track.your.shot.feature.players.playerlist

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
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.nicholas.rutherford.track.your.shot.AppColors
import com.nicholas.rutherford.track.your.shot.base.resources.R
import com.nicholas.rutherford.track.your.shot.compose.components.Content
import com.nicholas.rutherford.track.your.shot.data.room.response.Player
import com.nicholas.rutherford.track.your.shot.data.room.response.PlayerPositions
import com.nicholas.rutherford.track.your.shot.data.room.response.fullName
import com.nicholas.rutherford.track.your.shot.data.shared.appbar.AppBar
import com.nicholas.rutherford.track.your.shot.feature.splash.DrawablesIds
import com.nicholas.rutherford.track.your.shot.feature.splash.StringsIds
import com.nicholas.rutherford.track.your.shot.helper.extensions.toPlayerPositionAbvId
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles

@Composable
fun PlayersListScreen(playerListScreenParams: PlayersListScreenParams) {
    val isPlayerListEmpty = playerListScreenParams.state.playerList.isEmpty()

    Content(
        ui = {
            if (!isPlayerListEmpty) {
                LazyColumn {
                    items(playerListScreenParams.state.playerList) { player ->
                        PlayerItem(player = player, onDeletePlayerClicked = playerListScreenParams.onDeletePlayerClicked)
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
            },
            onSecondaryIconButtonClicked = {
                playerListScreenParams.onAddPlayerClicked.invoke()
            }
        )
    )
}

@Composable
fun PlayerItem(
    player: Player,
    onDeletePlayerClicked: (player: Player) -> Unit
) {
    val imagePainter = if (!player.imageUrl.isNullOrEmpty()) {
        rememberAsyncImagePainter(model = player.imageUrl)
    } else {
        painterResource(id = DrawablesIds.launcherRound)
    }

    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .background(AppColors.White)
            .fillMaxWidth()
            .padding(16.dp),
        elevation = 2.dp
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
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

                player.position.value.toPlayerPositionAbvId()?.let { positionId ->
                    Text(
                        text = stringResource(
                            id =
                            R.string.x_position_x_player_name,
                            stringResource(id = positionId),
                            player.fullName()
                        ),
                        style = TextStyles.body,
                        textAlign = TextAlign.Start
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Box {
                    IconButton(
                        onClick = { expanded = true }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = ""
                        )
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(onClick = {
                            expanded = false
                        }) {
                            Text(
                                text = stringResource(id = R.string.view_x, player.fullName())
                            )
                        }

                        DropdownMenuItem(onClick = {
                            expanded = false
                            onDeletePlayerClicked.invoke(player)
                        }) {
                            Text(
                                text = stringResource(id = R.string.delete_x, player.fullName())
                            )
                        }
                    }
                }
            }

            Divider()
        }
    }
}

@Composable
fun AddNewPlayerEmptyState() {
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

@Composable
@Preview
fun PlayersListScreenWithItemsPreview() {
    PlayersListScreen(
        playerListScreenParams = PlayersListScreenParams(
            state = PlayersListState(
                playerList = listOf(
                    Player(
                        firstName = "first",
                        lastName = "last",
                        position = PlayerPositions.Center,
                        firebaseKey = "key",
                        imageUrl = null
                    )
                )
            ),
            onToolbarMenuClicked = {},
            updatePlayerListState = {},
            onAddPlayerClicked = {},
            onDeletePlayerClicked = {}
        )
    )
}

@Composable
@Preview
fun PlayerListScreenEmptyStatePreview() {
    PlayersListScreen(
        playerListScreenParams = PlayersListScreenParams(
            state = PlayersListState(playerList = emptyList()),
            onToolbarMenuClicked = {},
            updatePlayerListState = {},
            onAddPlayerClicked = {},
            onDeletePlayerClicked = {}
        )
    )
}
