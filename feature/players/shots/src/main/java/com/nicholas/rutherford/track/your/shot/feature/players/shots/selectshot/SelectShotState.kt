package com.nicholas.rutherford.track.your.shot.feature.players.shots.selectshot

import com.nicholas.rutherford.track.your.shot.data.room.response.DeclaredShot

/**
 * Represents the UI state for the Select Shot screen.
 *
 * @property declaredShotList A list of declared shots available for selection.
 *                            Typically populated from the local database or a remote source.
 *                            Defaults to an empty list.
 */
data class SelectShotState(
    val declaredShotList: List<DeclaredShot> = emptyList()
)

