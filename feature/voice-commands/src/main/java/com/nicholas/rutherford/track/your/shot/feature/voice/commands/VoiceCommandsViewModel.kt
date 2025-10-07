package com.nicholas.rutherford.track.your.shot.feature.voice.commands

import com.nicholas.rutherford.track.your.shot.base.vm.BaseViewModel
import com.nicholas.rutherford.track.your.shot.data.room.repository.SavedVoiceCommandRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.SavedVoiceCommand
import com.nicholas.rutherford.track.your.shot.data.room.response.VoiceCommandTypes
import com.nicholas.rutherford.track.your.shot.data.room.response.filterBy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Created by Nicholas Rutherford, last edited on 2025-10-06
 *
 * ViewModel for seeing a list of your current voice commands saved from
 * following categories: Start, Stop, Make, and Miss
 *
 * Responsibilities include:
 * - Ability to manage voice commands tied to Start, Stop, Make, and Miss
 * - Create new commands tied to Start, Stop, Make, and Miss
 *
 * @param application Provides access to string resources.
 * @param authenticationFirebase Provides Firebase authentication functions.
 * @param navigation Handles navigation and alert display.
 * @param scope CoroutineScope for launching asynchronous tasks.
 */
class VoiceCommandsViewModel(
    scope: CoroutineScope,
    private val navigation: VoiceCommandsNavigation,
    private val savedVoiceCommandRepository: SavedVoiceCommandRepository
) : BaseViewModel() {

    internal val voiceCommandMutableStateFlow = MutableStateFlow(value = VoiceCommandsState())
    val voiceCommandsStateFlow = voiceCommandMutableStateFlow.asStateFlow()

    init {
        scope.launch { fetchVoiceCommandsUpdateState() }
    }

    fun onToolbarMenuClicked() = navigation.openNavigationDrawer()

    fun onFilterSelected(filter: VoiceCommandFilter) {
        voiceCommandMutableStateFlow.update { state ->
            state.copy(
                selectedFilter = filter,
                filteredCommands = buildFilteredSavedCommands(state = state)
            )
        }
    }

    suspend fun fetchVoiceCommandsUpdateState() {
        val voiceCommands = savedVoiceCommandRepository.getAllVoiceCommands()

        voiceCommandMutableStateFlow.update { state ->
            state.copy(
                startCommands = voiceCommands.filterBy(type = VoiceCommandTypes.Start),
                stopCommands = voiceCommands.filterBy(type = VoiceCommandTypes.Stop),
                makeCommands = voiceCommands.filterBy(type = VoiceCommandTypes.Make),
                missCommands = voiceCommands.filterBy(type = VoiceCommandTypes.Miss),
                noneCommands = voiceCommands.filterBy(type = VoiceCommandTypes.None),
                filteredCommands = buildFilteredSavedCommands(state = state)
            )
        }
    }

    private fun buildFilteredSavedCommands(state: VoiceCommandsState): List<SavedVoiceCommand> {
        return when (state.selectedFilter) {
            VoiceCommandFilter.START -> state.startCommands
            VoiceCommandFilter.STOP -> state.stopCommands
            VoiceCommandFilter.MAKE -> state.makeCommands
            else -> state.missCommands
        }
    }

    fun onCreateCommandTypeClicked(type: Int?, phrase: String?) = navigation.navigateToCreateVoiceCommand(type = type, phrase = phrase)
}