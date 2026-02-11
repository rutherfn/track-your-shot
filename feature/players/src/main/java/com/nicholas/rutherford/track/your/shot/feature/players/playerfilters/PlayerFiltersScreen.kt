package com.nicholas.rutherford.track.your.shot.feature.players.playerfilters

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.your.shot.AppColors
import com.nicholas.rutherford.track.your.shot.base.resources.Colors
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.compose.components.FilterChips
import com.nicholas.rutherford.track.your.shot.compose.components.NumericRowStepper
import com.nicholas.rutherford.track.your.shot.helper.ui.Padding
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Screen for filtering players by various criteria.
 * Displays filter chips for positions that users can select/deselect.
 *
 * @param params Contains the screen state and callbacks for user interactions.
 */
@Composable
fun PlayerFiltersScreen(params: PlayerFiltersScreenParams) {
    BackHandler(enabled = true) { params.onToolbarMenuClicked.invoke() }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = AppColors.White)
            .verticalScroll(state = rememberScrollState())
            .padding(all = Padding.sixteen)
    ) {
        PlayerFiltersScreenContent(params = params)
    }
}

/**
 * Main content composable for the Player Filters Screen.
 * Orchestrates all filter sections and action buttons.
 *
 * @param params Contains the screen state and callbacks for user interactions.
 */
@Composable
private fun PlayerFiltersScreenContent(params: PlayerFiltersScreenParams) {
    ChoosePositionsFilterContent(params = params)
    HasShotsLoggedScreenContent(params = params)
    NumberOfShotsContent(params = params)
    PlayerFiltersButtons(params = params)
}

/**
 * Composable for the "Choose Positions" filter section.
 * Displays filter chips for selecting player positions (Point Guard, Shooting Guard, etc.).
 *
 * @param params Contains the screen state and callbacks for user interactions.
 */
@Composable
private fun ChoosePositionsFilterContent(params: PlayerFiltersScreenParams) {
    Text(
        text = stringResource(id = StringsIds.choosePositions),
        style = TextStyles.smallBold,
        modifier = Modifier.padding(bottom = 16.dp)
    )

    FilterChips(
        options = params.state.defaultPositions,
        selectedOptions = params.state.selectedPositions,
        onOptionToggled = params.onPositionToggled
    )

    HorizontalDivider(
        modifier = Modifier.padding(vertical = 8.dp),
        color = AppColors.Black.copy(alpha = 0.1f)
    )

    Spacer(modifier = Modifier.height(12.dp))
}

/**
 * Composable for the "Has Shots Logged" filter section.
 * Displays filter chips for filtering players by shot status (Has Shots, No Shots, Both).
 * Uses a special 3-item layout where the first two chips are side-by-side and the third is centered below.
 *
 * @param params Contains the screen state and callbacks for user interactions.
 */
@Composable
private fun HasShotsLoggedScreenContent(params: PlayerFiltersScreenParams) {
    Text(
        text = stringResource(id = StringsIds.hasShotsLogged),
        style = TextStyles.smallBold,
        modifier = Modifier.padding(bottom = 16.dp)
    )

    FilterChips(
        options = params.state.defaultShotLogsOptions,
        selectedOptions = params.state.selectedShotLogsOptions,
        onOptionToggled = params.onShotLogsToggled
    )

    HorizontalDivider(
        modifier = Modifier.padding(vertical = 8.dp),
        color = AppColors.Black.copy(alpha = 0.1f)
    )

    Spacer(modifier = Modifier.height(12.dp))
}

/**
 * Composable for the "Number of Shots" filter section.
 * Displays numeric steppers for setting minimum and maximum shot counts.
 * The max shots stepper is disabled when no minimum is set.
 * Includes helper text and a "Clear" button to reset shot range filters.
 * Also displays additional info card with last updated date and filter count.
 *
 * @param params Contains the screen state and callbacks for user interactions.
 */
