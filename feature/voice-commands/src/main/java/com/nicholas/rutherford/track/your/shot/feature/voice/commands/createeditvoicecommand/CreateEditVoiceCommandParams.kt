package com.nicholas.rutherford.track.your.shot.feature.voice.commands.createeditvoicecommand

data class CreateEditVoiceCommandParams(
    val state: CreateEditVoiceCommandState,
    val onRecordClicked: () -> Unit,
    val onSaveClicked: () -> Unit,
    val onToolbarMenuClicked: () -> Unit
)
