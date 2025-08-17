package com.nicholas.rutherford.track.your.shot.firebase.realtime

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Wrapper data class for an individual player report in Firebase Realtime Database,
 * including its unique Firebase key.
 *
 * @property reportFirebaseKey The unique key for this report in Firebase Realtime Database.
 * @property playerReport The actual individual player report data.
 */
data class IndividualPlayerReportWithKeyRealtimeResponse(
    val reportFirebaseKey: String,
    val playerReport: IndividualPlayerReportRealtimeResponse
)
