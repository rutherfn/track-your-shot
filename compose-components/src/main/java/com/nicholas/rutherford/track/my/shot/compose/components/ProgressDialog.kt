package com.nicholas.rutherford.track.my.shot.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.nicholas.rutherford.track.my.shot.feature.splash.Colors
import com.nicholas.rutherford.track.my.shot.helper.ui.Padding

/**
 * Default [Dialog] with given params to build dialogs with [CircularProgressIndicator] used in [Content]
 *
 * @param onDismissClicked triggers whenever the user attempts to dismiss the [Dialog]
 * @param title optional param that will draw a [Text] inside of said [Box]
 */
@Composable
fun ProgressDialog(
    onDismissClicked: () -> Unit,
    title: String? = null
) {
    Dialog(
        onDismissRequest = { onDismissClicked.invoke() },
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(100.dp)
                .background(color = Colors.whiteColor, shape = RoundedCornerShape(Padding.twelve))
        ) {
            CircularProgressIndicator()
            title?.let { text ->
                Text(
                    text = text
                )
            }
        }
    }
}