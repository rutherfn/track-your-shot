package com.nicholas.rutherford.track.your.shot.feature.players.playerlist

import com.nicholas.rutherford.track.your.shot.data.room.response.Player

/**
 * Parameters for the `PlayersListScreen` composable.
 *
 * This data class encapsulates all the necessary state and callback functions required to
 * render and interact with the Players List screen.
 *
 * @property state Represents the current state of the player list, including the list of players and sheet options.
 * @property onToolbarMenuClicked Callback triggered when the toolbar menu button is clicked.
 * @property updatePlayerListState Callback to refresh or update the player list state.
 * @property onAddPlayerClicked Callback triggered when the user chooses to add a new player.
 * @property onPlayerClicked Callback triggered when a specific player is selected from the list.
 * @property onSheetItemClicked Callback triggered when an item from the bottom sheet is clicked. The index corresponds to the clicked item.
 */
data class PlayersListScreenParams(
    val state: PlayersListState,
    val onToolbarMenuClicked: () -> Unit,
    val updatePlayerListState: () -> Unit,
    val onAddPlayerClicked: () -> Unit,
    val onPlayerClicked: (player: Player) -> Unit,
    val onSheetItemClicked: (index: Int) -> Unit
)

