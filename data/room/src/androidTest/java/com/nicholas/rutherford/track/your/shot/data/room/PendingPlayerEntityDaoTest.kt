package com.nicholas.rutherford.track.your.shot.data.room

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nicholas.rutherford.track.your.shot.data.room.dao.PendingPlayerDao
import com.nicholas.rutherford.track.your.shot.data.room.database.AppDatabase
import com.nicholas.rutherford.track.your.shot.data.room.entities.PendingPlayerEntity
import com.nicholas.rutherford.track.your.shot.data.test.room.TestPendingPlayerEntity
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PendingPlayerEntityDaoTest {

    private lateinit var appDatabase: AppDatabase
    private lateinit var pendingPlayerDao: PendingPlayerDao

    private val pendingPlayerEntity = TestPendingPlayerEntity.build()

    @Before
    fun setUp() {
        appDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        pendingPlayerDao = appDatabase.pendingPlayerDao()
    }

    @After
    fun teardown() {
        appDatabase.close()
    }

    @Test
    fun insert() = runBlocking {
        pendingPlayerDao.insert(pendingPlayerEntity = pendingPlayerEntity)

        assertThat(listOf(pendingPlayerEntity), equalTo(pendingPlayerDao.getAllPendingPlayers()))
    }

    @Test
    fun update() = runBlocking {
        val newPendingPlayerEntity = pendingPlayerEntity.copy(firstName = "first1", lastName = "last1")

        pendingPlayerDao.insert(pendingPlayerEntity = pendingPlayerEntity)

        assertThat(listOf(pendingPlayerEntity), equalTo(pendingPlayerDao.getAllPendingPlayers()))

        pendingPlayerDao.update(pendingPlayerEntity = newPendingPlayerEntity)

        assertThat(listOf(newPendingPlayerEntity), equalTo(pendingPlayerDao.getAllPendingPlayers()))
    }

    @Test
    fun deleteAllPendingPlayers() = runBlocking {
        pendingPlayerDao.insert(pendingPlayerEntity = pendingPlayerEntity)

        assertThat(listOf(pendingPlayerEntity), equalTo(pendingPlayerDao.getAllPendingPlayers()))

        pendingPlayerDao.deleteAllPendingPlayers()

        val pendingPlayerEmptyList: List<PendingPlayerEntity> = emptyList()

        assertThat(pendingPlayerEmptyList, equalTo(pendingPlayerDao.getAllPendingPlayers()))
    }

    @Test
    fun getAllPendingPlayers() = runBlocking {
        pendingPlayerDao.insert(pendingPlayerEntity = pendingPlayerEntity)

        assertThat(listOf(pendingPlayerEntity), equalTo(pendingPlayerDao.getAllPendingPlayers()))
    }

    @Test
    fun getPendingPlayerById() = runBlocking {
        pendingPlayerDao.insert(pendingPlayerEntity = pendingPlayerEntity)

        assertThat(pendingPlayerEntity, equalTo(pendingPlayerDao.getPlayerById(id = pendingPlayerEntity.id)))
    }

    @Test
    fun getPendingPlayerIdByName() = runBlocking {
        val newPendingPlayerEntity = pendingPlayerEntity.copy(id = 2, firstName = "first1", lastName = "last1")

        pendingPlayerDao.insert(pendingPlayerEntity = pendingPlayerEntity)
        pendingPlayerDao.insert(pendingPlayerEntity = newPendingPlayerEntity)

        assertThat(newPendingPlayerEntity.id, equalTo(pendingPlayerDao.getPendingPlayerIdByName(firstName = newPendingPlayerEntity.firstName, lastName = newPendingPlayerEntity.lastName)))
    }
}
