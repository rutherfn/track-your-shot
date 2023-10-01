package com.nicholas.rutherford.track.my.shot.data.shared.progress

data class Progress(
    val onDismissClicked: (() -> Unit)? = null,
    val title: String? = null,
    val shouldBeAbleToBeDismissed: Boolean = false
)
