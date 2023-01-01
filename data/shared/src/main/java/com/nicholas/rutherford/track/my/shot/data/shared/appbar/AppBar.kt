package com.nicholas.rutherford.track.my.shot.data.shared.appbar

data class AppBar(
    val toolbarTitle: String,
    val onBackButtonClicked: (() -> Unit),
    val iconContentDescription: String = ""
)
