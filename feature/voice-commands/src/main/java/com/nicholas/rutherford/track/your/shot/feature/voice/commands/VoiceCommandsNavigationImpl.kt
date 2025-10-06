package com.nicholas.rutherford.track.your.shot.feature.voice.commands

import com.nicholas.rutherford.track.your.shot.navigation.NavigationActions
import com.nicholas.rutherford.track.your.shot.navigation.Navigator

class VoiceCommandsNavigationImpl(private val navigator: Navigator) : VoiceCommandsNavigation {
    override fun openNavigationDrawer() = navigator.showNavigationDrawer(navigationDrawerAction = true)

    override fun navigateToCreateVoiceCommand(type: Int?, phrase: String?) = navigator.navigate(navigationAction = NavigationActions.VoiceCommands.createVoiceCommandsWithParams(type = type, phrase = phrase))
}