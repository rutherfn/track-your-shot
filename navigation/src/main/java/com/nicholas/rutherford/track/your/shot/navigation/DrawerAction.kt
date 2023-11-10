package com.nicholas.rutherford.track.your.shot.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavOptions

sealed class DrawerAction(
    val titleId: Int,
    val imageVector: ImageVector,
    val route: String?,
    val navOptions: NavOptions?
)
