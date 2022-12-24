package com.nicholas.rutherford.track.my.shot.compose.components

import androidx.compose.runtime.Composable
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton

/**
 * Default [AlertDialog] with given params to build alert through out navigation flow
 *
 * @param onDismissClicked triggers whenever the user attempts to dismiss the [AlertDialog]
 * @param onPositiveButtonClicked triggers whenever the user attempts to click the positive [TextButton]
 * @param positiveButtonText [Text] set for the positive button of the [AlertDialog]
 * @param onNegativeButtonClicked triggers whenever the user attempts to click the negative [TextButton]
 * @param negativeButtonText [Text] set for the negative button of the [AlertDialog]
 * @param title [Text] sets the title for the [AlertDialog]
 * @param description [Text] sets the description of the [AlertDialog]
 */
@Composable
fun AlertDialog(
    onDismissClicked: () -> Unit,
    onPositiveButtonClicked: () -> Unit,
    positiveButtonText: String,
    onNegativeButtonClicked: () -> Unit,
    negativeButtonText: String,
    title: String,
    description: String
) {
    AlertDialog(
        onDismissRequest = { onDismissClicked.invoke() },
        confirmButton = {
                        TextButton(
                            onClick = { onPositiveButtonClicked.invoke() },
                            content = { Text(text = positiveButtonText) }
                        )
        },
        dismissButton = {
                        TextButton(
                            onClick = { onNegativeButtonClicked.invoke() },
                            content = { Text(text = negativeButtonText) }
                        )
        },
        title = { Text(text = title) },
        text = { Text(text = description) }
    )
}