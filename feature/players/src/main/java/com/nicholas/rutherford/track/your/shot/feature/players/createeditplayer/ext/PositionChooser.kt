package com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer.ext

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.your.shot.base.resources.R
import com.nicholas.rutherford.track.your.shot.data.room.response.PlayerPositions
import com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer.CreateEditPlayerParams
import com.nicholas.rutherford.track.your.shot.helper.ui.Padding
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles

@Composable
fun PositionChooser(createEditPlayerParams: CreateEditPlayerParams) {
    var selectedOption by remember { mutableStateOf(value = "") }
    var isDropdownExpanded by remember { mutableStateOf(value = false) }

    val pointGuard = stringResource(id = R.string.point_guard)
    val shootingGuard = stringResource(id = R.string.shooting_guard)
    val smallForward = stringResource(id = R.string.small_forward)
    val powerForward = stringResource(id = R.string.power_forward)
    val center = stringResource(id = R.string.center)

    val options = listOf(pointGuard, shootingGuard, smallForward, powerForward, center)

    selectedOption = createEditPlayerParams.state.playerPositionString.ifEmpty { pointGuard }

    Box {
        Spacer(modifier = Modifier.height(Padding.sixteen))
        DropdownMenu(
            expanded = isDropdownExpanded,
            onDismissRequest = { isDropdownExpanded = false }
        ) {
            options.forEachIndexed { index, option ->
                DropdownMenuItem(
                    text = { Text(text = option, style = TextStyles.body) },
                    onClick = {
                        selectedOption = option
                        val newPosition = when (index) {
                            PlayerPositions.PointGuard.value -> pointGuard
                            PlayerPositions.ShootingGuard.value -> shootingGuard
                            PlayerPositions.SmallForward.value -> smallForward
                            PlayerPositions.PowerForward.value -> powerForward
                            else -> center
                        }
                        createEditPlayerParams.onPlayerPositionStringChanged(newPosition)
                        isDropdownExpanded = false
                    }
                )
            }
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(id = R.string.position),
                modifier = Modifier.padding(start = Padding.four),
                style = TextStyles.body
            )
            Spacer(modifier = Modifier.height(Padding.eight))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selectedOption,
                    modifier = Modifier
                        .padding(start = Padding.four)
                        .clickable { isDropdownExpanded = !isDropdownExpanded },
                    style = TextStyles.body
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "2",
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .size(20.dp)
                        .clickable { isDropdownExpanded = !isDropdownExpanded }
                )
            }
        }
    }
}
