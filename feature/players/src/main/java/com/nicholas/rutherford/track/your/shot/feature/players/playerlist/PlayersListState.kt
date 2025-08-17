package com.nicholas.rutherford.track.your.shot.feature.players.playerlist

import com.nicholas.rutherford.track.your.shot.data.room.response.Player
import com.nicholas.rutherford.track.your.shot.data.room.response.PlayerPositions

/**
 * Represents the UI state for the Create/Edit Player screen.
 *
 * UI state for the Players List screen.
 *
 * This data class holds all relevant UI-related data for rendering the list of players
 * and managing selected player and bottom sheet options.
 *
 * @property playerList The list of players currently displayed on the screen.
 * @property selectedPlayer The currently selected player, used when interacting with bottom sheet options.
 *                          Defaults to an empty player instance with default values.
 * @property sheetOptions A list of string options to be displayed in the bottom sheet when a player is selected.
 */
data class PlayersListState(
    val playerList: List<Player> = emptyList(),
    val selectedPlayer: Player = Player(
        firstName = "",
        lastName = "",
        position = PlayerPositions.None,
        firebaseKey = "",
        imageUrl = "",
        shotsLoggedList = emptyList()
    ),
    val sheetOptions: List<String> = emptyList()
)
