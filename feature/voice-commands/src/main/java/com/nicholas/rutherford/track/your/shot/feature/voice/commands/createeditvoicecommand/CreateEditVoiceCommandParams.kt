package com.nicholas.rutherford.track.your.shot.feature.voice.commands.createeditvoicecommand

import com.nicholas.rutherford.track.your.shot.data.room.response.VoiceCommandTypes

data class CreateEditVoiceCommandParams(
    val state: CreateEditVoiceCommandState,
    val onRecordPhraseClicked: () -> Unit,
    val onSaveNewVoiceCommandClicked: () -> Unit,
    val onEditVoiceCommandClicked: (String, VoiceCommandTypes) -> Unit,
    val onDeleteVoiceCommandClicked: () -> Unit,
    val onToolbarMenuClicked: () -> Unit,
    val onDismissErrorClicked: () -> Unit,
    val onTryAgainClicked: () -> Unit,
    val onRecordAgainClicked: () -> Unit
)
