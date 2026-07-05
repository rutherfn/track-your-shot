package com.nicholas.rutherford.track.your.shot.feature.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.your.shot.AppColors
import com.nicholas.rutherford.track.your.shot.helper.ui.Padding
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles

/**
 * Created by Nicholas Rutherford, last edited on 2026-07-03
 *
 * Displays a horizontally scrollable row of single-select player filter chips.
 *
 * @param filterOptions Available filter options to display.
 * @param selectedFilter Currently selected filter option.
 * @param onFilterSelected Callback invoked when a filter chip is selected.
 * @param modifier Optional modifier applied to the filter row container.
 */
@Composable
fun StatisticsPlayerFilterRow(
    filterOptions: List<String>,
    selectedFilter: String,
    onFilterSelected: (filter: String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(Padding.eight),
        contentPadding = PaddingValues(horizontal = Padding.sixteen)
    ) {
        items(filterOptions) { filterOption ->
            StatisticsPlayerFilterChip(
                label = filterOption,
                isSelected = filterOption == selectedFilter,
                onClicked = { onFilterSelected(filterOption) }
            )
        }
    }
}

/**
 * Displays a single player filter chip used in [StatisticsPlayerFilterRow].
 *
 * @param label Text displayed on the chip.
 * @param isSelected Whether this chip is currently selected.
 * @param onClicked Callback invoked when the chip is clicked.
 */
@Composable
private fun StatisticsPlayerFilterChip(
    label: String,
    isSelected: Boolean,
    onClicked: () -> Unit
) {
    Card(
        modifier = Modifier
            .clickable(onClick = onClicked)
            .then(
                if (!isSelected) {
                    Modifier.border(
                        width = 1.dp,
                        color = AppColors.Black.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(Padding.twenty)
                    )
                } else {
                    Modifier
                }
            ),
        shape = RoundedCornerShape(size = Padding.twenty),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                AppColors.Orange
            } else {
                AppColors.OffWhite
            }
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) {
                Padding.four
            } else {
                Padding.two
            }
        )
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = Padding.sixteen, vertical = Padding.eight),
            style = TextStyles.bodyBold,
            color = if (isSelected) {
                AppColors.White
            } else {
                AppColors.Black
            },
            maxLines = 1
        )
    }
}

/**
 * Preview of [StatisticsPlayerFilterRow] with multiple filter options.
 */
@Preview(showBackground = true)
@Composable
fun StatisticsPlayerFilterRowPreview() {
    StatisticsPlayerFilterRow(
        modifier = Modifier.background(AppColors.White),
        filterOptions = listOf("All", "John Doe", "Jane Smith"),
        selectedFilter = "All",
        onFilterSelected = {}
    )
}
