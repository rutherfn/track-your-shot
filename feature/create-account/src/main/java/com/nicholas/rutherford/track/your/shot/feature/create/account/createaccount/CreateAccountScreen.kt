package com.nicholas.rutherford.track.your.shot.feature.create.account.createaccount

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.your.shot.base.resources.Colors
import com.nicholas.rutherford.track.your.shot.base.resources.DrawablesIds
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.compose.components.Content
import com.nicholas.rutherford.track.your.shot.compose.components.TextFieldNoPadding
import com.nicholas.rutherford.track.your.shot.data.shared.appbar.AppBar
import com.nicholas.rutherford.track.your.shot.helper.ui.Padding
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles

@Composable
fun CreateAccountScreen(createAccountScreenParams: CreateAccountScreenParams) {
    BackHandler(enabled = true) {
        createAccountScreenParams.onBackButtonClicked.invoke()
    }
    Content(
        ui = {
            CreateAccountScreenContent(createAccountScreenParams = createAccountScreenParams)
        },
        appBar = AppBar(
            toolbarTitle = stringResource(id = StringsIds.createAccount),
            onIconButtonClicked = { createAccountScreenParams.onBackButtonClicked() }
        )
    )
}

@Composable
fun CreateAccountScreenContent(createAccountScreenParams: CreateAccountScreenParams) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(Padding.twenty),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = DrawablesIds.launcherRound),
            contentDescription = stringResource(id = StringsIds.loginIconDescription),
            modifier = Modifier
                .scale(scale = 1.5f)
                .padding(bottom = 20.dp)
        )

        Spacer(modifier = Modifier.height(Padding.four))

        TextFieldNoPadding(
            label = stringResource(id = StringsIds.usernameRequired),
            value = createAccountScreenParams.state.username ?: stringResource(id = StringsIds.empty),
            onValueChange = { newUsername ->
                createAccountScreenParams.onUsernameValueChanged(newUsername)
            },
            footerText = "Username must be 8-30 characters long, contain only letters, numbers, underscores (_), and periods (.), start with a letter, and cannot have consecutive underscores or periods, or start/end with an underscore or period."
        )

        Spacer(modifier = Modifier.height(Padding.four))

        TextFieldNoPadding(
            label = stringResource(id = StringsIds.emailRequired),
            value = createAccountScreenParams.state.email ?: stringResource(id = StringsIds.empty),
            onValueChange = { newEmail ->
                createAccountScreenParams.onEmailValueChanged(newEmail)
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            footerText = "Email must include: a local part (e.g., yourname), an '@' symbol, and a domain (e.g., example.com)."
        )

        Spacer(modifier = Modifier.height(Padding.four))

        TextFieldNoPadding(
            label = stringResource(id = StringsIds.passwordRequired),
            value = createAccountScreenParams.state.password ?: stringResource(id = StringsIds.empty),
            onValueChange = { newPassword ->
                createAccountScreenParams.onPasswordValueChanged(newPassword)
            },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            footerText = stringResource(id = StringsIds.passwordHelperText)
        )

        Spacer(modifier = Modifier.height(Padding.eight))

        Button(
            onClick = { createAccountScreenParams.onCreateAccountButtonClicked() },
            shape = RoundedCornerShape(size = 50.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(Padding.twelve),
            colors = ButtonDefaults.buttonColors(backgroundColor = Colors.secondaryColor),
            content = {
                Text(
                    text = stringResource(id = StringsIds.continueText),
                    style = TextStyles.small,
                    color = Color.White
                )
            }
        )
    }
}
