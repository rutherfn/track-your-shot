package com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer

import com.nicholas.rutherford.track.your.shot.data.room.response.ShotLogged
import com.nicholas.rutherford.track.your.shot.data.shared.sheet.Sheet
import com.nicholas.rutherford.track.your.shot.feature.splash.StringsIds

data class CreateEditPlayerState(
    val firstName: String = "",
    val lastName: String = "",
    val editedPlayerUrl: String = "",
    val toolbarNameResId: Int = StringsIds.createPlayer,
    val playerPositionString: String = "",
    val hintLogNewShotText: String = "",
    val shots: List<ShotLogged> = emptyList(),
    val sheet: Sheet? = null
)
