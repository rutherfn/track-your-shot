package com.nicholas.rutherford.track.your.shot.feature.players.shots.selectshot

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.your.shot.AppColors
import com.nicholas.rutherford.track.your.shot.base.resources.R
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.compose.components.EnhancedSearchTextField
import com.nicholas.rutherford.track.your.shot.data.room.response.DeclaredShot
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles

/**
 * Composable screen used to select a declared shot from a list, optionally filtered by a search query.
 *
 * Displays a search field, a list of declared shots, or an empty state if no results are found.
 * Also handles system back button behavior and UI interactions via [SelectShotParams].
 *
 * @param selectShotParams All state and callback parameters required to drive the Select Shot screen.
 */
@Composable
fun SelectShotScreen(selectShotParams: SelectShotParams) {
    var query by remember { mutableStateOf("") }

    BackHandler(true) {
        selectShotParams.onBackButtonClicked()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.White)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        EnhancedSearchTextField(
            value = query,
            onValueChange = { newSearchQuery ->
                query = newSearchQuery
                selectShotParams.onSearchValueChanged(newSearchQuery)
            },
            onClearClick = {
                selectShotParams.onCancelIconClicked(query)
                query = ""
            },
            placeholderValue = stringResource(id = StringsIds.findShotsByName)
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (selectShotParams.state.declaredShotList.isNotEmpty()) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(selectShotParams.state.declaredShotList) { declaredShot ->
                    DeclaredShotItem(
                        declaredShot = declaredShot,
                        onItemClicked = selectShotParams.onItemClicked
                    )
                }
            }
        } else {
            SelectShotEmptyState(
                modifier = Modifier.weight(1f)
            )
        }
    }
}

/**
 * Composable representing a single declared shot item in the list.
 *
 * Provides a clickable surface showing the shot's title. When expanded,
 * it shows the shot's category and description. Users can toggle between
 * collapsed and expanded states via clickable "Show more"/"Show less" text.
 *
 * @param declaredShot The declared shot item to display.
 * @param onItemClicked Callback triggered when the card is clicked, passing the shot ID.
 */
@Composable
fun DeclaredShotItem(
    declaredShot: DeclaredShot,
    onItemClicked: (type: Int) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClicked(declaredShot.id) },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = AppColors.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = declaredShot.title,
                style = TextStyles.smallBold,
                color = AppColors.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (!isExpanded) {
                Text(
                    text = stringResource(id = StringsIds.showMore),
                    style = TextStyles.body.copy(
                        color = AppColors.Orange,
                        textDecoration = TextDecoration.Underline
                    ),
                    modifier = Modifier
                        .clickable { isExpanded = true }
                        .padding(vertical = 4.dp)
                )
            } else {
                Text(
                    text = stringResource(
                        id = R.string.x_shot_category,
                        declaredShot.shotCategory.replaceFirstChar { it.uppercaseChar() }
                    ),
                    style = TextStyles.body,
                    color = AppColors.Black.copy(alpha = 0.8f)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = declaredShot.description,
                    style = TextStyles.body,
                    color = AppColors.Black.copy(alpha = 0.7f)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = stringResource(id = StringsIds.showLess),
                    style = TextStyles.body.copy(
                        color = AppColors.Orange,
                        textDecoration = TextDecoration.Underline
                    ),
                    modifier = Modifier
                        .clickable { isExpanded = false }
                        .padding(vertical = 4.dp)
                )
            }
        }
    }
}

/**
 * Composable displayed when there are no declared shots to show.
 *
 * Shows a centered image and text explaining that no results were found,
 * providing feedback to the user in empty or no-match states.
 *
 * @param modifier Optional [Modifier] to control layout and size.
 */
@Composable
fun SelectShotEmptyState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
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
                painter = painterResource(id = R.drawable.ic_basketball_log_shot_empty_state),
                contentDescription = null,
                modifier = Modifier.size(140.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(id = StringsIds.noShotsResultsFound),
                style = TextStyles.medium,
                color = AppColors.Black,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(id = StringsIds.noShotsResultsFoundDescription),
                style = TextStyles.smallBold,
                color = AppColors.Black.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }
}
