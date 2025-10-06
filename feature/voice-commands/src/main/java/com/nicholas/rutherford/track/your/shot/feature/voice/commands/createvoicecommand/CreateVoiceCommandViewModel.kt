package com.nicholas.rutherford.track.your.shot.feature.voice.commands.createvoicecommand

import androidx.lifecycle.SavedStateHandle
import com.nicholas.rutherford.track.your.shot.base.vm.BaseViewModel
import com.nicholas.rutherford.track.your.shot.data.room.response.VoiceCommandTypes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CreateVoiceCommandViewModel(
    savedStateHandle: SavedStateHandle,
    private val navigation: CreateVoiceCommandNavigation
) : BaseViewModel() {

    private var type: VoiceCommandTypes? = null
    private var phrase: String? = null
    private val voiceCommandTypeValueParam: Int? = savedStateHandle.get<Int>("voiceCommandTypeValueParam")
    private val recordedPhraseParam: String? = savedStateHandle.get<String>("recordedPhraseParam")

    internal val createVoiceCommandMutableStateFlow =
        MutableStateFlow(value = CreateVoiceCommandState())

    val createVoiceCommandStateFlow = createVoiceCommandMutableStateFlow.asStateFlow()

    init {
        voiceCommandTypeValueParam?.let { value ->
            type = VoiceCommandTypes.fromValue(value = value)
        }
        phrase = recordedPhraseParam
    }

    fun onToolbarMenuClicked() = navigation.pop()
}