package com.nicholas.rutherford.track.my.shot.compose.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nicholas.rutherford.track.my.shot.helper.ui.Padding

@Composable
fun ContentWithTopBackAppBar(
    toolbarTitle: String,
    onBackButtonClicked: (() -> Unit),
    iconContentDescription: String,
    content: @Composable () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                Text(text = toolbarTitle)
            }, navigationIcon = {
                IconButton(onClick = { onBackButtonClicked.invoke() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = iconContentDescription
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(Padding.eight))

        content.invoke()
    }
}