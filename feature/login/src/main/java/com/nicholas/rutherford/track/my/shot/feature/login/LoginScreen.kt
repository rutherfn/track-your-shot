package com.nicholas.rutherford.track.my.shot.feature.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.my.shot.AppColors
import com.nicholas.rutherford.track.my.shot.compose.components.Content
import com.nicholas.rutherford.track.my.shot.feature.splash.Colors
import com.nicholas.rutherford.track.my.shot.feature.splash.DrawablesIds
import com.nicholas.rutherford.track.my.shot.feature.splash.StringsIds
import com.nicholas.rutherford.track.my.shot.helper.ui.Padding
import com.nicholas.rutherford.track.my.shot.helper.ui.TextStyles
import com.nicholas.rutherford.track.myshot.compose.content.test.rule.drawableId
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(loginScreenParams: LoginScreenParams) {
    Content(
        ui = {
            LoginScreenContent(loginScreenParams = loginScreenParams)
        }
    )
}

@Composable
private fun LoginScreenContent(loginScreenParams: LoginScreenParams) {
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
            modifier = Modifier.padding(8.dp).testTag(tag = LoginTags.LOGIN_TEXT),
            style = TextStyles.medium
        )

        Spacer(modifier = Modifier.height(Padding.eight))
        TextField(
            label = { Text(text = stringResource(id = StringsIds.email)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag(tag = LoginTags.EMAIL_TEXT_FIELD),
            value = loginScreenParams.state.email ?: stringResource(id = StringsIds.empty),
            onValueChange = { newEmail -> loginScreenParams.onEmailValueChanged.invoke(newEmail) },
            textStyle = TextStyles.body,
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
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            onValueChange = { newPassword -> loginScreenParams.onPasswordValueChanged.invoke(newPassword) },
            textStyle = TextStyles.body,
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(backgroundColor = Colors.whiteColor)
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
