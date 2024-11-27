package com.nicholas.rutherford.track.your.shot.feature.settings.onboardingeducation

import com.nicholas.rutherford.track.your.shot.data.shared.EducationInfo

data class OnboardingEducationState(
    val educationInfoList: List<EducationInfo> = emptyList()
)