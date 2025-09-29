package com.nicholas.rutherford.track.your.shot.data.shared.snackbar

/**
 * Created by Nicholas Rutherford, last edited on 2025-09-28
 *
 * Data class representing the configuration for a snack bar UI component.
 *
 * @property message The message to be displayed in the snack bar.
 * @property actionLabel The label for the action button in the snack bar.
 * @property withDismissAction Whether the snack bar should have a dismiss action.
 */
data class SnackBarInfo(
    val message: String,
    val actionLabel: String? = null,
    val withDismissAction: Boolean = false
)
