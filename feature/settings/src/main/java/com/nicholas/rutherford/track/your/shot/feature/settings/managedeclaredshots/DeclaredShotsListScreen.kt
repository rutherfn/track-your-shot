package com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.your.shot.AppColors
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.compose.components.Content
import com.nicholas.rutherford.track.your.shot.data.room.response.DeclaredShot
import com.nicholas.rutherford.track.your.shot.data.shared.appbar.AppBar
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles

@Composable
fun DeclaredShotsListScreen(declaredShotsListScreenParams: DeclaredShotsListScreenParams) {
    val isDeclaredShotListEmpty = declaredShotsListScreenParams.state.declaredShotsList.isEmpty()

    Content(
        ui = {
            if (!isDeclaredShotListEmpty) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(declaredShotsListScreenParams.state.declaredShotsList) { declaredShot ->
                        DeclaredShotItem(
                            declaredShot = declaredShot,
                            onDeclaredShotClicked = declaredShotsListScreenParams.onDeclaredShotClicked
                        )
                    }
                }
            } else {
                DeclaredShotListEmptyState()
            }
        },
        appBar = AppBar(
            toolbarTitle = stringResource(id = StringsIds.shots),
            shouldShowMiddleContentAppBar = false

        )
    )
}

@Composable
private fun DeclaredShotItem(declaredShot: DeclaredShot, onDeclaredShotClicked: (id: Int) -> Unit) {
    Card(
        modifier = Modifier
            .background(AppColors.White)
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                onDeclaredShotClicked.invoke(declaredShot.id)
            },
        elevation = 2.dp
    ) {
        Column {
            Text(
                text = declaredShot.title,
                style = TextStyles.bodyBold,
                textAlign = TextAlign.Start
            )
        }
    }
}

@Composable
private fun DeclaredShotListEmptyState() {
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
                painter = painterResource(id = com.nicholas.rutherford.track.your.shot.base.resources.R.drawable.ic_basketball_player_empty_state),
                contentDescription = null,
                modifier = Modifier.size(120.dp)
            )

            Text(
                text = stringResource(id = StringsIds.noShotsCreated),
                style = TextStyles.medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Text(
                text = stringResource(id = StringsIds.noShotsDeclaredDescription),
                style = TextStyles.smallBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            TextButton(
                onClick = { },
                content = { Text(text = stringResource(id = StringsIds.addShot)) },
                colors = ButtonDefaults.textButtonColors(contentColor = AppColors.OrangeVariant)
            )
        }
    }
}
