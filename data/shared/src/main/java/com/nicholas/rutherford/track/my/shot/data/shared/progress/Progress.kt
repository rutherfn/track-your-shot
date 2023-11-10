package com.nicholas.rutherford.track.your.shot.data.shared.progress

data class Progress(
    val onDismissClicked: (() -> Unit)? = null,
    val title: String? = null,
    val shouldBeAbleToBeDismissed: Boolean = false
)
