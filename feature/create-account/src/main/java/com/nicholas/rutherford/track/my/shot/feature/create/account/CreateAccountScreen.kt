package com.nicholas.rutherford.track.my.shot.feature.create.account

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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.my.shot.compose.components.ContentWithTopBackAppBar
import com.nicholas.rutherford.track.my.shot.compose.components.TextFieldNoPadding
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

        TextFieldNoPadding(
            label = stringResource(id = StringsIds.userName),
            value = state.username ?: stringResource(id = StringsIds.empty),
            onValueChange = { newUsername ->
                viewModel.onUsernameValueChanged(newUsername = newUsername)
            }
        )

        Spacer(modifier = Modifier.height(Padding.four))

        TextFieldNoPadding(
            label = stringResource(id = StringsIds.email),
            value = state.email ?: stringResource(id = StringsIds.empty),
            onValueChange = { newEmail ->
                viewModel.onEmailValueChanged(newEmail = newEmail)
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(Padding.four))

        TextFieldNoPadding(
            label = stringResource(id = StringsIds.password),
            value = state.password ?: stringResource(id = StringsIds.empty),
            onValueChange = { newPassword ->
                viewModel.onPasswordValueChanged(newPassword = newPassword)
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

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
