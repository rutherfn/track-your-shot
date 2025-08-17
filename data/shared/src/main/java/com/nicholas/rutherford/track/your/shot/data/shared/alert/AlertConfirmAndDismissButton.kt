package com.nicholas.rutherford.track.your.shot.data.shared.alert

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Data class representing a button used in an alert, which can either confirm an action or dismiss the alert.
 *
 * @property buttonText The text displayed on the button.
 * @property onButtonClicked Optional callback invoked when the button is clicked.
 */
data class AlertConfirmAndDismissButton(
    val buttonText: String,
    val onButtonClicked: (() -> Unit)? = null
)
