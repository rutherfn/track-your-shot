package com.nicholas.rutherford.track.your.shot.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.your.shot.AppColors
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles

/**
 * Default [BasicTextField] with custom functionality to be reused directly for searching
 *
 * @param value defined value that is the body text inside the [BasicTextField]
 * @param onValueChange executes whenever the value is changed from the [BasicTextField] gets the new value
 * @param onCancelIconClicked executes whenever the cancel [Icon] is clicked
 * @param placeholderValue defined value used for setting a placeholder when [value] is empty
 */
@Composable
fun SearchTextField(
    value: String,
    onValueChange: (String) -> Unit,
    onCancelIconClicked: () -> Unit,
    placeholderValue: String
) {
    val isFocused by remember { mutableStateOf(false) }
    var shouldClearFocus by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect(shouldClearFocus) {
        if (shouldClearFocus) {
            focusManager.clearFocus()
            shouldClearFocus = false
        }
    }

    val indicatorColor = if (isFocused) AppColors.Orange else AppColors.LightGray

    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        textStyle = TextStyles.body,
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
                contentDescription = "Search icon",
                modifier = Modifier.size(20.dp)
            )
                      },
                trailingIcon = {
                    if (value.isNotEmpty()) {
                        Icon(
                            imageVector = Icons.Filled.Cancel,
                            contentDescription = "Clear text",
                            modifier = Modifier
                                .size(20.dp)
                                .clickable {
                                    onCancelIconClicked()
                                }
                        )
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
                colors = TextFieldDefaults.colors(
                    cursorColor = AppColors.Black,
                    unfocusedIndicatorColor = AppColors.Black,
                    focusedIndicatorColor = AppColors.Orange
                )
            )


    HorizontalDivider(
        modifier = Modifier
            .fillMaxWidth()
            .height(2.dp)
            .background(indicatorColor)
    )
}
