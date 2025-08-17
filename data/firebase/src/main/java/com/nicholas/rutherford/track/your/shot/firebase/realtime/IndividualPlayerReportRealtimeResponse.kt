package com.nicholas.rutherford.track.your.shot.firebase.realtime

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Data class representing an individual player report stored in Firebase Realtime Database.
 * Contains information about the date the report was logged, the player’s name, and the URL to the PDF report.
 *
 * @property loggedDateValue The timestamp (in milliseconds) when the report was logged.
 * @property playerName The full name of the player associated with this report.
 * @property pdfUrl The URL pointing to the PDF file of the report.
 */
data class IndividualPlayerReportRealtimeResponse(
    val loggedDateValue: Long = 0L,
    val playerName: String = "",
    val pdfUrl: String = ""
)
