package com.nicholas.rutherford.track.my.shot.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.nicholas.rutherford.track.my.shot.data.shared.alert.AlertConfirmAndDismissButton
import com.nicholas.rutherford.track.my.shot.helper.ui.Padding
import com.nicholas.rutherford.track.my.shot.helper.ui.TextStyles

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
