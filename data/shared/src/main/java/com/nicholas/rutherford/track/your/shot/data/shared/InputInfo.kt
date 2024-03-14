package com.nicholas.rutherford.track.your.shot.data.shared

data class InputInfo(
    val titleResId: Int,
    val confirmButtonResId: Int,
    val dismissButtonResId: Int,
    val placeholderResId: Int,
    val startingInputAmount: Int?,
    val onConfirmButtonClicked: ((value: String) -> Unit),
    val onDismissButtonClicked: (() -> Unit)? = null,
)
