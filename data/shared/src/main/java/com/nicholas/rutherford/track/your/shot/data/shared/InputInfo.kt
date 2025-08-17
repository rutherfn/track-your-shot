package com.nicholas.rutherford.track.your.shot.data.shared

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Data class representing the configuration and callbacks for an input dialog.
 *
 * @property titleResId Resource ID for the title of the input dialog.
 * @property confirmButtonResId Resource ID for the confirm button text.
 * @property dismissButtonResId Resource ID for the dismiss button text.
 * @property placeholderResId Resource ID for the placeholder text in the input field.
 * @property startingInputAmount Optional starting numeric value for the input field.
 * @property onConfirmButtonClicked Lambda invoked when the confirm button is clicked,
 *                                providing the entered string value.
 * @property onDismissButtonClicked Optional lambda invoked when the dismiss button is clicked.
 */
data class InputInfo(
    val titleResId: Int,
    val confirmButtonResId: Int,
    val dismissButtonResId: Int,
    val placeholderResId: Int,
    val startingInputAmount: Int?,
    val onConfirmButtonClicked: ((value: String) -> Unit),
    val onDismissButtonClicked: (() -> Unit)? = null
)
