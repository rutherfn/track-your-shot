package com.nicholas.rutherford.track.your.shot.data.room

import android.app.Application
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.data.room.dao.PlayerDao
import com.nicholas.rutherford.track.your.shot.data.room.dao.PlayerFilterDao
import com.nicholas.rutherford.track.your.shot.data.room.database.AppDatabase
import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerRepositoryImpl
import com.nicholas.rutherford.track.your.shot.data.room.response.HasShotsLoggedFilter
import com.nicholas.rutherford.track.your.shot.data.room.response.Player
import com.nicholas.rutherford.track.your.shot.data.room.response.PlayerFilter
import com.nicholas.rutherford.track.your.shot.data.room.response.PlayerPositions
import com.nicholas.rutherford.track.your.shot.data.room.response.toPlayerFilterEntities
import com.nicholas.rutherford.track.your.shot.data.test.room.TestPlayer
import com.nicholas.rutherford.track.your.shot.data.test.room.TestShotLogged
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PlayerRepositoryImplTest {

    private lateinit var appDatabase: AppDatabase
    private lateinit var playerDao: PlayerDao
    private lateinit var playerFilterDao: PlayerFilterDao
    private val application: Application = ApplicationProvider.getApplicationContext()

    private val player = TestPlayer().create()

    private lateinit var playerRepositoryImpl: PlayerRepositoryImpl

    @Before
    fun setup() {
        appDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        playerDao = appDatabase.playerDao()
        playerFilterDao = appDatabase.playerFilterDao()

        playerRepositoryImpl = PlayerRepositoryImpl(
            playerDao = playerDao,
            playerFilterDao = playerFilterDao,
            application = application
        )
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

    @Test
    fun updatePlayer() = runBlocking {
        val updatedPlayer = player.copy(firstName = "name1", lastName = "name2")

        playerRepositoryImpl.createPlayer(player = player)

        assertThat(playerRepositoryImpl.fetchAllPlayers(), equalTo(listOf(player)))

        playerRepositoryImpl.updatePlayer(currentPlayer = player, newPlayer = updatedPlayer)

        assertThat(playerRepositoryImpl.fetchAllPlayers(), equalTo(listOf(updatedPlayer)))
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

        playerRepositoryImpl.createPlayer(player = player1)
        playerRepositoryImpl.createPlayer(player = player2)

        assertThat(playerRepositoryImpl.fetchAllPlayers(), equalTo(listOf(player1, player2)))

        playerRepositoryImpl.deletePlayerByName(
            firstName = player1.firstName,
            lastName = player1.lastName
        )

        assertThat(playerRepositoryImpl.fetchAllPlayers(), equalTo(listOf(player2)))
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
    fun fetchPlayerByName() = runBlocking {
        val playerWithEmptyFirstName = player.copy(firstName = "", lastName = "name2")
        val playerWithEmptyLastName = player.copy(firstName = "name2", lastName = "")

        val validPlayer1 = player.copy(firstName = "name1", lastName = "name1")
        val validPlayer2 = player.copy(firstName = "name2", lastName = "name2")

        playerRepositoryImpl.createPlayer(playerWithEmptyFirstName)
        playerRepositoryImpl.createPlayer(playerWithEmptyLastName)

        assertThat(playerRepositoryImpl.fetchPlayerByName(firstName = playerWithEmptyFirstName.firstName, lastName = playerWithEmptyFirstName.lastName), nullValue())
        assertThat(playerRepositoryImpl.fetchPlayerByName(firstName = playerWithEmptyLastName.firstName, lastName = playerWithEmptyLastName.lastName), nullValue())

        playerRepositoryImpl.createPlayer(validPlayer1)
        playerRepositoryImpl.createPlayer(validPlayer2)

        val fetchedPlayer1 = playerRepositoryImpl.fetchPlayerByName(firstName = validPlayer1.firstName, lastName = validPlayer1.lastName)
        assertThat(fetchedPlayer1?.firstName, equalTo(validPlayer1.firstName))
        assertThat(fetchedPlayer1?.lastName, equalTo(validPlayer1.lastName))
        assertThat(fetchedPlayer1?.position, equalTo(validPlayer1.position))
        assertThat(fetchedPlayer1?.firebaseKey, equalTo(validPlayer1.firebaseKey))
        assertThat(fetchedPlayer1?.imageUrl, equalTo(validPlayer1.imageUrl))
        assertThat(fetchedPlayer1?.shotsLoggedList, equalTo(validPlayer1.shotsLoggedList))

        val fetchedPlayer2 = playerRepositoryImpl.fetchPlayerByName(firstName = validPlayer2.firstName, lastName = validPlayer2.lastName)
        assertThat(fetchedPlayer2?.firstName, equalTo(validPlayer2.firstName))
        assertThat(fetchedPlayer2?.lastName, equalTo(validPlayer2.lastName))
        assertThat(fetchedPlayer2?.position, equalTo(validPlayer2.position))
        assertThat(fetchedPlayer2?.firebaseKey, equalTo(validPlayer2.firebaseKey))
        assertThat(fetchedPlayer2?.imageUrl, equalTo(validPlayer2.imageUrl))
        assertThat(fetchedPlayer2?.shotsLoggedList, equalTo(validPlayer2.shotsLoggedList))
    }

    @Test
    fun fetchAllPlayers() = runBlocking {
        val newPlayer = player.copy(firstName = "name1", lastName = "name2")

        playerRepositoryImpl.createPlayer(player = player)
        playerRepositoryImpl.createPlayer(player = newPlayer)

        assertThat(playerRepositoryImpl.fetchAllPlayers(), equalTo(listOf(player, newPlayer)))
    }

    @Test
    fun fetchPlayerIdByName() = runBlocking {
        val playerId = 1
        val newPlayer1 = player.copy(firstName = "name1", lastName = "name2")
        val newPlayer2 = player.copy(firstName = "name2", lastName = "name3")

        playerRepositoryImpl.createPlayer(player = newPlayer1)
        playerRepositoryImpl.createPlayer(player = newPlayer2)

        assertThat(playerRepositoryImpl.fetchPlayerIdByName(firstName = "name1", lastName = "name2"), equalTo(playerId))
    }

    @Test
    fun fetchPlayerCount() = runBlocking {
        val newPlayer = player.copy(firstName = "name1", lastName = "name2")

        playerRepositoryImpl.createPlayer(player = player)
        playerRepositoryImpl.createPlayer(player = newPlayer)

        assertThat(2, equalTo(playerRepositoryImpl.fetchPlayerCount()))
    }

    @Test
    fun fetchPlayerByQuery() = runBlocking {
        val newPlayer = player.copy(firstName = "name1", lastName = "name2")

        playerRepositoryImpl.createPlayer(player = player)
        playerRepositoryImpl.createPlayer(player = newPlayer)

        assertThat(playerRepositoryImpl.fetchPlayerByQuery(query = "Name"), equalTo(listOf(newPlayer)))
        assertThat(playerRepositoryImpl.fetchPlayerByQuery(query = "First"), equalTo(listOf(player)))
    }

    @Test
    fun fetchAllPlayersWithFiltersUsesActiveFilterFromDatabase() = runBlocking {
        val player1 = player.copy(
            firstName = "name1",
            lastName = "name1",
            position = PlayerPositions.PointGuard
        )
        val player2 = player.copy(
            firstName = "name2",
            lastName = "name2",
            position = PlayerPositions.Center
        )

        playerRepositoryImpl.createPlayer(player = player1)
        playerRepositoryImpl.createPlayer(player = player2)

        val filter = PlayerFilter(
            hasShotsLogged = null,
            minShots = null,
            maxShots = null,
            selectedPositions = listOf(application.getString(StringsIds.pointGuard))
        )

        val (filterEntity, positionEntities) = filter.toPlayerFilterEntities()
        playerFilterDao.saveActiveFilter(filterEntity)
        if (positionEntities.isNotEmpty()) {
            playerFilterDao.saveFilterPositions(positionEntities)
        }

        val filteredPlayers = playerRepositoryImpl.fetchAllPlayersWithFilters()
        assertThat(filteredPlayers.size, equalTo(1))
        assertThat(filteredPlayers[0].firstName, equalTo("name1"))
    }

    @Test
    fun fetchAllPlayersWithFilterWithNoFiltersReturnsAllPlayers() = runBlocking {
        val player1 = player.copy(firstName = "name1", lastName = "name1")
        val player2 = player.copy(firstName = "name2", lastName = "name2")

        playerRepositoryImpl.createPlayer(player = player1)
        playerRepositoryImpl.createPlayer(player = player2)

        val filter = PlayerFilter(
            hasShotsLogged = null,
            minShots = null,
            maxShots = null,
            selectedPositions = emptyList()
        )

        val filteredPlayers = playerRepositoryImpl.fetchAllPlayersWithFilter(filter = filter)
        assertThat(filteredPlayers.size, equalTo(2))
    }

    @Test
    fun fetchAllPlayersWithFilterWithPositionFilterReturnsFilteredPlayers() = runBlocking {
        val player1 = player.copy(
            firstName = "name1",
            lastName = "name1",
            position = PlayerPositions.PointGuard
        )
        val player2 = player.copy(
            firstName = "name2",
            lastName = "name2",
            position = PlayerPositions.Center
        )

        playerRepositoryImpl.createPlayer(player = player1)
        playerRepositoryImpl.createPlayer(player = player2)

        val filter = PlayerFilter(
            hasShotsLogged = null,
            minShots = null,
            maxShots = null,
            selectedPositions = listOf(application.getString(StringsIds.pointGuard))
        )

        val filteredPlayers = playerRepositoryImpl.fetchAllPlayersWithFilter(filter = filter)
        assertThat(filteredPlayers.size, equalTo(1))
        assertThat(filteredPlayers[0].firstName, equalTo("name1"))
    }

    @Test
    fun fetchAllPlayersWithFilterWithHasShotsLoggedFilterReturnsFilteredPlayers() = runBlocking {
        val player1 = player.copy(
            firstName = "name1",
            lastName = "name1",
            shotsLoggedList = listOf(TestShotLogged.build())
        )
        val player2 = player.copy(
            firstName = "name2",
            lastName = "name2",
            shotsLoggedList = emptyList()
        )

        playerRepositoryImpl.createPlayer(player = player1)
        playerRepositoryImpl.createPlayer(player = player2)

        val filter = PlayerFilter(
            hasShotsLogged = HasShotsLoggedFilter.HasShots,
            minShots = null,
            maxShots = null,
            selectedPositions = emptyList()
        )

        val filteredPlayers = playerRepositoryImpl.fetchAllPlayersWithFilter(filter = filter)
        assertThat(filteredPlayers.size, equalTo(1))
        assertThat(filteredPlayers[0].firstName, equalTo("name1"))
    }

    @Test
    fun fetchAllPlayersWithFilterWithShotCountRangeFilterReturnsFilteredPlayers() = runBlocking {
        val player1 = player.copy(
            firstName = "name1",
            lastName = "name1",
            shotsLoggedList = listOf(TestShotLogged.build(), TestShotLogged.build(), TestShotLogged.build())
        )
        val player2 = player.copy(
            firstName = "name2",
            lastName = "name2",
            shotsLoggedList = listOf(TestShotLogged.build())
        )

        playerRepositoryImpl.createPlayer(player = player1)
        playerRepositoryImpl.createPlayer(player = player2)

        val filter = PlayerFilter(
            hasShotsLogged = null,
            minShots = 2,
            maxShots = 5,
            selectedPositions = emptyList()
        )

        val filteredPlayers = playerRepositoryImpl.fetchAllPlayersWithFilter(filter = filter)
        assertThat(filteredPlayers.size, equalTo(1))
        assertThat(filteredPlayers[0].firstName, equalTo("name1"))
    }

    @Test
    fun matchesPositionFilterWhenSelectedPositionsIsEmptyShouldReturnTrue() = runBlocking {
        val filter = PlayerFilter(selectedPositions = emptyList())
        val result = playerRepositoryImpl.matchesPositionFilter(player = player, filter = filter)
        assertThat(result, equalTo(true))
    }

    @Test
    fun matchesPositionFilterWhenSelectedPositionsContainsAllShouldReturnTrue() = runBlocking {
        val filter = PlayerFilter(selectedPositions = listOf(application.getString(StringsIds.all)))
        val result = playerRepositoryImpl.matchesPositionFilter(player = player, filter = filter)
        assertThat(result, equalTo(true))
    }

    @Test
    fun matchesPositionFilterWhenSelectedPositionsContainsPlayerPositionShouldReturnTrue() = runBlocking {
        val playerWithPosition = player.copy(position = PlayerPositions.PointGuard)
        val filter = PlayerFilter(selectedPositions = listOf(application.getString(StringsIds.pointGuard)))
        val result = playerRepositoryImpl.matchesPositionFilter(player = playerWithPosition, filter = filter)
        assertThat(result, equalTo(true))
    }

    @Test
    fun matchesPositionFilterWhenSelectedPositionsDoesNotContainPlayerPositionShouldReturnFalse() = runBlocking {
        val playerWithPosition = player.copy(position = PlayerPositions.PointGuard)
        val filter = PlayerFilter(selectedPositions = listOf(application.getString(StringsIds.center)))
        val result = playerRepositoryImpl.matchesPositionFilter(player = playerWithPosition, filter = filter)
        assertThat(result, equalTo(false))
    }

    @Test
    fun matchesHasShotsLoggedFilterWhenHasShotsLoggedIsHasShotsAndPlayerHasShotsShouldReturnTrue() = runBlocking {
        val playerWithShots = player.copy(shotsLoggedList = listOf(TestShotLogged.build()))
        val filter = PlayerFilter(hasShotsLogged = HasShotsLoggedFilter.HasShots)
        val result = playerRepositoryImpl.matchesHasShotsLoggedFilter(player = playerWithShots, filter = filter)
        assertThat(result, equalTo(true))
    }

    @Test
    fun matchesHasShotsLoggedFilterWhenHasShotsLoggedIsHasShotsAndPlayerHasNoShotsShouldReturnFalse() = runBlocking {
        val playerWithoutShots = player.copy(shotsLoggedList = emptyList())
        val filter = PlayerFilter(hasShotsLogged = HasShotsLoggedFilter.HasShots)
        val result = playerRepositoryImpl.matchesHasShotsLoggedFilter(player = playerWithoutShots, filter = filter)
        assertThat(result, equalTo(false))
    }

    @Test
    fun matchesHasShotsLoggedFilterWhenHasShotsLoggedIsNoShotsAndPlayerHasNoShotsShouldReturnTrue() = runBlocking {
        val playerWithoutShots = player.copy(shotsLoggedList = emptyList())
        val filter = PlayerFilter(hasShotsLogged = HasShotsLoggedFilter.NoShots)
        val result = playerRepositoryImpl.matchesHasShotsLoggedFilter(player = playerWithoutShots, filter = filter)
        assertThat(result, equalTo(true))
    }

    @Test
    fun matchesHasShotsLoggedFilterWhenHasShotsLoggedIsNoShotsAndPlayerHasShotsShouldReturnFalse() = runBlocking {
        val playerWithShots = player.copy(shotsLoggedList = listOf(TestShotLogged.build()))
        val filter = PlayerFilter(hasShotsLogged = HasShotsLoggedFilter.NoShots)
        val result = playerRepositoryImpl.matchesHasShotsLoggedFilter(player = playerWithShots, filter = filter)
        assertThat(result, equalTo(false))
    }

    @Test
    fun matchesHasShotsLoggedFilterWhenHasShotsLoggedIsBothShouldReturnTrue() = runBlocking {
        val filter = PlayerFilter(hasShotsLogged = HasShotsLoggedFilter.Both)
        val result = playerRepositoryImpl.matchesHasShotsLoggedFilter(player = player, filter = filter)
        assertThat(result, equalTo(true))
    }

    @Test
    fun matchesHasShotsLoggedFilterWhenHasShotsLoggedIsNoneShouldReturnTrue() = runBlocking {
        val filter = PlayerFilter(hasShotsLogged = HasShotsLoggedFilter.None)
        val result = playerRepositoryImpl.matchesHasShotsLoggedFilter(player = player, filter = filter)
        assertThat(result, equalTo(true))
    }

    @Test
    fun matchesHasShotsLoggedFilterWhenHasShotsLoggedIsNullShouldReturnTrue() = runBlocking {
        val filter = PlayerFilter(hasShotsLogged = null)
        val result = playerRepositoryImpl.matchesHasShotsLoggedFilter(player = player, filter = filter)
        assertThat(result, equalTo(true))
    }

    @Test
    fun matchesShotCountRangeFilterWhenMinShotsAndMaxShotsAreNullShouldReturnTrue() = runBlocking {
        val filter = PlayerFilter(minShots = null, maxShots = null)
        val result = playerRepositoryImpl.matchesShotCountRangeFilter(player = player, filter = filter)
        assertThat(result, equalTo(true))
    }

    @Test
    fun matchesShotCountRangeFilterWhenPlayerShotsCountIsWithinRangeShouldReturnTrue() = runBlocking {
        val playerWithShots = player.copy(shotsLoggedList = listOf(TestShotLogged.build(), TestShotLogged.build(), TestShotLogged.build()))
        val filter = PlayerFilter(minShots = 2, maxShots = 5)
        val result = playerRepositoryImpl.matchesShotCountRangeFilter(player = playerWithShots, filter = filter)
        assertThat(result, equalTo(true))
    }

    @Test
    fun matchesShotCountRangeFilterWhenPlayerShotsCountIsBelowMinShotsShouldReturnFalse() = runBlocking {
        val playerWithShots = player.copy(shotsLoggedList = listOf(TestShotLogged.build()))
        val filter = PlayerFilter(minShots = 2, maxShots = null)
        val result = playerRepositoryImpl.matchesShotCountRangeFilter(player = playerWithShots, filter = filter)
        assertThat(result, equalTo(false))
    }

    @Test
    fun matchesShotCountRangeFilterWhenPlayerShotsCountIsAboveMaxShotsShouldReturnFalse() = runBlocking {
        val playerWithShots = player.copy(shotsLoggedList = listOf(TestShotLogged.build(), TestShotLogged.build(), TestShotLogged.build()))
        val filter = PlayerFilter(minShots = null, maxShots = 2)
        val result = playerRepositoryImpl.matchesShotCountRangeFilter(player = playerWithShots, filter = filter)
        assertThat(result, equalTo(false))
    }

    @Test
    fun matchesShotCountRangeFilterWhenPlayerShotsCountEqualsMinShotsShouldReturnTrue() = runBlocking {
        val playerWithShots = player.copy(shotsLoggedList = listOf(TestShotLogged.build(), TestShotLogged.build()))
        val filter = PlayerFilter(minShots = 2, maxShots = null)
        val result = playerRepositoryImpl.matchesShotCountRangeFilter(player = playerWithShots, filter = filter)
        assertThat(result, equalTo(true))
    }

    @Test
    fun matchesShotCountRangeFilterWhenPlayerShotsCountEqualsMaxShotsShouldReturnTrue() = runBlocking {
        val playerWithShots = player.copy(shotsLoggedList = listOf(TestShotLogged.build(), TestShotLogged.build()))
        val filter = PlayerFilter(minShots = null, maxShots = 2)
        val result = playerRepositoryImpl.matchesShotCountRangeFilter(player = playerWithShots, filter = filter)
        assertThat(result, equalTo(true))
    }

    @Test
    fun matchesShotCountRangeFilterWhenOnlyMinShotsIsSetAndPlayerMeetsRequirementShouldReturnTrue() = runBlocking {
        val playerWithShots = player.copy(shotsLoggedList = listOf(TestShotLogged.build(), TestShotLogged.build(), TestShotLogged.build()))
        val filter = PlayerFilter(minShots = 2, maxShots = null)
        val result = playerRepositoryImpl.matchesShotCountRangeFilter(player = playerWithShots, filter = filter)
        assertThat(result, equalTo(true))
    }

    @Test
    fun matchesShotCountRangeFilterWhenOnlyMaxShotsIsSetAndPlayerMeetsRequirementShouldReturnTrue() = runBlocking {
        val playerWithShots = player.copy(shotsLoggedList = listOf(TestShotLogged.build()))
        val filter = PlayerFilter(minShots = null, maxShots = 5)
        val result = playerRepositoryImpl.matchesShotCountRangeFilter(player = playerWithShots, filter = filter)
        assertThat(result, equalTo(true))
    }
}
