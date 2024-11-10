package com.nicholas.rutherford.track.your.shot.feature.settings.termsconditions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.nicholas.rutherford.track.your.shot.compose.components.Content
import com.nicholas.rutherford.track.your.shot.feature.splash.StringsIds
import com.nicholas.rutherford.track.your.shot.helper.ui.Padding
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles

@Composable
fun TermsConditionsScreen() {
    Content(
        ui = {
            TermsConditionsContent()
        }
    )
}

@Composable
fun TermsConditionsContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(
                start = Padding.sixteen,
                end = Padding.sixteen,
                bottom = Padding.sixteen
            ),
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(modifier = Modifier.height(Padding.sixteen))

        Text(
            text = stringResource(id = StringsIds.termsConditions),
            style = TextStyles.smallBold
        )

        Spacer(modifier = Modifier.height(Padding.eight))

        Text(
            text = stringResource(id = StringsIds.termsConditionsDescription),
            style = TextStyles.body
        )
    }
}

@Preview
@Composable
fun TermsConditionsScreenPreview() {
    Column(modifier = Modifier.background(Color.White)) {
        TermsConditionsScreen()
    }
}