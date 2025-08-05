package com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots.declaredshotslist

/**
 * Parameters used to render the DeclaredShotsListScreen UI.
 *
 * @property state The current state of the declared shots list screen.
 * @property onAddDeclaredShotClicked Callback function triggered when the user clicks to add a new declared shot.
 * @property onDeclaredShotClicked Callback function triggered when the user selects an existing declared shot from the list.
 * @property onToolbarMenuClicked Callback function triggered when the user interacts with the toolbar menu.
 */
data class DeclaredShotsListScreenParams(
    val state: DeclaredShotsListState,
    val onAddDeclaredShotClicked: () -> Unit,
    val onDeclaredShotClicked: (title: String) -> Unit,
    val onToolbarMenuClicked: () -> Unit
)

