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
import androidx.compose.ui.res.stringResource
import com.nicholas.rutherford.track.my.shot.feature.splash.StringsIds
import com.nicholas.rutherford.track.my.shot.helper.ui.Padding

@Composable
fun ContentWithTopBackAppBar(
    toolbarTitle: String,
    onBackButtonClicked: (() -> Unit),
    content: @Composable () -> Unit,
    iconContentDescription: Int = StringsIds.empty,
) {
    Column(modifier = Modifier.fillMaxSize()) {
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
}
