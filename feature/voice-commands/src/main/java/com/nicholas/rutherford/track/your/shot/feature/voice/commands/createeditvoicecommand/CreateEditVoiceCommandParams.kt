package com.nicholas.rutherford.track.your.shot.feature.voice.commands.createeditvoicecommand

data class CreateEditVoiceCommandParams(
    val state: CreateEditVoiceCommandState,
    val onRecordPhraseClicked: () -> Unit,
    val onSaveNewVoiceCommandClicked: () -> Unit,
    val onToolbarMenuClicked: () -> Unit,
    val onDismissErrorClicked: () -> Unit,
    val onTryAgainClicked: () -> Unit,
    val onRecordAgainClicked: () -> Unit
)
