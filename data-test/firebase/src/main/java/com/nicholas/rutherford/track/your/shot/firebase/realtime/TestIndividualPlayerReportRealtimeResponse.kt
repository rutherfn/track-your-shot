package com.nicholas.rutherford.track.your.shot.firebase.realtime

object TestIndividualPlayerReportRealtimeResponse {
    fun create(): IndividualPlayerReportRealtimeResponse {
        return IndividualPlayerReportRealtimeResponse(
            loggedDateValue = LOGGED_DATE_VALUE,
            playerName = PLAYER_NAME,
            pdfUrl = PDF_URL
        )
    }
}

const val LOGGED_DATE_VALUE = 22L
const val PLAYER_NAME = "player name"
const val PDF_URL = "pdf_url"
