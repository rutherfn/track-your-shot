package com.nicholas.rutherford.track.your.shot.feature.shots

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Holds the UI state for the Shot list screen.
 *
 * @property shotList A list of [ShotLoggedWithPlayer] items representing all shots logged in the current context.
 *                    Defaults to an empty list.
 * @property playerFilteredName The name of the player currently being filtered. Empty string means no filter is applied.
 * @property searchQuery The current search query entered by the user for filtering shots by player name.
 */
data class ShotsListState(
    val shotList: List<ShotLoggedWithPlayer> = emptyList(),
    val playerFilteredName: String = "",
    val searchQuery: String = ""
)
