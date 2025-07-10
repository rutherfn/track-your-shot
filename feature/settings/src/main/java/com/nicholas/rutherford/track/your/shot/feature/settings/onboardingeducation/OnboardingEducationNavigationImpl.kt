package com.nicholas.rutherford.track.your.shot.feature.settings.onboardingeducation

import com.nicholas.rutherford.track.your.shot.navigation.NavigationDestinations
import com.nicholas.rutherford.track.your.shot.navigation.Navigator

class OnboardingEducationNavigationImpl(private val navigator: Navigator) : OnboardingEducationNavigation {
    override fun pop(isFirstTimeLaunchedParam: Boolean) {
        val popRouteAction = if (isFirstTimeLaunchedParam) {
            NavigationDestinations.PLAYERS_LIST_SCREEN
        } else {
            NavigationDestinations.SETTINGS_SCREEN
        }
        navigator.pop(popRouteAction = popRouteAction)
    }
}
