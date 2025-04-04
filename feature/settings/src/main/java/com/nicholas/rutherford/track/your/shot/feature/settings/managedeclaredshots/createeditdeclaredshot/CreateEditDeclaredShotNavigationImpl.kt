package com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots.createeditdeclaredshot

import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import com.nicholas.rutherford.track.your.shot.navigation.Navigator

class CreateEditDeclaredShotNavigationImpl(private val navigator: Navigator) : CreateEditDeclaredShotNavigation {
    override fun pop() = navigator.pop(popRouteAction = Constants.POP_DEFAULT_ACTION)
}