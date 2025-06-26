package com.nicholas.rutherford.track.your.shot.feature.settings.onboardingeducation

import com.nicholas.rutherford.track.your.shot.navigation.NavigationDestinations
import com.nicholas.rutherford.track.your.shot.navigation.Navigator

class OnboardingEducationNavigationImpl(private val navigator: Navigator) : OnboardingEducationNavigation {
    override fun pop() = navigator.pop(popRouteAction = NavigationDestinations.SETTINGS_SCREEN)
}
