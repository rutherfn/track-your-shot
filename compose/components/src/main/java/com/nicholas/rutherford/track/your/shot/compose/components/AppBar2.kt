package com.nicholas.rutherford.track.your.shot.compose.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class AppBar2(
    val toolbarId: Int,
    val shouldShow: Boolean = true,
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