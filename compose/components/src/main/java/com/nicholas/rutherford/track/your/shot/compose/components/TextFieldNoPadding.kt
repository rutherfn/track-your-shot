package com.nicholas.rutherford.track.your.shot.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.your.shot.AppColors
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.helper.ui.Padding
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Default lightweight TextField with no default start padding
 *
 * @param label defined label that is the title text inside the [TextField]
 * @param value defined value that is the body text inside the [TextField]
 * @param onValueChange executes whenever the value is changed from the [TextField] gets the new value
 * @param keyboardOptions sets the [KeyboardOptions] inside the [TextField]
 * @param textStyle sets the [TextStyles] inside the [TextField]
 * @param singleLine sets whenever the [TextField] is a single line or not
 * @param footerText sets the [Text] of the footer of the [TextField] if not null
 */
@Composable
fun TextFieldNoPadding(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
    textStyle: TextStyle = TextStyles.body,
    singleLine: Boolean = true,
    footerText: String? = null
) {
    var isFocused by remember { mutableStateOf(false) }
    var shouldClearFocus by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val negativeOffsetX = (-8).dp

    if (shouldClearFocus) {
        focusManager.clearFocus()
        shouldClearFocus = false
        isFocused = false
    }

    Column {
        Box(modifier = Modifier.clipToBounds()) {
            TextField(
                value = value,
                onValueChange = onValueChange,
                label = { Text(text = label) },
                visualTransformation = visualTransformation,
                keyboardOptions = keyboardOptions,
                keyboardActions = KeyboardActions(
                    onDone = {
                        isFocused = false
                        shouldClearFocus = true
                    }
                ),
                textStyle = textStyle,
                singleLine = singleLine,
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(x = negativeOffsetX)
                    .onFocusChanged { isFocused = it.isFocused },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent,
                    focusedIndicatorColor = AppColors.Orange,
                    unfocusedIndicatorColor = Color.Black,
                    disabledIndicatorColor = Color.Gray,
                    errorIndicatorColor = Color.Red
                )
            )
        }

        if (isFocused && !footerText.isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(Padding.eight))
            Text(
                text = footerText,
                style = TextStyles.bodySmall,
                color = AppColors.LightGray
            )
        }
    }
}

/**
 * Preview of the [TextFieldNoPadding] component with mock values.
 */
@Preview
@Composable
fun TextFieldNoPaddingPreview() {
    Column(modifier = Modifier.background(AppColors.White)) {
        TextFieldNoPadding(
            label = stringResource(id = StringsIds.usernameRequired),
            value = "Value 1",
            onValueChange = {}
        )
    }
}
