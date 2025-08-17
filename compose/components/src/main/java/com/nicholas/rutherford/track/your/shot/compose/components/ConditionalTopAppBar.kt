package com.nicholas.rutherford.track.your.shot.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.tooling.preview.Preview
import com.nicholas.rutherford.track.your.shot.AppColors
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.helper.ui.Padding
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Custom [TopAppBar] that will either draw a [ComplexTopAppBar] or [SimpleTopAppBar]
 *
 * @param appBar builds out the actual content for displaying [TopAppBar]
 */
@Composable
fun ConditionalTopAppBar(appBar: AppBar) {
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
 * @param appBar ]data model containing title and callbacks
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SimpleTopAppBar(appBar: AppBar) {
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
 *  @param appBar ]data model containing title and callbacks
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ComplexTopAppBar(appBar: AppBar) {
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

/**
 * Preview for ConditionalTopAppBar showing the SimpleTopAppBar variant.
 */
@Preview(showBackground = true)
@Composable
fun SimpleTopAppBarPreview() {
    Column(modifier = Modifier.background(Color.White)) {
        ConditionalTopAppBar(
            appBar = AppBar(
                toolbarId = StringsIds.createPlayer,
                toolbarTitle = "Simple Toolbar",
                shouldShowMiddleContentAppBar = false,
                shouldShowIcon = true,
                shouldShowSecondaryButton = true
            )
        )
    }
}

/**
 * Preview for ConditionalTopAppBar showing the ComplexTopAppBar variant.
 */
@Preview(showBackground = true)
@Composable
fun ComplexTopAppBarPreview() {
    Column(modifier = Modifier.background(Color.White)) {
        ConditionalTopAppBar(
            appBar = AppBar(
                toolbarId = StringsIds.createPlayer,
                toolbarTitle = "Complex Toolbar",
                shouldShowMiddleContentAppBar = true,
                shouldShowIcon = true,
                shouldShowSecondaryButton = true
            )
        )
    }
}

/**
 * Combined preview showing multiple top app bars in one screen.
 */
@Preview(showBackground = true)
@Composable
fun CombinedTopAppBarPreview() {
    Column(modifier = Modifier.background(Color.White)) {
        ConditionalTopAppBar(
            appBar = AppBar(
                toolbarId = StringsIds.createPlayer,
                toolbarTitle = "Simple Toolbar",
                shouldShowMiddleContentAppBar = false,
                shouldShowIcon = true,
                shouldShowSecondaryButton = true
            )
        )

        ConditionalTopAppBar(
            appBar = AppBar(
                toolbarId = StringsIds.createPlayer,
                toolbarTitle = "Complex Toolbar",
                shouldShowMiddleContentAppBar = true,
                shouldShowIcon = true,
                shouldShowSecondaryButton = true
            )
        )
    }
}
