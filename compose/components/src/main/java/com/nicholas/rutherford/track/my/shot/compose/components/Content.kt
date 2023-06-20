package com.nicholas.rutherford.track.my.shot.compose.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import com.nicholas.rutherford.track.my.shot.TrackMyShotTheme
import com.nicholas.rutherford.track.my.shot.data.shared.appbar.AppBar
import com.nicholas.rutherford.track.my.shot.helper.ui.Padding
import com.nicholas.rutherford.track.my.shot.helper.ui.TextStyles

/**
 * Default Content with optional back [TopAppBar]. Used for default content views inside of [Composable]
 * It uses [TrackMyShotTheme] to set theming of the content block
 *
 * @param ui used to set body of the interface below the optional [TopAppBar] via a [Composable]
 * @param appBar optional param that is responsible for creating a [TopAppBar] with set properties if not null
 * @param imageVector optional param that will set a new default [ImageVector] if not null to the [TopAppBar]
 * @param [imageVector] optional param that will set a image vector to the [appBar] if not null
 * If the [imageVector] is null then go ahead and set the vector image to [Icons.Filled.ArrowBack]
 * @param invokeFunctionOnInit optional param that will invoke a function on the [Content] function invoke state
 */
@Composable
fun Content(
    ui: @Composable () -> Unit,
    appBar: AppBar? = null,
    imageVector: ImageVector? = null,
    invokeFunctionOnInit: (() -> Unit?)? = null
) {
    TrackMyShotTheme {
        Column {
            invokeFunctionOnInit?.invoke()
            appBar?.let { bar ->
                TopAppBar(
                    title = {
                        Text(
                            text = bar.toolbarTitle,
                            modifier = Modifier.testTag(tag = TopAppBarTestTags.TOOLBAR_TITLE),
                            style = TextStyles.toolbar
                        )
                    }, navigationIcon = {
                    IconButton(
                        onClick = { bar.onIconButtonClicked.invoke() },
                        modifier = Modifier.testTag(tag = TopAppBarTestTags.TOOLBAR_BUTTON_ICON)
                    ) {
                        Icon(
                            imageVector = imageVector ?: Icons.Filled.ArrowBack,
                            contentDescription = bar.iconContentDescription
                        )
                    }
                }
                )

                Spacer(modifier = Modifier.height(Padding.eight))
            }

            ui.invoke()
        }
    }
}
