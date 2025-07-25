package com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot

/**
 * Defines all state and callback parameters required by the Log Shot screen.
 *
 * This class is passed down to composables to handle UI state, user interactions,
 * and side effects in a centralized and consistent way.
 *
 * @property state The current UI state representing all data needed to display and interact with the log shot screen.
 * @property onBackButtonClicked Called when the user taps the back button to navigate away from the screen.
 * @property onDateShotsTakenClicked Called when the user selects the date on which the shots were taken.
 * @property onShotsMadeUpwardClicked Called when the user increments the number of shots made.
 * @property onShotsMadeDownwardClicked Called when the user decrements the number of shots made.
 * @property onShotsMissedUpwardClicked Called when the user increments the number of shots missed.
 * @property onShotsMissedDownwardClicked Called when the user decrements the number of shots missed.
 * @property onSaveClicked Called when the user saves the logged shot.
 * @property onDeleteShotClicked Called when the user deletes the logged shot.
 */
data class LogShotParams(
    val state: LogShotState,
    val onBackButtonClicked: () -> Unit,
    val onDateShotsTakenClicked: () -> Unit,
    val onShotsMadeUpwardClicked: (value: Int) -> Unit,
    val onShotsMadeDownwardClicked: (value: Int) -> Unit,
    val onShotsMissedUpwardClicked: (value: Int) -> Unit,
    val onShotsMissedDownwardClicked: (value: Int) -> Unit,
    val onSaveClicked: () -> Unit,
    val onDeleteShotClicked: () -> Unit
)

