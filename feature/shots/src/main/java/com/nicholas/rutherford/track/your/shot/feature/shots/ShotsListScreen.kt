package com.nicholas.rutherford.track.your.shot.feature.shots

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.your.shot.AppColors
import com.nicholas.rutherford.track.your.shot.base.resources.R
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.compose.components.EnhancedSearchTextField
import com.nicholas.rutherford.track.your.shot.data.room.response.ShotLogged
import com.nicholas.rutherford.track.your.shot.helper.extensions.toTimestampString
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles
import java.util.Date

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Displays the main screen for viewing a list of logged basketball shots.
 * If the shot list is empty, an empty state is shown encouraging users to add shots.
 *
 * The main [ShotsList] also allows for filtering and searching.
 *
 * @param params Contains the state and callback handlers for this screen.
 */
@Composable
fun ShotsListScreen(params: ShotsListScreenParams) {
    val isShotListEmpty = params.state.shotList.isEmpty()
    val searchQuery = params.state.searchQuery

    BackHandler { params.onToolbarMenuClicked.invoke() }

    if (isShotListEmpty && searchQuery.isEmpty()) {
        AddShotEmptyState()
    } else {
        ShotsList(params = params)
    }
}

/**
 * Displays an empty state when no shots have been added.
 *
 * Shows an image and messages encouraging the user to log a new shot.
 */
@Composable
private fun AddShotEmptyState() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_basketball_shot_empty_state),
                contentDescription = null,
                modifier = Modifier.size(120.dp)
            )

            Text(
                text = stringResource(id = StringsIds.noCurrentShotsAdded),
                style = TextStyles.medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Text(
                text = stringResource(id = StringsIds.hintAddNewShot),
                style = TextStyles.smallBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}

/**
 * Displays the search field and either the list of shots or empty search results.
 *
 * This content is shown when there are are shots in the database from all given players or when searching.
 * The search field is always visible at the top.
 *
 * @param params The parameters required for rendering and interacting with the shot list.
 */
@Composable
private fun ShotsList(params: ShotsListScreenParams) {
    val searchQuery = params.state.searchQuery
    val isShotListEmpty = params.state.shotList.isEmpty()

    val focusManager = LocalFocusManager.current
    var isSearchFocused by remember { mutableStateOf(false) }

    BackHandler(enabled = isSearchFocused) { focusManager.clearFocus() }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            EnhancedSearchTextField(
                value = searchQuery,
                onValueChange = params.onSearchTextChanged,
                onClearClick = { params.onSearchTextChanged("") },
                placeholderValue = stringResource(id = StringsIds.searchShots),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp).onFocusChanged { isSearchFocused = it.isFocused }
            )

            if (isShotListEmpty && searchQuery.isNotEmpty()) {
                SearchResultsEmptyState(onClearSearch = { params.onSearchTextChanged("") })
            } else if (!isShotListEmpty) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(params.state.shotList) { shot ->
                        ShotItem(shot = shot, onShotItemClicked = params.onShotItemClicked)
                    }
                }
            }
        }
    }
}

/**
 * Displays the empty state when search results are empty.
 *
 Gets shown when a user searches but no shots match the query.
 *
 * @param onClearSearch Callback invoked when the "Clear Search Results" button is clicked.
 */
@Composable
private fun SearchResultsEmptyState(
    onClearSearch: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_basketball_shot_empty_state),
                contentDescription = null,
                modifier = Modifier.size(120.dp)
            )

            Text(
                text = stringResource(id = StringsIds.noShotsResultsFound),
                style = TextStyles.medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Text(
                text = stringResource(id = StringsIds.noShotsResultsFoundDescription),
                style = TextStyles.smallBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Spacer(modifier = Modifier.padding(vertical = 4.dp))

            TextButton(
                onClick = onClearSearch,
                colors = ButtonDefaults.textButtonColors(contentColor = AppColors.OrangeVariant)
            ) {
                Text(
                    text = stringResource(id = StringsIds.clearSearchResults),
                    style = TextStyles.smallBold
                )
            }
        }
    }
}

/**
 * Displays a single shot entry in the list with basic information such as
 * the shot name, the player who took the shot, and the timestamp.
 *
 * @param shot The shot data with player info.
 * @param onShotItemClicked Callback triggered when the item is clicked.
 */
@Composable
private fun ShotItem(
    shot: ShotLoggedWithPlayer,
    onShotItemClicked: (ShotLoggedWithPlayer) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onShotItemClicked(shot) },
        colors = CardDefaults.cardColors(containerColor = AppColors.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = shot.shotLogged.shotName,
                style = TextStyles.bodyBold,
                textAlign = TextAlign.Start,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(height = 4.dp))

            Text(
                text = stringResource(id = StringsIds.shotTakenByX, shot.playerName),
                style = TextStyles.bodySmall,
                textAlign = TextAlign.Start,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = stringResource(id = StringsIds.shotTakenOnX, Date(shot.shotLogged.shotsAttemptedMillisecondsValue).toTimestampString()),
                style = TextStyles.bodySmall,
                modifier = Modifier.padding(top = 4.dp),
                textAlign = TextAlign.Start,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ShotListScreenEmptyStatePreview() {
    ShotsListScreen(
        params = ShotsListScreenParams(
            state = ShotsListState(shotList = emptyList(), searchQuery = ""),
            onHelpClicked = {},
            onToolbarMenuClicked = {},
            onShotItemClicked = {},
            shouldShowAllPlayerShots = false,
            onFilterChipClicked = {},
            onSearchTextChanged = {}
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun ShotListWithItemsPreview() {
    ShotsListScreen(
        params = ShotsListScreenParams(
            state = ShotsListState(
                shotList = listOf(
                    ShotLoggedWithPlayer(
                        shotLogged = ShotLogged(
                            id = 22,
                            shotName = "shot name",
                            shotType = 1,
                            shotsAttempted = 44,
                            shotsMade = 4,
                            shotsMissed = 40,
                            shotsMadePercentValue = 22.2,
                            shotsMissedPercentValue = 11.2,
                            shotsAttemptedMillisecondsValue = 22L,
                            shotsLoggedMillisecondsValue = 22L,
                            isPending = false
                        ),
                        playerId = 11,
                        playerName = "player name"
                    )
                ),
                searchQuery = ""
            ),
            onHelpClicked = {},
            onToolbarMenuClicked = {},
            onShotItemClicked = {},
            shouldShowAllPlayerShots = false,
            onFilterChipClicked = {},
            onSearchTextChanged = {}
        )
    )
}
