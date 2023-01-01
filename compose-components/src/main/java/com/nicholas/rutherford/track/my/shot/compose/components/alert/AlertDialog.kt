package com.nicholas.rutherford.track.my.shot.compose.components.alert

import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.my.shot.compose.components.alert.data.AlertConfirmAndDismissButton

/**
 * Default [AlertDialog] with given params to build alert through out navigation flow
 *
 * @param onDismissClicked triggers whenever the user attempts to dismiss the [AlertDialog]
 * @param title sets the [Text] for the [AlertDialog]
 * @param alertConfirmButton optional param that by default is set to null. If its set to true, will set [AlertDialog] confirmButton properties
 * @param alertDismissButton optional param that by default is set to null. If its set to true, will set [AlertDialog] dismissButton properties
 * @param description optional param that sets the [Text] of the [AlertDialog]
 */
@Composable
fun AlertDialog(
    onDismissClicked: () -> Unit,
    title: String,
    alertConfirmButton: AlertConfirmAndDismissButton? = null,
    alertDismissButton: AlertConfirmAndDismissButton? = null,
    description: String? = null
) {
    AlertDialog(
        onDismissRequest = { onDismissClicked.invoke() },
        confirmButton = {
            alertConfirmButton?.let { confirmButton ->
                TextButton(
                    onClick = { confirmButton.onButtonClicked.invoke() },
                    content = { Text(text = confirmButton.buttonText) }
                )
            }
        },
        dismissButton = {
            alertDismissButton?.let { dismissButton ->
                TextButton(
                    onClick = { dismissButton.onButtonClicked.invoke() },
                    content = { Text(text = dismissButton.buttonText) }
                )
            }
        },
        title = { Text(text = title) },
        text = { description?.let { text -> Text(text = text) } },
        modifier = Modifier.padding(32.dp)
    )
}
