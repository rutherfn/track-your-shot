package com.nicholas.rutherford.track.your.shot.feature.shots

/**
 * Holds the UI state for the Shot list screen.
 *
 * @property shotList A list of [ShotLoggedWithPlayer] items representing all shots logged in the current context.
 *                    Defaults to an empty list.
 */
data class ShotsListState(
    val shotList: List<ShotLoggedWithPlayer> = emptyList()
)

