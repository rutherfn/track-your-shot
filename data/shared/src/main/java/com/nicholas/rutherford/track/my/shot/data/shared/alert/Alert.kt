package com.nicholas.rutherford.track.my.shot.data.shared.alert

data class Alert(
    val title: String,
    val onDismissClicked: (() -> Unit)? = null,
    val confirmButton: AlertConfirmAndDismissButton? = null,
    val dismissButton: AlertConfirmAndDismissButton? = null,
    val description: String? = null
)
