package com.nicholas.rutherford.track.your.shot.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
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
import com.nicholas.rutherford.track.your.shot.data.shared.appbar.AppBar
import com.nicholas.rutherford.track.your.shot.helper.ui.Padding
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles

/**
 * Custom [TopAppBar] that will either draw a [ComplexTopAppBar] or [SimpleTopAppBar]
 *
 * @param appBar builds out the actual content for displaying [TopAppBar]
 */
@Composable
fun ConditionalTopAppBar(
    appBar: com.nicholas.rutherford.track.your.shot.compose.components.AppBar
) {
    if (appBar.shouldShowMiddleContentAppBar) {
        ComplexTopAppBar(
            appBar = appBar
        )
    } else {
        SimpleTopAppBar(
            appBar = appBar
        )
    }
}

/**
 * A simple top app bar with optional navigation and secondary icons.
 *
 * @param appBar [AppBar] data model containing title and callbacks
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SimpleTopAppBar(
    appBar: com.nicholas.rutherford.track.your.shot.compose.components.AppBar
) {
    TopAppBar(
        title = {
            Text(
                text = appBar.toolbarTitle ?: stringResource(appBar.toolbarId),
                modifier = Modifier.testTag(tag = TopAppBarTestTags.TOOLBAR_TITLE),
                style = TextStyles.toolbar,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = AppColors.White
            )
        },
        navigationIcon = {
            if (appBar.shouldShowIcon) {
                IconButton(
                    onClick = { appBar.onIconButtonClicked?.invoke() },
                    enabled = appBar.secondaryImageEnabled ?: true,
                    modifier = Modifier.testTag(tag = TopAppBarTestTags.TOOLBAR_BUTTON_ICON)
                ) {
                    Icon(
                        imageVector = appBar.imageVector ?: Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = appBar.iconContentDescription,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        },
        actions = {
            if (appBar.shouldShowSecondaryButton) {
                IconButton(
                    onClick = { appBar.onSecondaryIconButtonClicked?.invoke() },
                    modifier = Modifier.testTag(tag = TopAppBarTestTags.TOOLBAR_BUTTON_ICON)
                ) {
                    Icon(
                        imageVector = appBar.secondaryImageVector ?: Icons.Filled.Save,
                        contentDescription = "Secondary icon",
                        tint = appBar.secondaryIconTint ?: Color.Unspecified
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = AppColors.Orange
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
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ComplexTopAppBar(
    appBar: com.nicholas.rutherford.track.your.shot.compose.components.AppBar
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = AppColors.Orange
        ),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { appBar.onIconButtonClicked?.invoke() }) {
                    Icon(
                        imageVector = appBar.imageVector ?: Icons.Filled.Menu,
                        contentDescription = appBar.iconContentDescription,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }

                Text(
                    text = appBar.toolbarTitle ?: stringResource(appBar.toolbarId),
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
                        imageVector = appBar.secondaryImageVector ?: Icons.Filled.Add,
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
