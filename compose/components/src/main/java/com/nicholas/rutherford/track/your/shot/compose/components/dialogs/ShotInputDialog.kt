package com.nicholas.rutherford.track.your.shot.compose.components.dialogs

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.nicholas.rutherford.track.your.shot.TrackMyShotTheme
import com.nicholas.rutherford.track.your.shot.data.shared.InputInfo
import com.nicholas.rutherford.track.your.shot.feature.splash.Colors

@Composable
fun ShotInputDialog(inputInfo: InputInfo) {
    var numberText by remember { mutableStateOf(value = "") }

    TrackMyShotTheme {
        Dialog(
            onDismissRequest = { inputInfo.onDismissButtonClicked?.invoke() },
            properties = DialogProperties()
        ) {
            AlertDialog(
                onDismissRequest = {},
                confirmButton = {
                    TextButton(
                        onClick = { inputInfo.onConfirmButtonClicked.invoke(numberText) },
                        content = {
                            Text(text = stringResource(id = inputInfo.confirmButtonResId))
                        }
                    )
                },
                dismissButton = {
                    TextButton(
                        onClick = { inputInfo.onDismissButtonClicked?.invoke() },
                        content = { Text(text = stringResource(id = inputInfo.dismissButtonResId)) }
                    )
                },
                title = {
                    Text(
                        text = stringResource(id = inputInfo.titleResId),
                        modifier = Modifier.padding(start = 12.dp)
                    )
                },
                text = {
                    TextField(
                        value = numberText,
                        onValueChange = {
                            val count = it.toIntOrNull() ?: 0
                            if (it.matches(Regex(pattern = "\\d+")) && count <= 99) {
                                numberText = it
                            }
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                inputInfo.onConfirmButtonClicked.invoke(numberText)
                            }
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.textFieldColors(backgroundColor = Colors.whiteColor),
                        singleLine = true,
                        placeholder = { Text(text = stringResource(id = inputInfo.placeholderResId)) }
                    )
                },
                modifier = Modifier.padding(32.dp)
            )
        }
    }
}