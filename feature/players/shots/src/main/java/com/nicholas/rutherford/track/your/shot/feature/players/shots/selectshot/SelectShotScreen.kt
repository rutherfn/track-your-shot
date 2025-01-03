package com.nicholas.rutherford.track.your.shot.feature.players.shots.selectshot

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Help
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.your.shot.AppColors
import com.nicholas.rutherford.track.your.shot.base.resources.R
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.compose.components.Content
import com.nicholas.rutherford.track.your.shot.compose.components.SearchTextField
import com.nicholas.rutherford.track.your.shot.data.room.response.DeclaredShot
import com.nicholas.rutherford.track.your.shot.data.shared.appbar.AppBar
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles
import java.util.Locale

@Composable
fun SelectShotScreen(selectShotParams: SelectShotParams) {
    var query by remember { mutableStateOf("") }

    BackHandler(true) {
        selectShotParams.onBackButtonClicked.invoke()
    }

    LaunchedEffect(Unit) {
        selectShotParams.updateIsExistingPlayerAndPlayerId.invoke()
    }

    Content(
        ui = {
            SearchTextField(
                value = query,
                onValueChange = { newSearchQuery ->
                    query = newSearchQuery
                    selectShotParams.onSearchValueChanged.invoke(newSearchQuery)
                },
                onCancelIconClicked = {
                    selectShotParams.onCancelIconClicked.invoke(query)
                    query = ""
                },
                placeholderValue = stringResource(id = StringsIds.findShotsByName)
            )
            if (selectShotParams.state.declaredShotList.isNotEmpty()) {
                LazyColumn {
                    items(selectShotParams.state.declaredShotList) { declaredShot ->
                        DeclaredShotItem(
                            declaredShot = declaredShot,
                            onItemClicked = { type ->
                                selectShotParams.onItemClicked.invoke(type)
                            }
                        )
                    }
                }
            } else {
                SelectShotEmptyState()
            }
        },
        appBar = AppBar(
            toolbarTitle = stringResource(id = StringsIds.selectAShot),
            shouldShowMiddleContentAppBar = false,
            shouldIncludeSpaceAfterDeclaration = false,
            shouldShowSecondaryButton = true,
            onIconButtonClicked = {
                selectShotParams.onBackButtonClicked.invoke()
            },
            onSecondaryIconButtonClicked = { selectShotParams.onHelpIconClicked.invoke() }
        ),
        secondaryImageVector = Icons.Filled.Help
    )
}

@Composable
fun DeclaredShotItem(
    declaredShot: DeclaredShot,
    onItemClicked: (type: Int) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .background(AppColors.White)
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                onItemClicked.invoke(declaredShot.id)
            },
        elevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = declaredShot.title,
                style = TextStyles.smallBold,
                textAlign = TextAlign.Start
            )

            if (!isExpanded) {
                ClickableText(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = Color.Blue)) {
                            append(stringResource(id = StringsIds.showMore))
                        }
                    },
                    onClick = {
                        isExpanded = !isExpanded
                    }
                )
                Spacer(modifier = Modifier.width(16.dp))
            } else {
                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = stringResource(
                        id = R.string.x_shot_category,
                        declaredShot.shotCategory.replaceFirstChar { char ->
                            if (char.isLowerCase()) {
                                char.titlecase(Locale.getDefault())
                            } else {
                                char.toString()
                            }
                        }
                    ),
                    style = TextStyles.body,
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = declaredShot.description,
                    style = TextStyles.body,
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = Modifier.width(4.dp))

                ClickableText(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = Color.Blue)) {
                            append(stringResource(id = StringsIds.showLess))
                        }
                    },
                    onClick = {
                        isExpanded = !isExpanded
                    }
                )
            }
        }
    }
}

@Composable
fun SelectShotEmptyState() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_basketball_log_shot_empty_state),
                contentDescription = null,
                modifier = Modifier.size(120.dp)
            )

            Text(
                text = stringResource(id = StringsIds.noShotsResultsFound),
                style = TextStyles.medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Text(
                text = stringResource(id = StringsIds.noShotsResultsFoundDescription),
                style = TextStyles.smallBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}
