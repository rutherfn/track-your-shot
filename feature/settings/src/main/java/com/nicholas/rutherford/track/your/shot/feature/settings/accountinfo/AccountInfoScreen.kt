package com.nicholas.rutherford.track.your.shot.feature.settings.accountinfo

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.helper.ui.Padding
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Displays the account info screen for info that includes email and username.
 *
 * @param params Contains the state and callback handlers for this screen.
 */
@Composable
fun AccountInfoScreen(params: AccountInfoParams) {
    BackHandler { params.onToolbarMenuClicked.invoke() }
    AccountInfoContent(username = params.usernameArgument, email = params.emailArgument)
}

/**
 * Displays content on the Account Info screen that includes email and username.
 */
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

@Preview(showBackground = true)
@Composable
private fun AccountInfoScreenPreview() {
    AccountInfoScreen(
        params = AccountInfoParams(
            onToolbarMenuClicked = {},
            usernameArgument = "username",
            emailArgument = "email@mailinator.com"
        )
    )
}
