package com.nicholas.rutherford.track.my.shot.feature.home

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.nicholas.rutherford.track.my.shot.compose.components.Content

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    emailArgument: String?
) {

    Content(
        ui = {
            CreateAccountContent(viewModel = viewModel)
        }
    )

    LaunchedEffect(Unit) {
        emailArgument?.let { email ->
            viewModel.collectAccountInfoFlowByEmail(email = email)
        }
    }
}

@Composable
fun CreateAccountContent(viewModel: HomeViewModel) {
    Button(
        onClick = { viewModel.navigateTest() },
        modifier = Modifier.testTag(tag = HomeTags.TEST_BUTTON),
        content = { Text("Log Out") }
    )
}
