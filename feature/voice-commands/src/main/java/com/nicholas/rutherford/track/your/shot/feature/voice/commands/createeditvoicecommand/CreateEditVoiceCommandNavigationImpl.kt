package com.nicholas.rutherford.track.your.shot.feature.voice.commands.createeditvoicecommand

import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.your.shot.navigation.NavigationDestinations
import com.nicholas.rutherford.track.your.shot.navigation.Navigator

class CreateEditVoiceCommandNavigationImpl(private val navigator: Navigator) : CreateEditVoiceCommandNavigation {
    override fun disableProgress() = navigator.progress(progressAction = null)

    override fun enableProgress(progress: Progress) = navigator.progress(progressAction = progress)

    override fun pop() = navigator.pop(popRouteAction = NavigationDestinations.VOICE_COMMANDS_SCREEN)
}
