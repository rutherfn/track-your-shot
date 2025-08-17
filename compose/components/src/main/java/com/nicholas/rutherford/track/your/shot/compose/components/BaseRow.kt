package com.nicholas.rutherford.track.your.shot.compose.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.your.shot.AppColors
import com.nicholas.rutherford.track.your.shot.helper.extensions.safeLet
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * BaseRow is a flexible and reusable row component to display a title along with optional
 * subtext and/or icon. Can include click handling and divider display.
 *
 * @param title Main text of the row.
 * @param onClicked Optional lambda invoked when the row is clicked.
 * @param subTextColor Color of the optional subtext. Defaults to [Color.Unspecified].
 * @param titleStyle Style of the title text. Defaults to [TextStyles.smallBold].
 * @param iconTint Tint color of the optional icon. Defaults to MaterialTheme.colorScheme.onSurface with 60% alpha.
 * @param subText Optional text displayed to the right of the title.
 * @param imageVector Optional [ImageVector] displayed to the right of the title or subText.
 * @param shouldShowDivider If true, a horizontal divider will be shown below the row.
 */
@Composable
fun BaseRow(
    title: String,
    onClicked: (() -> Unit)? = null,
    subTextColor: Color = Color.Unspecified,
    titleStyle: TextStyle = TextStyles.smallBold,
    iconTint: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
    subText: String? = null,
    imageVector: ImageVector? = null,
    shouldShowDivider: Boolean = false
) {
    val rowModifier = onClicked?.let {
        Modifier
            .fillMaxSize()
            .fillMaxWidth()
            .clickable { it.invoke() }
            .padding(8.dp)
    } ?: run {
        Modifier
            .fillMaxSize()
            .fillMaxWidth()
            .padding(8.dp)
    }

    Row(
        modifier = rowModifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(start = 4.dp),
            style = titleStyle
        )

        RightRowContent(
            subTextColor = subTextColor,
            subText = subText,
            imageVector = imageVector,
            iconTint = iconTint
        )
    }

    if (shouldShowDivider) {
        HorizontalDivider()
    }
}

@Composable
private fun RightRowContent(
    subTextColor: Color = AppColors.LightGray,
    subText: String? = null,
    imageVector: ImageVector? = null,
    iconTint: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
) {
    if (imageVector != null && subText != null) {
        safeLet(imageVector, subText) { vector, text ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = text,
                    modifier = Modifier.padding(end = 8.dp),
                    style = TextStyles.body,
                    color = subTextColor
                )

                Icon(
                    imageVector = vector,
                    contentDescription = "",
                    tint = iconTint,
                    modifier = Modifier
                        .size(24.dp)
                        .padding(end = 4.dp)
                )
            }
        }
    } else {
        imageVector?.let { vector ->
            Icon(
                imageVector = vector,
                contentDescription = "",
                tint = iconTint,
                modifier = Modifier.padding(start = 4.dp)
            )
        }

        subText?.let { text ->
            if (text.isNotEmpty()) {
                Text(
                    text = text,
                    modifier = Modifier.padding(start = 4.dp),
                    style = TextStyles.body,
                    color = subTextColor
                )
            }
        }
    }
}

/** ------------------- PREVIEWS ------------------- **/

@Preview(showBackground = true)
@Composable
fun BaseRowPreview_TitleOnly() {
    BaseRow(title = "Title Only")
}

@Preview(showBackground = true)
@Composable
fun BaseRowPreview_TitleWithSubText() {
    BaseRow(
        title = "Title with SubText",
        subText = "SubText",
        subTextColor = AppColors.Orange
    )
}

@Preview(showBackground = true)
@Composable
fun BaseRowPreview_TitleWithIcon() {
    BaseRow(
        title = "Title with Icon",
        imageVector = Icons.AutoMirrored.Filled.ArrowForward
    )
}

@Preview(showBackground = true)
@Composable
fun BaseRowPreview_TitleWithSubTextAndIcon() {
    BaseRow(
        title = "Full Row",
        subText = "Details",
        imageVector = Icons.Default.Info,
        subTextColor = AppColors.Black,
        iconTint = AppColors.Orange,
        shouldShowDivider = true,
        onClicked = { }
    )
}
