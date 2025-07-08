package com.nicholas.rutherford.track.your.shot.feature.create.account.createaccount

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.your.shot.AppColors
import com.nicholas.rutherford.track.your.shot.TrackYourShotTheme
import com.nicholas.rutherford.track.your.shot.base.resources.Colors
import com.nicholas.rutherford.track.your.shot.base.resources.DrawablesIds
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.compose.components.TextFieldNoPadding
import com.nicholas.rutherford.track.your.shot.helper.ui.Padding
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles

/**
 * Top-level composable for the create account screen. Delegates rendering to [ CreateAccountScreenContent].
 *
 * @param createAccountScreenParams UI state and callbacks for the screen.
 */
@Composable
fun CreateAccountScreen(createAccountScreenParams: CreateAccountScreenParams) {
    TrackYourShotTheme {
        CreateAccountScreenContent(createAccountScreenParams = createAccountScreenParams)
    }
}

/**
 * Main UI content composable for the Create Account screen.
 *
 * Displays form fields for username, email, and password inputs along with helper texts.
 * Provides a button to submit the create account request.
 *
 * @param createAccountScreenParams UI state and callbacks for the screen.
 */
@Composable
fun CreateAccountScreenContent(createAccountScreenParams: CreateAccountScreenParams) {
    BackHandler(enabled = true) { createAccountScreenParams.onBackButtonClicked.invoke() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(Padding.twenty)
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(Padding.twentyFour))

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
            footerText = stringResource(StringsIds.usernameHelperText)
        )

        Spacer(modifier = Modifier.height(Padding.four))

        TextFieldNoPadding(
            label = stringResource(id = StringsIds.emailRequired),
            value = createAccountScreenParams.state.email ?: stringResource(id = StringsIds.empty),
            onValueChange = { newEmail ->
                createAccountScreenParams.onEmailValueChanged(newEmail)
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            footerText = "Email must include: a local part (e.g., username), an '@' symbol, and a domain (e.g., example.com)."
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
            colors = ButtonDefaults.buttonColors(containerColor = Colors.secondaryColor),
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

/**
 * Preview composable showing a sample Create Account screen with mock state and empty callbacks.
 */
@Preview
@Composable
private fun CreateAccountScreenPreview() {
    Column(modifier = Modifier.background(AppColors.White)) {
        CreateAccountScreen(
            createAccountScreenParams = CreateAccountScreenParams(
                state = CreateAccountState(
                    username = "username",
                    email = "emailtest@gmail.com",
                    password = "Password"
                ),
                onUsernameValueChanged = {},
                onEmailValueChanged = {},
                onPasswordValueChanged = {},
                onCreateAccountButtonClicked = {},
                onBackButtonClicked = {}
            )
        )
    }
}
