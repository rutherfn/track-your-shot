package com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots.createeditdeclaredshot

/**
 * Holds user-entered data for creating a new declared shot.
 *
 * @property name Name of the shot being created.
 * @property description Description of the shot, explaining details or context.
 * @property category Category or grouping the shot belongs to
 */
data class CreateShotInfo(
    val name: String = "",
    val description: String = "",
    val category: String = ""
)
