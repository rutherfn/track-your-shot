package com.nicholas.rutherford.track.your.shot.compose.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.your.shot.AppColors

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * A reusable search input field with built-in search and clear icons.
 * Uses TextFieldValue to properly manage cursor position.
 *
 * @param value The current text input.
 * @param onValueChange Callback triggered when the text changes.
 * @param onClearClick Callback triggered when the clear icon is clicked.
 * @param placeholderValue Placeholder text shown when the input is empty.
 * @param modifier Optional [Modifier] to customize layout or styling.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnhancedSearchTextField(
    value: String,
    onValueChange: (String) -> Unit,
    onClearClick: () -> Unit,
    placeholderValue: String,
    modifier: Modifier = Modifier
) {
    // Use TextFieldValue to preserve cursor position
    // Remember the state without a key so it persists across recompositions
    var currentTextFieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = value,
                selection = TextRange(value.length)
            )
        )
    }
    
    // Track the last text we sent to onValueChange to distinguish user input from external updates
    var lastSentText by remember { mutableStateOf(value) }
    
    // Get focus manager to clear focus when needed
    val focusManager = LocalFocusManager.current
    
    // Sync with external value changes (from ViewModel/StateFlow)
    // Only update if the text changed externally (not from user typing)
    LaunchedEffect(value) {
        // If the external value is different from what we last sent, it's an external update
        // In that case, update the TextFieldValue and place cursor at the end
        if (value != lastSentText && currentTextFieldValue.text != value) {
            currentTextFieldValue = TextFieldValue(
                text = value,
                selection = TextRange(value.length)
            )
            lastSentText = value // Update lastSentText to match external value
        }
    }
    
    OutlinedTextField(
        value = currentTextFieldValue,
        onValueChange = { newValue ->
            currentTextFieldValue = newValue
            lastSentText = newValue.text
            onValueChange(newValue.text)
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        placeholder = { Text(text = placeholderValue, color = AppColors.Black.copy(alpha = 0.5f)) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon",
                tint = AppColors.Black.copy(alpha = 0.6f)
            )
        },
        trailingIcon = {
            if (value.isNotEmpty()) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Clear Search",
                    tint = AppColors.Black.copy(alpha = 0.6f),
                    modifier = Modifier
                        .clickable {
                            onClearClick()
                            focusManager.clearFocus()
                        }
                )
            }
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
            }
        ),
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = AppColors.Orange,
            unfocusedBorderColor = AppColors.Black.copy(alpha = 0.3f),
            cursorColor = AppColors.Orange,
            focusedLabelColor = AppColors.Orange,
            unfocusedLabelColor = AppColors.Black.copy(alpha = 0.5f)
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun EnhancedSearchTextFieldPreview() {
    var text by remember { mutableStateOf("Search term") }

    EnhancedSearchTextField(
        value = text,
        onValueChange = { text = it },
        onClearClick = { text = "" },
        placeholderValue = ""
    )
}
