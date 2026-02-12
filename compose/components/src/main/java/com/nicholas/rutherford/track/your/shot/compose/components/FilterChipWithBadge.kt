package com.nicholas.rutherford.track.your.shot.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.your.shot.AppColors
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles

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