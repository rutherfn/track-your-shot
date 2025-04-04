package com.nicholas.rutherford.track.your.shot.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.nicholas.rutherford.track.your.shot.data.shared.appbar.AppBar
import com.nicholas.rutherford.track.your.shot.helper.ui.Padding
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles

/**
 * Custom [TopAppBar] that will either draw a [ComplexTopAppBar] or [SimpleTopAppBar]
 *
 * @param appBar builds out the actual content for displaying [TopAppBar]
 * @param imageVector [ImageVector] used for both app bars for displaying [Icon] left of title
 * @param secondaryImageVector [ImageVector] used for [ComplexTopAppBar] and [SimpleTopAppBar] for displaying [Icon] right of title
 * @param secondaryImageEnabled [Boolean] that determines if the secondary image will be enabled or not
 */
@Composable
fun ConditionalTopAppBar(
    appBar: AppBar,
    imageVector: ImageVector?,
    secondaryImageVector: ImageVector?,
    secondaryImageEnabled: Boolean?,
    secondaryIconTint: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
) {
    if (appBar.shouldShowMiddleContentAppBar) {
        ComplexTopAppBar(
            appBar = appBar,
            imageVector = imageVector,
            secondaryImageVector = secondaryImageVector,
            secondaryImageEnabled = secondaryImageEnabled
        )
    } else {
        SimpleTopAppBar(
            appBar = appBar,
            imageVector = imageVector,
            secondaryImageVector = secondaryImageVector,
            secondaryImageEnabled = secondaryImageEnabled,
            secondaryIconTint = secondaryIconTint
        )
    }
}

@Composable
private fun SimpleTopAppBar(
    appBar: AppBar,
    imageVector: ImageVector?,
    secondaryImageVector: ImageVector?,
    secondaryImageEnabled: Boolean?,
    secondaryIconTint: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
) {
    TopAppBar(
        title = {
            Text(
                text = appBar.toolbarTitle,
                modifier = Modifier.testTag(tag = TopAppBarTestTags.TOOLBAR_TITLE),
                style = TextStyles.toolbar,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(
                onClick = { appBar.onIconButtonClicked?.invoke() },
                enabled = secondaryImageEnabled ?: true,
                modifier = Modifier.testTag(tag = TopAppBarTestTags.TOOLBAR_BUTTON_ICON)
            ) {
                Icon(
                    imageVector = imageVector ?: Icons.Filled.ArrowBack,
                    contentDescription = appBar.iconContentDescription
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
                        imageVector = secondaryImageVector ?: Icons.Filled.Save,
                        contentDescription = "Secondary icon",
                        tint = secondaryIconTint
                    )
                }
            }
        }
    )
    if (appBar.shouldIncludeSpaceAfterDeclaration) {
        Spacer(modifier = Modifier.height(Padding.eight))
    }
}

@Composable
private fun ComplexTopAppBar(
    appBar: AppBar,
    imageVector: ImageVector?,
    secondaryImageVector: ImageVector?,
    secondaryImageEnabled: Boolean?
) {
    TopAppBar(
        content = {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    appBar.onIconButtonClicked?.invoke()
                }) {
                    Icon(
                        imageVector = imageVector ?: Icons.Filled.Menu,
                        contentDescription = appBar.iconContentDescription
                    )
                }

                Text(
                    text = appBar.toolbarTitle,
                    modifier = Modifier.testTag(tag = TopAppBarTestTags.TOOLBAR_TITLE),
                    style = TextStyles.toolbar,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                IconButton(
                    enabled = secondaryImageEnabled ?: true,
                    onClick = { appBar.onSecondaryIconButtonClicked?.invoke() }
                ) {
                    Icon(
                        imageVector = secondaryImageVector ?: Icons.Filled.Add,
                        contentDescription = appBar.secondaryIconContentDescription
                    )
                }
            }
        }
    )

    if (appBar.shouldIncludeSpaceAfterDeclaration) {
        Spacer(modifier = Modifier.height(Padding.eight))
    }
}

@Preview
@Composable
fun ConditionalTopAppBarForSimplePreview() {
    ConditionalTopAppBar(
        appBar = AppBar(
            toolbarTitle = "Title 1",
            shouldShowMiddleContentAppBar = false
        ),
        imageVector = null,
        secondaryImageVector = null,
        secondaryImageEnabled = true
    )
}

@Preview
@Composable
fun ConditionalTopAppBarForComplexPreview() {
    ConditionalTopAppBar(
        appBar = AppBar(
            toolbarTitle = "Title 1",
            shouldShowMiddleContentAppBar = true
        ),
        imageVector = null,
        secondaryImageVector = Icons.Filled.AddCircle,
        secondaryImageEnabled = true
    )
}
