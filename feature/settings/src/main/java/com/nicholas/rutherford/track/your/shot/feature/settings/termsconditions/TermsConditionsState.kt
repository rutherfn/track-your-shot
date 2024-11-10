package com.nicholas.rutherford.track.your.shot.feature.settings.termsconditions

data class TermsConditionsState(
    val infoList: List<TermsConditionInfo> = emptyList(),
    val buttonText: String = ""
)