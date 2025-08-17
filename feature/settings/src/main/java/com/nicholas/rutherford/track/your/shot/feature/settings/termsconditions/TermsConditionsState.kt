package com.nicholas.rutherford.track.your.shot.feature.settings.termsconditions

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * UI state for the Terms & Conditions screen.
 *
 * This class represents the visual state of the Terms & Conditions screen, including
 * the list of content sections to be shown and the text displayed on the bottom action button.
 *
 * @param infoList A list of [TermsConditionInfo] objects representing each section of the terms.
 * @param buttonText The text displayed on the acknowledge or close button at the bottom of the screen.
 */
data class TermsConditionsState(
    val infoList: List<TermsConditionInfo> = emptyList(),
    val buttonText: String = ""
)
