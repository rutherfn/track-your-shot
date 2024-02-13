package com.nicholas.rutherford.track.your.shot.data.room

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nicholas.rutherford.track.your.shot.data.room.dao.PendingPlayerDao
import com.nicholas.rutherford.track.your.shot.data.room.database.AppDatabase
import com.nicholas.rutherford.track.your.shot.data.room.repository.PendingPlayerRepositoryImpl
import com.nicholas.rutherford.track.your.shot.data.room.response.Player
import com.nicholas.rutherford.track.your.shot.data.room.response.PlayerPositions
import com.nicholas.rutherford.track.your.shot.data.test.room.TestPlayer
import com.nicholas.rutherford.track.your.shot.data.test.room.TestShotLogged
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PendingPlayerRepositoryImplTest {

    private lateinit var appDatabase: AppDatabase
    private lateinit var pendingPlayerDao: PendingPlayerDao

    private val player = TestPlayer().create()

    private lateinit var pendingPlayerRepositoryImpl: PendingPlayerRepositoryImpl

    @Before
    fun setUp() {
        appDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        pendingPlayerDao = appDatabase.pendingPlayerDao()

        pendingPlayerRepositoryImpl =
            PendingPlayerRepositoryImpl(pendingPlayerDao = pendingPlayerDao)
    }

    @After
    fun teardown() {
        appDatabase.close()
    }

    @Test
    fun createPendingPlayer() = runBlocking {
        pendingPlayerRepositoryImpl.createPendingPlayer(player = player)

        assertThat(pendingPlayerRepositoryImpl.fetchAllPendingPlayers(), equalTo(listOf(player)))
    }

    @Test
    fun updatePendingPlayer() = runBlocking {
        val updatedPlayer = player.copy(firstName = "name1", lastName = "name2")

        pendingPlayerRepositoryImpl.createPendingPlayer(player = player)

        assertThat(pendingPlayerRepositoryImpl.fetchAllPendingPlayers(), equalTo(listOf(player)))

        pendingPlayerRepositoryImpl.updatePendingPlayer(currentPendingPlayer = player, newPendingPlayer = updatedPlayer)

        assertThat(
            pendingPlayerRepositoryImpl.fetchAllPendingPlayers(),
            equalTo(listOf(updatedPlayer))
        )
    }

    @Test
    fun deletePlayer() = runBlocking {
        val player1 = Player(
            firstName = "name1",
            lastName = "name2",
            position = PlayerPositions.Center,
            imageUrl = null,
            firebaseKey = "key1",
            shotsLoggedList = listOf(TestShotLogged.build())
        )
        val player2 = Player(
            firstName = "name3",
            lastName = "name4",
            position = PlayerPositions.SmallForward,
            imageUrl = null,
            firebaseKey = "ley2",
            shotsLoggedList = emptyList()
        )

        pendingPlayerRepositoryImpl.createPendingPlayer(player = player1)
        pendingPlayerRepositoryImpl.createPendingPlayer(player = player2)

        assertThat(
            pendingPlayerRepositoryImpl.fetchAllPendingPlayers(),
            equalTo(listOf(player1, player2))
        )

        pendingPlayerRepositoryImpl.deleteAllPendingPlayers()

        assertThat(pendingPlayerRepositoryImpl.fetchAllPendingPlayers(), equalTo(emptyList()))
    }

    @Test
    fun fetchAllPendingPlayers() = runBlocking {
        val player1 = Player(
            firstName = "name1",
            lastName = "name2",
            position = PlayerPositions.Center,
            imageUrl = null,
            firebaseKey = "key1",
            shotsLoggedList = listOf(TestShotLogged.build())
        )
        val player2 = Player(
            firstName = "name3",
            lastName = "name4",
            position = PlayerPositions.SmallForward,
            imageUrl = null,
            firebaseKey = "ley2",
            shotsLoggedList = emptyList()
        )

        pendingPlayerRepositoryImpl.createPendingPlayer(player = player1)
        pendingPlayerRepositoryImpl.createPendingPlayer(player = player2)

        assertThat(
            pendingPlayerRepositoryImpl.fetchAllPendingPlayers(),
            equalTo(listOf(player1, player2))
        )
    }
}
