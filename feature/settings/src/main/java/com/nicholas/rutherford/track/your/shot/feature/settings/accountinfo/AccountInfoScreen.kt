package com.nicholas.rutherford.track.your.shot.feature.settings.accountinfo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.your.shot.compose.components.BaseRowCard
import com.nicholas.rutherford.track.your.shot.compose.components.Content
import com.nicholas.rutherford.track.your.shot.data.shared.appbar.AppBar
import com.nicholas.rutherford.track.your.shot.helper.ui.Padding

@Composable
fun AccountInfoScreen(params: AccountInfoParams) {
    Content(
        ui = { AccountInfoContent(params = params) },
        appBar = AppBar(
            toolbarTitle = stringResource(id = params.state.toolbarTitleId),
            shouldShowMiddleContentAppBar = false,
            shouldShowSecondaryButton = true,
            onIconButtonClicked = { params.onToolbarIconButtonClicked.invoke() },
            onSecondaryIconButtonClicked = { params.onToolbarSecondaryIconButtonClicked.invoke() }
        )
    )
}

@Composable
fun AccountInfoContent(params: AccountInfoParams) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(
                start = Padding.sixteen,
                end = Padding.sixteen,
                bottom = Padding.sixteen
            ),
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(modifier = Modifier.height(Padding.sixteen))

        if (params.state.shouldEditAccountInfoDetails) {
            // ui logic goes here
        } else {
            BaseRowCard(title = params.state.email)
            Spacer(Modifier.height(8.dp))
            BaseRowCard(title = params.state.username)
        }
    }
}

@Preview
@Composable
fun AccountInfoScreenPreview() {
    Column(modifier = Modifier.background(Color.White)) {
    }
}
