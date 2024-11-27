package com.nicholas.rutherford.track.your.shot.feature.settings.onboardingeducation

data class OnboardingEducationParams(
    val onGotItButtonClicked: () -> Unit,
    val state: OnboardingEducationState
)