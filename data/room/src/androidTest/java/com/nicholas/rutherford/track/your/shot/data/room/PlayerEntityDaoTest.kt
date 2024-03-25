package com.nicholas.rutherford.track.your.shot.data.room

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nicholas.rutherford.track.your.shot.data.room.dao.PlayerDao
import com.nicholas.rutherford.track.your.shot.data.room.database.AppDatabase
import com.nicholas.rutherford.track.your.shot.data.test.room.TestPlayerEntity
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PlayerEntityDaoTest {

    private lateinit var appDatabase: AppDatabase
    private lateinit var playerDao: PlayerDao

    private val playerEntity = TestPlayerEntity().create()

    @Before
    fun setUp() {
        appDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        playerDao = appDatabase.playerDao()
    }

    @After
    fun teardown() {
        appDatabase.close()
    }

    @Test
    fun insert() = runBlocking {
        playerDao.insert(playerEntity = playerEntity)

        assertThat(listOf(playerEntity), equalTo(playerDao.getAllPlayers()))
    }

    @Test
    fun update() = runBlocking {
        val newPlayerEntity = playerEntity.copy(firstName = "first1", lastName = "last1")

        playerDao.insert(playerEntity = playerEntity)

        assertThat(listOf(playerEntity), equalTo(playerDao.getAllPlayers()))

        playerDao.update(playerEntity = newPlayerEntity)

        assertThat(listOf(newPlayerEntity), equalTo(playerDao.getAllPlayers()))
    }

    @Test
    fun deletePlayerByName() = runBlocking {
        playerDao.insert(playerEntity = playerEntity)

        assertThat(listOf(playerEntity), equalTo(playerDao.getAllPlayers()))

        playerDao.deletePlayerByName(
            firstName = playerEntity.firstName,
            lastName = playerEntity.lastName
        )

        assertThat(emptyList(), equalTo(playerDao.getAllPlayers()))
    }

    @Test
    fun deleteAllPlayers() = runBlocking {
        val newPlayerEntity = playerEntity.copy(id = 2, firstName = "first1", lastName = "last1")

        playerDao.insert(playerEntity = playerEntity)
        playerDao.insert(playerEntity = newPlayerEntity)

        assertThat(listOf(playerEntity, newPlayerEntity), equalTo(playerDao.getAllPlayers()))

        playerDao.deleteAllPlayers()

        assertThat(emptyList(), equalTo(playerDao.getAllPlayers()))
    }

    @Test
    fun getPlayerById() = runBlocking {
        val newPlayerEntity = playerEntity.copy(id = 2, firstName = "first1", lastName = "last1")

        playerDao.insert(playerEntity = playerEntity)
        playerDao.insert(playerEntity = newPlayerEntity)

        assertThat(newPlayerEntity, equalTo(playerDao.getPlayerById(id = 2)))
    }

    @Test
    fun getPlayerIdByName() = runBlocking {
        val newPlayerEntity = playerEntity.copy(id = 2, firstName = "first1", lastName = "last1")

        playerDao.insert(playerEntity = playerEntity)
        playerDao.insert(playerEntity = newPlayerEntity)

        assertThat(newPlayerEntity.id, equalTo(playerDao.getPlayerIdByName(firstName = "first1", lastName = "last1")))
    }

    @Test
    fun getPlayersByName() = runBlocking {
        val newPlayerEntity = playerEntity.copy(id = 2, firstName = "first1", lastName = "last1")

        playerDao.insert(playerEntity = playerEntity)
        playerDao.insert(playerEntity = newPlayerEntity)

        assertThat(newPlayerEntity, equalTo(playerDao.getPlayersByName(firstName = newPlayerEntity.firstName, lastName = newPlayerEntity.lastName)))
    }

    @Test
    fun getAllPlayers() = runBlocking {
        val newPlayerEnity = playerEntity.copy(id = 2, firstName = "first1", lastName = "last1")

        playerDao.insert(playerEntity = playerEntity)
        playerDao.insert(playerEntity = newPlayerEnity)

        assertThat(listOf(playerEntity, newPlayerEnity), equalTo(playerDao.getAllPlayers()))
    }

    @Test
    fun getPlayerCount() = runBlocking {
        val newPlayerEntity = playerEntity.copy(id = 2, firstName = "first1", lastName = "last1")

        playerDao.insert(playerEntity = playerEntity)
        playerDao.insert(playerEntity = newPlayerEntity)

        assertThat(2, equalTo(playerDao.getPlayerCount()))
    }
}
