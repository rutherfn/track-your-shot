package com.nicholas.rutherford.track.your.shot.feature.settings.termsconditions

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * UI parameters required for rendering and interacting with the Terms & Conditions screen.
 *
 * This class holds the current UI state and callback functions for user actions, allowing
 * the screen to remain stateless and reactive.
 *
 * @param onBackClicked Callback triggered when the user presses the back button.
 * @param onCloseAcceptButtonClicked Callback triggered when the user presses the close or acknowledge button.
 * @param onDevEmailClicked Callback triggered when the user taps the developer email link.
 * @param state The current state of the Terms & Conditions screen, including button text and content list.
 */
data class TermsConditionsParams(
    val onBackClicked: () -> Unit,
    val onCloseAcceptButtonClicked: () -> Unit,
    val onDevEmailClicked: () -> Unit,
    val state: TermsConditionsState
)
