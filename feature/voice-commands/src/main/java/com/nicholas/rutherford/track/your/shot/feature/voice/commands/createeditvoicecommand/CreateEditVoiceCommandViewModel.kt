package com.nicholas.rutherford.track.your.shot.feature.voice.commands.createeditvoicecommand

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import com.nicholas.rutherford.track.your.shot.base.vm.BaseViewModel
import com.nicholas.rutherford.track.your.shot.compose.components.dialogs.ProgressDialog
import com.nicholas.rutherford.track.your.shot.data.room.response.VoiceCommandTypes
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.your.shot.feature.voice.commands.VoiceCommandState
import com.nicholas.rutherford.voice.flow.core.VoicePhraseCapture
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Locale

class CreateEditVoiceCommandViewModel(
    savedStateHandle: SavedStateHandle,
    application: Application,
    private val navigation: CreateEditVoiceCommandNavigation
) : BaseViewModel() {

    private var type: VoiceCommandTypes? = null
    private var phrase: String? = null
    private val voiceCommandTypeValueParam: Int? = savedStateHandle.get<Int>("voiceCommandTypeValueParam")
    private val recordedPhraseParam: String? = savedStateHandle.get<String>("recordedPhraseParam")

    internal val createEditVoiceCommandMutableStateFlow =
        MutableStateFlow(value = CreateEditVoiceCommandState())

    val createEditVoiceCommandStateFlow = createEditVoiceCommandMutableStateFlow.asStateFlow()

    private val voicePhraseCapture = VoicePhraseCapture(
        context = application,
        onPhraseCaptured = { phrase -> onPhraseCaptured(phrase = phrase) },
        onError = { error -> onErrorPhraseCaptured(error = error) },
        language = Locale.ENGLISH
    )

    init {
        voiceCommandTypeValueParam?.let { value ->
            type = VoiceCommandTypes.fromValue(value = value)
        }
        phrase = recordedPhraseParam

        createEditVoiceCommandMutableStateFlow.update { state ->
            state.copy(
                type = type ?: VoiceCommandTypes.None,
                recordedPhrase = phrase
            )
        }
    }

    fun onToolbarMenuClicked() {
        voicePhraseCapture.stopListening()
        navigation.pop()
    }

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
        createEditVoiceCommandMutableStateFlow.update { state ->
            state.copy(
                recordedPhrase = phrase,
                isRecording = false,
                voiceCommandState = VoiceCommandState.EDITING_EXISTING
            )
        }

        voicePhraseCapture.stopListening()
    }

    fun onErrorPhraseCaptured(error: String) {
        createEditVoiceCommandMutableStateFlow.update { state ->
            state.copy(
                isRecording = false,
                voiceCommandState = VoiceCommandState.RECORDING_ERROR,
                voiceCapturedErrorDescription = error
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

    fun onSaveNewVoiceCommand() {
        navigation.enableProgress(progress = Progress())
    }
}
