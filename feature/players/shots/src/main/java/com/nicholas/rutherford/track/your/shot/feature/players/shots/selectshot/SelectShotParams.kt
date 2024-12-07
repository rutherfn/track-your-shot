package com.nicholas.rutherford.track.your.shot.feature.players.shots.selectshot

import com.nicholas.rutherford.track.your.shot.data.room.response.DeclaredShot

data class SelectShotParams(
    val state: SelectShotState,
    val onSearchValueChanged: (newSearchQuery: String) -> Unit,
    val onBackButtonClicked: () -> Unit,
    val onCancelIconClicked: (query: String) -> Unit,
    val onnDeclaredShotItemClicked: (declaredShot: DeclaredShot) -> Unit,
    val onHelpIconClicked: () -> Unit,
    val updateIsExistingPlayerAndPlayerId: () -> Unit,
    val onItemClicked: (shotType: Int) -> Unit
)
