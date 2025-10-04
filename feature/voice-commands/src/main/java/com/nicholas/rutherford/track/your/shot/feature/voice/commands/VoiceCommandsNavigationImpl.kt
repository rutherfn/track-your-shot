package com.nicholas.rutherford.track.your.shot.feature.voice.commands

import com.nicholas.rutherford.track.your.shot.navigation.Navigator

class VoiceCommandsNavigationImpl(private val navigator: Navigator) : VoiceCommandsNavigation {
    override fun openNavigationDrawer() = navigator.showNavigationDrawer(navigationDrawerAction = true)
}