package com.nicholas.rutherford.track.my.shot.feature.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import com.nicholas.rutherford.track.my.shot.helper.ui.Padding
import com.nicholas.rutherford.track.my.shot.helper.ui.TextStyles

@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    val state = viewModel.loginStateFlow.collectAsState().value

    Box(modifier = Modifier.fillMaxSize()) {
        ClickableText(
            text = AnnotatedString(stringResource(id = state.clickMeToCreateAccountId)),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(Padding.twenty),
            onClick = { },
            style = TextStyles.HyperLink
        )
    }

    Column(
        modifier = Modifier.padding(Padding.twenty),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
    }
}
