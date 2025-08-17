package com.nicholas.rutherford.track.your.shot.feature.settings.termsconditions

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * UI model representing a single section in the Terms & Conditions screen.
 *
 * This class is used to display individual terms items, each consisting of a title and
 * a corresponding description. These items are typically rendered in a vertical list.
 *
 * @param title The heading of the term section (e.g., "Accounts", "Contacting Us").
 * @param description The detailed explanation associated with the section title.
 */
data class TermsConditionInfo(
    val title: String,
    val description: String
)
