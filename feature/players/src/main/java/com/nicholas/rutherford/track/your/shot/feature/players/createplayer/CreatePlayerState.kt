package com.nicholas.rutherford.track.your.shot.feature.players.createplayer

import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import com.nicholas.rutherford.track.your.shot.data.shared.sheet.Sheet
import com.nicholas.rutherford.track.your.shot.feature.splash.StringsIds

data class CreatePlayerState(
    val firstName: String = "",
    val lastName: String = "",
    val playerPositionStringResId: Int = StringsIds.pointGuard,
    val imageUri: Uri? = null,
    val sheet: Sheet? = null
)
