package com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Represents the UI state for the Log Shot screen.
 *
 * @property shotName The name of the shot being logged. Defaults to an empty string.
 * @property playerName The name of the player associated with the shot. Defaults to an empty string.
 * @property playerPosition The player's position index (e.g., used to identify position from a list). Defaults to 0.
 * @property shotsLoggedDateValue The formatted date string representing when the shot was logged.
 *                                Defaults to an empty string.
 * @property shotsTakenDateValue The formatted date string representing when the shot was taken.
 *                                Defaults to an empty string.
 * @property shotsMade The number of shots made. Defaults to 0.
 * @property shotsMissed The number of shots missed. Defaults to 0.
 * @property shotsAttempted The total number of shots attempted. Defaults to 0.
 * @property shotsMadePercentValue The percentage of shots made as a formatted string (e.g., "75%").
 *                                 Defaults to an empty string.
 * @property shotsMissedPercentValue The percentage of shots missed as a formatted string (e.g., "25%").
 *                                   Defaults to an empty string.
 * @property deleteShotButtonVisible Flag indicating whether the delete shot button should be visible in the UI.
 *                                   Defaults to false.
 */
data class LogShotState(
    val shotName: String = "",
    val playerName: String = "",
    val playerPosition: Int = 0,
    val shotsLoggedDateValue: String = "",
    val shotsTakenDateValue: String = "",
    val shotsMade: Int = 0,
    val shotsMissed: Int = 0,
    val shotsAttempted: Int = 0,
    val shotsMadePercentValue: String = "",
    val shotsMissedPercentValue: String = "",
    val deleteShotButtonVisible: Boolean = false
)
