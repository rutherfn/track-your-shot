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
import com.nicholas.rutherford.track.my.shot.data.shared.progressdialog.ProgressDialog
import com.nicholas.rutherford.track.my.shot.helper.ui.Padding

/**
 * Default Content with optional back [TopAppBar]. Used for default content views inside of [Composable]
 *
 * @param ui used to set body of the interface below the optional [TopAppBar] via a [Composable]
 * @param appBar optional param that is responsible for creating a [TopAppBar] with set properties if not null
 * @param alert optional param by default is set to null. When not null, init the custom [AlertDialog] with its proper data class fields
 * @param progress optional param by default is set to null. When not null, init the custom [ProgressDialog] with its proper data class fields
 */
@Composable
fun Content(
    ui: @Composable () -> Unit,
    appBar: AppBar? = null,
    alert: Alert? = null,
    progress: ProgressDialog? = null
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
    }

    alert?.let { alertDialog ->
        AlertDialog(
            onDismissClicked = alertDialog.onDismissClicked,
            title = alertDialog.title,
            confirmButton = alertDialog.confirmButton,
            dismissButton = alertDialog.dismissButton,
            description = alertDialog.description
        )
    }
    progress?.let { progressDialog ->
        ProgressDialog(
            onDismissClicked = progressDialog.onDismissClicked,
            title = progressDialog.title
        )
    }
}
