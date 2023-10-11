package com.nicholas.rutherford.track.my.shot.data.room.daos

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nicholas.rutherford.track.my.shot.data.room.dao.PlayerDao
import com.nicholas.rutherford.track.my.shot.data.room.database.AppDatabase
import com.nicholas.rutherford.track.my.shot.data.test.room.TestPlayerEntity
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

        assertThat(playerEntity, equalTo(playerDao.getPlayerById(id = playerEntity.id)))
    }

    @Test
    fun update() = runBlocking {
        val newPlayerEnity = playerEntity.copy(firstName = "first1", lastName = "last1")

        playerDao.insert(playerEntity = playerEntity)

        assertThat(playerEntity, equalTo(playerDao.getPlayerById(id = playerEntity.id)))

        playerDao.update(playerEntity = newPlayerEnity)

        assertThat(playerDao.getPlayerById(id = newPlayerEnity.id), equalTo(newPlayerEnity))
    }

    @Test
    fun delete() = runBlocking {
        playerDao.insert(playerEntity = playerEntity)

        assertThat(playerEntity, equalTo(playerDao.getPlayerById(id = playerEntity.id)))

        playerDao.delete(playerEntity = playerEntity)

        assertThat(null, equalTo(playerDao.getPlayerById(id = playerEntity.id)))
    }

    @Test
    fun deleteAllPlayers() = runBlocking {
        val newPlayerEnity = playerEntity.copy(id = 2, firstName = "first1", lastName = "last1")

        playerDao.insert(playerEntity = playerEntity)
        playerDao.insert(playerEntity = newPlayerEnity)

        assertThat(playerEntity, equalTo(playerDao.getPlayerById(id = playerEntity.id)))
        assertThat(newPlayerEnity, equalTo(playerDao.getPlayerById(id = newPlayerEnity.id)))

        playerDao.deleteAllPlayers()

        assertThat(null, equalTo(playerDao.getPlayerById(id = playerEntity.id)))
        assertThat(null, equalTo(playerDao.getPlayerById(id = newPlayerEnity.id)))
    }

    @Test
    fun getPlayerById() = runBlocking {
        val newPlayerEnity = playerEntity.copy(id = 2, firstName = "first1", lastName = "last1")

        playerDao.insert(playerEntity = playerEntity)
        playerDao.insert(playerEntity = newPlayerEnity)

        assertThat(playerEntity, equalTo(playerDao.getPlayerById(id = playerEntity.id)))
    }

    @Test
    fun getPlayerByName() = runBlocking {
        val newPlayerEnity = playerEntity.copy(id = 2, firstName = "first1", lastName = "last1")

        playerDao.insert(playerEntity = playerEntity)
        playerDao.insert(playerEntity = newPlayerEnity)

        assertThat(newPlayerEnity, equalTo(playerDao.getPlayersByName(firstName = newPlayerEnity.firstName, lastName = newPlayerEnity.lastName)))
    }

    @Test
    fun getAllPlayers() = runBlocking {
        val newPlayerEnity = playerEntity.copy(id = 2, firstName = "first1", lastName = "last1")

        playerDao.insert(playerEntity = playerEntity)
        playerDao.insert(playerEntity = newPlayerEnity)

        assertThat(listOf(playerEntity, newPlayerEnity), equalTo(playerDao.getAllPlayers()))
    }
}