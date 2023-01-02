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
import com.nicholas.rutherford.track.my.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.my.shot.data.shared.appbar.AppBar
import com.nicholas.rutherford.track.my.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.my.shot.helper.ui.Padding

/**
 * Default Content with optional back [TopAppBar]. Used for default content views inside of [Composable]
 *
 * @param ui used to set body of the interface below the optional [TopAppBar] via a [Composable]
 * @param appBar optional param that is responsible for creating a [TopAppBar] with set properties if not null
 * @param progress optional param by default is set to null. When not null, init the custom [Progress] with its proper data class fields
 */
@Composable
fun Content(
    ui: @Composable () -> Unit,
    appBar: AppBar? = null,
    progress: Progress? = null
) {
    Column {
        appBar?.let { bar ->
            TopAppBar(
                title = {
                    Text(text = bar.toolbarTitle)
                }, navigationIcon = {
                IconButton(onClick = { bar.onBackButtonClicked.invoke() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = bar.iconContentDescription
                    )
                }
            }
            )

            Spacer(modifier = Modifier.height(Padding.eight))
        }

        ui.invoke()

        progress?.let { progressDialog ->
            ProgressDialog(
                onDismissClicked = progressDialog.onDismissClicked,
                title = progressDialog.title
            )
        }
    }
}
