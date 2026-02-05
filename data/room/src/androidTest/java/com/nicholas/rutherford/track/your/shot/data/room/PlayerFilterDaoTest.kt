package com.nicholas.rutherford.track.your.shot.data.room

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nicholas.rutherford.track.your.shot.data.room.dao.PlayerFilterDao
import com.nicholas.rutherford.track.your.shot.data.room.database.AppDatabase
import com.nicholas.rutherford.track.your.shot.data.room.entities.PlayerFilterEntity
import com.nicholas.rutherford.track.your.shot.data.room.entities.PlayerFilterPositionEntity
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PlayerFilterDaoTest {

    private lateinit var appDatabase: AppDatabase
    private lateinit var playerFilterDao: PlayerFilterDao

    @Before
    fun setUp() {
        appDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        playerFilterDao = appDatabase.playerFilterDao()
    }

    @After
    fun teardown() {
        appDatabase.close()
    }

    @Test
    fun getActiveFilterWhenNoFilterExistsReturnsNull() = runBlocking {
        assertThat(playerFilterDao.getActiveFilter(), nullValue())
    }

    @Test
    fun getActiveFilterWhenFilterExistsReturnsFilter() = runBlocking {
        val filterEntity = PlayerFilterEntity(
            id = 1,
            hasShotsLogged = true,
            minShots = 10,
            maxShots = 50,
            lastUpdated = System.currentTimeMillis()
        )

        playerFilterDao.saveActiveFilter(filterEntity)

        val fetchedFilter = playerFilterDao.getActiveFilter()
        assertThat(fetchedFilter?.hasShotsLogged, equalTo(filterEntity.hasShotsLogged))
        assertThat(fetchedFilter?.minShots, equalTo(filterEntity.minShots))
        assertThat(fetchedFilter?.maxShots, equalTo(filterEntity.maxShots))
    }

    @Test
    fun saveActiveFilterInsertsNewFilter() = runBlocking {
        val filterEntity = PlayerFilterEntity(
            id = 1,
            hasShotsLogged = true,
            minShots = 5,
            maxShots = 20,
            lastUpdated = System.currentTimeMillis()
        )

        playerFilterDao.saveActiveFilter(filterEntity)

        val fetchedFilter = playerFilterDao.getActiveFilter()
        assertThat(fetchedFilter?.hasShotsLogged, equalTo(filterEntity.hasShotsLogged))
        assertThat(fetchedFilter?.minShots, equalTo(filterEntity.minShots))
        assertThat(fetchedFilter?.maxShots, equalTo(filterEntity.maxShots))
    }

    @Test
    fun saveActiveFilterUpdatesExistingFilter() = runBlocking {
        val initialFilter = PlayerFilterEntity(
            id = 1,
            hasShotsLogged = true,
            minShots = 10,
            maxShots = null,
            lastUpdated = System.currentTimeMillis()
        )

        val updatedFilter = PlayerFilterEntity(
            id = 1,
            hasShotsLogged = false,
            minShots = 20,
            maxShots = 100,
            lastUpdated = System.currentTimeMillis()
        )

        playerFilterDao.saveActiveFilter(initialFilter)
        assertThat(playerFilterDao.getActiveFilter()?.hasShotsLogged, equalTo(true))

        playerFilterDao.saveActiveFilter(updatedFilter)
        val fetchedFilter = playerFilterDao.getActiveFilter()
        assertThat(fetchedFilter?.hasShotsLogged, equalTo(false))
        assertThat(fetchedFilter?.minShots, equalTo(20))
        assertThat(fetchedFilter?.maxShots, equalTo(100))
    }

    @Test
    fun clearActiveFilterRemovesFilter() = runBlocking {
        val filterEntity = PlayerFilterEntity(
            id = 1,
            hasShotsLogged = true,
            minShots = 10,
            maxShots = 50,
            lastUpdated = System.currentTimeMillis()
        )

        playerFilterDao.saveActiveFilter(filterEntity)
        val fetchedFilter = playerFilterDao.getActiveFilter()
        assertThat(fetchedFilter?.hasShotsLogged, equalTo(filterEntity.hasShotsLogged))
        assertThat(fetchedFilter?.minShots, equalTo(filterEntity.minShots))
        assertThat(fetchedFilter?.maxShots, equalTo(filterEntity.maxShots))

        playerFilterDao.clearActiveFilter()

        assertThat(playerFilterDao.getActiveFilter(), nullValue())
    }

    @Test
    fun clearActiveFilterCascadesToPositions() = runBlocking {
        val filterEntity = PlayerFilterEntity(
            id = 1,
            hasShotsLogged = true,
            minShots = null,
            maxShots = null,
            lastUpdated = System.currentTimeMillis()
        )

        val positionEntities = listOf(
            PlayerFilterPositionEntity(filterId = 1, position = "PG"),
            PlayerFilterPositionEntity(filterId = 1, position = "SG")
        )

        playerFilterDao.saveActiveFilter(filterEntity)
        playerFilterDao.saveFilterPositions(positionEntities)

        assertThat(playerFilterDao.getActiveFilterPositions().size, equalTo(2))

        playerFilterDao.clearActiveFilter()

        assertThat(playerFilterDao.getActiveFilterPositions().size, equalTo(0))
    }

    @Test
    fun getActiveFilterPositionsWhenNoPositionsReturnsEmptyList() = runBlocking {
        val filterEntity = PlayerFilterEntity(
            id = 1,
            hasShotsLogged = null,
            minShots = null,
            maxShots = null,
            lastUpdated = System.currentTimeMillis()
        )

        playerFilterDao.saveActiveFilter(filterEntity)

        assertThat(playerFilterDao.getActiveFilterPositions(), equalTo(emptyList()))
    }

    @Test
    fun getActiveFilterPositionsReturnsPositionsInSortedOrder() = runBlocking {
        val filterEntity = PlayerFilterEntity(
            id = 1,
            hasShotsLogged = null,
            minShots = null,
            maxShots = null,
            lastUpdated = System.currentTimeMillis()
        )

        val positionEntities = listOf(
            PlayerFilterPositionEntity(filterId = 1, position = "PF"),
            PlayerFilterPositionEntity(filterId = 1, position = "C"),
            PlayerFilterPositionEntity(filterId = 1, position = "SG"),
            PlayerFilterPositionEntity(filterId = 1, position = "PG"),
            PlayerFilterPositionEntity(filterId = 1, position = "SF")
        )

        playerFilterDao.saveActiveFilter(filterEntity)
        playerFilterDao.saveFilterPositions(positionEntities)

        val positions = playerFilterDao.getActiveFilterPositions()
        assertThat(positions, equalTo(listOf("C", "PF", "PG", "SF", "SG")))
    }

    @Test
    fun saveFilterPositionsInsertsNewPositions() = runBlocking {
        val filterEntity = PlayerFilterEntity(
            id = 1,
            hasShotsLogged = null,
            minShots = null,
            maxShots = null,
            lastUpdated = System.currentTimeMillis()
        )

        val positionEntities = listOf(
            PlayerFilterPositionEntity(filterId = 1, position = "PG"),
            PlayerFilterPositionEntity(filterId = 1, position = "SG"),
            PlayerFilterPositionEntity(filterId = 1, position = "SF")
        )

        playerFilterDao.saveActiveFilter(filterEntity)
        playerFilterDao.saveFilterPositions(positionEntities)

        val positions = playerFilterDao.getActiveFilterPositions()
        assertThat(positions.size, equalTo(3))
        assertThat(positions, equalTo(listOf("PG", "SF", "SG")))
    }

    @Test
    fun saveFilterPositionsReplacesExistingPositions() = runBlocking {
        val filterEntity = PlayerFilterEntity(
            id = 1,
            hasShotsLogged = null,
            minShots = null,
            maxShots = null,
            lastUpdated = System.currentTimeMillis()
        )

        val initialPositions = listOf(
            PlayerFilterPositionEntity(filterId = 1, position = "PG"),
            PlayerFilterPositionEntity(filterId = 1, position = "SG")
        )

        val updatedPositions = listOf(
            PlayerFilterPositionEntity(filterId = 1, position = "SF"),
            PlayerFilterPositionEntity(filterId = 1, position = "PF"),
            PlayerFilterPositionEntity(filterId = 1, position = "C")
        )

        playerFilterDao.saveActiveFilter(filterEntity)
        playerFilterDao.saveFilterPositions(initialPositions)
        assertThat(playerFilterDao.getActiveFilterPositions().size, equalTo(2))

        // Clear positions first, then save new ones (simulating repository behavior)
        playerFilterDao.clearFilterPositions()
        playerFilterDao.saveFilterPositions(updatedPositions)
        val positions = playerFilterDao.getActiveFilterPositions()
        assertThat(positions.size, equalTo(3))
        assertThat(positions, equalTo(listOf("C", "PF", "SF")))
    }

    @Test
    fun clearFilterPositionsRemovesAllPositions() = runBlocking {
        val filterEntity = PlayerFilterEntity(
            id = 1,
            hasShotsLogged = null,
            minShots = null,
            maxShots = null,
            lastUpdated = System.currentTimeMillis()
        )

        val positionEntities = listOf(
            PlayerFilterPositionEntity(filterId = 1, position = "PG"),
            PlayerFilterPositionEntity(filterId = 1, position = "SG"),
            PlayerFilterPositionEntity(filterId = 1, position = "SF")
        )

        playerFilterDao.saveActiveFilter(filterEntity)
        playerFilterDao.saveFilterPositions(positionEntities)
        assertThat(playerFilterDao.getActiveFilterPositions().size, equalTo(3))

        playerFilterDao.clearFilterPositions()

        assertThat(playerFilterDao.getActiveFilterPositions(), equalTo(emptyList()))
    }

    @Test
    fun clearFilterPositionsDoesNotRemoveFilter() = runBlocking {
        val filterEntity = PlayerFilterEntity(
            id = 1,
            hasShotsLogged = true,
            minShots = 10,
            maxShots = 50,
            lastUpdated = System.currentTimeMillis()
        )

        val positionEntities = listOf(
            PlayerFilterPositionEntity(filterId = 1, position = "PG")
        )

        playerFilterDao.saveActiveFilter(filterEntity)
        playerFilterDao.saveFilterPositions(positionEntities)

        playerFilterDao.clearFilterPositions()

        val fetchedFilter = playerFilterDao.getActiveFilter()
        assertThat(fetchedFilter?.hasShotsLogged, equalTo(filterEntity.hasShotsLogged))
        assertThat(fetchedFilter?.minShots, equalTo(filterEntity.minShots))
        assertThat(fetchedFilter?.maxShots, equalTo(filterEntity.maxShots))
    }

    @Test
    fun hasActiveFilterWhenNoFilterExistsReturnsFalse() = runBlocking {
        assertThat(playerFilterDao.hasActiveFilter(), equalTo(false))
    }

    @Test
    fun hasActiveFilterWhenFilterExistsReturnsTrue() = runBlocking {
        val filterEntity = PlayerFilterEntity(
            id = 1,
            hasShotsLogged = true,
            minShots = null,
            maxShots = null,
            lastUpdated = System.currentTimeMillis()
        )

        playerFilterDao.saveActiveFilter(filterEntity)

        assertThat(playerFilterDao.hasActiveFilter(), equalTo(true))
    }

    @Test
    fun hasActiveFilterAfterClearReturnsFalse() = runBlocking {
        val filterEntity = PlayerFilterEntity(
            id = 1,
            hasShotsLogged = true,
            minShots = null,
            maxShots = null,
            lastUpdated = System.currentTimeMillis()
        )

        playerFilterDao.saveActiveFilter(filterEntity)
        assertThat(playerFilterDao.hasActiveFilter(), equalTo(true))

        playerFilterDao.clearActiveFilter()

        assertThat(playerFilterDao.hasActiveFilter(), equalTo(false))
    }
}
