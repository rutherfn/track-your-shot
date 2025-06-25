package com.nicholas.rutherford.track.your.shot.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles

@Composable
fun NumericRowStepper(
    title: String,
    onDownwardClicked: ((value: Int) -> Unit),
    onUpwardClicked: ((value: Int) -> Unit),
    titleStyle: TextStyle = TextStyles.smallBold,
    shouldShowDivider: Boolean = false,
    defaultValue: Int = 0
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                modifier = Modifier.padding(start = 4.dp),
                style = titleStyle,
                color = LocalContentColor.current
            )

            NumericRowStepperRightContent(
                defaultValue = defaultValue,
                onDownwardClicked = onDownwardClicked,
                onUpwardClicked = onUpwardClicked
            )
        }

        if (shouldShowDivider) {
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        }
    }
}

@Composable
fun NumericRowStepperRightContent(
    defaultValue: Int,
    onDownwardClicked: ((value: Int) -> Unit),
    onUpwardClicked: ((value: Int) -> Unit)
) {
    var value by remember { mutableIntStateOf(defaultValue) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        IconButton(
            onClick = {
                if (value != 0) {
                    value -= 1
                    onDownwardClicked.invoke(value)
                }
            },
            modifier = Modifier
                .size(28.dp)
                .background(Color.Gray.copy(alpha = 0.2f), shape = CircleShape)
                .clip(CircleShape)
                .padding(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowDownward,
                contentDescription = "Decrease Value",
                tint = LocalContentColor.current.copy(alpha = 0.0f),
                modifier = Modifier.size(24.dp)
            )
        }

        Text(
            text = value.toString(),
            style = TextStyles.body,
            color = LocalContentColor.current
        )

        IconButton(
            onClick = {
                if (value < 99) {
                    value++
                    onUpwardClicked.invoke(value)
                }
            },
            modifier = Modifier
                .size(28.dp)
                .background(Color.Gray.copy(alpha = 0.2f), shape = CircleShape)
                .clip(CircleShape)
                .padding(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowUpward,
                contentDescription = "Increase Value",
                tint = LocalContentColor.current.copy(alpha = 0.0f),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
@Preview
fun StepperRowPreview() {
    Column(modifier = Modifier.background(Color.White)) {
        NumericRowStepper(
            title = stringResource(id = StringsIds.dateShotsLogged),
            onDownwardClicked = { },
            onUpwardClicked = {}
        )
    }
}
