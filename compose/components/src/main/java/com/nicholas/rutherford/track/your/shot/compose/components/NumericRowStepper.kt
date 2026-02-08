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
import androidx.compose.runtime.LaunchedEffect
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
import com.nicholas.rutherford.track.your.shot.AppColors
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * A composable displaying a numeric stepper row with a title and increment/decrement buttons.
 *
 * @param title The title displayed on the left side of the row.
 * @param onDownwardClicked Callback invoked when the decrement button is pressed with the new value.
 * @param onUpwardClicked Callback invoked when the increment button is pressed with the new value.
 * @param titleStyle Optional text style for the title, defaults to `TextStyles.smallBold`.
 * @param shouldShowDivider Optional flag to show a horizontal divider below the row.
 * @param defaultValue Initial value of the stepper, defaults to 0. Used when [currentValue] is null.
 * @param currentValue Optional external state value. If provided, the stepper will use this value instead of internal state.
 *                     This allows external state management for cases where the value needs to be controlled from outside.
 * @param enabled Whether the stepper is enabled. When false, buttons are disabled and content appears grayed out. Defaults to true.
 * @param modifier Optional [Modifier] to customize the row layout. Defaults to padding(8.dp) for backward compatibility.
 * @param titleModifier Optional [Modifier] to customize the title text layout. Defaults to padding(start = 4.dp) for backward compatibility.
 */
@Composable
fun NumericRowStepper(
    title: String,
    onDownwardClicked: ((value: Int) -> Unit),
    onUpwardClicked: ((value: Int) -> Unit),
    titleStyle: TextStyle = TextStyles.smallBold,
    shouldShowDivider: Boolean = false,
    defaultValue: Int = 0,
    currentValue: Int? = null,
    enabled: Boolean = true,
    modifier: Modifier = Modifier.padding(8.dp),
    titleModifier: Modifier = Modifier.padding(start = 4.dp)
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .fillMaxWidth()
                .then(modifier),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                modifier = titleModifier,
                style = titleStyle,
                color = if (enabled) LocalContentColor.current else LocalContentColor.current.copy(alpha = 0.38f)
            )

            NumericRowStepperRightContent(
                defaultValue = defaultValue,
                currentValue = currentValue,
                onDownwardClicked = onDownwardClicked,
                onUpwardClicked = onUpwardClicked,
                enabled = enabled
            )
        }

        if (shouldShowDivider) {
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        }
    }
}

/**
 * Internal composable for the numeric value display and increment/decrement buttons.
 *
 * @param defaultValue Initial value of the stepper. Used when [currentValue] is null.
 * @param currentValue Optional external state value. If provided, uses this instead of internal state.
 * @param onDownwardClicked Callback invoked when decrementing the value.
 * @param onUpwardClicked Callback invoked when incrementing the value.
 * @param enabled Whether the stepper is enabled. When false, buttons are disabled and content appears grayed out.
 */
@Composable
fun NumericRowStepperRightContent(
    defaultValue: Int,
    currentValue: Int?,
    onDownwardClicked: ((value: Int) -> Unit),
    onUpwardClicked: ((value: Int) -> Unit),
    enabled: Boolean = true
) {
    var internalValue by remember { mutableIntStateOf(defaultValue) }

    // Use external value if provided, otherwise use internal state
    val displayValue = currentValue ?: internalValue

    // Sync internal state when defaultValue changes (only if not using external state)
    LaunchedEffect(defaultValue) {
        if (currentValue == null) {
            internalValue = defaultValue
        }
    }

    // Sync internal state when external value changes
    LaunchedEffect(currentValue) {
        currentValue?.let { internalValue = it }
    }

    val disabledAlpha = 0.38f
    val enabledIconTint = AppColors.Black
    val disabledIconTint = AppColors.Black.copy(alpha = disabledAlpha)
    val enabledBackgroundAlpha = 0.2f
    val disabledBackgroundAlpha = 0.1f

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        IconButton(
            onClick = {
                if (enabled && displayValue != 0) {
                    val newValue = displayValue - 1
                    if (currentValue == null) {
                        internalValue = newValue
                    }
                    onDownwardClicked.invoke(newValue)
                }
            },
            enabled = enabled,
            modifier = Modifier
                .size(28.dp)
                .background(
                    Color.Gray.copy(alpha = if (enabled) enabledBackgroundAlpha else disabledBackgroundAlpha),
                    shape = CircleShape
                )
                .clip(CircleShape)
                .padding(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowDownward,
                contentDescription = "Decrease Value",
                tint = if (enabled) enabledIconTint else disabledIconTint,
                modifier = Modifier.size(24.dp)
            )
        }

        Text(
            text = displayValue.toString(),
            style = TextStyles.body,
            color = if (enabled) LocalContentColor.current else LocalContentColor.current.copy(alpha = disabledAlpha)
        )

        IconButton(
            onClick = {
                if (enabled && displayValue < 99) {
                    val newValue = displayValue + 1
                    if (currentValue == null) {
                        internalValue = newValue
                    }
                    onUpwardClicked.invoke(newValue)
                }
            },
            enabled = enabled,
            modifier = Modifier
                .size(28.dp)
                .background(
                    Color.Gray.copy(alpha = if (enabled) enabledBackgroundAlpha else disabledBackgroundAlpha),
                    shape = CircleShape
                )
                .clip(CircleShape)
                .padding(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowUpward,
                contentDescription = "Increase Value",
                tint = if (enabled) enabledIconTint else disabledIconTint,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

/**
 * Preview of [NumericRowStepper] composable.
 */
@Composable
@Preview(showBackground = true)
fun StepperRowPreview() {
    Column(modifier = Modifier.background(Color.White)) {
        NumericRowStepper(
            title = stringResource(id = StringsIds.dateShotsLogged),
            onDownwardClicked = { },
            onUpwardClicked = {}
        )
    }
}
