package com.nicholas.rutherford.track.your.shot.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.nicholas.rutherford.track.your.shot.data.room.entities.IndividualPlayerReportEntity

@Dao
interface IndividualPlayerReportDao {

    @Insert
    suspend fun insert(individualPlayerReportEntity: IndividualPlayerReportEntity)

    @Insert
    suspend fun insertAll(individualPlayerReports: List<IndividualPlayerReportEntity>)

    @Query("SELECT * FROM individualPlayerReport")
    suspend fun getAllPlayerReports(): List<IndividualPlayerReportEntity>

    @Query("DELETE FROM individualPlayerReport")
    suspend fun deleteAll()

    @Query("DELETE FROM individualPlayerReport WHERE firebaseKey = :firebaseKey")
    suspend fun deleteReportByFirebaseKey(firebaseKey: String)

    @Query("SELECT COUNT(*) FROM individualPlayerReport")
    suspend fun getPlayerReportsCount(): Int
}
