package com.nicholas.rutherford.track.my.shot.data.room.repositorys

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nicholas.rutherford.track.my.shot.data.room.dao.PlayerDao
import com.nicholas.rutherford.track.my.shot.data.room.database.AppDatabase
import com.nicholas.rutherford.track.my.shot.data.room.repository.player.PlayerRepositoryImpl
import com.nicholas.rutherford.track.my.shot.data.test.room.TestPlayer
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PlayerRepositoryImplTest {

    private lateinit var appDatabase: AppDatabase
    private lateinit var playerDao: PlayerDao

    private val player = TestPlayer().create()

    private lateinit var playerRepositoryImpl: PlayerRepositoryImpl

    @Before
    fun setup() {
        appDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        playerDao = appDatabase.playerDao()

        playerRepositoryImpl = PlayerRepositoryImpl(playerDao = playerDao)
    }

    @After
    fun teardown() {
        appDatabase.close()
    }

    @Test
    fun createPlayer() = runBlocking {
        playerRepositoryImpl.createPlayer(player = player)

        assertThat(playerRepositoryImpl.fetchAllPlayers(), equalTo(listOf(player)))
    }

    fun updatePlayer() = runBlocking {
        val updatedPlayer = player.copy(firstName = "name1", lastName = "name2")

        playerRepositoryImpl.createPlayer(player = player)

        assertThat(playerRepositoryImpl.fetchAllPlayers(), equalTo(listOf(player)))

        playerRepositoryImpl.updatePlayer(player = updatedPlayer)

        assertThat(playerRepositoryImpl.fetchAllPlayers(), equalTo(listOf(updatedPlayer)))
    }

    @Test
    fun deletePlayer() = runBlocking {
        val newPlayer = player.copy(firstName = "name1", lastName = "name2")

        playerRepositoryImpl.createPlayer(player = player)
        playerRepositoryImpl.createPlayer(player = newPlayer)

        assertThat(playerRepositoryImpl.fetchAllPlayers(), equalTo(listOf(player, newPlayer)))

        playerRepositoryImpl.deletePlayer(player = player)

        assertThat(playerRepositoryImpl.fetchAllPlayers(), equalTo(listOf(newPlayer)))
    }

    @Test
    fun deleteAllPlayers() = runBlocking {
        val newPlayer = player.copy(firstName = "name1", lastName = "name2")

        playerRepositoryImpl.createPlayer(player = player)
        playerRepositoryImpl.createPlayer(player = newPlayer)

        assertThat(playerRepositoryImpl.fetchAllPlayers(), equalTo(listOf(player, newPlayer)))

        playerRepositoryImpl.deleteAllPlayers()

        assertThat(playerRepositoryImpl.fetchAllPlayers(), equalTo(emptyList()))
    }

    @Test
    fun fetchPlayerById() = runBlocking {
        val newPlayer = player.copy(firstName = "name1", lastName = "name2")

        playerRepositoryImpl.createPlayer(player = player)
        playerRepositoryImpl.createPlayer(player = newPlayer)

        assertThat(playerRepositoryImpl.fetchPlayerById(id = player.id), equalTo(player))
    }

//    @Test
//    fun fetchPlayerByName() = runBlocking {
//        val playerWithEmptyFirstName = player.copy(id = 2, firstName = "", lastName = "name2")
//        val playerWithEmptyLastName = player.copy(id = 3, firstName = "name2", lastName = "")
//
//        val validPlayer1 = player.copy(id = 1, firstName = "name1", lastName = "name1")
//        val validPlayer2 = player.copy(id = 4, firstName = "name2", lastName = "name2")
//
//        playerRepositoryImpl.createPlayer(playerWithEmptyFirstName)
//        playerRepositoryImpl.createPlayer(playerWithEmptyLastName)
//
//        assertThat(playerRepositoryImpl.fetchPlayerByName(firstName = playerWithEmptyFirstName.firstName, lastName = playerWithEmptyFirstName.lastName), equalTo(null))
//        assertThat(playerRepositoryImpl.fetchPlayerByName(firstName = playerWithEmptyLastName.firstName, lastName = playerWithEmptyLastName.lastName), equalTo(null))
//
//        playerRepositoryImpl.createPlayer(validPlayer1)
//        playerRepositoryImpl.createPlayer(validPlayer2)
//
//        assertThat(playerRepositoryImpl.fetchPlayerByName(firstName = validPlayer1.firstName, lastName = validPlayer1.lastName), equalTo(validPlayer1))
//        assertThat(playerRepositoryImpl.fetchPlayerByName(firstName = validPlayer2.firstName, lastName = validPlayer2.lastName), equalTo(validPlayer2))
//    }

    @Test
    fun fetchAllPlayers() = runBlocking {
        val newPlayer = player.copy(firstName = "name1", lastName = "name2")

        playerRepositoryImpl.createPlayer(player = player)
        playerRepositoryImpl.createPlayer(player = newPlayer)

        assertThat(playerRepositoryImpl.fetchAllPlayers(), equalTo(listOf(player, newPlayer)))
    }
}
