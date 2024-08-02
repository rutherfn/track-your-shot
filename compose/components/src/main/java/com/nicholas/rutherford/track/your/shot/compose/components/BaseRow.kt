package com.nicholas.rutherford.track.your.shot.compose.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.your.shot.AppColors
import com.nicholas.rutherford.track.your.shot.helper.extensions.safeLet
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles

/**
 * Basic Row that is responsible for showing content shared inside [Row]
 *
 * @param title sets the [Text] of the [BaseRow]
 * @param onClicked triggers whenever [BaseRow] is clicked
 * @param subText sets the [Text] of a [BaseRow] to right of [title]
 * @param imageVector sets the [ImageVector] of the [Icon] to the right of [BaseRow] if not null
 */
@Composable
fun BaseRow(
    title: String,
    onClicked: (() -> Unit)? = null,
    subTextColor: Color = Color.Unspecified,
    titleStyle: TextStyle = TextStyles.smallBold,
    iconTint: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current),
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
        Divider()
    }
}

@Composable
private fun RightRowContent(
    subTextColor: Color = AppColors.LightGray,
    subText: String? = null,
    imageVector: ImageVector? = null,
    iconTint: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
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
