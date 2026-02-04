package com.nicholas.rutherford.track.your.shot.feature.players.playerlist

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.rememberAsyncImagePainter
import com.nicholas.rutherford.track.your.shot.AppColors
import com.nicholas.rutherford.track.your.shot.base.resources.DrawablesIds
import com.nicholas.rutherford.track.your.shot.base.resources.R
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.compose.components.BottomSheetWithOptions
import com.nicholas.rutherford.track.your.shot.compose.components.EnhancedSearchTextField
import com.nicholas.rutherford.track.your.shot.data.room.response.Player
import com.nicholas.rutherford.track.your.shot.data.room.response.PlayerPositions
import com.nicholas.rutherford.track.your.shot.data.room.response.fullName
import com.nicholas.rutherford.track.your.shot.data.shared.sheet.Sheet
import com.nicholas.rutherford.track.your.shot.helper.extensions.toPlayerPositionAbvId
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Represents the UI state for the Create/Edit Player screen.
 *
 * Displays the main screen for the list of players.
 *
 * This composable shows either the list of players or an empty state UI if no players are available.
 *
 * @param playerListScreenParams Contains the screen state and callbacks used to interact with the screen.
 */
@Composable
fun PlayersListScreen(playerListScreenParams: PlayersListScreenParams) {
    val isPlayerListEmpty = playerListScreenParams.state.playerList.isEmpty()
    val searchQuery = playerListScreenParams.state.searchQuery
    val hasAnyPlayersInDatabase = playerListScreenParams.state.hasAnyPlayersInDatabase

    if (!hasAnyPlayersInDatabase && searchQuery.isEmpty()) {
        AddNewPlayerEmptyStateContent(
            filterCount = playerListScreenParams.state.filterCount,
            onFilterChipClicked = playerListScreenParams.onFilterChipClicked
        )
    } else {
        PlayerListContentWithSearch(
            playerListScreenParams = playerListScreenParams,
            isPlayerListEmpty = isPlayerListEmpty,
            searchQuery = searchQuery
        )
    }
}

/**
 * Displays the search field and either the list of players or empty search results.
 *
 * This content is shown when there are players in the database or when searching.
 * The search field is always visible at the top.
 *
 * @param playerListScreenParams The parameters required for rendering and interacting with the player list.
 * @param isPlayerListEmpty Whether the current player list is empty.
 * @param searchQuery The current search query.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlayerListContentWithSearch(
    playerListScreenParams: PlayersListScreenParams,
    isPlayerListEmpty: Boolean,
    searchQuery: String
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    var isSearchFocused by remember { mutableStateOf(false) }

    BackHandler(enabled = isSearchFocused) {
        focusManager.clearFocus()
    }

    BottomSheetWithOptions(
        sheetState = sheetState,
        sheetInfo = Sheet(
            title = stringResource(id = StringsIds.chooseOption),
            values = playerListScreenParams.state.sheetOptions
        ),
        onSheetItemClicked = { _, index ->
            scope.launch {
                sheetState.hide()
                playerListScreenParams.onSheetItemClicked.invoke(index)
            }
        },
        onCancelItemClicked = { scope.launch { sheetState.hide() } },
        content = {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(modifier = Modifier.fillMaxSize()) {
                    EnhancedSearchTextField(
                        value = searchQuery,
                        onValueChange = playerListScreenParams.onSearchTextChanged,
                        onClearClick = { playerListScreenParams.onSearchTextChanged("") },
                        placeholderValue = stringResource(id = StringsIds.searchPlayers),
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .onFocusChanged { isSearchFocused = it.isFocused }
                    )
                    
                    if (isPlayerListEmpty && searchQuery.isNotEmpty()) {
                        SearchResultsEmptyState(
                            onClearSearch = { playerListScreenParams.onSearchTextChanged("") }
                        )
                    } else if (!isPlayerListEmpty) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(playerListScreenParams.state.playerList) { player ->
                                PlayerItem(
                                    player = player,
                                    onPlayerClicked = playerListScreenParams.onPlayerClicked,
                                    sheetState = sheetState,
                                    scope = scope
                                )
                            }
                        }
                    }
                }
                
                FilterChipWithBadge(
                    filterCount = playerListScreenParams.state.filterCount,
                    onFilterChipClicked = playerListScreenParams.onFilterChipClicked,
                    modifier = Modifier.zIndex(10f).align(Alignment.BottomCenter).padding(bottom = 16.dp)
                )
            }
        }
    )
}

/**
 * Displays a filter chip with optional badge showing the count of active filters.
 *
 * The chip always shows "Filters" text. If there are active filters (filterCount > 0),
 * it displays a circular badge with the count number. The chip is positioned at the
 * bottom center of the screen and is always visible with elevated card styling.
 *
 * @param filterCount The number of active filters. If 0, no badge is shown.
 * @param onFilterChipClicked Callback when the filter chip is clicked.
 * @param modifier Optional modifier for positioning and styling.
 */
