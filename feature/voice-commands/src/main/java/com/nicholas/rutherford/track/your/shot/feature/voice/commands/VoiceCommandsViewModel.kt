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
            VoiceCommandFilter.VIEW_ALL -> getAllCommands(state = state)
            VoiceCommandFilter.START -> state.startCommands
            VoiceCommandFilter.STOP -> state.stopCommands
            VoiceCommandFilter.MAKE -> state.makeCommands
            VoiceCommandFilter.MISS -> state.missCommands
        }
    }

    private fun getAllCommands(state: VoiceCommandsState): List<SavedVoiceCommand> = state.startCommands + state.stopCommands + state.makeCommands + state.missCommands + state.noneCommands
}