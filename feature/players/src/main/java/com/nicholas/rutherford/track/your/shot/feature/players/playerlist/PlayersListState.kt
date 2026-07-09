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
 * @property filterCount The number of active filters applied to the player list.
 * @property searchQuery The current search query entered by the user.
 * @property hasAnyPlayersInDatabase A boolean indicating whether there are any players in the database.
 * @property isLoading Whether the initial player list load is still in progress.
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
    val sheetOptions: List<String> = emptyList(),
    val filterCount: Int = 0,
    val searchQuery: String = "",
    val hasAnyPlayersInDatabase: Boolean = false,
    val isLoading: Boolean = true
)
