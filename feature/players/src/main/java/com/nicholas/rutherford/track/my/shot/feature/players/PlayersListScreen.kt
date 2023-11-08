package com.nicholas.rutherford.track.my.shot.feature.players

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.nicholas.rutherford.track.my.shot.base.resources.R
import com.nicholas.rutherford.track.my.shot.compose.components.Content
import com.nicholas.rutherford.track.my.shot.data.room.response.Player
import com.nicholas.rutherford.track.my.shot.data.shared.appbar.AppBar
import com.nicholas.rutherford.track.my.shot.feature.splash.Colors
import com.nicholas.rutherford.track.my.shot.feature.splash.DrawablesIds
import com.nicholas.rutherford.track.my.shot.feature.splash.StringsIds
import com.nicholas.rutherford.track.my.shot.helper.ui.Padding
import com.nicholas.rutherford.track.my.shot.helper.ui.TextStyles

@Composable
fun PlayersListScreen(playerListScreenParams: PlayersListScreenParams) {
    Content(
        ui = {
            if (playerListScreenParams.state.playerList.isNotEmpty()) {
                LazyColumn {
                    items(playerListScreenParams.state.playerList) { player ->
                        PlayerItem(player = player)
                    }
                }
            } else {
                AddNewPlayerEmptyState()
            }
        },
        appBar = AppBar(
            toolbarTitle = stringResource(id = R.string.players),
            shouldShowMiddleContentAppBar = true,
            onIconButtonClicked = {
                println("get here drawer statedadad ")
                playerListScreenParams.onToolbarMenuClicked.invoke()
            }
        ),
        imageVector = Icons.Filled.CheckCircle,
        secondaryImageVector = Icons.Filled.CheckCircle,
    )
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun PlayerItem(player: Player) {
    val imagePainter = if (!player.imageUrl.isNullOrEmpty()) {
        rememberImagePainter(data = player.imageUrl)
    } else {
        painterResource(id = DrawablesIds.launcherRound)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = imagePainter,
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .padding(4.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = player.firstName,
            style = TextStyles.body,
            textAlign = TextAlign.Start
        )
    }
}

@Composable
fun AddNewPlayerEmptyState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = { },
            shape = RoundedCornerShape(size = 32.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(Padding.twelve),
            colors = ButtonDefaults.buttonColors(backgroundColor = Colors.secondaryColor),
            contentPadding = PaddingValues(16.dp),
            content = {
                Text(
                    text = stringResource(id = StringsIds.addPlayer),
                    color = Color.White
                )
            }
        )
    }
}