@Composable
private fun FilterChipWithBadge(
    filterCount: Int,
    onFilterChipClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clickable { onFilterChipClicked() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (filterCount > 0) {
                AppColors.Orange
            } else {
                AppColors.White
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = stringResource(id = StringsIds.filters),
                style = TextStyles.bodyBold,
                color = if (filterCount > 0) {
                    AppColors.White
                } else {
                    AppColors.Black
                }
            )
            
            if (filterCount > 0) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(AppColors.White),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = filterCount.toString(),
                        style = TextStyles.smallBold,
                        color = AppColors.Orange
                    )
                }
            }
        }
    }
}

/**
 * Displays a single player item as a card.
 *
 * The card shows the player's image, name, and position. Clicking the card opens the bottom sheet.
 *
 * @param player The player data to display.
 * @param onPlayerClicked Callback when a player is selected.
 * @param sheetState Controls the visibility of the bottom sheet.
 * @param scope Coroutine scope to launch showing the sheet.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlayerItem(
    player: Player,
    onPlayerClicked: (player: Player) -> Unit,
    sheetState: SheetState,
    scope: CoroutineScope
) {
    val imagePainter = if (!player.imageUrl.isNullOrEmpty()) {
        rememberAsyncImagePainter(model = player.imageUrl)
    } else {
        painterResource(id = DrawablesIds.launcherRound)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable {
                onPlayerClicked(player)
                scope.launch { sheetState.show() }
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AppColors.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = imagePainter,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(AppColors.OffWhite)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                player.position.value.toPlayerPositionAbvId()?.let { positionId ->
                    Text(
                        text = stringResource(
                            id = R.string.x_position_x_player_name,
                            stringResource(id = positionId),
                            player.fullName()
                        ),
                        style = TextStyles.bodyBold,
                        maxLines = 1
                    )
                }

                Text(
                    text = stringResource(StringsIds.tapForMore),
                    style = TextStyles.body,
                    color = Color.Gray
                )
            }
        }
    }
}

/**
 * Displays the empty state when no players are available in the database.
 *
 * This UI encourages users to add a new player to their list.
 * Note: This does not show the search field since there are no players to search.
 */
@Composable
private fun AddNewPlayerEmptyStateContent(
    filterCount: Int = 0,
    onFilterChipClicked: () -> Unit = {}
) {
    Box(modifier = Modifier.fillMaxSize()) {
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
                    painter = painterResource(id = R.drawable.ic_basketball_player_empty_state),
                    contentDescription = null,
                    modifier = Modifier.size(120.dp)
                )

                Text(
                    text = stringResource(id = StringsIds.noCurrentPlayersAdded),
                    style = TextStyles.medium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                Text(
                    text = stringResource(id = StringsIds.hintAddNewPlayer),
                    style = TextStyles.smallBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
        
        FilterChipWithBadge(
            filterCount = filterCount,
            onFilterChipClicked = onFilterChipClicked,
            modifier = Modifier.zIndex(10f).align(Alignment.BottomCenter).padding(bottom = 16.dp)
        )
    }
}

/**
 * Displays the empty state when search results are empty.
 *
 * This UI is shown when a user searches but no players match the query.
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
                painter = painterResource(id = R.drawable.ic_basketball_player_empty_state),
                contentDescription = null,
                modifier = Modifier.size(120.dp)
            )

            Text(
                text = stringResource(id = StringsIds.noPlayersResultsFound),
                style = TextStyles.medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Text(
                text = stringResource(id = StringsIds.noPlayersResultsFoundDescription),
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
 * Preview of [PlayersListScreen] with players present.
 */
@Composable
@Preview
private fun PlayersListScreenWithItemsPreview() {
    PlayersListScreen(
        playerListScreenParams = PlayersListScreenParams(
            state = PlayersListState(
                playerList = listOf(
                    Player(
                        firstName = "first",
                        lastName = "last",
                        position = PlayerPositions.Center,
                        firebaseKey = "key",
                        imageUrl = null,
                        shotsLoggedList = emptyList()
                    )
                )
            ),
            onToolbarMenuClicked = {},
            onAddPlayerClicked = {},
            onPlayerClicked = {},
            onSheetItemClicked = {},
            onFilterChipClicked = {},
            onSearchTextChanged = {}
        )
    )
}

/**
 * Preview of [PlayersListScreen] in the empty state when no players exist.
 */
@Composable
@Preview
fun PlayerListScreenEmptyStatePreview() {
    PlayersListScreen(
        playerListScreenParams = PlayersListScreenParams(
            state = PlayersListState(playerList = emptyList()),
            onToolbarMenuClicked = {},
            onAddPlayerClicked = {},
            onPlayerClicked = {},
            onSheetItemClicked = {},
            onFilterChipClicked = {},
            onSearchTextChanged = {}
        )
    )
}

/**
 * Preview of [PlayersListScreen] showing the empty search results state.
 */
@Composable
@Preview
fun PlayerListScreenEmptySearchResultsPreview() {
    PlayersListScreen(
        playerListScreenParams = PlayersListScreenParams(
            state = PlayersListState(
                playerList = emptyList(),
                searchQuery = "nonexistent",
                hasAnyPlayersInDatabase = true
            ),
            onToolbarMenuClicked = {},
            onAddPlayerClicked = {},
            onPlayerClicked = {},
            onSheetItemClicked = {},
            onFilterChipClicked = {},
            onSearchTextChanged = {}
        )
    )
}
