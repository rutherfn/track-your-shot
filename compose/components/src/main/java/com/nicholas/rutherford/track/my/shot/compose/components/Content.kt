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
import com.nicholas.rutherford.track.my.shot.data.shared.appbar.AppBar
import com.nicholas.rutherford.track.my.shot.helper.ui.Padding

/**
 * Default Content with optional back [TopAppBar]. Used for default content views inside of [Composable]
 *
 * @param ui used to set body of the interface below the optional [TopAppBar] via a [Composable]
 * @param appBar optional param that is responsible for creating a [TopAppBar] with set properties if not null
 * @param imageVector optional param that will set a new default [ImageVector] if not null to the [TopAppBar]
 * [imageVector] is not being passed in to [appBar] to avoid adding Compose dependence to data module
 */
@Composable
fun Content(
    ui: @Composable () -> Unit,
    appBar: AppBar? = null,
    imageVector: ImageVector? = null
) {
    Column {
        appBar?.let { bar ->
            TopAppBar(
                title = {
                    Text(text = bar.toolbarTitle)
                }, navigationIcon = {
                IconButton(onClick = { bar.onIconButtonClicked.invoke() }) {
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
