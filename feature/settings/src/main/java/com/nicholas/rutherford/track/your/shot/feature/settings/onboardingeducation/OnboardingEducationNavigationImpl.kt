package com.nicholas.rutherford.track.your.shot.feature.settings.onboardingeducation

import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import com.nicholas.rutherford.track.your.shot.navigation.Navigator

class OnboardingEducationNavigationImpl(private val navigator: Navigator) : OnboardingEducationNavigation {
    override fun pop() = navigator.pop(popRouteAction = Constants.POP_DEFAULT_ACTION)
}
