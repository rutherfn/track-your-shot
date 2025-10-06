package com.nicholas.rutherford.track.your.shot.feature.voice.commands.createvoicecommand

data class CreateVoiceCommandParams(
    val state: CreateVoiceCommandState,
    val onRecordClicked: () -> Unit,
    val onSaveClicked: () -> Unit,
    val onToolbarMenuClicked: () -> Unit
)
