package com.nicholas.rutherford.track.your.shot.feature.settings.onboardingeducation

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Parameters used to render the OnboardingEducationScreen UI.
 *
 * @property onGotItButtonClicked Callback function triggered when the user clicks the "Got It" button.
 * @property state The current state of the onboarding education screen.
 */
data class OnboardingEducationParams(
    val onGotItButtonClicked: () -> Unit,
    val state: OnboardingEducationState
)
