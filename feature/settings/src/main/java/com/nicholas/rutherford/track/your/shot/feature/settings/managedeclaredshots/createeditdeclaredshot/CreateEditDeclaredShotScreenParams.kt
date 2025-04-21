package com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots.createeditdeclaredshot

data class CreateEditDeclaredShotScreenParams(
    val state: CreateEditDeclaredShotState,
    val onToolbarMenuClicked: () -> Unit,
    val onDeleteShotClicked: (id: Int) -> Unit,
    val onEditShotPencilClicked: () -> Unit,
    val onEditShotNameValueChanged: (shotName: String) -> Unit,
    val onEditShotDescriptionValueChanged: (description: String) -> Unit,
    val onEditShotCategoryValueChanged: (category: String) -> Unit,
    val onCreateShotNameValueChanged: (shotName: String) -> Unit,
    val onCreateShotDescriptionValueChanged: (description: String) -> Unit,
    val onCreateShotCategoryValueChanged: (category: String) -> Unit,
    val onEditOrCreateNewShot: () -> Unit
)
