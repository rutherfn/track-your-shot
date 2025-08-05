package com.nicholas.rutherford.track.your.shot.compose.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.your.shot.AppColors
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles

/**
 * Default [TextField] with custom functionality to be reused
 * more specifically, allocating for more space for multiline [TextField]
 *
 * @param value defined value that is the body text inside the [TextField]
 * @param onValueChange executes whenever the value is changed from the [TextField] gets the new value
 * @param placeholderValue defined value used for setting a placeholder when [value] is empty
 */
@Composable
fun CoreMultilineTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholderValue: String
) {
    var isFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }

    var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }

    val color = if (isFocused) {
        AppColors.Orange
    } else {
        AppColors.Black
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged { isFocused = it.isFocused }
            .animateContentSize()
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyles.body,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            ),
            // cursorBrush = SolidColor(AppColors.Black),
            interactionSource = interactionSource,
            singleLine = false,
            placeholder = {
                if (value.isEmpty()) {
                    Text(text = placeholderValue, style = TextStyles.body)
                }
            },
            colors = TextFieldDefaults.colors(
                cursorColor = AppColors.Black,
                focusedIndicatorColor = AppColors.Orange,
                unfocusedIndicatorColor = AppColors.Black,
                disabledIndicatorColor = AppColors.Black
            ),
            shape = TextFieldDefaults.shape
        )
    }

    HorizontalDivider(
        modifier = Modifier
            .fillMaxWidth()
            .height(2.dp),
        color = color
    )
}
