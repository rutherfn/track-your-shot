package com.nicholas.rutherford.track.my.shot.compose.components.alerts

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.nicholas.rutherford.track.my.shot.data.shared.alert.AlertConfirmAndDismissButton
import com.nicholas.rutherford.track.my.shot.helper.ui.Padding

@Composable
fun DialogWithTextField(
    onDismissClicked: () -> Unit,
    title: String,
    textFieldValue: String,
    textFieldLabelValue: String,
    onValueChange: (value: String) -> Unit,
    confirmButton: AlertConfirmAndDismissButton? = null,
    dismissButton: AlertConfirmAndDismissButton? = null
) {
    Dialog(onDismissRequest = { onDismissClicked.invoke() } ) {
        Surface(shape = MaterialTheme.shapes.medium) {
            Column {
                Column(modifier = Modifier.padding(Padding.twentyFour)) {
                    Text(
                        text = title,
                        modifier = Modifier.padding(Padding.sixteen),
                        style = MaterialTheme.typography.h6
                    )

                    Spacer(modifier = Modifier.size(16.dp))

                    OutlinedTextField(
                        value = textFieldValue,
                        onValueChange = { onValueChange.invoke(it) },
                        label =  { Text(text = textFieldLabelValue) }
                    )

                    Row(
                        modifier = Modifier.size(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
                    ) {
                        confirmButton?.let { button ->
                            TextButton(
                                onClick = { button.onButtonClicked.invoke() },
                                content = { Text(text = button.buttonText)}
                            )
                        }

                        dismissButton?.let { button ->
                            TextButton(
                                onClick = { button.onButtonClicked.invoke() },
                                content = { Text(text = button.buttonText)}
                            )
                        }

                    }
                }
            }
        }
    }
}