package com.nicholas.rutherford.track.your.shot.data.room

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nicholas.rutherford.track.your.shot.data.room.dao.ShotIgnoringDao
import com.nicholas.rutherford.track.your.shot.data.room.database.AppDatabase
import com.nicholas.rutherford.track.your.shot.data.test.room.TestShotIgnoringEntity
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ShotIgnoringDaoTest {

    private lateinit var appDatabase: AppDatabase
    private lateinit var shotIgnoringDao: ShotIgnoringDao

    private val shotIgnoringEntity = TestShotIgnoringEntity.build()

    @Before
    fun setUp() {
        appDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        shotIgnoringDao = appDatabase.shotIgnoringDao()
    }

    @After
    fun teardown() {
        appDatabase.close()
    }

    @Test
    fun insert() = runBlocking {
        shotIgnoringDao.insert(shotIgnoringEntity = shotIgnoringEntity)

        assertThat(listOf(shotIgnoringEntity), equalTo(shotIgnoringDao.getAllShots()))
    }

    @Test
    fun update() = runBlocking {
        shotIgnoringDao.insert(shotIgnoringEntity = shotIgnoringEntity)

        assertThat(listOf(shotIgnoringEntity), equalTo(shotIgnoringDao.getAllShots()))

        shotIgnoringDao.update(shotIgnoringEntity = shotIgnoringEntity.copy(shotId = 99))

        assertThat(listOf(shotIgnoringEntity.copy(shotId = 99)), equalTo(shotIgnoringDao.getAllShots()))
    }

    @Test
    fun insertAll() = runBlocking {
        shotIgnoringDao.insertAll(shotsIgnoringEntity = listOf(shotIgnoringEntity, shotIgnoringEntity.copy(id = 44, shotId = 101)))

        assertThat(listOf(shotIgnoringEntity, shotIgnoringEntity.copy(id = 44, shotId = 101)), equalTo(shotIgnoringDao.getAllShots()))
    }

    @Test
    fun getAllShots() = runBlocking {
        shotIgnoringDao.insert(shotIgnoringEntity = shotIgnoringEntity)

        assertThat(listOf(shotIgnoringEntity), equalTo(shotIgnoringDao.getAllShots()))
    }

    @Test
    fun deleteAll() = runBlocking {
        shotIgnoringDao.insert(shotIgnoringEntity = shotIgnoringEntity)

        assertThat(listOf(shotIgnoringEntity), equalTo(shotIgnoringDao.getAllShots()))

        shotIgnoringDao.deleteAll()

        assertThat(emptyList(), equalTo(shotIgnoringDao.getAllShots()))
    }

    @Test
    fun deleteShotById() = runBlocking {
        shotIgnoringDao.insert(shotIgnoringEntity = shotIgnoringEntity)

        assertThat(listOf(shotIgnoringEntity), equalTo(shotIgnoringDao.getAllShots()))

        shotIgnoringDao.deleteByShotId(shotId = shotIgnoringEntity.shotId)

        assertThat(emptyList(), equalTo(shotIgnoringDao.getAllShots()))
    }
}
