package com.nicholas.rutherford.track.your.shot.compose.components.dialogs

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import com.nicholas.rutherford.track.your.shot.TrackYourShotTheme
import com.nicholas.rutherford.track.your.shot.base.resources.Colors
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.data.shared.InputInfo

@Composable
fun ShotInputDialog(inputInfo: InputInfo) {
    var numberText by remember { mutableStateOf(value = "") }

    TrackYourShotTheme {
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
                        modifier = Modifier.padding(start = 18.dp)
                    )
                },
                text = {
                    val placeholderText = if (inputInfo.startingInputAmount == null) {
                        stringResource(id = inputInfo.placeholderResId)
                    } else {
                        val inputAmount = inputInfo.startingInputAmount ?: 0

                        if (inputAmount <= 1) {
                            stringResource(id = StringsIds.xShot, inputAmount.toString())
                        } else {
                            stringResource(id = StringsIds.xShots, inputAmount.toString())
                        }
                    }
                    TextField(
                        value = numberText,
                        onValueChange = { value ->
                            val shotInput = value.toIntOrNull() ?: 0
                            numberText = if (shotInput <= 99) {
                                value
                            } else {
                                numberText
                            }
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                inputInfo.onConfirmButtonClicked.invoke(numberText)
                            }
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Colors.whiteColor,
                            unfocusedContainerColor = Colors.whiteColor
                        ),
                        singleLine = true,
                        placeholder = { Text(text = placeholderText) }
                    )
                },
                modifier = Modifier.padding(32.dp)
            )
        }
    }
}
