package com.nicholas.rutherford.track.your.shot.data.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nicholas.rutherford.track.your.shot.data.room.response.IndividualPlayerReport

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Room entity representing an individual player's report stored in the local database.
 *
 * @property id The unique identifier for the report.
 * @property loggedDateValue The timestamp (in milliseconds) when the report was logged.
 * @property playerName The name of the player associated with this report.
 * @property firebaseKey The Firebase key associated with this report.
 * @property pdfUrl The URL to the PDF file of the player's report.
 */
@Entity(tableName = "individualPlayerReport")
data class IndividualPlayerReportEntity(
    @PrimaryKey
    val id: Int,

    @ColumnInfo("loggedDateValue")
    val loggedDateValue: Long,

    @ColumnInfo("playerName")
    val playerName: String,

    @ColumnInfo(name = "firebaseKey")
    val firebaseKey: String,

    @ColumnInfo("pdfUrl")
    val pdfUrl: String
)

/**
 * Converts an [IndividualPlayerReportEntity] to its corresponding domain model [IndividualPlayerReport].
 *
 * @return A [IndividualPlayerReport] instance with values mapped from this entity.
 */
fun IndividualPlayerReportEntity.toIndividualPlayerReport(): IndividualPlayerReport {
    return IndividualPlayerReport(
        id = id,
        loggedDateValue = loggedDateValue,
        playerName = playerName,
        firebaseKey = firebaseKey,
        pdfUrl = pdfUrl
    )
}
