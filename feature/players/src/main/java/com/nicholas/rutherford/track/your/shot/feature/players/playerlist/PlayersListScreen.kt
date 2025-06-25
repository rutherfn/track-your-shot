package com.nicholas.rutherford.track.your.shot.feature.players.playerlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
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
import com.nicholas.rutherford.track.your.shot.base.resources.DrawablesIds
import com.nicholas.rutherford.track.your.shot.base.resources.R
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.compose.components.BottomSheetWithOptions
import com.nicholas.rutherford.track.your.shot.compose.components.Content
import com.nicholas.rutherford.track.your.shot.data.room.response.Player
import com.nicholas.rutherford.track.your.shot.data.room.response.PlayerPositions
import com.nicholas.rutherford.track.your.shot.data.room.response.fullName
import com.nicholas.rutherford.track.your.shot.data.shared.appbar.AppBar
import com.nicholas.rutherford.track.your.shot.data.shared.sheet.Sheet
import com.nicholas.rutherford.track.your.shot.helper.extensions.toPlayerPositionAbvId
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun PlayersListScreen(playerListScreenParams: PlayersListScreenParams) {
    val isPlayerListEmpty = playerListScreenParams.state.playerList.isEmpty()

    Content(
        ui = {
            if (!isPlayerListEmpty) {
                PlayerList(playerListScreenParams = playerListScreenParams)
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
            },
            shouldIncludeSpaceAfterDeclaration = false
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlayerList(playerListScreenParams: PlayersListScreenParams) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    BottomSheetWithOptions(
        sheetState = sheetState,
        sheetInfo = Sheet(
            title = stringResource(id = StringsIds.chooseOption),
            values = playerListScreenParams.state.sheetOptions
        ),
        onSheetItemClicked = { _, index ->
            scope.launch {
                sheetState.hide()
                playerListScreenParams.onSheetItemClicked.invoke(index)
            }
        },
        onCancelItemClicked = { scope.launch { sheetState.hide() } },
        content = {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(playerListScreenParams.state.playerList) { player ->
                    PlayerItem(
                        player = player,
                        onPlayerClicked = playerListScreenParams.onPlayerClicked,
                        sheetState = sheetState,
                        scope = scope
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlayerItem(
    player: Player,
    onPlayerClicked: (player: Player) -> Unit,
    sheetState: SheetState,
    scope: CoroutineScope
) {
    println("get here test")
    val imagePainter = if (!player.imageUrl.isNullOrEmpty()) {
        rememberAsyncImagePainter(model = player.imageUrl)
    } else {
        painterResource(id = DrawablesIds.launcherRound)
    }

    Card(
        modifier = Modifier
            .background(AppColors.White)
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                onPlayerClicked.invoke(player)
                scope.launch { sheetState.show() }
            }
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
                        text = stringResource(id = R.string.x_position_x_player_name, stringResource(id = positionId), player.fullName()),
                        style = TextStyles.bodyBold,
                        textAlign = TextAlign.Start
                    )
                }
            }

            HorizontalDivider()
        }
    }
}

@Composable
private fun AddNewPlayerEmptyState() {
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
private fun PlayersListScreenWithItemsPreview() {
    PlayersListScreen(
        playerListScreenParams = PlayersListScreenParams(
            state = PlayersListState(
                playerList = listOf(
                    Player(
                        firstName = "first",
                        lastName = "last",
                        position = PlayerPositions.Center,
                        firebaseKey = "key",
                        imageUrl = null,
                        shotsLoggedList = emptyList()
                    )
                )
            ),
            onToolbarMenuClicked = {},
            updatePlayerListState = {},
            onAddPlayerClicked = {},
            onPlayerClicked = {},
            onSheetItemClicked = {}
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
            onPlayerClicked = {},
            onSheetItemClicked = {}
        )
    )
}
