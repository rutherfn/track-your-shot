package com.nicholas.rutherford.track.my.shot.compose.components.alert.data

data class Alert(
    val onDismissClicked: () -> Unit,
    val title: String,
    val alertConfirmButton: AlertConfirmAndDismissButton? = null,
    val alertDismissButton: AlertConfirmAndDismissButton? = null,
    val description: String? = null
)
