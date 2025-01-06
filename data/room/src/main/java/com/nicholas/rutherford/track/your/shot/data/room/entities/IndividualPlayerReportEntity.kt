package com.nicholas.rutherford.track.your.shot.data.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nicholas.rutherford.track.your.shot.data.room.response.IndividualPlayerReport

@Entity(tableName = "individualPlayerReport")
data class IndividualPlayerReportEntity(
    @PrimaryKey
    val id: Int,
    @ColumnInfo("loggedDateValue")
    val loggedDateValue: Long,
    @ColumnInfo("playerName")
    val playerName: String,
    @ColumnInfo("pdfUrl")
    val pdfUrl: String
)

fun IndividualPlayerReportEntity.toIndividualPlayerReport(): IndividualPlayerReport {
    return IndividualPlayerReport(
        loggedDateValue = loggedDateValue,
        playerName = playerName,
        pdfUrl = pdfUrl
    )
}
