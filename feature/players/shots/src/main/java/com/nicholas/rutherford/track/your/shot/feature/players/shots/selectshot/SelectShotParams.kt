package com.nicholas.rutherford.track.your.shot.feature.players.shots.selectshot

import com.nicholas.rutherford.track.your.shot.data.room.response.DeclaredShot

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Defines all state and callback parameters required by the Select Shot screen.
 *
 * This class is passed to composables to centralize UI state handling and user interaction callbacks,
 * enabling a clean and maintainable architecture for shot selection.
 *
 * @property state The current UI state representing the search query and available shots.
 * @property onSearchValueChanged Called when the user types into the search field to filter shots.
 * @property onBackButtonClicked Called when the user taps the back button to navigate away.
 * @property onCancelIconClicked Called when the user taps the cancel (clear search) icon, passing the current query.
 * @property onnDeclaredShotItemClicked Called when the user selects a [DeclaredShot] item from the list.
 * @property onHelpIconClicked Called when the user taps the help icon to get more information.
 * @property onItemClicked Called when the user selects a shot type (identified by an integer).
 */
data class SelectShotParams(
    val state: SelectShotState,
    val onSearchValueChanged: (newSearchQuery: String) -> Unit,
    val onBackButtonClicked: () -> Unit,
    val onCancelIconClicked: (query: String) -> Unit,
    val onnDeclaredShotItemClicked: (declaredShot: DeclaredShot) -> Unit,
    val onHelpIconClicked: () -> Unit,
    val onItemClicked: (shotType: Int) -> Unit
)
