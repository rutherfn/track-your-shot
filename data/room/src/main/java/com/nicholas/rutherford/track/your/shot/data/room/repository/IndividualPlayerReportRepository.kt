package com.nicholas.rutherford.track.your.shot.data.room.repository

import com.nicholas.rutherford.track.your.shot.data.room.response.IndividualPlayerReport

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Repository interface defining CRUD operations for IndividualPlayerReport data.
 * Provides an abstraction layer over the underlying database, allowing
 * usage of IndividualPlayerReport domain models instead of database entities.
 */
interface IndividualPlayerReportRepository {

    /** Inserts a single [IndividualPlayerReport] into the database. */
    suspend fun createReport(report: IndividualPlayerReport)

    /** Inserts a list of [IndividualPlayerReport] into the database. */
    suspend fun createReports(individualPlayerReports: List<IndividualPlayerReport>)

    /** Retrieves all player reports from the database as a list of [IndividualPlayerReport]. */
    suspend fun fetchAllReports(): List<IndividualPlayerReport>

    /** Deletes all player reports from the database. */
    suspend fun deleteAllReports()

    /**
     * Deletes a player report by its [firebaseKey].
     *
     * @param firebaseKey The Firebase key identifying the report to delete.
     */
    suspend fun deleteReportByFirebaseKey(firebaseKey: String)

    /** Retrieves the total count of player reports in the database. */
    suspend fun fetchReportCount(): Int
}
