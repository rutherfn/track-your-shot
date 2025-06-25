package com.nicholas.rutherford.track.your.shot.feature.settings.termsconditions

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.compose.components.Content
import com.nicholas.rutherford.track.your.shot.helper.ui.Padding
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles

@Composable
fun TermsConditionsScreen(params: TermsConditionsParams) {
    LaunchedEffect(Unit) { params.updateButtonTextState.invoke() }

    Content(
        ui = {
            TermsConditionsContent(params = params)
        }
    )
}

@Composable
fun TermsConditionsContent(params: TermsConditionsParams) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = Padding.sixteen)
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(
                    start = Padding.sixteen,
                    end = Padding.sixteen,
                    bottom = Padding.sixteen
                ),
            contentPadding = PaddingValues(bottom = 72.dp)
        ) {
            items(params.state.infoList) { info ->
                TermsConditionsItem(info = info)
            }
            item {
                TermsConditionFooterItem(params = params)
            }
        }

        Button(
            onClick = { params.onCloseAcceptButtonClicked.invoke() },
            shape = RoundedCornerShape(size = 50.dp),
            enabled = true,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(Padding.sixteen)
                .fillMaxWidth()
        ) {
            Text(
                text = params.state.buttonText
            )
        }
    }
}

@Composable
fun TermsConditionsItem(info: TermsConditionInfo) {
    Spacer(modifier = Modifier.height(Padding.sixteen))

    Text(
        text = info.title,
        style = TextStyles.smallBold
    )

    Spacer(modifier = Modifier.height(Padding.eight))

    Text(
        text = info.description,
        style = TextStyles.body,
        lineHeight = 20.sp,
        textAlign = TextAlign.Justify
    )

    Spacer(modifier = Modifier.height(Padding.twelve))
}

@Composable
fun TermsConditionFooterItem(params: TermsConditionsParams) {
    Spacer(modifier = Modifier.height(Padding.eight))

    Text(
        text = stringResource(id = StringsIds.devEmail),
        style = TextStyles.hyperLink.copy(color = Color.Blue),
        modifier = Modifier.clickable { params.onDevEmailClicked.invoke() }
    )

    Spacer(modifier = Modifier.height(Padding.eight))

    Text(
        text = stringResource(id = StringsIds.thisDocumentWasLastUpdatedOn),
        style = TextStyles.body
    )
}

@Preview
@Composable
fun TermsConditionsScreenPreview() {
    Column(modifier = Modifier.background(Color.White)) {
        TermsConditionsScreen(
            params = TermsConditionsParams(
                updateButtonTextState = {},
                onCloseAcceptButtonClicked = {},
                onDevEmailClicked = {},
                state = TermsConditionsState(
                    infoList = listOf(
                        TermsConditionInfo(
                            title = stringResource(StringsIds.termsConditions),
                            description = stringResource(StringsIds.termsConditionsDescription)
                        )
                    )
                ),
                isAcknowledgeConditions = false
            )
        )
    }
}
