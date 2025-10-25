package com.nicholas.rutherford.track.your.shot.feature.voice.commands.voicecommandlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SportsBasketball
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.your.shot.AppColors
import com.nicholas.rutherford.track.your.shot.base.resources.Colors
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.data.room.response.SavedVoiceCommand
import com.nicholas.rutherford.track.your.shot.data.room.response.VoiceCommandTypes
import com.nicholas.rutherford.track.your.shot.feature.voice.commands.VoiceCommandFilter
import com.nicholas.rutherford.track.your.shot.feature.voice.commands.toDisplayLabel
import com.nicholas.rutherford.track.your.shot.feature.voice.commands.toType
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles

/**
 * Main screen composable for displaying and managing voice commands.
 * Shows a list of saved voice commands with filtering capabilities by command type.
 * If the user does mot have any saved voice command for that type, display a empty state
 * that will allow the user to go and save a new voice command
 * 
 * @param params Contains the state and callback functions for the screen
 */
@Composable
fun VoiceCommandListScreen(params: VoiceCommandListParams) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Text(
            text = stringResource(id = StringsIds.setupVoiceCommandDescription),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        VoiceFilters(state = params.state, onFilterSelected = params.onFilterSelected)

        if (params.state.filteredCommands.isEmpty()) {
            VoiceCommandsEmptyStateContent(
                state = params.state,
                onCreateCommandTypeClicked = params.onCreateEditCommandTypeClicked
            )
        } else {
            VoiceCommandTypeListContent(
                state = params.state,
                onEditCommandTypeClicked = params.onCreateEditCommandTypeClicked
            )
        }
    }
}

/**
 * Composable that displays horizontal filter chips for voice command types.
 * Allows users to filter voice commands by Start, Stop, Make, and Miss categories.
 * 
 * @param state Current state containing the selected filter
 * @param onFilterSelected Callback when a filter chip is selected
 */
@Composable
private fun VoiceFilters(
    state: VoiceCommandListState,
    onFilterSelected: (filter: VoiceCommandFilter) -> Unit
) {
    Spacer(modifier = Modifier.height(16.dp))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        VoiceCommandFilter.entries.forEach { filter ->
            FilterChip(
                onClick = { onFilterSelected.invoke(filter) },
                label = {
                    Text(
                        text = filter.toDisplayLabel(),
                        style = TextStyles.bodyBold
                    )
                },
                selected = state.selectedFilter == filter,
                leadingIcon = if (state.selectedFilter == filter) {
                    {
                        Icon(
                            imageVector = Icons.Filled.SportsBasketball,
                            contentDescription = "Selected",
                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                        )
                    }
                } else {
                    null
                }
            )
        }
    }

    Spacer(modifier = Modifier.height(16.dp))
}

/**
 * Composable that displays an empty state when no voice commands exist for the selected filter.
 * Shows a card with instructions and a button to create the voice command.
 * 
 * @param state Current state containing the selected filter information
 * @param onCreateCommandTypeClicked Callback when the create command button is clicked
 */
@Composable
private fun VoiceCommandsEmptyStateContent(
    state: VoiceCommandListState,
    onCreateCommandTypeClicked: (type: Int?, phrase: String?) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppColors.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(StringsIds.noXCommandsYet, state.selectedFilter.toDisplayLabel()),
                style = TextStyles.smallBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(id = StringsIds.setupVoiceCommandDescription, state.selectedFilter.toDisplayLabel()),
                style = TextStyles.body,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Colors.secondaryColor),
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier.fillMaxWidth(),
                onClick = { onCreateCommandTypeClicked.invoke(state.selectedFilter.toType().value, null) }
            ) {
                Text(
                    text = stringResource(StringsIds.createXCommand, state.selectedFilter.toDisplayLabel()),
                    style = TextStyles.smallBold,
                    color = Color.White
                )
            }
        }
    }
}

/**
 * Composable that displays a list of voice commands as cards.
 * Shows filtered voice commands with basketball icons and command names.
 * 
 * @param state Current state containing the filtered commands to display
 * @param onEditCommandTypeClicked Callback when the edit command button is clicked
 */
@Composable
private fun VoiceCommandTypeListContent(
    state: VoiceCommandListState,
    onEditCommandTypeClicked: (type: Int?, phrase: String?) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        state.filteredCommands.forEach { command ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { onEditCommandTypeClicked.invoke(command.type.value, command.name) },
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.SportsBasketball,
                        contentDescription = "Voice Command",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    
                    Spacer(modifier = Modifier.size(12.dp))
                    
                    Text(
                        text = command.name,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VoiceCommandListScreenEmptyStatePreview() {
    val params = VoiceCommandListParams(
        state = VoiceCommandListState(),
        onToolbarMenuClicked = {},
        onFilterSelected = {},
        onCreateEditCommandTypeClicked = { test, test2 -> }
    )
    Column(modifier = Modifier.background(AppColors.White)) {
        VoiceCommandListScreen(params = params)
    }
}

@Preview(showBackground = true)
@Composable
fun VoiceCommandListScreenWithDataPreview() {
    val params = VoiceCommandListParams(
        state = VoiceCommandListState(
            startCommands = listOf(
                SavedVoiceCommand(id = 1, name = "Begin Session", firebaseKey = "key", type = VoiceCommandTypes.Start),
                SavedVoiceCommand(id = 2, name = "Start Recording", firebaseKey = "key", type = VoiceCommandTypes.Start),
                SavedVoiceCommand(id = 3, name = "Let's Go", firebaseKey = "key", type = VoiceCommandTypes.Start)
            ),
            stopCommands = listOf(
                SavedVoiceCommand(id = 4, name = "End Session", firebaseKey = "key", type = VoiceCommandTypes.Stop),
                SavedVoiceCommand(id = 5, name = "Stop Recording", firebaseKey = "key", type = VoiceCommandTypes.Stop)
            ),
            makeCommands = listOf(
                SavedVoiceCommand(id = 6, name = "Made It", firebaseKey = "key", type = VoiceCommandTypes.Make),
                SavedVoiceCommand(id = 7, name = "Good Shot", firebaseKey = "key", type = VoiceCommandTypes.Make),
                SavedVoiceCommand(id = 8, name = "Perfect", firebaseKey = "key", type = VoiceCommandTypes.Make)
            ),
            missCommands = listOf(
                SavedVoiceCommand(id = 9, name = "Missed It", firebaseKey = "key", type = VoiceCommandTypes.Miss),
                SavedVoiceCommand(id = 10, name = "Bad Shot", firebaseKey = "key", type = VoiceCommandTypes.Miss)
            ),
            selectedFilter = VoiceCommandFilter.START,
            filteredCommands = listOf(
                SavedVoiceCommand(id = 1, name = "Begin Session", firebaseKey = "key", type = VoiceCommandTypes.Start),
                SavedVoiceCommand(id = 2, name = "Start Recording", firebaseKey = "key", type = VoiceCommandTypes.Start),
                SavedVoiceCommand(id = 3, name = "Let's Go", firebaseKey = "key", type = VoiceCommandTypes.Start)
            )
        ),
        onToolbarMenuClicked = {},
        onFilterSelected = {},
        onCreateEditCommandTypeClicked = { test, test2 -> }
    )

    Column(modifier = Modifier.background(AppColors.White)) {
        VoiceCommandListScreen(params = params)
    }
}
