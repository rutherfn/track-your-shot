package com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots.createeditdeclaredshot

import com.nicholas.rutherford.track.your.shot.data.room.response.DeclaredShot

data class CreateEditDeclaredShotState(
    val currentDeclaredShot: DeclaredShot? = null,
    val toolbarTitle: String = "",
    val declaredShotState: DeclaredShotState = DeclaredShotState.NONE
)