package com.nicholas.rutherford.track.your.shot.data.room.response

import com.nicholas.rutherford.track.your.shot.data.room.entities.IndividualPlayerReportEntity

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Represents a report for an individual player's performance.
 *
 * @property id Unique identifier for the report.
 * @property loggedDateValue Timestamp representing when the report was logged.
 * @property playerName Name of the player associated with the report.
 * @property firebaseKey Firebase identifier for this report.
 * @property pdfUrl URL pointing to the PDF version of the report.
 */
data class IndividualPlayerReport(
    val id: Int,
    val loggedDateValue: Long,
    val playerName: String,
    val firebaseKey: String,
    val pdfUrl: String
)

/**
 * Converts an [IndividualPlayerReport] instance to its corresponding
 * [IndividualPlayerReportEntity] for Room database storage.
 *
 * @return [IndividualPlayerReportEntity] instance with the same values.
 */
fun IndividualPlayerReport.toIndividualPlayerReportEntity(): IndividualPlayerReportEntity {
    return IndividualPlayerReportEntity(
        id = id,
        loggedDateValue = loggedDateValue,
        playerName = playerName,
        firebaseKey = firebaseKey,
        pdfUrl = pdfUrl
    )
}
