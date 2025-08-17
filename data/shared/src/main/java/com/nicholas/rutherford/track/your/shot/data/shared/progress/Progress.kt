package com.nicholas.rutherford.track.your.shot.data.shared.progress

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Data class representing a progress dialog or UI state with optional title, dismissal behavior,
 * and a callback for when the dialog is dismissed.
 *
 * @property onDismissClicked Optional callback invoked when the progress UI is dismissed.
 * @property title Optional title text displayed on the progress UI.
 * @property shouldBeAbleToBeDismissed Flag indicating if the progress UI can be dismissed by the user. Defaults to false.
 */
data class Progress(
    val onDismissClicked: (() -> Unit)? = null,
    val title: String? = null,
    val shouldBeAbleToBeDismissed: Boolean = false
)
