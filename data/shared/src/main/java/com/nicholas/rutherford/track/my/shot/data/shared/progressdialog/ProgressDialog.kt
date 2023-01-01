package com.nicholas.rutherford.track.my.shot.data.shared.progressdialog

data class ProgressDialog (
    val onDismissClicked: () -> Unit,
    val title: String? = null
)