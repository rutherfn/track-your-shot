package com.nicholas.rutherford.track.your.shot.compose.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.your.shot.AppColors

/**
 * A reusable search input field with built-in search and clear icons.
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
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
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
                        .clickable { onClearClick() }
                )
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = AppColors.Orange,
            unfocusedBorderColor = AppColors.Black.copy(alpha = 0.3f),
            cursorColor = AppColors.Orange,
            focusedLabelColor = AppColors.Orange,
            unfocusedLabelColor = AppColors.Black.copy(alpha = 0.5f),
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun EnhancedSearchTextFieldPreview() {
    var text = "Search term"

    EnhancedSearchTextField(
        value = text,
        onValueChange = { text = it },
        onClearClick = { text = "" },
        placeholderValue = ""
    )
}
