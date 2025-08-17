package com.nicholas.rutherford.track.your.shot.data.room.repository

import com.nicholas.rutherford.track.your.shot.data.room.dao.IndividualPlayerReportDao
import com.nicholas.rutherford.track.your.shot.data.room.entities.toIndividualPlayerReport
import com.nicholas.rutherford.track.your.shot.data.room.response.IndividualPlayerReport
import com.nicholas.rutherford.track.your.shot.data.room.response.toIndividualPlayerReportEntity

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Repository implementation for managing IndividualPlayerReport data.
 * Provides a layer over IndividualPlayerReportDao, allowing use of
 * IndividualPlayerReport domain models instead of database entities.
 */
class IndividualPlayerReportRepositoryImpl(
    private val individualPlayerReportDao: IndividualPlayerReportDao
) : IndividualPlayerReportRepository {

    /** Inserts a single [IndividualPlayerReport] into the database. */
    override suspend fun createReport(report: IndividualPlayerReport) =
        individualPlayerReportDao.insert(individualPlayerReportEntity = report.toIndividualPlayerReportEntity())

    /** Inserts a list of [IndividualPlayerReport] into the database. */
    override suspend fun createReports(individualPlayerReports: List<IndividualPlayerReport>) =
        individualPlayerReportDao.insertAll(
            individualPlayerReports = individualPlayerReports.map { it.toIndividualPlayerReportEntity() }
        )

    /** Retrieves all player reports from the database as a list of [IndividualPlayerReport]. */
    override suspend fun fetchAllReports(): List<IndividualPlayerReport> =
        individualPlayerReportDao.getAllPlayerReports().map { it.toIndividualPlayerReport() }

    /** Deletes all player reports from the database. */
    override suspend fun deleteAllReports() = individualPlayerReportDao.deleteAll()

    /**
     * Deletes a player report by its [firebaseKey].
     *
     * @param firebaseKey The Firebase key identifying the report to delete.
     */
    override suspend fun deleteReportByFirebaseKey(firebaseKey: String) =
        individualPlayerReportDao.deleteReportByFirebaseKey(firebaseKey = firebaseKey)

    /** Retrieves the total count of player reports in the database. */
    override suspend fun fetchReportCount(): Int = individualPlayerReportDao.getPlayerReportsCount()
}
