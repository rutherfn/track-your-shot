package com.nicholas.rutherford.track.my.shot.feature.create.account

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.my.shot.compose.components.ContentWithTopBackAppBar
import com.nicholas.rutherford.track.my.shot.feature.splash.Colors
import com.nicholas.rutherford.track.my.shot.feature.splash.StringsIds
import com.nicholas.rutherford.track.my.shot.helper.ui.Padding
import com.nicholas.rutherford.track.my.shot.helper.ui.TextStyles

@Composable
fun CreateAccountScreen(viewModel: CreateAccountViewModel) {
    val state = viewModel.createAccountStateFlow.collectAsState().value

    ContentWithTopBackAppBar(
        toolbarTitle = stringResource(id = StringsIds.createAccount),
        onBackButtonClicked = { viewModel.onBackButtonClicked() },
        content = {
            CreateAccountScreenContent(state = state, viewModel = viewModel)
        }
    )
}

@Composable
fun CreateAccountScreenContent(
    state: CreateAccountState,
    viewModel: CreateAccountViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(Padding.twenty)
    ) {

        Text(
            text = stringResource(id = StringsIds.allFieldsAreRequired),
            style = TextStyles.small
        )

        Spacer(modifier = Modifier.height(Padding.four))

        BoxWithConstraints(
            modifier = Modifier.clipToBounds()
        ) {
            TextField(
                label = { Text(text = stringResource(id = StringsIds.userName)) },
                modifier = Modifier
                    .requiredWidth(maxWidth + Padding.sixteen)
                    .offset(x = (-8).dp)
                    .fillMaxWidth(),
                value = state.username ?: stringResource(id = StringsIds.empty),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                onValueChange = {
                        newUsername ->
                    viewModel.onUsernameValueChanged(newUsername = newUsername)
                },
                textStyle = TextStyles.body,
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Colors.whiteColor)
            )
        }

        Spacer(modifier = Modifier.height(Padding.four))

        BoxWithConstraints(
            modifier = Modifier.clipToBounds()
        ) {
            TextField(
                label = { Text(text = stringResource(id = StringsIds.email)) },
                modifier = Modifier
                    .requiredWidth(maxWidth + Padding.sixteen)
                    .offset(x = (-8).dp)
                    .fillMaxWidth(),
                value = state.email ?: stringResource(id = StringsIds.empty),
                onValueChange = {
                        newEmail ->
                    viewModel.onEmailValueChanged(newEmail = newEmail)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                textStyle = TextStyles.body,
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Colors.whiteColor)
            )
        }

        Spacer(modifier = Modifier.height(Padding.four))

        BoxWithConstraints(
            modifier = Modifier.clipToBounds()
        ) {
            TextField(
                label = { Text(text = stringResource(id = StringsIds.password)) },
                modifier = Modifier
                    .requiredWidth(maxWidth + Padding.sixteen)
                    .offset(x = (-8).dp)
                    .fillMaxWidth(),
                value = state.password ?: stringResource(id = StringsIds.empty),
                onValueChange = {
                        newPassword ->
                    viewModel.onPasswordValueChanged(newPassword = newPassword)
                },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                textStyle = TextStyles.body,
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Colors.whiteColor)
            )
        }

        Spacer(modifier = Modifier.height(Padding.eight))

        Button(
            onClick = { },
            shape = RoundedCornerShape(size = 50.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(Padding.twentyFour),
            colors = ButtonDefaults.buttonColors(backgroundColor = Colors.secondaryColor),
            content = {
                Text(
                    text = stringResource(id = StringsIds.createAccount),
                    style = TextStyles.small,
                    color = Color.White
                )
            }
        )
    }
}
