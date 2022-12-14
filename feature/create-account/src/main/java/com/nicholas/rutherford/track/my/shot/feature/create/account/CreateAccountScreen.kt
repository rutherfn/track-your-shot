package com.nicholas.rutherford.track.my.shot.feature.create.account

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.nicholas.rutherford.track.my.shot.feature.splash.StringsIds

@Composable
fun CreateAccountScreen(viewModel: CreateAccountViewModel) {
    val state = viewModel.createAccountStateFlow.collectAsState().value

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                Text(text = stringResource(id = StringsIds.createAccount))
            }, navigationIcon = {
                IconButton(onClick = { viewModel.onBackButtonClicked() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack, contentDescription = stringResource(id = StringsIds.empty)
                    )
                }
            }
        )
    }
}
