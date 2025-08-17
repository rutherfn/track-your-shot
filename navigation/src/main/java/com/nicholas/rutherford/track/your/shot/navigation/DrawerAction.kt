package com.nicholas.rutherford.track.your.shot.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavOptions

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16.
 *
 * Represents a navigation action that can be displayed and triggered
 * from within the app's navigation drawer.
 *
 * This sealed class ensures that all possible drawer actions are defined
 * in one place, making it easier to manage and maintain drawer navigation.
 *
 * @property titleId The string resource ID for the drawer item's title.
 * @property imageVector The icon representing the drawer action.
 * @property route The navigation route associated with this action, or `null` if it does not navigate.
 * @property navOptions Optional navigation options to configure behavior such as
 * animations, back stack handling, and launch mode.
 */
sealed class DrawerAction(
    val titleId: Int,
    val imageVector: ImageVector,
    val route: String?,
    val navOptions: NavOptions?
)