@Composable
private fun NumberOfShotsContent(params: PlayerFiltersScreenParams) {
    Text(
        text = stringResource(id = StringsIds.numberOfShots),
        style = TextStyles.smallBold,
        modifier = Modifier.padding(bottom = 16.dp)
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppColors.OffWhite
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            NumericRowStepper(
                title = stringResource(StringsIds.minShots),
                defaultValue = params.state.minShots ?: 0,
                currentValue = params.state.minShots,
                onDownwardClicked = { value ->
                    params.onMinShotsChanged(value)
                },
                onUpwardClicked = { value ->
                    params.onMinShotsChanged(value)
                },
                shouldShowDivider = false,
                modifier = Modifier,
                titleModifier = Modifier
            )

            Spacer(modifier = Modifier.height(12.dp))

            NumericRowStepper(
                title = stringResource(StringsIds.maxShots),
                defaultValue = params.state.maxShots ?: 0,
                currentValue = params.state.maxShots,
                onDownwardClicked = { value -> params.onMaxShotsChanged(value) },
                onUpwardClicked = { value -> params.onMaxShotsChanged(value) },
                shouldShowDivider = false,
                enabled = params.state.minShots != null,
                modifier = Modifier,
                titleModifier = Modifier
            )

            HorizontalDivider(
                modifier = Modifier.padding(top = 12.dp, bottom = 8.dp),
                color = AppColors.Black.copy(alpha = 0.08f)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = StringsIds.resetDefaults),
                    style = TextStyles.smallBold
                )

                TextButton(
                    onClick = params.onClearShotRangeClicked,
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = stringResource(id = StringsIds.clear),
                        style = TextStyles.bodyBold,
                        color = AppColors.Orange
                    )
                }
            }
        }
    }

    Text(
        text = stringResource(id = StringsIds.setMinimumShotCountDescription),
        style = TextStyles.body,
        color = AppColors.Black.copy(alpha = 0.6f),
        modifier = Modifier.padding(top = 12.dp, start = 4.dp, end = 4.dp)
    )

    Spacer(modifier = Modifier.height(12.dp))

    AdditionalInfoContent(params = params)
}

/**
 * Composable for displaying additional filter information.
 * Shows a card with the last updated timestamp and the current filter count.
 * Only displays information when available (non-empty date or filter count > 0).
 *
 * @param params Contains the screen state and callbacks for user interactions.
 */
@Composable
private fun AdditionalInfoContent(params: PlayerFiltersScreenParams) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppColors.OffWhite
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            if (params.state.lastUpdatedFilterDateValue.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = StringsIds.lastUpdated),
                        style = TextStyles.smallBold
                    )
                    Text(
                        text = params.state.lastUpdatedFilterDateValue,
                        style = TextStyles.body,
                        color = AppColors.Black.copy(alpha = 0.7f)
                    )
                }
            }

            if (params.state.filterCount > 0) {
                if (params.state.lastUpdatedFilterDateValue.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 4.dp),
                        color = AppColors.Black.copy(alpha = 0.08f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = StringsIds.numberOfFilters),
                        style = TextStyles.smallBold
                    )
                    Text(
                        text = params.state.filterCount.toString(),
                        style = TextStyles.bodyBold,
                        color = AppColors.Orange
                    )
                }
            }
        }
    }

    HorizontalDivider(
        modifier = Modifier.padding(vertical = 8.dp),
        color = AppColors.Black.copy(alpha = 0.1f)
    )

    Spacer(modifier = Modifier.height(16.dp))
}

/**
 * Composable for the action buttons at the bottom of the filter screen.
 * Displays "See Results" button (orange) and "Reset Filters" button (red).
 * The reset button is disabled when no filters are active (filterCount == 0).
 *
 * @param params Contains the screen state and callbacks for user interactions.
 */
