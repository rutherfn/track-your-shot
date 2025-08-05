package com.nicholas.rutherford.track.your.shot.feature.settings.onboardingeducation

import com.nicholas.rutherford.track.your.shot.navigation.NavigationActions
import com.nicholas.rutherford.track.your.shot.navigation.NavigationDestinations
import com.nicholas.rutherford.track.your.shot.navigation.Navigator

/**
 * Implementation of [OnboardingEducationNavigation] using a [Navigator] to perform
 * navigation via predefined [NavigationActions].
 */
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
