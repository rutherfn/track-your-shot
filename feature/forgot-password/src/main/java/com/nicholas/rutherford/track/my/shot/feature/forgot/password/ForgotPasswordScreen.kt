package com.nicholas.rutherford.track.my.shot.feature.forgot.password

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.my.shot.feature.splash.Colors
import com.nicholas.rutherford.track.my.shot.feature.splash.StringsIds
import com.nicholas.rutherford.track.my.shot.helper.ui.Padding
import com.nicholas.rutherford.track.my.shot.helper.ui.TextStyles

@Composable
fun ForgotPasswordScreen(viewModel: ForgotPasswordViewModel) {
    val state = viewModel.forgotPasswordStateFlow.collectAsState().value

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                Text(text = stringResource(id = StringsIds.forgotPassword))
            }, navigationIcon = {
                IconButton(onClick = {
                    viewModel.onBackButtonClicked()
                }) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "")
                }
            }
        )

        Spacer(modifier = Modifier.height(Padding.eight))

        TextField(
            label = { Text(text = stringResource(id = StringsIds.forgotPassword)) },
            value = state.email ?: stringResource(id = StringsIds.empty),
            onValueChange = { newEmail -> viewModel.onEmailValueChanged(newEmail = newEmail) },
            textStyle = TextStyles.body,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color(color = 0x00000000)
            )
        )

        Spacer(modifier = Modifier.height(Padding.four))

        Button(
            onClick = {},
            shape = RoundedCornerShape(size = 30.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(height = 50.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Colors.secondaryColor),
            content = {
                Text(
                    text = stringResource(id = StringsIds.resetPassword),
                    style = TextStyles.medium
                )
            }
        )
    }
}
