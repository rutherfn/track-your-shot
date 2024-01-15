package com.nicholas.rutherford.track.your.shot.feature.players.shots

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.your.shot.AppColors
import com.nicholas.rutherford.track.your.shot.compose.components.Content
import com.nicholas.rutherford.track.your.shot.data.room.response.DeclaredShot
import com.nicholas.rutherford.track.your.shot.data.shared.appbar.AppBar
import com.nicholas.rutherford.track.your.shot.feature.splash.StringsIds
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles

@Composable
fun SelectShotScreen(selectShotParams: SelectShotParams) {
    val isShotsDeclaredListEmpty = selectShotParams.state.declaredShotList.isEmpty()

    Content(
        ui = {
             if (!isShotsDeclaredListEmpty) {
                 LazyColumn {
                     items(selectShotParams.state.declaredShotList) { declaredShot ->
                         DeclaredShotItem(declaredShot = declaredShot)
                     }
                 }
             }
        },
        appBar = AppBar(
            toolbarTitle = stringResource(id = StringsIds.selectAShot),
            shouldShowMiddleContentAppBar = false,
            shouldIncludeSpaceAfterDeclaration = false,
            shouldShowSecondaryButton = true,
            onIconButtonClicked = {},
            onSecondaryIconButtonClicked = {}
        )
    )
}

@Composable
fun DeclaredShotItem(declaredShot: DeclaredShot) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .background(AppColors.White)
            .fillMaxWidth()
            .padding(16.dp),
        elevation = 2.dp
    ) {
        Column {
            Text(
                text = declaredShot.title,
                style = TextStyles.body,
                textAlign = TextAlign.Start
            )

            ClickableText(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.Blue)) {
                        append("Tap to expand")
                    }
                },
                onClick = { offset ->
                    // Toggle the expansion state
                    isExpanded = !isExpanded
                },
                modifier = Modifier.clickable { /* No-op click listener to enable the ClickableText */ }
            )

            // Description and Shot Category (visible when expanded)
            if (isExpanded) {
                Text(text = declaredShot.description)
                Text(text = "Category: ${declaredShot.shotCategory}")
            }
        }

    }
}