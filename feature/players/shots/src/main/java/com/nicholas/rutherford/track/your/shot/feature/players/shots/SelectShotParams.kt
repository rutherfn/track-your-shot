package com.nicholas.rutherford.track.your.shot.feature.players.shots

import com.nicholas.rutherford.track.your.shot.data.room.response.DeclaredShot

data class SelectShotParams(
    val state: SelectShotState,
    val onnDeclaredShotItemClicked: (declaredShot: DeclaredShot) -> Unit,
    val onHelpIconClicked: () -> Unit
)
