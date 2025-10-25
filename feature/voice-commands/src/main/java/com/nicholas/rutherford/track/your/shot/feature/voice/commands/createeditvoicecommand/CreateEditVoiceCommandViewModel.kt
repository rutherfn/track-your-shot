package com.nicholas.rutherford.track.your.shot.feature.voice.commands.createeditvoicecommand

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.SavedStateHandle
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.base.vm.BaseViewModel
import com.nicholas.rutherford.track.your.shot.data.room.repository.SavedVoiceCommandRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.SavedVoiceCommand
import com.nicholas.rutherford.track.your.shot.data.room.response.VoiceCommandTypes
import com.nicholas.rutherford.track.your.shot.data.room.response.VoiceCommandTypes.Companion.toDisplayLabel
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.alert.AlertConfirmAndDismissButton
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.your.shot.feature.voice.commands.VoiceCommandState
import com.nicholas.rutherford.track.your.shot.firebase.core.create.CreateFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.firebase.core.delete.DeleteFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.firebase.core.update.UpdateFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.firebase.realtime.SavedVoiceCommandRealtimeResponse
import com.nicholas.rutherford.track.your.shot.firebase.realtime.SavedVoiceCommandRealtimeWithKeyResponse
import com.nicholas.rutherford.track.your.shot.helper.extensions.safeLet
import com.nicholas.rutherford.voice.flow.core.VoicePhraseCapture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale

/**
 * Created by Nicholas Rutherford, last edited on 2025-10-06
 *
 * ViewModel for managing the saved voice commands commands
 *
 * Responsibilities include:
 * - Fetching all saved voice commands whenever the [onCreate] block is called
 * - Saving new voice commands to the database
 * - Updating existing voice commands in the database
 * - Deleting voice commands from the database
 *
 * @param savedStateHandle Contains navigation parameters (commandType, phrase) passed from previous screen.
 * @param application The Application instance used to fetch string resources.
 * @param scope CoroutineScope for launching asynchronous tasks
 * @param createFirebaseUserInfo Firebase helper to create voice commands from Realtime Database
 * @param updateFirebaseUserInfo Firebase helper to update voice commands from Realtime Database
 * @param deleteFirebaseUserInfo Firebase helper to delete voice commands from Realtime Database
 * @param savedVoiceCommandRepository Repository for voice command data operations
 * @param navigation Handles navigation to create/edit voice command screens
 */
class CreateEditVoiceCommandViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val application: Application,
    private val scope: CoroutineScope,
    private val createFirebaseUserInfo: CreateFirebaseUserInfo,
    private val updateFirebaseUserInfo: UpdateFirebaseUserInfo,
    private val deleteFirebaseUserInfo: DeleteFirebaseUserInfo,
    private val savedVoiceCommandRepository: SavedVoiceCommandRepository,
    private val navigation: CreateEditVoiceCommandNavigation
) : BaseViewModel() {

    private var type: VoiceCommandTypes? = null
    private var phrase: String? = null
    private var isCommandAlreadyCreated: Boolean = false
    private val voiceCommandTypeValueParam: Int? = savedStateHandle.get<Int>("voiceCommandTypeValueParam")
    private val recordedPhraseParam: String? = savedStateHandle.get<String>("recordedPhraseParam")

    internal val createEditVoiceCommandMutableStateFlow = MutableStateFlow(value = CreateEditVoiceCommandState())

    val createEditVoiceCommandStateFlow = createEditVoiceCommandMutableStateFlow.asStateFlow()

    /**
     * Type of [VoicePhraseCapture] that comes from voice-flow library
     * Creates a instance to capture phrases; and goes ahead and captures the phrase
     *
     */
    private val voicePhraseCapture = VoicePhraseCapture(
        context = application,
        onPhraseCaptured = { phrase -> onPhraseCaptured(phrase = phrase) },
        onError = { error -> onErrorPhraseCaptured(error = error) },
        language = Locale.ENGLISH
    )

    /**
     * [onCreate] function block that gets called when the view model gets created.
     * Updates the state set from the [savedStateHandle] parameters.
     */
    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        scope.launch { setInfoFromParamsAndUpdateState() }
    }
    /**
     * [onCleared] function block that gets called when the view model is cleared.
     * Stops listening for voice commands.
     *
     * We don't want to continue listening to commands
     * Doing so, will introduce a memory leak.
     */

    override fun onCleared() {
        super.onCleared()
        voicePhraseCapture.stopListening()
    }

    internal fun isCurrentlyEditingSavedVoiceCommand(phrase: String?, type: VoiceCommandTypes?): Boolean {
        return type?.let { voiceCommandType ->
            voiceCommandType != VoiceCommandTypes.None && !phrase.isNullOrEmpty()
        } ?: false
    }

    /**
     * [onCreate] function block that gets called when the view model gets created.
     * Updates the state set from the [savedStateHandle] parameters.
     */
    private suspend fun setInfoFromParamsAndUpdateState() {
        voiceCommandTypeValueParam?.let { value -> type = VoiceCommandTypes.fromValue(value = value) }
        phrase = recordedPhraseParam
        isCommandAlreadyCreated = safeLet(phrase, type) { voicePhrase, voiceType -> buildIsCommandAlreadyCreated(voicePhrase = voicePhrase, voiceType = voiceType)} ?: false

        val isEditingShot = isCurrentlyEditingSavedVoiceCommand(phrase = phrase, type = type)

        createEditVoiceCommandMutableStateFlow.update { state ->
            state.copy(
                type = type ?: VoiceCommandTypes.None,
                recordedPhrase = phrase,
                voiceCommandState = if (isEditingShot) {
                    VoiceCommandState.EDITING_EXISTING
                } else {
                    VoiceCommandState.CREATING
                },
                isCommandAlreadyCreated = isCommandAlreadyCreated
            )
        }
    }

    private suspend fun buildIsCommandAlreadyCreated(voicePhrase: String, voiceType: VoiceCommandTypes): Boolean {
        return savedVoiceCommandRepository.getVoiceCommandByName(name = voicePhrase)?.let { savedVoiceCommand ->
            savedVoiceCommand.type == voiceType
        } ?: false
    }

    private fun buildHasOverriddenExistingCommand(phrase: String) : Boolean {
        return recordedPhraseParam?.let { existingPhrase ->
            existingPhrase != phrase
        } ?: false
    }

    private fun buildSavedNewCommandDescriptionAlert(type: VoiceCommandTypes): String {
        return when (type) {
            is VoiceCommandTypes.Start -> application.getString(StringsIds.savedNewStartCommandDescription)
            is VoiceCommandTypes.Stop -> application.getString(StringsIds.savedNewStopCommandDescription)
            is VoiceCommandTypes.Make -> application.getString(StringsIds.savedNewMakeCommandDescription)
            else -> application.getString(StringsIds.savedNewMissCommandDescription)
        }
    }

    private fun buildEditCommandDescriptionAlert(type: VoiceCommandTypes): String {
        return when (type) {
            is VoiceCommandTypes.Start -> application.getString(StringsIds.editedNewStartCommandDescription)
            is VoiceCommandTypes.Stop -> application.getString(StringsIds.editedNewStopCommandDescription)
            is VoiceCommandTypes.Make -> application.getString(StringsIds.editedNewMakeCommandDescription)
            else -> application.getString(StringsIds.editedNewMissCommandDescription)
        }
    }

    fun onToolbarMenuClicked() = navigation.pop()

    fun onRecordPhraseClicked() {
        createEditVoiceCommandMutableStateFlow.update { state ->
            state.copy(
                voiceCommandState = VoiceCommandState.RECORDING_NEW,
                isRecording = true
            )
        }
        voicePhraseCapture.startListening()
    }

    fun onPhraseCaptured(phrase: String) {
        this.phrase = phrase

        createEditVoiceCommandMutableStateFlow.update { state ->
            state.copy(
                recordedPhrase = phrase,
                isRecording = false,
                voiceCommandState = VoiceCommandState.EDITING_EXISTING,
                hasOverriddenExistingCommand = buildHasOverriddenExistingCommand(phrase = phrase)
            )
        }

        voicePhraseCapture.stopListening()
    }

    fun onErrorPhraseCaptured(error: String) {
        createEditVoiceCommandMutableStateFlow.update { state ->
            state.copy(
                isRecording = false,
                voiceCommandState = VoiceCommandState.RECORDING_ERROR,
                voiceCapturedErrorDescription = error,
                hasOverriddenExistingCommand = false
            )
        }
    }

    fun onDismissErrorClicked() {
        createEditVoiceCommandMutableStateFlow.update { state ->
            state.copy(
                voiceCommandState = VoiceCommandState.CREATING,
                voiceCapturedErrorDescription = null
            )
        }
    }

    fun onTryAgainClicked() {
        createEditVoiceCommandMutableStateFlow.update { state ->
            state.copy(
                voiceCommandState = VoiceCommandState.RECORDING_NEW,
                isRecording = true
            )
        }
        voicePhraseCapture.startListening()
    }

    fun onRecordAgainClicked() {
        createEditVoiceCommandMutableStateFlow.update { state ->
            state.copy(
                voiceCommandState = VoiceCommandState.EDITING_EXISTING,
                isRecording = true
            )
        }
        voicePhraseCapture.startListening()
    }

    fun dismissProgressAndShowNoCommandEnteredAlert() {
        navigation.disableProgress()
        navigation.alert(alert = errorNoCommandEnteredAlert())
    }

    fun dismissProgressAndShowCouldNotDeleteCommandAlert() {
        navigation.disableProgress()
        navigation.alert(alert = errorCouldNotDeleteCommandAlert())
    }

    fun areYouSureYouWantToDeleteCommandAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.deletingCommand),
            confirmButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.yes),
                onButtonClicked = {
                    scope.launch { attemptToDeleteVoiceCommand() }
                }
            ),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.no)
            ),
            description =  application.getString(StringsIds.areYouSureYouWantToDeleteCommand)
        )
    }

    fun savedNewCommandAlert(type: VoiceCommandTypes): Alert {
        return Alert(
            title = application.getString(StringsIds.newCommandSavedSuccessfully),
            dismissButton = AlertConfirmAndDismissButton(buttonText = application.getString(StringsIds.gotIt)),
            description =  buildSavedNewCommandDescriptionAlert(type = type)
        )
    }

    fun editNewCommandAlert(type: VoiceCommandTypes): Alert {
        return Alert(
            title = application.getString(StringsIds.editCommandSuccessfully),
            dismissButton = AlertConfirmAndDismissButton(buttonText = application.getString(StringsIds.gotIt)),
            description = buildEditCommandDescriptionAlert(type = type)
        )
    }

    fun deleteCommandAlert(type: VoiceCommandTypes): Alert {
        return Alert(
            title = application.getString(StringsIds.deleteCommandSuccessfully),
            dismissButton = AlertConfirmAndDismissButton(buttonText = application.getString(StringsIds.gotIt)),
            description = application.getString(StringsIds.deleteCommandSuccessfullyDescription, type.toDisplayLabel())
        )
    }

    fun errorNoCommandEnteredAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.noCommandEntered),
            dismissButton = AlertConfirmAndDismissButton(buttonText = application.getString(StringsIds.gotIt)),
            description = application.getString(StringsIds.noCommandEnteredDescription)
        )
    }

    fun errorSavingNewVoiceCommandAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.cannotSaveVoiceCommand),
            dismissButton = AlertConfirmAndDismissButton(buttonText = application.getString(StringsIds.gotIt)),
            description = application.getString(StringsIds.errorVoiceCommandSavedFailure)
        )
    }

    fun errorEditingExistingVoiceCommandAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.cannotEditVoiceCommand),
            dismissButton = AlertConfirmAndDismissButton(buttonText = application.getString(StringsIds.gotIt)),
            description = application.getString(StringsIds.errorVoiceCommandEditFailure),
        )
    }

    fun errorCannotEditSameVoiceCommandAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.cannotEditVoiceCommand),
            dismissButton = AlertConfirmAndDismissButton(buttonText = application.getString(StringsIds.gotIt)),
            description = application.getString(StringsIds.errorVoiceCommandEditFailedWithSameName)
        )
    }

    fun errorCouldNotDeleteCommandAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.couldNotDeleteCommand),
            dismissButton = AlertConfirmAndDismissButton(buttonText = application.getString(StringsIds.gotIt)),
            description = application.getString(StringsIds.couldNotDeleteCommandDescription)
        )
    }

    fun onDeleteVoiceCommand() = navigation.alert(alert = areYouSureYouWantToDeleteCommandAlert())

    fun attemptToDeleteVoiceCommand() {
        scope.launch {
            navigation.enableProgress(progress = Progress())

            recordedPhraseParam?.let { phraseParam ->
                savedVoiceCommandRepository.getVoiceCommandByName(name = phraseParam)
                    ?.let { savedVoiceCommand ->
                        deleteFirebaseUserInfo.deleteSavedVoiceCommand(
                            savedVoiceCommandKey = savedVoiceCommand.firebaseKey
                        ).collectLatest { hasCommandBeenDeleted ->
                            if (hasCommandBeenDeleted) {
                                savedVoiceCommandRepository.deleteSavedVoiceCommand(savedVoiceCommand = savedVoiceCommand)

                                navigation.disableProgress()
                                navigation.pop()
                                navigation.alert(alert = deleteCommandAlert(type = type ?: VoiceCommandTypes.None))
                            } else {
                                dismissProgressAndShowCouldNotDeleteCommandAlert()
                            }
                        }

                    } ?: dismissProgressAndShowCouldNotDeleteCommandAlert()
            } ?: dismissProgressAndShowNoCommandEnteredAlert()
        }
    }

    fun onEditVoiceCommand(phrase: String, type: VoiceCommandTypes) {
        scope.launch {
            navigation.enableProgress(progress = Progress())

            recordedPhraseParam?.let { phraseParam ->
                if (phraseParam == phrase) {
                    navigation.disableProgress()
                    navigation.alert(alert = errorCannotEditSameVoiceCommandAlert())
                } else {
                    val currentCommand =
                        savedVoiceCommandRepository.getVoiceCommandByName(name = phraseParam)

                    savedVoiceCommandRepository.getVoiceCommandByName(name = phraseParam)
                        ?.let { savedVoiceCommand ->
                            updateFirebaseUserInfo.updateSavedVoiceCommand(
                                savedVoiceCommandRealtimeWithKeyResponse = SavedVoiceCommandRealtimeWithKeyResponse(
                                    savedVoiceCommandKey = savedVoiceCommand.firebaseKey,
                                    savedVoiceCommandInfo = SavedVoiceCommandRealtimeResponse(
                                        name = phrase,
                                        typeValue = savedVoiceCommand.type.value
                                    )

                                )
                            ).collectLatest { hasUpdatedVoiceCommand ->
                                if (hasUpdatedVoiceCommand) {
                                    savedVoiceCommandRepository.updateSavedVoiceCommand(
                                        savedVoiceCommand = SavedVoiceCommand(
                                            id = currentCommand?.id ?: 1,
                                            name = phrase,
                                            firebaseKey = savedVoiceCommand.firebaseKey,
                                            type = type
                                        )
                                    )

                                    navigation.disableProgress()
                                    navigation.pop()
                                    navigation.alert(alert = editNewCommandAlert(type = type))
                                } else {
                                    navigation.disableProgress()
                                    navigation.alert(alert = errorEditingExistingVoiceCommandAlert())
                                }
                            }
                        }
                }
            } ?: dismissProgressAndShowNoCommandEnteredAlert()
        }
    }

    fun onSaveNewVoiceCommand() {
        scope.launch {
            navigation.enableProgress(progress = Progress())

            phrase?.let { commandName ->
                createFirebaseUserInfo.attemptToCreateSavedVoiceCommandFirebaseRealtimeDatabaseResponseFlow(
                    savedVoiceCommandRealtimeResponse = SavedVoiceCommandRealtimeResponse(
                        name = commandName,
                        typeValue = type?.value ?: VoiceCommandTypes.None.value
                    )
                ).collectLatest { result ->
                    if (result.first || !result.second.isNullOrEmpty()) {
                        val currentSavedCommandSize = savedVoiceCommandRepository.getVoiceCommandSize()

                        savedVoiceCommandRepository.createSavedVoiceCommand(
                            savedVoiceCommand = SavedVoiceCommand(
                                id = currentSavedCommandSize + 1,
                                name = commandName,
                                firebaseKey = result.second ?: "",
                                type = type ?: VoiceCommandTypes.None
                            )
                        )
                        navigation.disableProgress()
                        navigation.pop()
                        navigation.alert(alert = savedNewCommandAlert(type = type ?: VoiceCommandTypes.None))
                    } else {
                        navigation.disableProgress()
                        navigation.alert(alert = errorSavingNewVoiceCommandAlert())
                    }
                }
            } ?: dismissProgressAndShowNoCommandEnteredAlert()
        }
    }
}
