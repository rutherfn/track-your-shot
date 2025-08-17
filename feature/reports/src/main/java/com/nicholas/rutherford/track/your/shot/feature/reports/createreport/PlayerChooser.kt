package com.nicholas.rutherford.track.your.shot.feature.reports.createreport

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.your.shot.base.resources.R
import com.nicholas.rutherford.track.your.shot.data.room.response.Player
import com.nicholas.rutherford.track.your.shot.data.room.response.PlayerPositions
import com.nicholas.rutherford.track.your.shot.data.room.response.fullName
import com.nicholas.rutherford.track.your.shot.helper.ui.Padding
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Composable that displays a dropdown menu allowing the user to select a player.
 *
 * @param params Contains the state of the player selection and callbacks for player change.
 */
@Composable
fun PlayerChooser(params: CreateReportParams) {
    var selectedOption by remember { mutableStateOf(value = "") }
    var isDropdownExpanded by remember { mutableStateOf(value = false) }

    val options = params.state.playerOptions

    selectedOption = params.state.selectedPlayer?.fullName() ?: ""

    Box {
        Spacer(modifier = Modifier.height(Padding.sixteen))
        DropdownMenu(
            expanded = isDropdownExpanded,
            onDismissRequest = { isDropdownExpanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(text = option, style = TextStyles.body) },
                    onClick = {
                        selectedOption = option
                        isDropdownExpanded = false
                        params.onPlayerChanged.invoke(option)
                    }
                )
            }
        }
    }

    Column(modifier = Modifier.fillMaxWidth().padding(top = 12.dp)) {
        Text(
            text = stringResource(id = R.string.selected_player),
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
                contentDescription = "Dropdown Arrow",
                modifier = Modifier
                    .padding(start = 4.dp)
                    .size(20.dp)
                    .clickable { isDropdownExpanded = !isDropdownExpanded }
            )
        }
    }
}

/**
 * Preview of the [PlayerChooser] composable.
 */
@Preview(showBackground = true)
@Composable
fun PlayerChooserPreview() {
    PlayerChooser(
        params = CreateReportParams(
            onToolbarMenuClicked = {},
            attemptToGeneratePlayerReport = {},
            onPlayerChanged = {},
            state = CreateReportState(
                selectedPlayer = Player(firstName = "Alice", lastName = "Johnson", imageUrl = null, position = PlayerPositions.PointGuard, firebaseKey = "firebase", shotsLoggedList = emptyList()),
                playerOptions = listOf("Alice Johnson", "Bob Smith", "Charlie Davis")
            )
        )
    )
}
