package com.nicholas.rutherford.track.your.shot.feature.voice.commands.voicecommandlist

import androidx.lifecycle.LifecycleOwner
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
 * ViewModel for managing the voice command list screen state and interactions.
 *
 * Responsibilities include:
 * - Fetching all saved voice commands from the repository
 * - Categorizing commands by type (Start, Stop, Make, Miss)
 * - Filtering commands based on selected filter
 * - Managing navigation to create new voice commands
 * - Updating state when commands are added, edited, or deleted
 *
 * @param scope CoroutineScope for launching asynchronous tasks
 * @param navigation Handles navigation to create/edit voice command screens
 * @param savedVoiceCommandRepository Repository for voice command data operations
 */
class VoiceCommandListViewModel(
    private val scope: CoroutineScope,
    private val navigation: VoiceCommandListNavigation,
    private val savedVoiceCommandRepository: SavedVoiceCommandRepository
) : BaseViewModel() {

    internal val voiceCommandMutableStateFlow = MutableStateFlow(value = VoiceCommandListState())
    val voiceCommandsStateFlow = voiceCommandMutableStateFlow.asStateFlow()

    /**
     * [onCreate] function block that gets called when the view model gets created.
     * Fetches all voice commands from the repository and updates the state.
     */
    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        scope.launch { fetchVoiceCommandsAndUpdateState() }
    }

    /**
     * Handles toolbar menu click events.
     * Opens the navigation drawer when the toolbar menu is tapped.
     */
    fun onToolbarMenuClicked() = navigation.openNavigationDrawer()

    /**
     * Handles voice command filter selection.
     * Updates the selected filter and rebuilds the filtered commands list.
     * 
     * @param filter The selected voice command filter (Start, Stop, Make, Miss)
     */
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

    /**
     * Fetches all voice commands from the repository and updates the state.
     * Categorizes commands by type and sets the default filter to START.
     */
    private suspend fun fetchVoiceCommandsAndUpdateState() {
        val voiceCommands = savedVoiceCommandRepository.getAllVoiceCommands()

        val startCommands = voiceCommands.filterBy(type = VoiceCommandTypes.Start)
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

    /**
     * Builds a filtered list of voice commands based on the selected filter.
     * Returns the appropriate command list for the given filter type.
     * 
     * @param selectedFilter The currently selected filter type
     * @param startCommands List of START type commands
     * @param stopCommands List of STOP type commands  
     * @param makeCommands List of MAKE type commands
     * @param missCommands List of MISS type commands
     * @return Filtered list of commands matching the selected filter
     */
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

    /**
     * Handles navigation to the create edit voice command screen.
     * Passes the selected command type and optional phrase to the create screen.
     * 
     * @param type The voice command type value (Start, Stop, Make, Miss)
     * @param phrase Optional pre-filled phrase for the command
     */
    fun onCreateEditCommandTypeClicked(type: Int?, phrase: String?) = navigation.navigateToCreateEditVoiceCommand(type = type, phrase = phrase)
}
