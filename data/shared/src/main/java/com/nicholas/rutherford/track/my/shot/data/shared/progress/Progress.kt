package com.nicholas.rutherford.track.my.shot.data.shared.progress

data class Progress(
    val onDismissClicked: () -> Unit,
    val title: String? = null
)
