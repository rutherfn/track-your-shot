package com.nicholas.rutherford.track.your.shot.data.room.response

import com.nicholas.rutherford.track.your.shot.data.room.entities.IndividualPlayerReportEntity

data class IndividualPlayerReport(
    val id: Int,
    val loggedDateValue: Long,
    val playerName: String,
    val firebaseKey: String,
    val pdfUrl: String
)

fun IndividualPlayerReport.toIndividualPlayerReportEntity(): IndividualPlayerReportEntity {
    return IndividualPlayerReportEntity(
        id = id,
        loggedDateValue = loggedDateValue,
        playerName = playerName,
        firebaseKey = firebaseKey,
        pdfUrl = pdfUrl
    )
}
