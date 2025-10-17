package com.nicholas.rutherford.track.your.shot.feature.voice.commands.createeditvoicecommand

import androidx.lifecycle.SavedStateHandle
import com.nicholas.rutherford.track.your.shot.base.vm.BaseViewModel
import com.nicholas.rutherford.track.your.shot.data.room.response.VoiceCommandTypes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CreateEditVoiceCommandViewModel(
    savedStateHandle: SavedStateHandle,
    private val navigation: CreateEditVoiceCommandNavigation
) : BaseViewModel() {

    private var type: VoiceCommandTypes? = null
    private var phrase: String? = null
    private val voiceCommandTypeValueParam: Int? = savedStateHandle.get<Int>("voiceCommandTypeValueParam")
    private val recordedPhraseParam: String? = savedStateHandle.get<String>("recordedPhraseParam")

    internal val createEditVoiceCommandMutableStateFlow =
        MutableStateFlow(value = CreateEditVoiceCommandState())

    val createEditVoiceCommandStateFlow = createEditVoiceCommandMutableStateFlow.asStateFlow()

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
        navigation.pop()
    }
}
