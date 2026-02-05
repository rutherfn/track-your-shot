package com.nicholas.rutherford.track.your.shot.data.room

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nicholas.rutherford.track.your.shot.data.room.dao.PlayerFilterDao
import com.nicholas.rutherford.track.your.shot.data.room.database.AppDatabase
import com.nicholas.rutherford.track.your.shot.data.room.repository.DEFAULT_FILTER_COUNT
import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerFilterRepositoryImpl
import com.nicholas.rutherford.track.your.shot.data.room.response.PlayerFilter
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PlayerFilterRepositoryImplTest {

    private lateinit var appDatabase: AppDatabase
    private lateinit var playerFilterDao: PlayerFilterDao
    private lateinit var playerFilterRepositoryImpl: PlayerFilterRepositoryImpl

    @Before
    fun setup() {
        appDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        playerFilterDao = appDatabase.playerFilterDao()

        playerFilterRepositoryImpl = PlayerFilterRepositoryImpl(playerFilterDao = playerFilterDao)
    }

    @After
    fun teardown() {
        appDatabase.close()
    }

    @Test
    fun fetchActiveFilterWhenNoFilterExistsReturnsNull() = runBlocking {
        assertThat(playerFilterRepositoryImpl.fetchActiveFilter(), nullValue())
    }

    @Test
    fun fetchActiveFilterWhenFilterExistsReturnsFilter() = runBlocking {
        val filter = PlayerFilter(
            hasShotsLogged = true,
            minShots = 10,
            maxShots = 50,
            selectedPositions = listOf("PG", "SG").sorted()
        )

        playerFilterRepositoryImpl.saveActiveFilter(filter)

        val fetchedFilter = playerFilterRepositoryImpl.fetchActiveFilter()
        assertThat(fetchedFilter, equalTo(filter))
    }

    @Test
    fun fetchActiveFilterWhenFilterExistsWithNoPositionsReturnsFilter() = runBlocking {
        val filter = PlayerFilter(
            hasShotsLogged = false,
            minShots = null,
            maxShots = null,
            selectedPositions = emptyList()
        )

        playerFilterRepositoryImpl.saveActiveFilter(filter)

        val fetchedFilter = playerFilterRepositoryImpl.fetchActiveFilter()
        assertThat(fetchedFilter, equalTo(filter))
    }

    @Test
    fun saveActiveFilterSavesFilterWithPositions() = runBlocking {
        val filter = PlayerFilter(
            hasShotsLogged = true,
            minShots = 5,
            maxShots = 20,
            selectedPositions = listOf("PG", "SG", "SF").sorted()
        )

        playerFilterRepositoryImpl.saveActiveFilter(filter)

        val fetchedFilter = playerFilterRepositoryImpl.fetchActiveFilter()
        assertThat(fetchedFilter, equalTo(filter))
    }

    @Test
    fun saveActiveFilterUpdatesExistingFilter() = runBlocking {
        val initialFilter = PlayerFilter(
            hasShotsLogged = true,
            minShots = 10,
            maxShots = null,
            selectedPositions = listOf("PG").sorted()
        )

        val updatedFilter = PlayerFilter(
            hasShotsLogged = false,
            minShots = 20,
            maxShots = 100,
            selectedPositions = listOf("SG", "SF", "PF", "C").sorted()
        )

        playerFilterRepositoryImpl.saveActiveFilter(initialFilter)
        assertThat(playerFilterRepositoryImpl.fetchActiveFilter(), equalTo(initialFilter))

        playerFilterRepositoryImpl.saveActiveFilter(updatedFilter)
        assertThat(playerFilterRepositoryImpl.fetchActiveFilter(), equalTo(updatedFilter))
    }

    @Test
    fun saveActiveFilterClearsPreviousPositionsWhenUpdating() = runBlocking {
        val initialFilter = PlayerFilter(
            hasShotsLogged = null,
            minShots = null,
            maxShots = null,
            selectedPositions = listOf("PG", "SG", "SF").sorted()
        )

        val updatedFilter = PlayerFilter(
            hasShotsLogged = null,
            minShots = null,
            maxShots = null,
            selectedPositions = listOf("PF", "C").sorted()
        )

        playerFilterRepositoryImpl.saveActiveFilter(initialFilter)
        playerFilterRepositoryImpl.saveActiveFilter(updatedFilter)

        val fetchedFilter = playerFilterRepositoryImpl.fetchActiveFilter()
        assertThat(fetchedFilter?.selectedPositions, equalTo(listOf("C", "PF")))
    }

    @Test
    fun saveActiveFilterWithEmptyPositionsListClearsAllPositions() = runBlocking {
        val filterWithPositions = PlayerFilter(
            hasShotsLogged = null,
            minShots = null,
            maxShots = null,
            selectedPositions = listOf("PG", "SG").sorted()
        )

        val filterWithoutPositions = PlayerFilter(
            hasShotsLogged = null,
            minShots = null,
            maxShots = null,
            selectedPositions = emptyList()
        )

        playerFilterRepositoryImpl.saveActiveFilter(filterWithPositions)
        playerFilterRepositoryImpl.saveActiveFilter(filterWithoutPositions)

        val fetchedFilter = playerFilterRepositoryImpl.fetchActiveFilter()
        assertThat(fetchedFilter?.selectedPositions, equalTo(emptyList()))
    }

    @Test
    fun clearActiveFilterRemovesFilterAndPositions() = runBlocking {
        val filter = PlayerFilter(
            hasShotsLogged = true,
            minShots = 10,
            maxShots = 50,
            selectedPositions = listOf("PG", "SG", "SF").sorted()
        )

        playerFilterRepositoryImpl.saveActiveFilter(filter)
        assertThat(playerFilterRepositoryImpl.fetchActiveFilter(), equalTo(filter))

        playerFilterRepositoryImpl.clearActiveFilter()

        assertThat(playerFilterRepositoryImpl.fetchActiveFilter(), nullValue())
        assertThat(playerFilterRepositoryImpl.hasActiveFilter(), equalTo(false))
    }

    @Test
    fun hasActiveFilterWhenNoFilterExistsReturnsFalse() = runBlocking {
        assertThat(playerFilterRepositoryImpl.hasActiveFilter(), equalTo(false))
    }

    @Test
    fun hasActiveFilterWhenFilterExistsReturnsTrue() = runBlocking {
        val filter = PlayerFilter(
            hasShotsLogged = true,
            minShots = null,
            maxShots = null,
            selectedPositions = emptyList()
        )

        playerFilterRepositoryImpl.saveActiveFilter(filter)

        assertThat(playerFilterRepositoryImpl.hasActiveFilter(), equalTo(true))
    }

    @Test
    fun getActiveFilterCountWhenNoFilterExistsReturnsDefaultCount() = runBlocking {
        assertThat(playerFilterRepositoryImpl.getActiveFilterCount(), equalTo(DEFAULT_FILTER_COUNT))
    }

    @Test
    fun getActiveFilterCountWithOnlyPositionsReturnsPositionCount() = runBlocking {
        val filter = PlayerFilter(
            hasShotsLogged = null,
            minShots = null,
            maxShots = null,
            selectedPositions = listOf("PG", "SG", "SF").sorted()
        )

        playerFilterRepositoryImpl.saveActiveFilter(filter)

        assertThat(playerFilterRepositoryImpl.getActiveFilterCount(), equalTo(3))
    }

    @Test
    fun getActiveFilterCountWithHasShotsLoggedReturnsOne() = runBlocking {
        val filter = PlayerFilter(
            hasShotsLogged = true,
            minShots = null,
            maxShots = null,
            selectedPositions = emptyList()
        )

        playerFilterRepositoryImpl.saveActiveFilter(filter)

        assertThat(playerFilterRepositoryImpl.getActiveFilterCount(), equalTo(1))
    }

    @Test
    fun getActiveFilterCountWithMinShotsReturnsOne() = runBlocking {
        val filter = PlayerFilter(
            hasShotsLogged = null,
            minShots = 10,
            maxShots = null,
            selectedPositions = emptyList()
        )

        playerFilterRepositoryImpl.saveActiveFilter(filter)

        assertThat(playerFilterRepositoryImpl.getActiveFilterCount(), equalTo(1))
    }

    @Test
    fun getActiveFilterCountWithMaxShotsReturnsOne() = runBlocking {
        val filter = PlayerFilter(
            hasShotsLogged = null,
            minShots = null,
            maxShots = 50,
            selectedPositions = emptyList()
        )

        playerFilterRepositoryImpl.saveActiveFilter(filter)

        assertThat(playerFilterRepositoryImpl.getActiveFilterCount(), equalTo(1))
    }

    @Test
    fun getActiveFilterCountWithAllFiltersReturnsCorrectCount() = runBlocking {
        val filter = PlayerFilter(
            hasShotsLogged = true, // +1
            minShots = 10, // +1
            maxShots = 50, // +1
            selectedPositions = listOf("PG", "SG", "SF", "PF", "C").sorted() // +5
        )

        playerFilterRepositoryImpl.saveActiveFilter(filter)

        assertThat(playerFilterRepositoryImpl.getActiveFilterCount(), equalTo(8))
    }

    @Test
    fun getActiveFilterCountWithMixedFiltersReturnsCorrectCount() = runBlocking {
        val filter = PlayerFilter(
            hasShotsLogged = false, // +1
            minShots = null,
            maxShots = 100, // +1
            selectedPositions = listOf("PG", "SG").sorted() // +2
        )

        playerFilterRepositoryImpl.saveActiveFilter(filter)

        assertThat(playerFilterRepositoryImpl.getActiveFilterCount(), equalTo(4))
    }
}
