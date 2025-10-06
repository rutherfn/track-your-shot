package com.nicholas.rutherford.track.your.shot.feature.voice.commands.createvoicecommand

import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import com.nicholas.rutherford.track.your.shot.navigation.Navigator

class CreateVoiceCommandNavigationImpl(private val navigator: Navigator) : CreateVoiceCommandNavigation {

    override fun pop() = navigator.pop(popRouteAction = Constants.POP_DEFAULT_ACTION)
}