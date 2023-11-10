package com.nicholas.rutherford.track.your.shot.data.shared.appbar

data class AppBar(
    val toolbarTitle: String,
    val shouldShowMiddleContentAppBar: Boolean = false,
    val onIconButtonClicked: (() -> Unit)? = null,
    val iconContentDescription: String = "",
    val onSecondaryIconButtonClicked: (() -> Unit)? = null,
    val secondaryIconContentDescription: String? = null
)
