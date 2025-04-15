package com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots.createeditdeclaredshot

data class CreateEditDeclaredShotScreenParams(
    val state: CreateEditDeclaredShotState,
    val onToolbarMenuClicked: () -> Unit,
    val onDeleteShotClicked: (id: Int) -> Unit,
    val onEditShotPencilClicked: () -> Unit,
    val onShotNameValueChanged: (shotName: String) -> Unit,
    val onShotDescriptionValueChanged: (description: String) -> Unit,
    val onShotCategoryValueChanged: (category: String) -> Unit,
    val onEditOrCreateNewShot: () -> Unit
)
