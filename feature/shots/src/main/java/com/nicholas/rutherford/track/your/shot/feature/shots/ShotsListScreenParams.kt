package com.nicholas.rutherford.track.your.shot.feature.shots

/**
 * Parameters used to render the ShotsListScreen UI.
 *
 * @property state The current UI state, containing a list of all shots with their associated players.
 * @property onHelpClicked Callback function triggered when the user taps the help icon or option in the UI.
 * @property onToolbarMenuClicked Callback function triggered when the user interacts with the toolbar menu.
 * @property onShotItemClicked Callback function triggered when a shot item is selected from the list.
 *                              Receives the selected [ShotLoggedWithPlayer] as input.
 * @property shouldShowAllPlayerShots Flag to indicate whether shots for all players should be displayed.
 *                                    If false, the UI may filter or restrict to a subset of players.
 */
data class ShotsListScreenParams(
    val state: ShotsListState,
    val onHelpClicked: () -> Unit,
    val onToolbarMenuClicked: () -> Unit,
    val onShotItemClicked: (ShotLoggedWithPlayer) -> Unit,
    val shouldShowAllPlayerShots: Boolean
)

