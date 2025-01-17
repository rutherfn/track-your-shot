package com.nicholas.rutherford.track.your.shot.firebase.realtime

data class IndividualPlayerReportWithKeyRealtimeResponse(
    val reportFirebaseKey: String,
    val playerReport: IndividualPlayerReportRealtimeResponse
)
