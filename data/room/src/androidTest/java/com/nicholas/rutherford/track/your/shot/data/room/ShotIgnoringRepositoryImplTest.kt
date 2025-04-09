package com.nicholas.rutherford.track.your.shot.data.room

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nicholas.rutherford.track.your.shot.data.room.dao.ShotIgnoringDao
import com.nicholas.rutherford.track.your.shot.data.room.database.AppDatabase
import com.nicholas.rutherford.track.your.shot.data.room.entities.toShotIgnoring
import com.nicholas.rutherford.track.your.shot.data.room.repository.ShotIgnoringRepositoryImpl
import com.nicholas.rutherford.track.your.shot.data.test.room.TestShotIgnoringEntity
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ShotIgnoringRepositoryImplTest {

    private lateinit var appDatabase: AppDatabase
    private lateinit var shotIgnoringDao: ShotIgnoringDao

    private val shotIgnoring = TestShotIgnoringEntity.build().toShotIgnoring()

    private lateinit var shotIgnoringRepositoryImpl: ShotIgnoringRepositoryImpl

    @Before
    fun setup() {
        appDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        shotIgnoringDao = appDatabase.shotIgnoringDao()

        shotIgnoringRepositoryImpl = ShotIgnoringRepositoryImpl(shotIgnoringDao = shotIgnoringDao)
    }

    @After
    fun teardown() {
        appDatabase.close()
    }

    @Test
    fun createShotIgnoring() = runBlocking {
        shotIgnoringRepositoryImpl.createShotIgnoring(shotIgnoring = shotIgnoring)

        assertThat(shotIgnoringRepositoryImpl.fetchAllIgnoringShots(), equalTo(listOf(shotIgnoring)))
    }

    @Test
    fun updateShotIgnoring() = runBlocking {
        shotIgnoringRepositoryImpl.createShotIgnoring(shotIgnoring = shotIgnoring)

        assertThat(shotIgnoringRepositoryImpl.fetchAllIgnoringShots(), equalTo(listOf(shotIgnoring)))

        shotIgnoringRepositoryImpl.updateShotIgnoring(shotIgnoring = shotIgnoring.copy(shotId = 99))

        assertThat(shotIgnoringRepositoryImpl.fetchAllIgnoringShots(), equalTo(listOf(shotIgnoring.copy(shotId = 99))))
    }

    @Test
    fun createListOfShotIgnoring() = runBlocking {
        shotIgnoringRepositoryImpl.createListOfShotIgnoring(shotIgnoringList = listOf(shotIgnoring, shotIgnoring.copy(id = 44, shotId = 99)))

        assertThat(shotIgnoringRepositoryImpl.fetchAllIgnoringShots(), equalTo(listOf(shotIgnoring, shotIgnoring.copy(id = 44, shotId = 99))))
    }

    @Test
    fun fetchAllIgnoringShots() = runBlocking {
        shotIgnoringRepositoryImpl.createShotIgnoring(shotIgnoring = shotIgnoring)

        assertThat(shotIgnoringRepositoryImpl.fetchAllIgnoringShots(), equalTo(listOf(shotIgnoring)))
    }

    @Test
    fun deleteAllShotsIgnoring() = runBlocking {
        shotIgnoringRepositoryImpl.createListOfShotIgnoring(shotIgnoringList = listOf(shotIgnoring, shotIgnoring.copy(id = 44, shotId = 99)))

        assertThat(shotIgnoringRepositoryImpl.fetchAllIgnoringShots(), equalTo(listOf(shotIgnoring, shotIgnoring.copy(id = 44, shotId = 99))))

        shotIgnoringRepositoryImpl.deleteAllShotsIgnoring()

        assertThat(shotIgnoringRepositoryImpl.fetchAllIgnoringShots(), equalTo(emptyList()))
    }

    @Test
    fun deleteShotIgnoringByShotId() = runBlocking {
        shotIgnoringRepositoryImpl.createListOfShotIgnoring(shotIgnoringList = listOf(shotIgnoring, shotIgnoring.copy(id = 44, shotId = 99)))

        assertThat(shotIgnoringRepositoryImpl.fetchAllIgnoringShots(), equalTo(listOf(shotIgnoring, shotIgnoring.copy(id = 44, shotId = 99))))

        shotIgnoringRepositoryImpl.deleteShotIgnoringByShotId(shotId = 99)

        assertThat(shotIgnoringRepositoryImpl.fetchAllIgnoringShots(), equalTo(listOf(shotIgnoring)))
    }
}
