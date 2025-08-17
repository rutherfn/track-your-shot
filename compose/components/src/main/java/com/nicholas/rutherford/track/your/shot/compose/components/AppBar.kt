package com.nicholas.rutherford.track.your.shot.compose.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Data model representing the configuration of a custom Top App Bar in the app.
 *
 * This class is used with [ConditionalTopAppBar], [SimpleTopAppBar], and [ComplexTopAppBar]
 * to define the appearance, behavior, and actions of the app bar.
 *
 * @param toolbarId Resource ID for the toolbar title string. Used if [toolbarTitle] is null.
 * @param toolbarTitle Optional text to display as the toolbar title. Overrides [toolbarId] if not null.
 * @param shouldShow Whether the AppBar should be displayed. Defaults to true.
 * @param shouldShowIcon Whether the leading icon (navigation) should be shown. Defaults to true.
 * @param shouldShowMiddleContentAppBar Whether the AppBar should use the "complex" style with centered content. Defaults to false.
 * @param shouldShowSecondaryButton Whether to display a secondary action icon on the AppBar. Defaults to false.
 * @param shouldIncludeSpaceAfterDeclaration If true, adds spacing below the AppBar for layout purposes. Defaults to true.
 * @param onIconButtonClicked Optional callback triggered when the leading icon is clicked.
 * @param iconContentDescription Content description for the leading icon for accessibility.
 * @param onSecondaryIconButtonClicked Optional callback triggered when the secondary icon is clicked.
 * @param secondaryIconContentDescription Content description for the secondary icon for accessibility.
 * @param imageVector Optional [ImageVector] for the leading icon. Defaults to null (uses a default back/menu icon in the component).
 * @param secondaryImageVector Optional [ImageVector] for the secondary icon. Defaults to null (uses a default save/add icon in the component).
 * @param secondaryImageEnabled Optional boolean to enable or disable the secondary icon button. Defaults to null (enabled by default).
 * @param secondaryIconTint Optional color to tint the secondary icon. Defaults to null (uses MaterialTheme default).
 */
data class AppBar(
    val toolbarId: Int,
    var toolbarTitle: String? = null,
    val shouldShow: Boolean = true,
    val shouldShowIcon: Boolean = true,
    val shouldShowMiddleContentAppBar: Boolean = false,
    val shouldShowSecondaryButton: Boolean = false,
    val shouldIncludeSpaceAfterDeclaration: Boolean = true,
    val onIconButtonClicked: (() -> Unit)? = null,
    val iconContentDescription: String = "",
    val onSecondaryIconButtonClicked: (() -> Unit)? = null,
    val secondaryIconContentDescription: String? = null,
    val imageVector: ImageVector? = null,
    val secondaryImageVector: ImageVector? = null,
    val secondaryImageEnabled: Boolean? = null,
    val secondaryIconTint: Color? = null
)
