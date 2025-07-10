package com.nicholas.rutherford.track.your.shot.feature.login

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.your.shot.AppColors
import com.nicholas.rutherford.track.your.shot.TrackYourShotTheme
import com.nicholas.rutherford.track.your.shot.base.resources.Colors
import com.nicholas.rutherford.track.your.shot.base.resources.DrawablesIds
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.helper.ui.Padding
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles
import com.nicholas.rutherford.track.yourshot.compose.content.test.rule.drawableId

/**
 * Top-level composable for the login screen. Delegates rendering to [LoginScreenContent].
 *
 * @param loginScreenParams UI state and callbacks for the screen.
 */
@Composable
fun LoginScreen(loginScreenParams: LoginScreenParams) {
    TrackYourShotTheme {
        LoginScreenContent(loginScreenParams = loginScreenParams)
    }
}

/**
 * A reusable text field composable for both email and password inputs.
 *
 * @param labelRes String resource for the field label.
 * @param value Current input value.
 * @param onValueChange Callback to update the value.
 * @param testTag Tag used for UI testing.
 * @param isPassword Whether the field should obscure text input.
 * @param onDone Action to perform on IME 'Done'.
 */
@Composable
private fun LoginTextField(
    @StringRes labelRes: Int,
    value: String?,
    onValueChange: (String) -> Unit,
    testTag: String,
    isPassword: Boolean = false,
    onDone: () -> Unit
) {
    TextField(
        label = { Text(text = stringResource(id = labelRes)) },
        value = value ?: stringResource(id = StringsIds.empty),
        onValueChange = onValueChange,
        textStyle = TextStyles.body,
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .testTag(testTag),
        keyboardOptions = KeyboardOptions(
            keyboardType = if (isPassword) {
                KeyboardType.Password
            } else {
                KeyboardType.Text
            },
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = { onDone() }),
        visualTransformation = if (isPassword) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Colors.whiteColor,
            unfocusedContainerColor = Colors.whiteColor
        )
    )
}

/**
 * Displays the content of the login screen including logo, form fields,
 * and actions for login, forgot password, and account creation.
 *
 * @param loginScreenParams Screen state and callbacks.
 */
@Composable
private fun LoginScreenContent(loginScreenParams: LoginScreenParams) {
    val focusManager = LocalFocusManager.current
    var shouldClearFocus by remember { mutableStateOf(false) }

    LaunchedEffect(shouldClearFocus) {
        if (shouldClearFocus) {
            focusManager.clearFocus()
            shouldClearFocus = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(14.dp)
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        loginScreenParams.state.launcherDrawableId?.let { drawableId ->
            Image(
                painter = painterResource(id = drawableId),
                contentDescription = stringResource(id = StringsIds.loginIconDescription),
                modifier = Modifier
                    .scale(2.0f)
                    .padding(bottom = 20.dp)
                    .testTag(LoginTags.LOGIN_APP_IMAGE)
                    .semantics { this.drawableId = drawableId }
            )
        }

        Text(
            text = stringResource(id = StringsIds.proceedWithYourAccount),
            style = TextStyles.small,
            modifier = Modifier.testTag(LoginTags.PROCEED_WITH_YOUR_ACCOUNT_TEXT)
        )

        Spacer(modifier = Modifier.height(Padding.eight))

        Text(
            text = stringResource(id = StringsIds.login),
            style = TextStyles.medium,
            modifier = Modifier
                .padding(8.dp)
                .testTag(LoginTags.LOGIN_TEXT)
        )

        Spacer(modifier = Modifier.height(Padding.eight))

        LoginTextField(
            labelRes = StringsIds.email,
            value = loginScreenParams.state.email,
            onValueChange = loginScreenParams.onEmailValueChanged,
            testTag = LoginTags.EMAIL_TEXT_FIELD,
            onDone = { shouldClearFocus = true }
        )

        Spacer(modifier = Modifier.height(Padding.twenty))

        LoginTextField(
            labelRes = StringsIds.password,
            value = loginScreenParams.state.password,
            onValueChange = loginScreenParams.onPasswordValueChanged,
            testTag = LoginTags.PASSWORD_TEXT_FIELD,
            isPassword = true,
            onDone = { shouldClearFocus = true }
        )

        Spacer(modifier = Modifier.height(Padding.twenty))

        Box(modifier = Modifier.padding(start = Padding.forty, end = Padding.forty)) {
            Button(
                onClick = { loginScreenParams.onLoginButtonClicked() },
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Padding.twelve)
                    .testTag(LoginTags.LOGIN_BUTTON),
                colors = ButtonDefaults.buttonColors(containerColor = Colors.secondaryColor)
            ) {
                Text(
                    text = stringResource(id = StringsIds.login),
                    style = TextStyles.small,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(Padding.twenty))

// TODO: Re-enable once "Forgot Password" functionality is implemented.
// Text(
//     text = stringResource(id = StringsIds.forgotPassword),
//     style = TextStyles.hyperLink,
//     modifier = Modifier
//         .clickable { loginScreenParams.onForgotPasswordClicked() }
//         .testTag(LoginTags.FORGOT_PASSWORD_TEXT)
// )
//
// Spacer(modifier = Modifier.height(Padding.eight))

        Text(
            text = stringResource(id = StringsIds.clickMeToCreateAccount),
            style = TextStyles.hyperLink,
            modifier = Modifier
                .clickable { loginScreenParams.onCreateAccountClicked() }
                .testTag(LoginTags.CLICK_ME_TO_CREATE_ACCOUNT_TEXT)
        )
    }
}

/**
 * Preview of the login screen with mock values.
 */
@Preview
@Composable
private fun LoginScreenPreview() {
    Column(modifier = Modifier.background(AppColors.White)) {
        LoginScreen(
            loginScreenParams = LoginScreenParams(
                state = LoginState(
                    launcherDrawableId = DrawablesIds.launcherRoundTest,
                    email = "emailtest@gmail.com",
                    password = "Password"
                ),
                onEmailValueChanged = {},
                onPasswordValueChanged = {},
                onLoginButtonClicked = {},
                onForgotPasswordClicked = {},
                onCreateAccountClicked = {}
            )
        )
    }
}
