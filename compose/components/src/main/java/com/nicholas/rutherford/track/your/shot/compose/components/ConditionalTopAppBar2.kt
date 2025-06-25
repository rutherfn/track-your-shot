package com.nicholas.rutherford.track.your.shot.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.nicholas.rutherford.track.your.shot.AppColors
import com.nicholas.rutherford.track.your.shot.base.resources.Colors
import com.nicholas.rutherford.track.your.shot.data.shared.appbar.AppBar
import com.nicholas.rutherford.track.your.shot.helper.ui.Padding
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles

/**
 * Custom [TopAppBar] that will either draw a [ComplexTopAppBar] or [SimpleTopAppBar]
 *
 * @param appBar builds out the actual content for displaying [TopAppBar]
 */
@Composable
fun ConditionalTopAppBar2(
    appBar: AppBar2
) {
    if (appBar.shouldShowMiddleContentAppBar) {
        ComplexTopAppBar2(
            appBar = appBar
        )
    } else {
        SimpleTopAppBar2(
            appBar = appBar
        )
    }
}

/**
 * A simple top app bar with optional navigation and secondary icons.
 *
 * @param appBar [AppBar] data model containing title and callbacks
 * @param imageVector optional leading icon
 * @param secondaryImageVector optional trailing icon
 * @param secondaryImageEnabled flag to enable/disable secondary icon
 * @param secondaryIconTint tint color for secondary icon
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SimpleTopAppBar2(
    appBar: AppBar2
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(appBar.toolbarId),
                modifier = Modifier.testTag(tag = TopAppBarTestTags.TOOLBAR_TITLE),
                style = TextStyles.toolbar,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = AppColors.White
            )
        },
        navigationIcon = {
            IconButton(
                onClick = { appBar.onIconButtonClicked?.invoke() },
                enabled = appBar.secondaryImageEnabled ?: true,
                modifier = Modifier.testTag(tag = TopAppBarTestTags.TOOLBAR_BUTTON_ICON)
            ) {
                Icon(
                    imageVector = appBar.imageVector ?: androidx.compose.material.icons.Icons.Filled.ArrowBack,
                    contentDescription = appBar.iconContentDescription,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        actions = {
            if (appBar.shouldShowSecondaryButton) {
                IconButton(
                    onClick = { appBar.onSecondaryIconButtonClicked?.invoke() },
                    modifier = Modifier.testTag(tag = TopAppBarTestTags.TOOLBAR_BUTTON_ICON)
                ) {
                    Icon(
                        imageVector = appBar.secondaryImageVector ?: androidx.compose.material.icons.Icons.Filled.Save,
                        contentDescription = "Secondary icon",
                        tint = appBar.secondaryIconTint ?: Color.Unspecified
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = AppColors.Orange,
        )
    )
    if (appBar.shouldIncludeSpaceAfterDeclaration) {
        Spacer(modifier = Modifier.height(Padding.eight))
    }
}

/**
 * A complex top app bar with a centered title and icons spaced on either side.
 *
 * @param appBar [AppBar] data model containing title and callbacks
 * @param imageVector optional leading icon
 * @param secondaryImageVector optional trailing icon
 * @param secondaryImageEnabled flag to enable/disable secondary icon
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ComplexTopAppBar2(
    appBar: AppBar2
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = AppColors.Orange,
        ),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { appBar.onIconButtonClicked?.invoke() }) {
                    Icon(
                        imageVector = appBar.imageVector ?: androidx.compose.material.icons.Icons.Filled.Menu,
                        contentDescription = appBar.iconContentDescription,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }

                Text(
                    text = stringResource(appBar.toolbarId),
                    modifier = Modifier.testTag(tag = TopAppBarTestTags.TOOLBAR_TITLE),
                    style = TextStyles.toolbar,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = AppColors.White
                )

                IconButton(
                    enabled = appBar.secondaryImageEnabled ?: true,
                    onClick = { appBar.onSecondaryIconButtonClicked?.invoke() }
                ) {
                    Icon(
                        imageVector = appBar.secondaryImageVector ?: androidx.compose.material.icons.Icons.Filled.Add,
                        contentDescription = appBar.secondaryIconContentDescription,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    )
    if (appBar.shouldIncludeSpaceAfterDeclaration) {
        Spacer(modifier = Modifier.height(Padding.eight))
    }
}


