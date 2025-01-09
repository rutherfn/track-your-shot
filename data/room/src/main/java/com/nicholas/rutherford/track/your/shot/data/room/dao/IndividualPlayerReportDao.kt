package com.nicholas.rutherford.track.your.shot.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.nicholas.rutherford.track.your.shot.data.room.entities.IndividualPlayerReportEntity

@Dao
interface IndividualPlayerReportDao {

    @Insert
    suspend fun insert(individualPlayerReportEntity: IndividualPlayerReportEntity)

    @Query("SELECT * FROM individualPlayerReport")
    suspend fun getAllPlayerReports(): List<IndividualPlayerReportEntity>

    @Query("DELETE FROM individualPlayerReport")
    suspend fun deleteAll()
}
