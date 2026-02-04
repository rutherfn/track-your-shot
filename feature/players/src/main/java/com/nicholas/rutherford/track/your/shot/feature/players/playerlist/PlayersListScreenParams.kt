package com.nicholas.rutherford.track.your.shot.feature.players.playerlist

import com.nicholas.rutherford.track.your.shot.data.room.response.Player
import com.nicholas.rutherford.track.your.shot.compose.components.EnhancedSearchTextField

/**
 * Represents the UI state for the Create/Edit Player screen.
 *
 * Parameters for the `PlayersListScreen` composable.
 *
 * This data class encapsulates all the necessary state and callback functions required to
 * render and interact with the Players List screen.
 *
 * @property state Represents the current state of the player list, including the list of players and sheet options.
 * @property onToolbarMenuClicked Callback triggered when the toolbar menu button is clicked.
 * @property onAddPlayerClicked Callback triggered when the user chooses to add a new player.
 * @property onPlayerClicked Callback triggered when a specific player is selected from the list.
 * @property onSheetItemClicked Callback triggered when an item from the bottom sheet is clicked. The index corresponds to the clicked item.
 * @property onFilterChipClicked Callback triggered when the filter chip is clicked.
 * @property onSearchTextChanged Callback triggered when the search text changes from the [EnhancedSearchTextField]
 */
data class PlayersListScreenParams(
    val state: PlayersListState,
    val onToolbarMenuClicked: () -> Unit,
    val onAddPlayerClicked: () -> Unit,
    val onPlayerClicked: (player: Player) -> Unit,
    val onSheetItemClicked: (index: Int) -> Unit,
    val onFilterChipClicked: () -> Unit,
    val onSearchTextChanged: (String) -> Unit
)
