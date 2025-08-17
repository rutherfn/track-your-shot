package com.nicholas.rutherford.track.your.shot.firebase.realtime

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Utility object for creating test instances of [IndividualPlayerReportRealtimeResponse].
 * Provides predefined values for logged date, player name, and PDF URL.
 */
object TestIndividualPlayerReportRealtimeResponse {

    /**
     * Creates a test [IndividualPlayerReportRealtimeResponse] instance with predefined
     * logged date, player name, and PDF URL.
     *
     * @return a [IndividualPlayerReportRealtimeResponse] populated with test values.
     */
    fun create(): IndividualPlayerReportRealtimeResponse {
        return IndividualPlayerReportRealtimeResponse(
            loggedDateValue = LOGGED_DATE_VALUE,
            playerName = PLAYER_NAME,
            pdfUrl = PDF_URL
        )
    }
}

/** Predefined test logged date value for [IndividualPlayerReportRealtimeResponse]. */
const val LOGGED_DATE_VALUE = 22L

/** Predefined test player name for [IndividualPlayerReportRealtimeResponse]. */
const val PLAYER_NAME = "player name"

/** Predefined test PDF URL for [IndividualPlayerReportRealtimeResponse]. */
const val PDF_URL = "pdf_url"
