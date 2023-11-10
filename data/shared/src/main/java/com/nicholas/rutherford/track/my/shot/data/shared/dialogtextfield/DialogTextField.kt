package com.nicholas.rutherford.track.your.shot.data.shared.dialogtextfield

import com.nicholas.rutherford.track.your.shot.data.shared.alert.AlertConfirmAndDismissButton

data class DialogTextField(
    val onDismissClicked: () -> Unit,
    val title: String,
    val textFieldValue: String,
    val textFieldLabelValue: String,
    val onValueChange: (value: String) -> Unit,
    val confirmButton: AlertConfirmAndDismissButton? = null,
    val dismissButton: AlertConfirmAndDismissButton? = null
)
