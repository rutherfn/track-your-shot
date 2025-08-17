package com.nicholas.rutherford.track.your.shot.data.shared

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Data class representing an educational item with a title, description, associated drawable,
 * button text, and optional visibility flag for additional information.
 *
 * @property title The main title of the educational item.
 * @property description The detailed description of the item.
 * @property drawableResId Resource ID for an associated image or icon.
 * @property buttonText Text displayed on the associated button.
 * @property moreInfoVisible Flag indicating whether additional information is currently visible. Defaults to false.
 */
data class EducationInfo(
    val title: String,
    val description: String,
    val drawableResId: Int,
    val buttonText: String,
    val moreInfoVisible: Boolean = false
)
