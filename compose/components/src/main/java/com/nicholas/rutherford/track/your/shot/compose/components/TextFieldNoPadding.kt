package com.nicholas.rutherford.track.your.shot.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.your.shot.AppColors
import com.nicholas.rutherford.track.your.shot.base.resources.Colors
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.helper.ui.Padding
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles

/**
 * Default lightweight TextField with no default start padding
 *
 * @param label defined label that is the title text inside the [TextField]
 * @param value defined value that is the body text inside the [TextField]
 * @param onValueChange executes whenever the value is changed from the [TextField] gets the new value
 * @param keyboardOptions sets the [KeyboardOptions] inside the [TextField]
 * @param textStyle sets the [TextStyles] inside the [TextField]
 * @param singleLine sets whenever the [TextField] is a single line or not
 * @param colors sets the [TextFieldColors] inside the [TextField]
 * @param footerText sets the [Text] of the footer of the [TextField] if not null
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TextFieldNoPadding(
    label: String,
    value: String,
    onValueChange: ((String) -> Unit),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
    textStyle: TextStyle = TextStyles.body,
    singleLine: Boolean = true,
    colors: TextFieldColors = TextFieldDefaults.textFieldColors(backgroundColor = Colors.whiteColor),
    footerText: String? = null
) {
    var isFocused by remember { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current

    var shouldClearFocus by remember { mutableStateOf(false) }

    if (shouldClearFocus) {
        LocalFocusManager.current.clearFocus()
        shouldClearFocus = false
        isFocused = false
    }

    // removes the starting padding of the TextField
    val negativeOffSetPaddingX = (-8).dp

    Column {
        BoxWithConstraints(
            modifier = Modifier.clipToBounds()
        ) {
            TextField(
                label = { Text(text = label) },
                modifier = Modifier
                    .requiredWidth(maxWidth + Padding.sixteen)
                    .offset(x = negativeOffSetPaddingX)
                    .onFocusChanged { isFocused = it.isFocused }
                    .fillMaxWidth(),
                value = value,
                visualTransformation = visualTransformation,
                keyboardOptions = keyboardOptions,
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        isFocused = false
                        shouldClearFocus = true
                    }
                ),
                onValueChange = { newUsername -> onValueChange.invoke(newUsername) },
                textStyle = textStyle,
                singleLine = singleLine,
                colors = colors
            )
        }

        if (isFocused) {
            footerText?.let { value ->
                Spacer(modifier = Modifier.height(Padding.eight))
                Text(
                    text = value,
                    style = TextStyles.bodySmall,
                    color = AppColors.LightGray
                )
            }
        }
    }
}

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
