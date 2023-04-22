package com.nicholas.rutherford.track.my.shot.compose.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.nicholas.rutherford.track.my.shot.data.shared.alert.AlertConfirmAndDismissButton
import com.nicholas.rutherford.track.my.shot.helper.ui.Padding
import com.nicholas.rutherford.track.my.shot.helper.ui.TextStyles

/**
 * Default [Dialog] with a given [TextField] inside with optional text and buttons. Used in [Content]
 *
 * @param onDismissClicked triggers whenever the user attempts to dismiss the [Dialog]
 * @param title value of [Text] that sets the title displayed inside of the [Dialog]
 * @param description value of [Text] that sets the description displayed inside of the [Dialog]
 * @param textFieldValue value of the [OutlinedTextField] that displays inside of the [Dialog]
 * @param textFieldLabelValue label of the [OutlinedTextField] that displays inside of the [Dialog]
 * @param onValueChange triggers when the value is changed from the [OutlinedTextField]
 * @param confirmButton optional param that by default is set to null. If its set to true, will set [DialogWithTextField] confirmButton properties
 * @param dismissButton optional param that by default is set to null. If its set to true, will set [DialogWithTextField] dismissButton properties
 */
@Composable
fun DialogWithTextField(
    onDismissClicked: () -> Unit,
    title: String,
    description: String,
    textFieldValue: String,
    textFieldLabelValue: String,
    onValueChange: (value: String) -> Unit,
    confirmButton: AlertConfirmAndDismissButton? = null,
    dismissButton: AlertConfirmAndDismissButton? = null
) {
    Dialog(onDismissRequest = { onDismissClicked.invoke() }) {
        Surface {
            Column {
                Column(modifier = Modifier.padding(Padding.twentyFour)) {
                    Text(
                        text = title,
                        modifier = Modifier.padding(Padding.eight),
                        style = TextStyles.medium
                    )

                    Spacer(modifier = Modifier.size(4.dp))

                    Text(
                        text = description,
                        modifier = Modifier.padding(Padding.eight),
                        style = TextStyles.body
                    )

                    Spacer(modifier = Modifier.size(16.dp))

                    OutlinedTextField(
                        value = textFieldValue,
                        onValueChange = { onValueChange.invoke(it) },
                        label = { Text(text = textFieldLabelValue) }
                    )
                }

                Row(
                    modifier = Modifier.padding(8.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
                ) {
                    confirmButton?.let { button ->
                        TextButton(
                            onClick = { button.onButtonClicked.invoke() },
                            content = { Text(text = button.buttonText) }
                        )
                    }

                    dismissButton?.let { button ->
                        TextButton(
                            onClick = { button.onButtonClicked.invoke() },
                            content = { Text(text = button.buttonText) }
                        )
                    }
                }
            }
        }
    }
}
