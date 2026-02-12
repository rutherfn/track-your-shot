package com.nicholas.rutherford.track.your.shot.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.your.shot.AppColors
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles

/**
 * Determines the badge size based on the filter count.
 *
 * @param filterCount The number of active filters.
 * @return The size in dp for the badge circle.
 */
private fun buildBadgeSize(filterCount: Int): Dp {
    return when {
        filterCount >= Constants.MAX_FILTER_COUNT -> Constants.MAX_BADGE_SIZE.dp
        filterCount >= 10 -> Constants.DOUBLE_DIGIT_BADGE_SIZE.dp
        else -> Constants.SINGLE_DIGIT_BADGE_SIZE.dp
    }
}

/**
 * Formats the filter count text for display in the badge.
 *
 * @param filterCount The number of active filters.
 * @return The formatted text to display (e.g., "1", "12", "99+").
 */
private fun buildBadgeText(filterCount: Int): String {
    return if (filterCount >= Constants.MAX_FILTER_COUNT) {
        Constants.MAX_FILTER_COUNT.toString() + "+"
    } else {
        filterCount.toString()
    }
}

/**
 * Displays a filter chip with optional badge showing the count of active filters.
 *
 * The chip always shows "Filters" text. If there are active filters (filterCount > 0),
 * it displays a circular badge with the count number. The badge size adjusts based on
 * the number of digits: single digit (24dp), double digit (28dp), or 99+ (36dp).
 *
 * @param filterCount The number of active filters. If 0, no badge is shown.
 * @param onFilterChipClicked Callback when the filter chip is clicked.
 * @param modifier Optional modifier for positioning and styling.
 */
@Composable
fun FilterChipWithBadge(
    filterCount: Int,
    onFilterChipClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clickable { onFilterChipClicked() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (filterCount > Constants.ZERO_FILTER_COUNT) {
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
                color = if (filterCount > Constants.ZERO_FILTER_COUNT) {
                    AppColors.White
                } else {
                    AppColors.Black
                }
            )

            if (filterCount > Constants.ZERO_FILTER_COUNT) {
                Box(
                    modifier = Modifier
                        .size(buildBadgeSize(filterCount = filterCount))
                        .clip(CircleShape)
                        .background(AppColors.White),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = buildBadgeText(filterCount = filterCount),
                        style = TextStyles.smallBold,
                        color = AppColors.Orange
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FilterChipWithBadgeNoFiltersPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.White)
            .padding(16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        FilterChipWithBadge(
            filterCount = 0,
            onFilterChipClicked = {},
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FilterChipWithBadgeOneFilterPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.White)
            .padding(16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        FilterChipWithBadge(
            filterCount = 1,
            onFilterChipClicked = {},
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FilterChipWithBadgeMultipleFiltersPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.White)
            .padding(16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        FilterChipWithBadge(
            filterCount = 12,
            onFilterChipClicked = {},
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FilterChipWithBadgeManyFiltersPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.White)
            .padding(16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        FilterChipWithBadge(
            filterCount = 99,
            onFilterChipClicked = {},
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}