package com.nicholas.rutherford.track.your.shot.data.shared.appbar

data class AppBar(
    val toolbarTitle: String,
    val shouldShowMiddleContentAppBar: Boolean = false,
    val shouldShowSecondaryButton: Boolean = false,
    val shouldIncludeSpaceAfterDeclaration: Boolean = true,
    val onIconButtonClicked: (() -> Unit)? = null,
    val iconContentDescription: String = "",
    val onSecondaryIconButtonClicked: (() -> Unit)? = null,
    val secondaryIconContentDescription: String? = null
)
