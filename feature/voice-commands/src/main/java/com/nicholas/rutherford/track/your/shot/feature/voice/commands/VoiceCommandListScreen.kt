package com.nicholas.rutherford.track.your.shot.feature.voice.commands

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
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Info
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
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.your.shot.AppColors
import com.nicholas.rutherford.track.your.shot.base.resources.Colors
import com.nicholas.rutherford.track.your.shot.compose.components.BaseRow
import com.nicholas.rutherford.track.your.shot.feature.voice.commands.voicecommandlist.VoiceCommandListParams
import com.nicholas.rutherford.track.your.shot.feature.voice.commands.voicecommandlist.VoiceCommandListState
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles

@Composable
fun VoiceCommandListScreen(params: VoiceCommandListParams) {
    val state = params.state
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        
        Text(
            text = "Set up voice commands to easily log your shots while playing. Use your custom phrases to automatically start or stop a voice session, and to record made or missed shots.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        VoiceFilters(state = state, onFilterSelected = params.onFilterSelected)

        if (state.filteredCommands.isEmpty()) {
            VoiceCommandsEmptyStateContent(state = state, onCreateCommandTypeClicked = params.onCreateCommandTypeClicked)
        } else {
            VoiceCommandTypeListContent(state = state)
        }
    }
}

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
                label = { Text(
                    text = filter.toDisplayLabel(),
                    style = TextStyles.bodyBold
                ) },
                selected = state.selectedFilter == filter,
                leadingIcon = if (state.selectedFilter == filter) {
                    {
                        Icon(
                            imageVector = Icons.Filled.Done,
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
                text = "No ${state.selectedFilter.toDisplayLabel()} Commands Yet",
                style = TextStyles.smallBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Create your first ${state.selectedFilter.toDisplayLabel().lowercase()} command to start logging shots with your voice",
                style = TextStyles.body,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Colors.secondaryColor),
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    onCreateCommandTypeClicked.invoke(state.selectedFilter.toType().value, null)
                }
            ) {
                Text(
                    text = "Create ${state.selectedFilter.toDisplayLabel()} Command",
                    style = TextStyles.smallBold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun VoiceCommandTypeListContent(state: VoiceCommandListState) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        state.filteredCommands.forEachIndexed { index, command ->
            BaseRow(
                title = command.name, // The phrase like "swish", "brick"
                imageVector = Icons.Default.Info, // or ArrowForward
                onClicked = {
                    // Handle command tap - you'll need to add this to params
                },
                shouldShowDivider = index < state.filteredCommands.size - 1
            )
        }
    }
}