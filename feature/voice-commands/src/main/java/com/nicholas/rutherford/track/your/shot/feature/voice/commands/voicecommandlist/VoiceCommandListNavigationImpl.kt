package com.nicholas.rutherford.track.your.shot.feature.voice.commands.voicecommandlist

import com.nicholas.rutherford.track.your.shot.navigation.NavigationActions
import com.nicholas.rutherford.track.your.shot.navigation.Navigator

class VoiceCommandListNavigationImpl(private val navigator: Navigator) :
    VoiceCommandListNavigation {
    override fun openNavigationDrawer() = navigator.showNavigationDrawer(navigationDrawerAction = true)

    override fun navigateToCreateVoiceCommand(type: Int?, phrase: String?) = navigator.navigate(navigationAction = NavigationActions.VoiceCommands.createEditVoiceCommandsWithParams(type = type, phrase = phrase))
}
