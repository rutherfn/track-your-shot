package com.nicholas.rutherford.track.your.shot.data.room

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nicholas.rutherford.track.your.shot.data.room.dao.IndividualPlayerReportDao
import com.nicholas.rutherford.track.your.shot.data.room.database.AppDatabase
import com.nicholas.rutherford.track.your.shot.data.test.room.TestIndividualPlayerReportEntity
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class IndividualPlayerReportDaoTest {
    private lateinit var appDatabase: AppDatabase

    private lateinit var individualPlayerReportDao: IndividualPlayerReportDao

    private val individualPlayerReportEntity = TestIndividualPlayerReportEntity.build()

    @Before
    fun setup() {
        appDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        individualPlayerReportDao = appDatabase.individualPlayerReportDao()
    }

    @After
    fun teardown() {
        appDatabase.close()
    }

    @Test
    fun insert() = runBlocking {
        individualPlayerReportDao.insert(individualPlayerReportEntity = individualPlayerReportEntity)

        assertThat(listOf(individualPlayerReportEntity), equalTo(individualPlayerReportDao.getAllPlayerReports()))
    }

    @Test
    fun insertAll() = runBlocking {
        individualPlayerReportDao.insertAll(individualPlayerReports = listOf(individualPlayerReportEntity, individualPlayerReportEntity.copy(id = 2)))

        assertThat(listOf(individualPlayerReportEntity, individualPlayerReportEntity.copy(id = 2)), equalTo(individualPlayerReportDao.getAllPlayerReports()))
    }

    @Test
    fun getAllPlayerReports() = runBlocking {
        individualPlayerReportDao.insert(individualPlayerReportEntity = individualPlayerReportEntity)
        individualPlayerReportDao.insert(individualPlayerReportEntity = individualPlayerReportEntity.copy(id = 2))

        assertThat(listOf(individualPlayerReportEntity, individualPlayerReportEntity.copy(id = 2)), equalTo(individualPlayerReportDao.getAllPlayerReports()))
    }

    @Test
    fun deleteAll() = runBlocking {
        individualPlayerReportDao.insert(individualPlayerReportEntity = individualPlayerReportEntity)
        individualPlayerReportDao.insert(individualPlayerReportEntity = individualPlayerReportEntity.copy(id = 2))

        assertThat(listOf(individualPlayerReportEntity, individualPlayerReportEntity.copy(id = 2)), equalTo(individualPlayerReportDao.getAllPlayerReports()))

        individualPlayerReportDao.deleteAll()

        assertThat(emptyList(), equalTo(individualPlayerReportDao.getAllPlayerReports()))
    }

    @Test
    fun deleteReportByFirebaseKey() = runBlocking {
        individualPlayerReportDao.insert(individualPlayerReportEntity = individualPlayerReportEntity.copy(firebaseKey = "firebaseKey1"))
        individualPlayerReportDao.insert(individualPlayerReportEntity = individualPlayerReportEntity.copy(id = 2, firebaseKey = "firebaseKey2"))

        assertThat(listOf(individualPlayerReportEntity.copy(firebaseKey = "firebaseKey1"), individualPlayerReportEntity.copy(id = 2, firebaseKey = "firebaseKey2")), equalTo(individualPlayerReportDao.getAllPlayerReports()))

        individualPlayerReportDao.deleteReportByFirebaseKey(firebaseKey = "firebaseKey2")

        assertThat(listOf(individualPlayerReportEntity.copy(firebaseKey = "firebaseKey1")), equalTo(individualPlayerReportDao.getAllPlayerReports()))
    }

    @Test
    fun getPlayerReportsCount() = runBlocking {
        individualPlayerReportDao.insert(individualPlayerReportEntity = individualPlayerReportEntity)
        individualPlayerReportDao.insert(individualPlayerReportEntity = individualPlayerReportEntity.copy(id = 2))

        assertThat(listOf(individualPlayerReportEntity, individualPlayerReportEntity.copy(id = 2)), equalTo(individualPlayerReportDao.getAllPlayerReports()))
        assertThat(2, equalTo(individualPlayerReportDao.getPlayerReportsCount()))
    }
}
