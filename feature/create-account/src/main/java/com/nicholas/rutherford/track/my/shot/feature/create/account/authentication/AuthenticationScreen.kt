package com.nicholas.rutherford.track.my.shot.feature.create.account.authentication

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import com.nicholas.rutherford.track.my.shot.compose.components.Content
import com.nicholas.rutherford.track.my.shot.data.shared.appbar.AppBar
import com.nicholas.rutherford.track.my.shot.feature.splash.Colors
import com.nicholas.rutherford.track.my.shot.feature.splash.DrawablesIds
import com.nicholas.rutherford.track.my.shot.feature.splash.StringsIds
import com.nicholas.rutherford.track.my.shot.helper.ui.Padding
import com.nicholas.rutherford.track.my.shot.helper.ui.Shared
import com.nicholas.rutherford.track.my.shot.helper.ui.TextStyles
import com.nicholas.rutherford.track.my.shot.navigation.OnLifecycleEvent

const val EMAIL_IMAGE_HEIGHT_WIDTH = 144

@Composable
fun AuthenticationScreen(viewModel: AuthenticationViewModel, usernameArgument: String?, emailArgument: String?) {
    val state = viewModel.authenticationStateFlow.collectAsState().value

    viewModel.updateUsernameAndEmail(
        usernameArgument = usernameArgument,
        emailArgument = emailArgument
    )

    OnLifecycleEvent { _, event ->
        if (event == Lifecycle.Event.ON_RESUME) {
            viewModel.onResume()
        }
    }

    Content(
        ui = {
            AuthenticationScreenContent(state = state, viewModel = viewModel)
        },
        appBar = AppBar(
            toolbarTitle = stringResource(id = StringsIds.verifyAccount),
            onIconButtonClicked = { viewModel.onNavigateClose() }
        ),
        imageVector = Icons.Filled.Close
    )
}

@Composable
fun AuthenticationScreenContent(
    state: AuthenticationState,
    viewModel: AuthenticationViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(Padding.twenty),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painterResource(id = DrawablesIds.email),
            contentDescription = "",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .height(EMAIL_IMAGE_HEIGHT_WIDTH.dp)
                .width(EMAIL_IMAGE_HEIGHT_WIDTH.dp)
                .padding(bottom = Padding.twenty)
        )

        Text(
            text = stringResource(id = StringsIds.emailHasBeenSentToVerifyAccountPleaseOpenEmailSentEmailToVerifyAccount),
            modifier = Modifier.padding(Padding.four),
            style = TextStyles.small,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(Padding.eight))

        Button(
            onClick = { viewModel.onOpenEmailClicked() },
            shape = RoundedCornerShape(size = Shared.buttonDefaultShapeSize),
            modifier = Modifier
                .fillMaxWidth()
                .padding(Padding.eight),
            colors = ButtonDefaults.buttonColors(backgroundColor = Colors.secondaryColor),
            content = {
                Text(
                    text = stringResource(id = StringsIds.openEmail),
                    style = TextStyles.small,
                    color = Color.White
                )
            }
        )

        Button(
            onClick = { viewModel.onResendEmailClicked() },
            shape = RoundedCornerShape(size = Shared.buttonDefaultShapeSize),
            modifier = Modifier
                .fillMaxWidth()
                .padding(Padding.eight),
            colors = ButtonDefaults.buttonColors(backgroundColor = Colors.secondaryColor),
            content = {
                Text(
                    text = stringResource(id = StringsIds.resendEmail),
                    style = TextStyles.small,
                    color = Color.White
                )
            }
        )
    }
}
