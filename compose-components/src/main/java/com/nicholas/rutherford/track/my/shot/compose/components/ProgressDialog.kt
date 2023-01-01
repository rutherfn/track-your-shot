package com.nicholas.rutherford.track.my.shot.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(14.dp)
    ) {
        Dialog(
            onDismissRequest = { onDismissClicked.invoke() },
            properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(100.dp)
                    .background(color = Color.White, shape = RoundedCornerShape(Padding.twelve))
            ) {
                Column {
                    CircularProgressIndicator()
                    title?.let { text ->
                        Text(
                            text = text
                        )
                    }
                }
            }
        }
    }
}
