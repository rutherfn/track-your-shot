package com.nicholas.rutherford.track.your.shot.feature.settings.termsconditions

import androidx.activity.compose.BackHandler
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nicholas.rutherford.track.your.shot.base.resources.Colors
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.helper.ui.Padding
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Entry point for the Terms & Conditions screen.
 *
 * @param params Container class holding all required state and callbacks.
 */
@Composable
fun TermsConditionsScreen(params: TermsConditionsParams) {
    TermsConditionsContent(params = params)
}

/**
 * Main content layout for displaying the Terms & Conditions screen.
 *
 * This composable includes:
 * - A list of information sections (titles and descriptions).
 * - A persistent bottom button (either "Acknowledge" or "Close").
 * - A clickable email for contacting the developer.
 *
 * @param params [TermsConditionsParams] containing UI state and navigation callbacks.
 */
@Composable
fun TermsConditionsContent(params: TermsConditionsParams) {
    BackHandler(enabled = true) { params.onBackClicked.invoke() }

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
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Colors.secondaryColor)
        ) {
            Text(
                text = params.state.buttonText
            )
        }
    }
}

/**
 * Renders a single section of the terms and conditions.
 *
 * @param info A [TermsConditionInfo] containing the title and description.
 */
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

/**
 * Renders the footer section of the terms and conditions.
 *
 * This includes:
 * - A clickable developer email address.
 * - A note indicating when the document was last updated.
 *
 * @param params [TermsConditionsParams] for handling click actions.
 */
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

/**
 * Preview of the TermsConditionsScreen with mock content.
 */
@Preview
@Composable
fun TermsConditionsScreenPreview() {
    Column(modifier = Modifier.background(Color.White)) {
        TermsConditionsScreen(
            params = TermsConditionsParams(
                onBackClicked = {},
                onCloseAcceptButtonClicked = {},
                onDevEmailClicked = {},
                state = TermsConditionsState(
                    infoList = listOf(
                        TermsConditionInfo(
                            title = stringResource(StringsIds.termsConditions),
                            description = stringResource(StringsIds.termsConditionsDescription)
                        )
                    )
                )
            )
        )
    }
}
