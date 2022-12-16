package com.nicholas.rutherford.track.my.shot.compose.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.nicholas.rutherford.track.my.shot.feature.splash.StringsIds
import com.nicholas.rutherford.track.my.shot.helper.ui.Padding

/**
 * Default Content with Top back arrow bar, used for content views
 *
 * @param toolbarTitle sets the title of the [TopAppBar]
 * @param onBackButtonClicked executes whenever the user clicks the [TopAppBar] back button
 * @param content [Composable] used to set body of content below the [TopAppBar]
 * @param iconContentDescription optional param responsible for setting description for back arrow button
 */
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
