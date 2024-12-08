package com.nicholas.rutherford.track.your.shot.feature.settings.accountinfo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.compose.components.Content
import com.nicholas.rutherford.track.your.shot.data.shared.appbar.AppBar
import com.nicholas.rutherford.track.your.shot.helper.ui.Padding
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles

@Composable
fun AccountInfoScreen(params: AccountInfoParams) {
    Content(
        ui = {
            AccountInfoContent(username = params.usernameArgument, email = params.emailArgument)
        },
        appBar = AppBar(
            toolbarTitle = stringResource(id = StringsIds.accountInfo),
            shouldShowMiddleContentAppBar = false,
            onIconButtonClicked = {
                params.onToolbarMenuClicked.invoke()
            }
        )
    )
}

@Composable
private fun AccountInfoContent(username: String, email: String) {
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

        Text(
            text = stringResource(id = StringsIds.username),
            style = TextStyles.smallBold
        )

        Spacer(modifier = Modifier.height(Padding.four))

        Text(
            text = username,
            style = TextStyles.body
        )

        Spacer(modifier = Modifier.height(Padding.eight))

        Text(
            text = stringResource(id = StringsIds.email),
            style = TextStyles.smallBold
        )

        Spacer(modifier = Modifier.height(Padding.four))

        Text(
            text = email,
            style = TextStyles.body
        )
    }
}
