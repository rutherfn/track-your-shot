package com.nicholas.rutherford.track.your.shot.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.your.shot.AppColors
import com.nicholas.rutherford.track.your.shot.helper.ui.Padding
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles

/**
 * Default [BasicTextField] with custom functionality to be reused directly for searching
 *
 * @param value defined value that is the body text inside the [BasicTextField]
 * @param onValueChange executes whenever the value is changed from the [BasicTextField] gets the new value
 * @param onCancelIconClicked executes whenever the cancel [Icon] is clicked
 * @param placeholderValue defined value used for setting a placeholder when [value] is empty
 */
@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun SearchTextField(
    value: String,
    onValueChange: (String) -> Unit,
    onCancelIconClicked: () -> Unit,
    placeholderValue: String
) {
    var isFocused by remember { mutableStateOf(false) }
    var shouldClearFocus by remember { mutableStateOf(false) }

    val interactionSource = remember { MutableInteractionSource() }

    val color = if (isFocused) {
        AppColors.Orange
    } else {
        AppColors.LightGray
    }

    if (shouldClearFocus) {
        LocalFocusManager.current.clearFocus()
        shouldClearFocus = false
    }

    BasicTextField(
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged { isFocused = it.isFocused },
        value = value,
        onValueChange = { onValueChange.invoke(it) },
        textStyle = TextStyles.body,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                isFocused = false
                shouldClearFocus = true
            }
        ),
        singleLine = true
    ) { innerTextField ->
        TextFieldDefaults.TextFieldDecorationBox(
            value = value,
            visualTransformation = VisualTransformation.None,
            innerTextField = innerTextField,
            singleLine = true,
            enabled = true,
            interactionSource = interactionSource,
            placeholder = {
                Text(
                    text = placeholderValue,
                    style = TextStyles.body,
                    color = AppColors.LightGray
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = null,
                    modifier = Modifier
                        .size(20.dp)
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.Cancel,
                    contentDescription = null,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { onCancelIconClicked.invoke() }
                )
            },
            contentPadding = PaddingValues(Padding.four),
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = AppColors.Black,
                unfocusedIndicatorColor = AppColors.Black,
                focusedIndicatorColor = AppColors.Orange
            )
        )
    }

    Divider(
        modifier = Modifier
            .fillMaxWidth()
            .height(2.dp)
            .background(color)
    )
}
