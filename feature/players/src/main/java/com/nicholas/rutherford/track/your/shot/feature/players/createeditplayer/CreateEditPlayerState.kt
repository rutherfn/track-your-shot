package com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer

import com.nicholas.rutherford.track.your.shot.data.shared.sheet.Sheet
import com.nicholas.rutherford.track.your.shot.feature.splash.StringsIds

data class CreateEditPlayerState(
    val firstName: String = "",
    val lastName: String = "",
    val playerPositionStringResId: Int = StringsIds.pointGuard,
    val sheet: Sheet? = null
)
