package com.nicholas.rutherford.track.your.shot.feature.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.your.shot.AppColors
import com.nicholas.rutherford.track.your.shot.base.resources.Colors
import com.nicholas.rutherford.track.your.shot.base.resources.DrawablesIds
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.compose.components.Content
import com.nicholas.rutherford.track.your.shot.helper.ui.Padding
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles
import com.nicholas.rutherford.track.yourshot.compose.content.test.rule.drawableId
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(loginScreenParams: LoginScreenParams) {
    Content(
        ui = {
            LoginScreenContent(loginScreenParams = loginScreenParams)
        }
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun LoginScreenContent(loginScreenParams: LoginScreenParams) {
    var isFocusedFirst by remember { mutableStateOf(false) }
    var isFocusedSecond by remember { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current
    val keyboardControllerTwo = LocalSoftwareKeyboardController.current

    var shouldClearFocusOne by remember { mutableStateOf(false) }
    var shouldClearFocusTwo by remember { mutableStateOf(false) }

    if (shouldClearFocusOne) {
        LocalFocusManager.current.clearFocus()
        shouldClearFocusOne = false
    }

    if (shouldClearFocusTwo) {
        LocalFocusManager.current.clearFocus()
        shouldClearFocusTwo = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(14.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        loginScreenParams.state.launcherDrawableId?.let { launcherDrawableId ->
            Image(
                painter = painterResource(id = launcherDrawableId),
                contentDescription = stringResource(id = StringsIds.loginIconDescription),
                modifier = Modifier
                    .scale(scale = 2.0f)
                    .padding(bottom = 20.dp)
                    .testTag(tag = LoginTags.LOGIN_APP_IMAGE)
                    .semantics { drawableId = launcherDrawableId }
            )
        }

        Text(
            text = stringResource(id = StringsIds.proceedWithYourAccount),
            style = TextStyles.small,
            modifier = Modifier.testTag(tag = LoginTags.PROCEED_WITH_YOUR_ACCOUNT_TEXT)
        )

        Spacer(modifier = Modifier.height(Padding.eight))
        Text(
            text = stringResource(id = StringsIds.login),
            modifier = Modifier
                .padding(8.dp)
                .testTag(tag = LoginTags.LOGIN_TEXT),
            style = TextStyles.medium
        )

        Spacer(modifier = Modifier.height(Padding.eight))

        TextField(
            label = { Text(text = stringResource(id = StringsIds.email)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .onFocusChanged { isFocusedFirst = it.isFocused }
                .testTag(tag = LoginTags.EMAIL_TEXT_FIELD),
            value = loginScreenParams.state.email ?: stringResource(id = StringsIds.empty),
            onValueChange = { newEmail -> loginScreenParams.onEmailValueChanged.invoke(newEmail) },
            textStyle = TextStyles.body,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    isFocusedFirst = false
                    shouldClearFocusOne = true
                }
            ),
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(backgroundColor = Colors.whiteColor)
        )

        Spacer(modifier = Modifier.height(Padding.twenty))
        TextField(
            label = { Text(text = stringResource(id = StringsIds.password)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag(tag = LoginTags.PASSWORD_TEXT_FIELD),
            value = loginScreenParams.state.password ?: stringResource(id = StringsIds.empty),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
            onValueChange = { newPassword -> loginScreenParams.onPasswordValueChanged.invoke(newPassword) },
            textStyle = TextStyles.body,
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(backgroundColor = Colors.whiteColor),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardControllerTwo?.hide()
                    isFocusedSecond = false
                    shouldClearFocusTwo = true
                }
            )
        )

        Spacer(modifier = Modifier.height(Padding.twenty))
        Box(modifier = Modifier.padding(start = Padding.forty, end = Padding.forty)) {
            Button(
                onClick = {
                    loginScreenParams.coroutineScope.launch { loginScreenParams.onLoginButtonClicked.invoke() }
                },
                shape = RoundedCornerShape(size = 50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Padding.twelve)
                    .testTag(tag = LoginTags.LOGIN_BUTTON),
                colors = ButtonDefaults.buttonColors(backgroundColor = Colors.secondaryColor),
                content = {
                    Text(
                        text = stringResource(id = StringsIds.login),
                        style = TextStyles.small,
                        color = Color.White
                    )
                }
            )
        }

        Spacer(modifier = Modifier.height(Padding.twenty))
        ClickableText(
            text = AnnotatedString(stringResource(id = StringsIds.forgotPassword)),
            onClick = { loginScreenParams.onForgotPasswordClicked.invoke() },
            style = TextStyles.hyperLink,
            modifier = Modifier.testTag(tag = LoginTags.FORGOT_PASSWORD_TEXT)
        )

        Spacer(modifier = Modifier.height(Padding.eight))
        ClickableText(
            text = AnnotatedString(stringResource(id = StringsIds.clickMeToCreateAccount)),
            onClick = { loginScreenParams.onCreateAccountClicked.invoke() },
            style = TextStyles.hyperLink,
            modifier = Modifier.testTag(tag = LoginTags.CLICK_ME_TO_CREATE_ACCOUNT_TEXT)
        )
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
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
                onCreateAccountClicked = {},
                coroutineScope = rememberCoroutineScope()
            )
        )
    }
}
