package com.nicholas.rutherford.track.your.shot.data.room.response

import com.nicholas.rutherford.track.your.shot.data.room.entities.IndividualPlayerReportEntity

data class IndividualPlayerReport(
    val loggedDateValue: Long,
    val playerName: String,
    val pdfUrl: String
)

fun IndividualPlayerReport.toIndividualPlayerReportEntity(): IndividualPlayerReportEntity {
    return IndividualPlayerReportEntity(
        id = 0,
        loggedDateValue = loggedDateValue,
        playerName = playerName,
        pdfUrl = pdfUrl
    )
}
