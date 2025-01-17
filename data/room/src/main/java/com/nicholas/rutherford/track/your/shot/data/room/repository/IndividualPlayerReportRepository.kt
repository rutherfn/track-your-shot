package com.nicholas.rutherford.track.your.shot.data.room.repository

import com.nicholas.rutherford.track.your.shot.data.room.response.IndividualPlayerReport

interface IndividualPlayerReportRepository {
    suspend fun createReport(report: IndividualPlayerReport)
    suspend fun createReports(individualPlayerReports: List<IndividualPlayerReport>)
    suspend fun fetchAllReports(): List<IndividualPlayerReport>
    suspend fun deleteAllReports()
    suspend fun deleteReportByFirebaseKey(firebaseKey: String)
    suspend fun fetchReportCount(): Int
}