@Composable
private fun PlayerFiltersButtons(params: PlayerFiltersScreenParams) {
    Button(
        onClick = params.onSeeResultsClicked,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = Padding.twelve),
        colors = ButtonDefaults.buttonColors(containerColor = Colors.secondaryColor),
        shape = RoundedCornerShape(50.dp)
    ) {
        Text(
            text = stringResource(
                id = StringsIds.seeResults,
                params.state.filteredPlayerCount
            ),
            style = TextStyles.bodyBold,
            color = AppColors.White
        )
    }

    Button(
        onClick = params.onResetFiltersClicked,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = Padding.twelve),
        colors = ButtonDefaults.buttonColors(containerColor = AppColors.Red),
        shape = RoundedCornerShape(50.dp),
        enabled = params.state.filterCount > 0
    ) {
        Text(
            text = stringResource(id = StringsIds.resetFilters),
            style = TextStyles.bodyBold,
            color = AppColors.White
        )
    }

    Spacer(modifier = Modifier.height(12.dp))
}

/**
 * Preview of [PlayerFiltersScreen] with no filters applied (default state).
 */
@Preview(showBackground = true)
@Composable
private fun PlayerFiltersScreenNoFiltersPreview() {
    PlayerFiltersScreen(
        params = PlayerFiltersScreenParams(
            state = PlayerFiltersState(
                filterCount = 0,
                lastUpdatedFilterDateValue = "",
                defaultPositions = listOf("Point Guard", "Shooting Guard", "Small Forward", "Power Forward", "Center", "All"),
                selectedPositions = emptyList(),
                defaultShotLogsOptions = listOf("Has Shots", "No Shots", "Both"),
                selectedShotLogsOptions = listOf("Both"),
                minShots = null,
                maxShots = null,
                filteredPlayerCount = 0
            ),
            onToolbarMenuClicked = {},
            onPositionToggled = {},
            onShotLogsToggled = {},
            onMinShotsChanged = {},
            onMaxShotsChanged = {},
            onSeeResultsClicked = {},
            onClearShotRangeClicked = {},
            onResetFiltersClicked = {}
        )
    )
}

/**
 * Preview of [PlayerFiltersScreen] with active filters applied.
 */
@Preview(showBackground = true)
@Composable
private fun PlayerFiltersScreenWithFiltersPreview() {
    PlayerFiltersScreen(
        params = PlayerFiltersScreenParams(
            state = PlayerFiltersState(
                filterCount = 3,
                lastUpdatedFilterDateValue = "Feb 07, 2026 06:01 PM",
                defaultPositions = listOf("Point Guard", "Shooting Guard", "Small Forward", "Power Forward", "Center", "All"),
                selectedPositions = listOf("Point Guard", "Shooting Guard"),
                defaultShotLogsOptions = listOf("Has Shots", "No Shots", "Both"),
                selectedShotLogsOptions = listOf("Has Shots"),
                minShots = 5,
                maxShots = 10,
                filteredPlayerCount = 12
            ),
            onToolbarMenuClicked = {},
            onPositionToggled = {},
            onShotLogsToggled = {},
            onMinShotsChanged = {},
            onMaxShotsChanged = {},
            onSeeResultsClicked = {},
            onClearShotRangeClicked = {},
            onResetFiltersClicked = {}
        )
    )
}

/**
 * Preview of [PlayerFiltersScreen] with max shots disabled (no min shots set).
 */
@Preview(showBackground = true)
@Composable
private fun PlayerFiltersScreenMaxShotsDisabledPreview() {
    PlayerFiltersScreen(
        params = PlayerFiltersScreenParams(
            state = PlayerFiltersState(
                filterCount = 2,
                lastUpdatedFilterDateValue = "Feb 07, 2026 06:01 PM",
                defaultPositions = listOf("Point Guard", "Shooting Guard", "Small Forward", "Power Forward", "Center", "All"),
                selectedPositions = listOf("Point Guard"),
                defaultShotLogsOptions = listOf("Has Shots", "No Shots", "Both"),
                selectedShotLogsOptions = listOf("No Shots"),
                minShots = null,
                maxShots = null,
                filteredPlayerCount = 5
            ),
            onToolbarMenuClicked = {},
            onPositionToggled = {},
            onShotLogsToggled = {},
            onMinShotsChanged = {},
            onMaxShotsChanged = {},
            onSeeResultsClicked = {},
            onClearShotRangeClicked = {},
            onResetFiltersClicked = {}
        )
    )
}
