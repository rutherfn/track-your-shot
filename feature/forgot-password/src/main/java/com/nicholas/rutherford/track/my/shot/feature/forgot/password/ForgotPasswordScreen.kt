package com.nicholas.rutherford.track.my.shot.feature.forgot.password

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.my.shot.compose.components.Content
import com.nicholas.rutherford.track.my.shot.data.shared.appbar.AppBar
import com.nicholas.rutherford.track.my.shot.feature.splash.Colors
import com.nicholas.rutherford.track.my.shot.feature.splash.StringsIds
import com.nicholas.rutherford.track.my.shot.helper.ui.Padding
import com.nicholas.rutherford.track.my.shot.helper.ui.TextStyles

@Composable
fun ForgotPasswordScreen(forgotPasswordScreenParams: ForgotPasswordScreenParams) {
    Content(
        ui = {
            ForgotPasswordScreenContent(forgotPasswordScreenParams = forgotPasswordScreenParams)
        },
        appBar = AppBar(
            toolbarTitle = stringResource(id = StringsIds.forgotPassword),
            onIconButtonClicked = { forgotPasswordScreenParams.onBackButtonClicked() }
        )
    )
}

@Composable
fun ForgotPasswordScreenContent(forgotPasswordScreenParams: ForgotPasswordScreenParams) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(14.dp)
    ) {
        TextField(
            label = { Text(text = stringResource(id = StringsIds.emailRequired)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            value = forgotPasswordScreenParams.state.email ?: stringResource(id = StringsIds.empty),
            onValueChange = { newEmail -> forgotPasswordScreenParams.onEmailValueChanged(newEmail) },
            textStyle = TextStyles.body,
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(backgroundColor = Colors.whiteColor)
        )

        Spacer(modifier = Modifier.height(Padding.four))

        Button(
            onClick = {
                forgotPasswordScreenParams.onSendPasswordResetButtonClicked(forgotPasswordScreenParams.state.email)
            },
            shape = RoundedCornerShape(size = 50.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(Padding.twentyFour),
            colors = ButtonDefaults.buttonColors(backgroundColor = Colors.secondaryColor),
            content = {
                Text(
                    text = stringResource(id = StringsIds.resetPassword),
                    style = TextStyles.small,
                    color = Color.White
                )
            }
        )
    }
}
