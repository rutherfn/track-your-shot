package com.nicholas.rutherford.track.your.shot.feature.settings.accountinfo

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.your.shot.base.resources.R
import com.nicholas.rutherford.track.your.shot.compose.components.BaseRowCard
import com.nicholas.rutherford.track.your.shot.compose.components.Content
import com.nicholas.rutherford.track.your.shot.compose.components.CoreTextField
import com.nicholas.rutherford.track.your.shot.data.shared.appbar.AppBar
import com.nicholas.rutherford.track.your.shot.feature.splash.StringsIds
import com.nicholas.rutherford.track.your.shot.helper.ui.Padding
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles

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
        ),
        secondaryImageVector = params.state.toolbarSecondaryImageVector
    )
}

@Composable
private fun AccountInfoContent(params: AccountInfoParams) {
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
            AccountEditContent(
                state = params.state,
                onNewEmailValueChanged = params.onNewEmailValueChanged,
                onConfirmNewEmailValueChanged = params.onConfirmNewEmailValueChanged,
                onNewUsernameValueChanged = params.onNewUsernameValueChanged,
                onConfirmNewUsernameValueChanged = params.onConfirmNewUsernameValueChanged
            )
        } else {
            AccountInfoViewContent(state = params.state)
        }
    }
}

@Composable
private fun AccountEditContent(
    state: AccountInfoState,
    onNewEmailValueChanged: (email: String) -> Unit,
    onConfirmNewEmailValueChanged: (email: String) -> Unit,
    onNewUsernameValueChanged: (username: String) -> Unit,
    onConfirmNewUsernameValueChanged: (username: String) -> Unit
) {
    Text(
        text = stringResource(id = StringsIds.currentEmail),
        style = TextStyles.smallBold
    )
    Text(
        text = state.email,
        style = TextStyles.body
    )
    Spacer(Modifier.height(16.dp))
    CoreTextField(
        value = state.newEmail,
        onValueChange = { email ->
            onNewEmailValueChanged.invoke(email)
        },
        placeholderValue = stringResource(id = R.string.empty),
        title = stringResource(id = StringsIds.newEmail)
    )
    Spacer(Modifier.height(16.dp))
    CoreTextField(
        value = state.confirmNewEmail,
        onValueChange = { email ->
            onConfirmNewEmailValueChanged.invoke(email)
        },
        placeholderValue = stringResource(id = R.string.empty),
        title = stringResource(id = StringsIds.confirmNewEmail)
    )
    Spacer(Modifier.height(16.dp))
    Text(
        text = stringResource(id = StringsIds.currentUsername),
        style = TextStyles.smallBold
    )
    Text(
        text = state.username,
        style = TextStyles.body
    )
    Spacer(Modifier.height(16.dp))
    CoreTextField(
        value = state.newUsername,
        onValueChange = { username ->
            onNewUsernameValueChanged.invoke(username)
        },
        placeholderValue = stringResource(id = R.string.empty_field),
        title = stringResource(id = StringsIds.newUsername)
    )
    Spacer(Modifier.height(16.dp))
    CoreTextField(
        value = state.confirmNewUsername,
        onValueChange = { username ->
            onConfirmNewUsernameValueChanged.invoke(username)
        },
        placeholderValue = stringResource(id = R.string.empty_field),
        title = stringResource(id = StringsIds.confirmNewUsername)
    )
}

@Composable
private fun AccountInfoViewContent(state: AccountInfoState) {
    Text(
        text = stringResource(id = StringsIds.email),
        style = TextStyles.smallBold,
        modifier = Modifier.padding(start = 8.dp)
    )
    BaseRowCard(title = state.email, titleStyle = TextStyles.body)
    Spacer(Modifier.height(8.dp))
    Text(
        text = stringResource(id = StringsIds.username),
        style = TextStyles.smallBold,
        modifier = Modifier.padding(start = 8.dp)
    )
    BaseRowCard(title = state.username, titleStyle = TextStyles.body)
}

@Preview
@Composable
fun AccountInfoScreenPreview() {
    Column(modifier = Modifier.background(Color.White)) {
    }
}
