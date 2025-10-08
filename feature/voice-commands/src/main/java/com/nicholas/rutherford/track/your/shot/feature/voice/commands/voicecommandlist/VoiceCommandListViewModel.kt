package com.nicholas.rutherford.track.your.shot.feature.voice.commands.voicecommandlist

import com.nicholas.rutherford.track.your.shot.base.vm.BaseViewModel
import com.nicholas.rutherford.track.your.shot.data.room.repository.SavedVoiceCommandRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.SavedVoiceCommand
import com.nicholas.rutherford.track.your.shot.data.room.response.VoiceCommandTypes
import com.nicholas.rutherford.track.your.shot.data.room.response.filterBy
import com.nicholas.rutherford.track.your.shot.feature.voice.commands.VoiceCommandFilter
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
 * - Ability to manage voice commands tied to Start, Stop, Make, and  Miss
 * - Create new commands tied to Start, Stop, Make, and Miss
 *
 * @param authenticationFirebase Provides Firebase authentication functions.
 * @param navigation Handles navigation and alert display.
 * @param scope CoroutineScope for launching asynchronous tasks.
 */
class VoiceCommandListViewModel(
    scope: CoroutineScope,
    private val navigation: VoiceCommandListNavigation,
    private val savedVoiceCommandRepository: SavedVoiceCommandRepository
) : BaseViewModel() {

    internal val voiceCommandMutableStateFlow = MutableStateFlow(value = VoiceCommandListState())
    val voiceCommandsStateFlow = voiceCommandMutableStateFlow.asStateFlow()

    init {
        scope.launch { fetchVoiceCommandsAndUpdateState() }
    }

    fun onToolbarMenuClicked() = navigation.openNavigationDrawer()

    fun onFilterSelected(filter: VoiceCommandFilter) {
        voiceCommandMutableStateFlow.update { state ->
            state.copy(
                selectedFilter = filter,
                filteredCommands = buildFilteredSavedCommands(
                    selectedFilter = filter,
                    startCommands = state.startCommands,
                    stopCommands = state.stopCommands,
                    makeCommands = state.makeCommands,
                    missCommands = state.missCommands
                )
            )
        }
    }

    private suspend fun fetchVoiceCommandsAndUpdateState() {
        val voiceCommands = savedVoiceCommandRepository.getAllVoiceCommands()

        val startCommands =  voiceCommands.filterBy(type = VoiceCommandTypes.Start)
        val stopCommands = voiceCommands.filterBy(type = VoiceCommandTypes.Stop)
        val makeCommands = voiceCommands.filterBy(type = VoiceCommandTypes.Make)
        val missCommands = voiceCommands.filterBy(type = VoiceCommandTypes.Miss)
        val noneCommands = voiceCommands.filterBy(type = VoiceCommandTypes.None)
        val selectedFilter = VoiceCommandFilter.START

        voiceCommandMutableStateFlow.update { state ->
            state.copy(
                startCommands = startCommands,
                stopCommands = stopCommands,
                makeCommands = makeCommands,
                missCommands = missCommands,
                noneCommands = noneCommands,
                selectedFilter = selectedFilter,
                filteredCommands = buildFilteredSavedCommands(
                    selectedFilter = selectedFilter,
                    startCommands = startCommands,
                    stopCommands = stopCommands,
                    makeCommands = makeCommands,
                    missCommands = missCommands
                )
            )
        }
    }

    internal fun buildFilteredSavedCommands(
        selectedFilter: VoiceCommandFilter,
        startCommands: List<SavedVoiceCommand>,
        stopCommands: List<SavedVoiceCommand>,
        makeCommands: List<SavedVoiceCommand>,
        missCommands: List<SavedVoiceCommand>
    ): List<SavedVoiceCommand> {
        return when (selectedFilter) {
            VoiceCommandFilter.START -> startCommands
            VoiceCommandFilter.STOP -> stopCommands
            VoiceCommandFilter.MAKE -> makeCommands
            else -> missCommands
        }
    }

    fun onCreateCommandTypeClicked(type: Int?, phrase: String?) = navigation.navigateToCreateVoiceCommand(type = type, phrase = phrase)
}