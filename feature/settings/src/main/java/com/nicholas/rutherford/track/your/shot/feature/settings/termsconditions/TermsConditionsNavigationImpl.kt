package com.nicholas.rutherford.track.your.shot.feature.settings.termsconditions

import com.nicholas.rutherford.track.your.shot.navigation.NavigationActions
import com.nicholas.rutherford.track.your.shot.navigation.Navigator

class TermsConditionsNavigationImpl(private val navigator: Navigator) : TermsConditionsNavigation {

    override fun finish() = navigator.finish(finishAction = true)
    override fun navigateToDevEmail(email: String) = navigator.emailDevAction(emailDevAction = email)
    override fun navigateToOnboarding() = navigator.navigate(navigationAction = NavigationActions.TermsConditions.onboardingEducation(isFirstTimeLaunched = true))
    override fun navigateToPlayerList() = navigator.navigate(navigationAction = NavigationActions.TermsConditions.playerList())
    override fun navigateToSettings() = navigator.navigate(navigationAction = NavigationActions.TermsConditions.settings())
}
