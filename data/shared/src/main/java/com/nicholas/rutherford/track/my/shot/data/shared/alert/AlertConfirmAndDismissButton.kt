package com.nicholas.rutherford.track.my.shot.data.shared.alert

data class AlertConfirmAndDismissButton(
    val buttonText: String,
    val onButtonClicked: (() -> Unit)? = null,
)
