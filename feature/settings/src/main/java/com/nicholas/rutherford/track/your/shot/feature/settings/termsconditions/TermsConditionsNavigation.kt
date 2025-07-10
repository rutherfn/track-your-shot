package com.nicholas.rutherford.track.your.shot.feature.settings.termsconditions

interface TermsConditionsNavigation {
    fun finish()
    fun navigateToDevEmail(email: String)
    fun navigateToOnboarding()
    fun navigateToPlayerList()
    fun navigateToSettings()
}
