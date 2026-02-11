package com.nicholas.rutherford.track.your.shot.compose.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.your.shot.AppColors
import com.nicholas.rutherford.track.your.shot.helper.ui.Padding
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles

private const val SPECIAL_LAYOUT_ITEM_COUNT = 3
private const val TWO_COLUMN_LAYOUT = 2
private const val FIRST_ROW_ITEM_COUNT = 2

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * A reusable filter chips component that displays a list of filter options in a configurable column layout.
 * Each chip can be selected/deselected and displays a checkmark icon when selected.
 *
 * @param options List of filter option strings to display (e.g., "Point Guard", "All").
 * @param selectedOptions List of currently selected filter options.
 * @param onOptionToggled Callback invoked when a filter option is clicked, passing the option string.
 * @param itemsPerRow Number of items to display per row. Defaults to 2 for a two-column layout.
 * @param modifier Optional [Modifier] to customize the layout or styling of the component.
 */
@Composable
fun FilterChips(
    options: List<String>,
    selectedOptions: List<String>,
    onOptionToggled: (String) -> Unit,
    itemsPerRow: Int = TWO_COLUMN_LAYOUT,
    @SuppressLint("ModifierParameter")
    modifier: Modifier = Modifier
) {
    if (options.size == SPECIAL_LAYOUT_ITEM_COUNT && itemsPerRow == TWO_COLUMN_LAYOUT) {
        FilterChipsThreeItemLayout(
            options = options,
            selectedOptions = selectedOptions,
            onOptionToggled = onOptionToggled,
            modifier = modifier
        )
    } else {
        FilterChipsDefaultLayout(
            options = options,
            selectedOptions = selectedOptions,
            onOptionToggled = onOptionToggled,
            itemsPerRow = itemsPerRow,
            modifier = modifier
        )
    }
}

/**
 * Special layout for exactly 3 items: first two side by side, third centered below.
 * The third chip is stretched to match the combined width of the two chips above.
 *
 * @param options List of exactly 3 filter option strings to display.
 * @param selectedOptions List of currently selected filter options.
 * @param onOptionToggled Callback invoked when a filter option is clicked, passing the option string.
 * @param modifier Optional [Modifier] to customize the layout or styling of the component.
 */
@Composable
private fun FilterChipsThreeItemLayout(
    options: List<String>,
    selectedOptions: List<String>,
    onOptionToggled: (String) -> Unit,
    @SuppressLint("ModifierParameter")
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(Padding.twelve)
    ) {
        options.take(FIRST_ROW_ITEM_COUNT).forEach { option ->
            FilterChipItem(
                option = option,
                isSelected = selectedOptions.contains(option),
                onOptionToggled = onOptionToggled,
                modifier = Modifier.weight(1f)
            )
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(Padding.twelve)
    ) {
        Spacer(modifier = Modifier.weight(0.5f))

        FilterChipItem(
            option = options[2],
            isSelected = selectedOptions.contains(options[2]),
            onOptionToggled = onOptionToggled,
            modifier = Modifier.weight(2f)
        )

        Spacer(modifier = Modifier.weight(0.5f))
    }
}

/**
 * Default layout: chunk items into rows based on itemsPerRow.
 * Each row contains up to itemsPerRow chips, with spacers filling empty slots.
 *
 * @param options List of filter option strings to display.
 * @param selectedOptions List of currently selected filter options.
 * @param onOptionToggled Callback invoked when a filter option is clicked, passing the option string.
 * @param itemsPerRow Number of items to display per row.
 * @param modifier Optional [Modifier] to customize the layout or styling of the component.
 */
