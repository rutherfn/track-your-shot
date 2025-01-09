package com.nicholas.rutherford.track.your.shot.data.room.repository

import com.nicholas.rutherford.track.your.shot.data.room.dao.IndividualPlayerReportDao
import com.nicholas.rutherford.track.your.shot.data.room.entities.toIndividualPlayerReport
import com.nicholas.rutherford.track.your.shot.data.room.response.IndividualPlayerReport
import com.nicholas.rutherford.track.your.shot.data.room.response.toIndividualPlayerReportEntity

class IndividualPlayerReportRepositoryImpl(private val individualPlayerReportDao: IndividualPlayerReportDao) : IndividualPlayerReportRepository {

    override suspend fun createReport(report: IndividualPlayerReport) = individualPlayerReportDao.insert(individualPlayerReportEntity = report.toIndividualPlayerReportEntity())

    override suspend fun fetchAllReports(): List<IndividualPlayerReport> = individualPlayerReportDao.getAllPlayerReports().map { it.toIndividualPlayerReport() }

    override suspend fun deleteAllReports() = individualPlayerReportDao.deleteAll()

    override suspend fun fetchReportCount(): Int = individualPlayerReportDao.getPlayerReportsCount()
}
