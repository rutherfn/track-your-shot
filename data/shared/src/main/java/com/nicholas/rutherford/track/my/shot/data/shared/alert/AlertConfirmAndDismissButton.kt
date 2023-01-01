package com.nicholas.rutherford.track.my.shot.data.shared.alert

data class AlertConfirmAndDismissButton(
    val onButtonClicked: () -> Unit,
    val buttonText: String
)
