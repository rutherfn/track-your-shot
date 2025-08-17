package com.nicholas.rutherford.track.your.shot.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.nicholas.rutherford.track.your.shot.data.room.entities.IndividualPlayerReportEntity

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Data Access Object (DAO) for performing CRUD operations on the [IndividualPlayerReportEntity] table.
 * Provides methods for inserting, querying, and deleting individual player reports.
 */
@Dao
interface IndividualPlayerReportDao {

    /**
     * Inserts a single individual player report into the database.
     *
     * @param individualPlayerReportEntity The report entity to insert.
     */
    @Insert
    suspend fun insert(individualPlayerReportEntity: IndividualPlayerReportEntity)

    /**
     * Inserts multiple individual player reports into the database.
     *
     * @param individualPlayerReports A list of report entities to insert.
     */
    @Insert
    suspend fun insertAll(individualPlayerReports: List<IndividualPlayerReportEntity>)

    /**
     * Retrieves all individual player reports from the database.
     *
     * @return A list of all [IndividualPlayerReportEntity] objects.
     */
    @Query("SELECT * FROM individualPlayerReport")
    suspend fun getAllPlayerReports(): List<IndividualPlayerReportEntity>

    /**
     * Deletes all individual player reports from the database.
     */
    @Query("DELETE FROM individualPlayerReport")
    suspend fun deleteAll()

    /**
     * Deletes a specific individual player report by its Firebase key.
     *
     * @param firebaseKey The Firebase key of the report to delete.
     */
    @Query("DELETE FROM individualPlayerReport WHERE firebaseKey = :firebaseKey")
    suspend fun deleteReportByFirebaseKey(firebaseKey: String)

    /**
     * Retrieves the total count of individual player reports in the database.
     *
     * @return The count of reports as an integer.
     */
    @Query("SELECT COUNT(*) FROM individualPlayerReport")
    suspend fun getPlayerReportsCount(): Int
}
