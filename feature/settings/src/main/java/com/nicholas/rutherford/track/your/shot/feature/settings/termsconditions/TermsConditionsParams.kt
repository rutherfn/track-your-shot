package com.nicholas.rutherford.track.your.shot.feature.settings.termsconditions

data class TermsConditionsParams(
    val updateButtonTextState:  () -> Unit,
    val onCloseAcceptButtonClicked: () -> Unit,
    val onDevEmailClicked: () -> Unit,
    val state: TermsConditionsState,
    val isAcknowledgeConditions: Boolean

)