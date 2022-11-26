package com.nicholas.rutherford.track.my.shot.feature.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.my.shot.feature.splash.Colors
import com.nicholas.rutherford.track.my.shot.feature.splash.StringsIds
import com.nicholas.rutherford.track.my.shot.helper.ui.Padding
import com.nicholas.rutherford.track.my.shot.helper.ui.TextStyles

@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    val state = viewModel.loginStateFlow.collectAsState().value

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = StringsIds.login),
            style = TextStyles.large
        )

        Spacer(modifier = Modifier.height(Padding.twenty))
        TextField(
            label = { Text(text = stringResource(id = StringsIds.login)) },
            value = state.username ?: stringResource(id = StringsIds.empty),
            onValueChange = { newUsername -> viewModel.onUsernameValueChanged(newUsername = newUsername) },
            textStyle = TextStyles.body
        )

        Spacer(modifier = Modifier.height(Padding.twenty))
        TextField(
            label = { Text(text = stringResource(id = StringsIds.password)) },
            value = state.password ?: stringResource(id = StringsIds.empty),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            onValueChange = { newPassword -> viewModel.onPasswordValueChanged(newPassword = newPassword) },
            textStyle = TextStyles.body
        )

        Spacer(modifier = Modifier.height(Padding.twenty))
        Box(modifier = Modifier.padding(start = Padding.forty, end = Padding.forty)) {
            Button(
                onClick = { },
                shape = RoundedCornerShape(size = 50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = 50.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Colors.secondaryColor),
                content = {
                    Text(
                        text = stringResource(id = StringsIds.login),
                        style = TextStyles.medium
                    )
                }
            )
        }

        Spacer(modifier = Modifier.height(Padding.twenty))
        ClickableText(
            text = AnnotatedString(stringResource(id = StringsIds.clickMeToRestPassword)),
            onClick = { },
            style = TextStyles.hyperLink
        )

        Spacer(modifier = Modifier.height(Padding.eight))
        ClickableText(
            text = AnnotatedString(stringResource(id = StringsIds.clickMeToCreateAccount)),
            onClick = { },
            style = TextStyles.hyperLink
        )
    }
}
