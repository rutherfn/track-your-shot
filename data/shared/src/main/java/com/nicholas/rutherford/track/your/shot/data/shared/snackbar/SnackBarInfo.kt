package com.nicholas.rutherford.track.your.shot.data.shared.snackbar

import androidx.compose.material3.SnackbarDuration

/**
 * Created by Nicholas Rutherford, last edited on 2025-09-28
 *
 * Data class representing the configuration for a snack bar UI component.
 *
 * @property onDateOkClicked Callback invoked when the user confirms a selected date. Provides the selected date as a String.
 * @property onDismissClicked Optional callback invoked when the date picker is dismissed without selecting a date.
 * @property dateValue Optional preselected date value to display in the date picker.
 */
data class SnackBarInfo(
    val message: String,
    val duration: SnackbarDuration,
    val actionLabel: String? = null,
    val withDismissAction: Boolean = false
)