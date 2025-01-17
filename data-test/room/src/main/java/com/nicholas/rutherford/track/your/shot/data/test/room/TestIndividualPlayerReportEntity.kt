package com.nicholas.rutherford.track.your.shot.data.test.room

import com.nicholas.rutherford.track.your.shot.data.room.entities.IndividualPlayerReportEntity

object TestIndividualPlayerReportEntity {

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
