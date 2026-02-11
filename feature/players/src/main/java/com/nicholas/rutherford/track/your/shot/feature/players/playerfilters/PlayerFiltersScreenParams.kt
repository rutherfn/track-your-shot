package com.nicholas.rutherford.track.your.shot.feature.players.playerfilters

/**
 * Represents the UI state for the Player Filters screen.
 *
 * Defines all state and callback parameters required by the Player Filters Screen.
 *
 * This class is passed down to composables to handle UI state, user interactions,
 * and side effects in a consistent and centralized way.
 *
 * @property state The current UI state of the player filters flow.
 * @property onToolbarMenuClicked Called when the user interacts with the top app bar menu
 * @property onPositionToggled Called when a player position is toggled
 * @property onShotLogsToggled Called when the shot logs for a player are toggled
 * @property onMinShotsChanged Called when the minimum number of shots is changed
 * @property onMaxShotsChanged Called when the maximum number of shots is changed
 * @property onSeeResultsClicked Called when the user taps the "See Results" button
 * @property onClearShotRangeClicked Called when the user taps the "Clear Shot Range" button
 * @property onResetFiltersClicked Called when the user taps the "Reset Filters" button
 */
data class PlayerFiltersScreenParams(
    val state: PlayerFiltersState,
    val onToolbarMenuClicked: () -> Unit,
    val onPositionToggled: (title: String) -> Unit,
    val onShotLogsToggled: (title: String) -> Unit,
    val onMinShotsChanged: (value: Int) -> Unit,
    val onMaxShotsChanged: (value: Int) -> Unit,
    val onSeeResultsClicked: () -> Unit,
    val onClearShotRangeClicked: () -> Unit,
    val onResetFiltersClicked: () -> Unit
)
