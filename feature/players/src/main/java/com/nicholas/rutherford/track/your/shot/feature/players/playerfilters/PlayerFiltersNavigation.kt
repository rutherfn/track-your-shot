package com.nicholas.rutherford.track.your.shot.feature.players.playerfilters

import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress

/**
 * Represents the UI state for the Player Filters screen.
 *
 * Defines navigation actions available from the player filters screen.
 */
interface PlayerFiltersNavigation {
    fun alert(alert: Alert)
    fun disableProgress()
    fun enableProgress(progress: Progress)
    fun pop()
}
