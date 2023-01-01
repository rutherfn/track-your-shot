package com.nicholas.rutherford.track.my.shot.compose.components.alert.data

data class AlertConfirmAndDismissButton(
    val onButtonClicked: () -> Unit,
    val buttonText: String
)