@Composable
private fun FilterChipsDefaultLayout(
    options: List<String>,
    selectedOptions: List<String>,
    onOptionToggled: (String) -> Unit,
    itemsPerRow: Int,
    @SuppressLint("ModifierParameter")
    modifier: Modifier = Modifier
) {
    options.chunked(itemsPerRow).forEach { rowOptions ->
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(Padding.twelve)
        ) {
            rowOptions.forEach { option ->
                FilterChipItem(
                    option = option,
                    isSelected = selectedOptions.contains(option),
                    onOptionToggled = onOptionToggled,
                    modifier = Modifier.weight(1f)
                )
            }

            repeat(itemsPerRow - rowOptions.size) {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

/**
 * Individual filter chip item that displays a single filter option.
 * Shows a checkmark icon when selected and changes color based on selection state.
 *
 * @param option The filter option string to display.
 * @param isSelected Whether this chip is currently selected.
 * @param onOptionToggled Callback invoked when the chip is clicked, passing the option string.
 * @param modifier Optional [Modifier] to customize the layout or styling of the chip.
 */
@Composable
private fun FilterChipItem(
    option: String,
    isSelected: Boolean,
    onOptionToggled: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clickable { onOptionToggled(option) }
            .then(
                if (!isSelected) {
                    Modifier.border(
                        width = 1.dp,
                        color = AppColors.Black.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(Padding.twenty)
                    )
                } else {
                    Modifier
                }
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                AppColors.Orange
            } else {
                AppColors.OffWhite
            }
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) {
                Padding.eight
            } else {
                Padding.two
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "",
                    tint = AppColors.White,
                    modifier = Modifier.size(Padding.twenty)
                )
            }
            Text(
                text = option,
                style = TextStyles.bodyBold,
                color = if (isSelected) {
                    AppColors.White
                } else {
                    AppColors.Black
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FilterChipsAllUnselectedPreview() {
    Column(
        modifier = Modifier
            .background(AppColors.White)
            .padding(16.dp)
    ) {
        FilterChips(
            options = listOf("Point Guard", "Shooting Guard", "Small Forward", "Power Forward", "Center"),
            selectedOptions = emptyList(),
            onOptionToggled = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FilterChipsSomeSelectedPreview() {
    Column(
        modifier = Modifier
            .background(AppColors.White)
            .padding(16.dp)
    ) {
        FilterChips(
            options = listOf("Point Guard", "Shooting Guard", "Small Forward", "Power Forward", "Center"),
            selectedOptions = listOf("Point Guard", "Small Forward", "Center"),
            onOptionToggled = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FilterChipsAllSelectedPreview() {
    Column(
        modifier = Modifier
            .background(AppColors.White)
            .padding(16.dp)
    ) {
        FilterChips(
            options = listOf("Point Guard", "Shooting Guard", "Small Forward", "Power Forward", "Center", "All"),
            selectedOptions = listOf("Point Guard", "Shooting Guard", "Small Forward", "Power Forward", "Center", "All"),
            onOptionToggled = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FilterChipsOddNumberOfItemsPreview() {
    Column(
        modifier = Modifier
            .background(AppColors.White)
            .padding(16.dp)
    ) {
        FilterChips(
            options = listOf("Option 1", "Option 2", "Option 3"),
            selectedOptions = listOf("Option 1"),
            onOptionToggled = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FilterChipsTwoItemsPreview() {
    Column(
        modifier = Modifier
            .background(AppColors.White)
            .padding(16.dp)
    ) {
        FilterChips(
            options = listOf("Filter A", "Filter B"),
            selectedOptions = listOf("Filter A"),
            onOptionToggled = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FilterChipsThreeItemsPerRowPreview() {
    Column(
        modifier = Modifier
            .background(AppColors.White)
            .padding(16.dp)
    ) {
        FilterChips(
            options = listOf("Option 1", "Option 2", "Option 3", "Option 4", "Option 5"),
            selectedOptions = listOf("Option 1", "Option 3"),
            onOptionToggled = {},
            itemsPerRow = 3
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FilterChipsSingleColumnPreview() {
    Column(
        modifier = Modifier
            .background(AppColors.White)
            .padding(16.dp)
    ) {
        FilterChips(
            options = listOf("Option 1", "Option 2", "Option 3"),
            selectedOptions = listOf("Option 2"),
            onOptionToggled = {},
            itemsPerRow = 1
        )
    }
}
