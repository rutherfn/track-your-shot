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
import androidx.compose.ui.res.stringResource
import com.nicholas.rutherford.track.my.shot.compose.components.alert.data.Alert
import com.nicholas.rutherford.track.my.shot.compose.components.alert.AlertDialog
import com.nicholas.rutherford.track.my.shot.feature.splash.StringsIds
import com.nicholas.rutherford.track.my.shot.helper.ui.Padding

/**
 * Default Content with back [TopAppBar]. Used for default content views
 *
 * @param toolbarTitle sets the title of the [TopAppBar]
 * @param onBackButtonClicked executes whenever the user clicks the [TopAppBar] back button
 * @param content [Composable] used to set body of content below the [TopAppBar]
 * @param iconContentDescription optional param responsible for setting description for back arrow button
 * @param alert optional param by default is set to null. When not null, init the custom [AlertDialog] with its proper data class fields
 */
@Composable
fun ContentWithTopBackAppBar(
    toolbarTitle: String,
    onBackButtonClicked: (() -> Unit),
    content: @Composable () -> Unit,
    iconContentDescription: Int = StringsIds.empty,
    alert: Alert? = null
) {
    Column {
        TopAppBar(
            title = {
                Text(text = toolbarTitle)
            }, navigationIcon = {
            IconButton(onClick = { onBackButtonClicked.invoke() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(id = iconContentDescription)
                )
            }
        }
        )

        Spacer(modifier = Modifier.height(Padding.eight))

        content.invoke()
    }

    alert?.let { alertDialog ->
        AlertDialog(
            onDismissClicked = alertDialog.onDismissClicked,
            title = alertDialog.title,
            alertConfirmButton = alertDialog.alertConfirmButton,
            alertDismissButton = alertDialog.alertDismissButton,
            description = alertDialog.description
        )
    }
}
