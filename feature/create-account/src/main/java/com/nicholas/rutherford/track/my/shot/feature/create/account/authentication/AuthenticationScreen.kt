package com.nicholas.rutherford.track.my.shot.feature.create.account.authentication

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.nicholas.rutherford.track.my.shot.compose.components.Content

@Composable
fun AuthenticationScreen(viewModel: AuthenticationViewModel) {
    val state = viewModel.authenticationStateFlow.collectAsState().value

    Content(
        ui = {
            AuthenticationScreenContent(state = state, viewModel = viewModel)
        },
        appBar = null
    )
}

@Composable
fun AuthenticationScreenContent(
    state: AuthenticationState,
    viewModel: AuthenticationViewModel
) {

}