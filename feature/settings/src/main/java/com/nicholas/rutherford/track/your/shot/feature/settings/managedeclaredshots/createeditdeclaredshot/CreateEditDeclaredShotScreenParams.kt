package com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots.createeditdeclaredshot

/**
 * Parameters used to render the CreateEditDeclaredShotScreen UI.
 *
 * @property state The current state of the screen.
 * @property onToolbarMenuClicked Callback function triggered when the user interacts with the toolbar menu.
 * @property onDeleteShotClicked Callback function triggered when the user deletes a shot.
 * @property onEditShotPencilClicked Callback function triggered when the user interacts with the pencil icon.
 * @property onEditShotNameValueChanged Callback function triggered when the user updates the shot name.
 * @property onEditShotDescriptionValueChanged Callback function triggered when the user updates the shot description.
 * @property onEditShotCategoryValueChanged Callback function triggered when the user updates the shot category.
 * @property onCreateShotNameValueChanged Callback function triggered when the user updates the shot name.
 * @property onCreateShotDescriptionValueChanged Callback function triggered when the user updates the shot description.
 * @property onCreateShotCategoryValueChanged Callback function triggered when the user updates the shot category.
 * @property onEditOrCreateNewShot Callback function triggered when the user interacts with the pencil icon.
 */
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
