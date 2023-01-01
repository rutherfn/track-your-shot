package com.nicholas.rutherford.track.my.shot.data.shared.alert

data class Alert(
    val onDismissClicked: () -> Unit,
    val title: String,
    val confirmButton: AlertConfirmAndDismissButton? = null,
    val dismissButton: AlertConfirmAndDismissButton? = null,
    val description: String? = null
)
