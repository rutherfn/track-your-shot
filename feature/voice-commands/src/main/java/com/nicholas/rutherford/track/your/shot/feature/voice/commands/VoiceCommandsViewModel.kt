package com.nicholas.rutherford.track.your.shot.feature.voice.commands

import com.nicholas.rutherford.track.your.shot.base.vm.BaseViewModel
import com.nicholas.rutherford.track.your.shot.data.room.repository.SavedVoiceCommandRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.VoiceCommandTypes
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

    suspend fun fetchVoiceCommandsUpdateState() {
        val voiceCommands = savedVoiceCommandRepository.getAllVoiceCommands()

        voiceCommandMutableStateFlow.update { state ->
            state.copy(
                startCommands = voiceCommands.filter { command -> command.type == VoiceCommandTypes.Start },
                stopCommands = voiceCommands.filter {  command -> command.type == VoiceCommandTypes.Stop },
                makeCommands = voiceCommands.filter {  command -> command.type == VoiceCommandTypes.Make },
                missCommands = voiceCommands.filter {  command -> command.type == VoiceCommandTypes.Miss },
                noneCommands = voiceCommands.filter {  command -> command.type == VoiceCommandTypes.None }
            )
        }

    }
}