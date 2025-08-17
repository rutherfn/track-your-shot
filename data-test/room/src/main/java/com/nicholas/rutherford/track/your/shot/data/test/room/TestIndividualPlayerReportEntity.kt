package com.nicholas.rutherford.track.your.shot.data.test.room

import com.nicholas.rutherford.track.your.shot.data.room.entities.IndividualPlayerReportEntity

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Utility object for creating test instances of [IndividualPlayerReportEntity].
 * Provides default values for testing purposes.
 */
object TestIndividualPlayerReportEntity {

    /**
     * Builds a test [IndividualPlayerReportEntity] instance.
     *
     * @return a new [IndividualPlayerReportEntity] with predefined test values.
     */
    fun build(): IndividualPlayerReportEntity {
        return IndividualPlayerReportEntity(
            id = INDIVIDUAL_PLAYER_REPORT_ID,
            loggedDateValue = LOGGED_DATE_VALUE,
            playerName = PLAYER_NAME,
            firebaseKey = INDIVIDUAL_FIREBASE_KEY,
            pdfUrl = PDF_URL
        )
    }

    const val INDIVIDUAL_PLAYER_REPORT_ID = 1
    const val LOGGED_DATE_VALUE = 11L
    const val PLAYER_NAME = "player name"
    const val INDIVIDUAL_FIREBASE_KEY = "firebase-key"
    const val PDF_URL = "url"
}
