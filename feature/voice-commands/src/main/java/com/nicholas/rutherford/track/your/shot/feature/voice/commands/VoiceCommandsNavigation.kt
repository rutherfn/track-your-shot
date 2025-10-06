package com.nicholas.rutherford.track.your.shot.feature.voice.commands

interface VoiceCommandsNavigation {
    fun openNavigationDrawer()
    fun navigateToCreateVoiceCommand(type: Int?, phrase: String?)
}