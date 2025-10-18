package com.nicholas.rutherford.track.your.shot.feature.voice.commands.voicecommandlist

interface VoiceCommandListNavigation {
    fun openNavigationDrawer()
    fun navigateToCreateEditVoiceCommand(type: Int?, phrase: String?)
}
