package com.nicholas.rutherford.track.your.shot.feature.settings.termsconditions

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Defines navigation actions available from the create account screen.
 */
interface TermsConditionsNavigation {
    fun finish()
    fun navigateToDevEmail(email: String)
    fun navigateToOnboarding()
    fun navigateToPlayerList()
    fun navigateToSettings()
}
