package com.nicholas.rutherford.track.your.shot.firebase.realtime

class TestIndividualPlayerReportWithKeyRealtimeResponse {

    fun create(): IndividualPlayerReportWithKeyRealtimeResponse {
        return IndividualPlayerReportWithKeyRealtimeResponse(
            reportFirebaseKey = REPORT_FIREBASE_KEY,
            playerReport = TestIndividualPlayerReportRealtimeResponse.create()
        )
    }
}

const val REPORT_FIREBASE_KEY = "-j0P5J2LcXmXF"
