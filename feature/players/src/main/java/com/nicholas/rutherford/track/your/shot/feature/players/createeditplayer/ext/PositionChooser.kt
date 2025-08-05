package com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer.ext

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.your.shot.AppColors
import com.nicholas.rutherford.track.your.shot.TrackYourShotTheme
import com.nicholas.rutherford.track.your.shot.base.resources.R

/**
 * A dropdown menu composable allowing the user to select a basketball player position.
 *
 * Displays a read-only [OutlinedTextField] that, when clicked, expands a dropdown
 * menu with predefined player position options. Selecting an option updates the
 * displayed value and invokes a callback to notify the parent of the new selection.
 *
 * @param onPlayerPositionStringChanged Callback invoked when the user selects a new position.
 *        Receives the selected position as a [String].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PositionChooser(onPlayerPositionStringChanged: (newPosition: String) -> Unit) {
    val pointGuard = stringResource(id = R.string.point_guard)
    val shootingGuard = stringResource(id = R.string.shooting_guard)
    val smallForward = stringResource(id = R.string.small_forward)
    val powerForward = stringResource(id = R.string.power_forward)
    val center = stringResource(id = R.string.center)

    val options = listOf(pointGuard, shootingGuard, smallForward, powerForward, center)

    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(pointGuard)}

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier
    ) {
        OutlinedTextField(
            value = selectedText,
            onValueChange = {},
            readOnly = true,
            label = { Text(text = stringResource(id = R.string.position)) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryEditable).fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AppColors.Orange,
                unfocusedBorderColor = AppColors.Black,
                cursorColor = AppColors.Orange,
                focusedLabelColor = AppColors.Orange,
                unfocusedLabelColor = AppColors.Black
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(color = AppColors.White)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(text = option) },
                    onClick = {
                        onPlayerPositionStringChanged(option)
                        selectedText = option
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview(
    name = "Position Chooser",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun PositionChooserPreview() {
    TrackYourShotTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(PaddingValues(16.dp))
        ) {
            PositionChooser(
                onPlayerPositionStringChanged = { selectedPosition ->
                    println("Selected Position: $selectedPosition")
                }
            )
        }
    }
}


