package com.nicholas.rutherford.track.your.shot.data.room

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nicholas.rutherford.track.your.shot.data.room.dao.IndividualPlayerReportDao
import com.nicholas.rutherford.track.your.shot.data.room.database.AppDatabase
import com.nicholas.rutherford.track.your.shot.data.room.repository.IndividualPlayerReportRepositoryImpl
import com.nicholas.rutherford.track.your.shot.data.test.room.TestIndividualPlayerReport
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class IndividualPlayerReportRepositoryImplTest {

    private lateinit var appDatabase: AppDatabase
    private lateinit var individualPlayerReportDao: IndividualPlayerReportDao

    private val individualPlayerReport = TestIndividualPlayerReport().build()

    private lateinit var individualPlayerReportRepositoryImpl: IndividualPlayerReportRepositoryImpl

    @Before
    fun setup() {
        appDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        individualPlayerReportDao = appDatabase.individualPlayerReportDao()

        individualPlayerReportRepositoryImpl = IndividualPlayerReportRepositoryImpl(individualPlayerReportDao = individualPlayerReportDao)
    }

    @After
    fun teardown() {
        appDatabase.close()
    }

    @Test
    fun createReport() = runBlocking {
        individualPlayerReportRepositoryImpl.createReport(report = individualPlayerReport)

        assertThat(individualPlayerReportRepositoryImpl.fetchAllReports(), equalTo(listOf(individualPlayerReport)))
    }

    @Test
    fun fetchAllReports() = runBlocking {
        individualPlayerReportRepositoryImpl.createReport(report = individualPlayerReport)
        individualPlayerReportRepositoryImpl.createReport(report = individualPlayerReport.copy(playerName = "playerTest"))

        assertThat(individualPlayerReportRepositoryImpl.fetchAllReports(), equalTo(listOf(individualPlayerReport, individualPlayerReport.copy(playerName = "playerTest"))))
    }

    @Test
    fun deleteAllReports() = runBlocking {
        individualPlayerReportRepositoryImpl.createReport(report = individualPlayerReport)
        individualPlayerReportRepositoryImpl.createReport(report = individualPlayerReport.copy(playerName = "playerTest"))

        assertThat(individualPlayerReportRepositoryImpl.fetchAllReports(), equalTo(listOf(individualPlayerReport, individualPlayerReport.copy(playerName = "playerTest"))))

        individualPlayerReportRepositoryImpl.deleteAllReports()

        assertThat(individualPlayerReportRepositoryImpl.fetchAllReports(), equalTo(emptyList()))
    }
}
