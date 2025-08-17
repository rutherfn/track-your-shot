package com.nicholas.rutherford.track.your.shot.data.test.firebase.realtime

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Utility object for creating test instances of [com.nicholas.rutherford.track.your.shot.firebase.realtime.IndividualPlayerReportRealtimeResponse].
 * Provides predefined values for logged date, player name, and PDF URL.
 */
object TestIndividualPlayerReportRealtimeResponse {

    /**
     * Creates a test [com.nicholas.rutherford.track.your.shot.firebase.realtime.IndividualPlayerReportRealtimeResponse] instance with predefined
     * logged date, player name, and PDF URL.
     *
     * @return a [com.nicholas.rutherford.track.your.shot.firebase.realtime.IndividualPlayerReportRealtimeResponse] populated with test values.
     */
    fun create(): com.nicholas.rutherford.track.your.shot.firebase.realtime.IndividualPlayerReportRealtimeResponse {
        return _root_ide_package_.com.nicholas.rutherford.track.your.shot.firebase.realtime.IndividualPlayerReportRealtimeResponse(
            loggedDateValue = LOGGED_DATE_VALUE,
            playerName = PLAYER_NAME,
            pdfUrl = PDF_URL
        )
    }
}

/** Predefined test logged date value for [com.nicholas.rutherford.track.your.shot.firebase.realtime.IndividualPlayerReportRealtimeResponse]. */
const val LOGGED_DATE_VALUE = 22L

/** Predefined test player name for [com.nicholas.rutherford.track.your.shot.firebase.realtime.IndividualPlayerReportRealtimeResponse]. */
const val PLAYER_NAME = "player name"

/** Predefined test PDF URL for [com.nicholas.rutherford.track.your.shot.firebase.realtime.IndividualPlayerReportRealtimeResponse]. */
const val PDF_URL = "pdf_url"
