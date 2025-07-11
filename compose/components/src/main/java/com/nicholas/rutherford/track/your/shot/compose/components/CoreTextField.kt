package com.nicholas.rutherford.track.your.shot.compose.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.your.shot.AppColors
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles

/**
 * A reusable [OutlinedTextField] with consistent styling and behavior for text input.
 *
 * This field includes a placeholder, handles the "Done" keyboard action by clearing focus,
 * and applies custom colors and rounded styling to match the app's design system.
 *
 * @param value The current text shown in the field.
 * @param onValueChange Callback invoked with the updated text when the input changes.
 * @param placeholderValue The placeholder text shown when [value] is empty.
 */
@Composable
fun CoreTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholderValue: String
) {
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        placeholder = {
            Text(
                text = placeholderValue,
                style = TextStyles.body
            )
        },
        textStyle = TextStyles.body,
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
            }
        ),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = AppColors.Orange,
            unfocusedBorderColor = AppColors.Black,
            cursorColor = AppColors.Orange,
            focusedLabelColor = AppColors.Orange,
            unfocusedLabelColor = AppColors.Black,
        )
    )
}

