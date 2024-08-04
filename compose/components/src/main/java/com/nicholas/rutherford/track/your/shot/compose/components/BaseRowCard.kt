package com.nicholas.rutherford.track.your.shot.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.your.shot.AppColors
import com.nicholas.rutherford.track.your.shot.helper.ui.Padding
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles

@Composable
fun BaseRowCard(
    title: String,
    onClicked: (() -> Unit)? = null,
    subTextColor: Color = Color.Unspecified,
    titleStyle: TextStyle = TextStyles.smallBold,
    iconTint: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current),
    subText: String? = null,
    imageVector: ImageVector? = null,
    shouldShowDivider: Boolean = false
) {
    Card(
        modifier = Modifier
            .background(AppColors.White)
            .fillMaxWidth()
            .padding(top = Padding.eight, bottom = Padding.eight)
            .clickable { onClicked?.invoke() },
        elevation = 2.dp
    ) {
        Column {
            BaseRow(
                title = title,
                onClicked = onClicked,
                subTextColor = subTextColor,
                titleStyle = titleStyle,
                iconTint = iconTint,
                subText = subText,
                imageVector = imageVector,
                shouldShowDivider = shouldShowDivider,
                shouldFillMaxSize = false
            )
        }
    }
}

@Composable
@Preview
fun BaseRowCardPreview() {
    Column(modifier = Modifier.background(Color.White)) {
        BaseRowCard(
            title = "Sample Base Row Card"
        )
    }
}
