package com.nicholas.rutherford.track.your.shot.feature.settings.debugtoggle

import com.nicholas.rutherford.track.your.shot.base.vm.BaseViewModel
import com.nicholas.rutherford.track.your.shot.base.vm.FlowCollectionTrigger
import com.nicholas.rutherford.track.your.shot.data.store.reader.DataStorePreferencesReader
import com.nicholas.rutherford.track.your.shot.data.store.writer.DataStorePreferencesWriter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Created by Nicholas Rutherford, last edited on 2025-09-29
 *
 * ViewModel responsible for managing debug toggle states in the settings screen.
 * This ViewModel handles the state management for debug-related toggles such as voice
 * recording and video upload debug features.
 *
 * The ViewModel extends [BaseViewModel] to inherit lifecycle-aware Flow collection
 * and automatic cleanup capabilities.
 *
 * @param dataStorePreferencesReader Interface for reading debug toggle preferences from DataStore
 * @param dataStoreWriterPreferencesWriter Interface for writing debug toggle preferences to DataStore
 * @param navigation Defines navigation actions available from the debug toggle screen.
 * @param scope CoroutineScope for managing ViewModel-level coroutines
 *
 * @see DebugToggleState for the state data class
 * @see BaseViewModel for lifecycle management and Flow collection utilities
 */
class DebugToggleViewModel(
    private val dataStorePreferencesReader: DataStorePreferencesReader,
    private val dataStoreWriterPreferencesWriter: DataStorePreferencesWriter,
    private val navigation: DebugToggleNavigation,
    private val scope: CoroutineScope
) : BaseViewModel() {

    // Override to provide the injected scope
    override fun getScope(): CoroutineScope? = scope

    // Override to start collecting flows immediately in init
    override fun getFlowCollectionTrigger(): FlowCollectionTrigger = FlowCollectionTrigger.INIT

    /**
     * Internal mutable state flow for debug toggle states.
     * This holds the current state of all debug-related toggles.
     */
    internal val debugToggleMutableStateFlow = MutableStateFlow(value = DebugToggleState())

    /**
     * Public read-only state flow for debug toggle states.
     * UI components should observe this flow to react to state changes.
     */
    val debugToggleStateFlow = debugToggleMutableStateFlow.asStateFlow()

    init {
        collectToggleFlows()
    }

    /**
     * Collects debug toggle states from DataStore using the BaseViewModel's collectFlows method.
     * This method combines two flows:
     * - Voice debug toggle state
     * - Video upload debug toggle state
     *
     * The collection is lifecycle-aware and will automatically pause/resume based on
     * the ViewModel's lifecycle state. Init flow collection is enabled in init
     * to ensure flows are collected immediately during initialization.
     */
    fun collectToggleFlows() {
        collectFlows(
            flow1 = dataStorePreferencesReader.readVoiceToggledDebugEnabledFlow(),
            flow2 = dataStorePreferencesReader.readUploadVideoToggledDebugEnabled()
        ) { voiceToggled, uploadVideoToggled ->
            debugToggleMutableStateFlow.update { state ->
                state.copy(
                    voiceToggledState = voiceToggled,
                    videoUploadToggleState = uploadVideoToggled
                )
            }
        }
    }

    /**
     * Handles back button click events.
     * Triggers navigation to pop the current screen from the back stack.
     */
    fun onBackClicked() = navigation.pop()

    /**
     * Handles voice debug toggle state changes.
     * Persists the new state to DataStore asynchronously.
     *
     * @param value The new state for the voice debug toggle
     */
    fun onVoiceDebugToggled(value: Boolean) = scope.launch { dataStoreWriterPreferencesWriter.saveVoiceToggledDebugEnabled(value = value) }

    /**
     * Handles video upload debug toggle state changes.
     * Persists the new state to DataStore asynchronously.
     *
     * @param value The new state for the video upload debug toggle
     */
    fun onVideoUploadDebugToggled(value: Boolean) = scope.launch { dataStoreWriterPreferencesWriter.saveUploadVideoToggledDebugEnabled(value = value) }
}
