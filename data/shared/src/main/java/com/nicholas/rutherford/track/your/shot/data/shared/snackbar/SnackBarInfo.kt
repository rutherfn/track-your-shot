package com.nicholas.rutherford.track.your.shot.data.shared.snackbar

import androidx.compose.material3.SnackbarDuration

/**
 * Created by Nicholas Rutherford, last edited on 2025-09-28
 *
 * Data class representing the configuration for a snack bar UI component.
 *
 * @property
 */
data class SnackBarInfo(
    val message: String,
    val duration: SnackbarDuration,
    val actionLabel: String? = null,
    val withDismissAction: Boolean = false
)