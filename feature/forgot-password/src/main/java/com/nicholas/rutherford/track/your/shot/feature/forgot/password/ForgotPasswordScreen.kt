package com.nicholas.rutherford.track.your.shot.feature.forgot.password

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.your.shot.TrackYourShotTheme
import com.nicholas.rutherford.track.your.shot.base.resources.Colors
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.helper.ui.Padding
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles

/**
 * Entry composable for the Forgot Password screen.
 *
 * @param forgotPasswordScreenParams holds UI state and event callbacks.
 */
@Composable
fun ForgotPasswordScreen(forgotPasswordScreenParams: ForgotPasswordScreenParams) {
    TrackYourShotTheme {
        ForgotPasswordScreenContent(forgotPasswordScreenParams = forgotPasswordScreenParams)
    }
}

/**
 * Main UI content for Forgot Password screen.
 *
 * Displays an email input field and a button to send password reset.
 *
 * @param forgotPasswordScreenParams UI state and event callbacks.
 */
@Composable
private fun ForgotPasswordScreenContent(forgotPasswordScreenParams: ForgotPasswordScreenParams) {
    val focusManager = LocalFocusManager.current
    var shouldClearFocus by remember { mutableStateOf(false) }

    LaunchedEffect(shouldClearFocus) {
        if (shouldClearFocus) {
            focusManager.clearFocus()
            shouldClearFocus = false
        }
    }

    BackHandler(enabled = true) { forgotPasswordScreenParams.onBackButtonClicked.invoke() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(14.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        TextField(
            label = { Text(text = stringResource(id = StringsIds.emailRequired)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                shouldClearFocus = true
                forgotPasswordScreenParams.onSendPasswordResetButtonClicked(forgotPasswordScreenParams.state.email)
            }),
            value = forgotPasswordScreenParams.state.email ?: "",
            onValueChange = { newEmail -> forgotPasswordScreenParams.onEmailValueChanged(newEmail) },
            textStyle = TextStyles.body,
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Colors.whiteColor,
                unfocusedContainerColor = Colors.whiteColor
            )
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
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(
                text = stringResource(id = StringsIds.resetPassword),
                style = TextStyles.small,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}


/**
 * Preview composable showing a sample Forgot Password screen.
 */
@Preview(showBackground = true)
@Composable
private fun ForgotPasswordScreenPreview() {
    MaterialTheme {
        ForgotPasswordScreen(
            forgotPasswordScreenParams = ForgotPasswordScreenParams(
                state = ForgotPasswordState(email = "emailtest@gmail.com"),
                onEmailValueChanged = {},
                onSendPasswordResetButtonClicked = {},
                onBackButtonClicked = {}
            )
        )
    }
}
