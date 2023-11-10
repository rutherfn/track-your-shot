package com.nicholas.rutherford.track.your.shot.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import com.nicholas.rutherford.track.your.shot.data.shared.appbar.AppBar
import com.nicholas.rutherford.track.your.shot.helper.ui.Padding
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles

/**
 * Custom [TopAppBar] that will either draw a [ComplexTopAppBar] or [SimpleTopAppBar]
 *
 * @param appBar builds out the actual content for displaying [TopAppBar]
 * @param imageVector [ImageVector] used for both app bars for displaying [Icon] left of title
 * @param secondaryImageVector [ImageVector] used for [ComplexTopAppBar] for displaying [Icon] right of title  vgy
 */
@Composable
fun ConditionalTopAppBar(
    appBar: AppBar,
    imageVector: ImageVector?,
    secondaryImageVector: ImageVector?
) {
    if (appBar.shouldShowMiddleContentAppBar) {
        ComplexTopAppBar(
            appBar = appBar,
            imageVector = imageVector,
            secondaryImageVector = secondaryImageVector
        )
    } else {
        SimpleTopAppBar(
            appBar = appBar,
            imageVector = imageVector
        )
    }
}

@Composable
private fun SimpleTopAppBar(
    appBar: AppBar,
    imageVector: ImageVector?
) {
    TopAppBar(
        title = {
            Text(
                text = appBar.toolbarTitle,
                modifier = Modifier.testTag(tag = TopAppBarTestTags.TOOLBAR_TITLE),
                style = TextStyles.toolbar
            )
        }, navigationIcon = {
        IconButton(
            onClick = { appBar.onIconButtonClicked?.invoke() },
            modifier = Modifier.testTag(tag = TopAppBarTestTags.TOOLBAR_BUTTON_ICON)
        ) {
            Icon(
                imageVector = imageVector ?: Icons.Filled.ArrowBack,
                contentDescription = appBar.iconContentDescription
            )
        }
    }
    )
    Spacer(modifier = Modifier.height(Padding.eight))
}

@Composable
private fun ComplexTopAppBar(
    appBar: AppBar,
    imageVector: ImageVector?,
    secondaryImageVector: ImageVector?
) {
    TopAppBar(
        content = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxSize(),
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
                    style = TextStyles.toolbar
                )

                IconButton(onClick = { appBar.onSecondaryIconButtonClicked?.invoke() }) {
                    Icon(
                        imageVector = secondaryImageVector ?: Icons.Filled.Add,
                        contentDescription = appBar.secondaryIconContentDescription
                    )
                }
            }
        },
    )
    Spacer(modifier = Modifier.height(Padding.eight))
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
        secondaryImageVector = null
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
        secondaryImageVector = Icons.Filled.AddCircle
    )
}
