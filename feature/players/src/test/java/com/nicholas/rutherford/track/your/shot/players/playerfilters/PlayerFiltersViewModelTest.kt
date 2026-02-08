package com.nicholas.rutherford.track.your.shot.players.playerfilters

import android.app.Application
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerFilterRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.HasShotsLoggedFilter
import com.nicholas.rutherford.track.your.shot.data.room.response.PlayerFilter
import com.nicholas.rutherford.track.your.shot.data.room.response.getFilterCount
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.your.shot.data.test.room.TestPlayer
import com.nicholas.rutherford.track.your.shot.data.test.room.TestPlayerFilter
import com.nicholas.rutherford.track.your.shot.feature.players.playerfilters.PlayerFiltersNavigation
import com.nicholas.rutherford.track.your.shot.feature.players.playerfilters.PlayerFiltersState
import com.nicholas.rutherford.track.your.shot.feature.players.playerfilters.PlayerFiltersViewModel
import com.nicholas.rutherford.track.your.shot.helper.extensions.date.DateExt
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PlayerFiltersViewModelTest {

    private lateinit var viewModel: PlayerFiltersViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    private val scope = CoroutineScope(SupervisorJob() + testDispatcher)

    private val application = mockk<Application>(relaxed = true)

    private val navigation = mockk<PlayerFiltersNavigation>(relaxed = true)

    private val playersFilterRepository = mockk<PlayerFilterRepository>(relaxed = true)

    private val playersRepository = mockk<PlayerRepository>(relaxed = true)

    private val dateExt = mockk<DateExt>(relaxed = true)

    private val defaultState = PlayerFiltersState()

    private fun mockStrings() {
        every { application.getString(StringsIds.discardFilterChanges) } returns "Discard Filter Changes?"
        every { application.getString(StringsIds.discardFilterChangesDescription) } returns "You have unsaved filter changes. Leaving now will discard all your current filter selections."
        every { application.getString(StringsIds.yes) } returns "Yes"
        every { application.getString(StringsIds.no) } returns "No"
        every { application.getString(StringsIds.clearShotRange) } returns "Clear Shot Range?"
        every { application.getString(StringsIds.clearShotRangeDescription) } returns "Are you sure you want to reset the minimum and maximum shot values to their defaults? This will set both values to 0."
        every { application.getString(StringsIds.resetFilters) } returns "Reset Filters"
        every { application.getString(StringsIds.resetFiltersDescription) } returns "Are you sure you want to reset all filters to their default state?"
        every { application.getString(StringsIds.pointGuard) } returns "Point Guard"
        every { application.getString(StringsIds.shootingGuard) } returns "Shooting Guard"
        every { application.getString(StringsIds.smallForward) } returns "Small Forward"
        every { application.getString(StringsIds.powerForward) } returns "Power Forward"
        every { application.getString(StringsIds.center) } returns "Center"
        every { application.getString(StringsIds.all) } returns "All"
        every { application.getString(StringsIds.hasShots) } returns "Has Shots"
        every { application.getString(StringsIds.noShots) } returns "No Shots"
        every { application.getString(StringsIds.both) } returns "Both"
    }

    @BeforeEach
    fun beforeEach() {
        mockStrings()

        viewModel = PlayerFiltersViewModel(
            scope = scope,
            application = application,
            navigation = navigation,
            playerFilterRepository = playersFilterRepository,
            playerRepository = playersRepository,
            dateExt = dateExt
        )
    }

    @Nested
    inner class Init {

        @Test
        fun `when filterActiveFilter returns not null should update initial filter and state`() {
            val playerFilter = TestPlayerFilter().create()
            val players = listOf(TestPlayer().create())

            coEvery { playersFilterRepository.fetchActiveFilter() } returns playerFilter
            coEvery { playersRepository.fetchAllPlayersWithFilter(filter = playerFilter) } returns players

            viewModel = PlayerFiltersViewModel(
                scope = scope,
                application = application,
                navigation = navigation,
                playerFilterRepository = playersFilterRepository,
                playerRepository = playersRepository,
                dateExt = dateExt
            )

            Assertions.assertEquals(viewModel.initialFilter, playerFilter)
            Assertions.assertEquals(viewModel.pendingFilter, playerFilter)
            Assertions.assertEquals(
                viewModel.playerFiltersMutableStateFlow.value,
                defaultState.copy(
                    filterCount = 4,
                    lastUpdatedFilterDateValue = playerFilter.lastUpdatedValue,
                    defaultPositions = listOf(
                        "Point Guard",
                        "Shooting Guard",
                        "Small Forward",
                        "Power Forward",
                        "Center",
                        "All"
                    ),
                    selectedPositions = playerFilter.selectedPositions,
                    defaultShotLogsOptions = listOf(
                        "Has Shots",
                        "No Shots",
                        "Both"
                    ),
                    selectedShotLogsOptions = listOf("Both"),
                    minShots = playerFilter.minShots,
                    maxShots = playerFilter.maxShots,
                    filteredPlayerCount = 1
                )
            )
        }

        @Test
        fun `when fetchActiveFilter returns null should update initial filter and state`() {
            val playerFilter = PlayerFilter(hasShotsLogged = HasShotsLoggedFilter.Both)
            val players = listOf(TestPlayer().create())

            coEvery { playersFilterRepository.fetchActiveFilter() } returns null
            coEvery { playersRepository.fetchAllPlayersWithFilter(filter = playerFilter) } returns players

            viewModel = PlayerFiltersViewModel(
                scope = scope,
                application = application,
                navigation = navigation,
                playerFilterRepository = playersFilterRepository,
                playerRepository = playersRepository,
                dateExt = dateExt
            )

            Assertions.assertEquals(viewModel.initialFilter, playerFilter)
            Assertions.assertEquals(viewModel.pendingFilter, playerFilter)
            Assertions.assertEquals(
                viewModel.playerFiltersMutableStateFlow.value,
                defaultState.copy(
                    filterCount = 0,
                    lastUpdatedFilterDateValue = "",
                    defaultPositions = listOf(
                        "Point Guard",
                        "Shooting Guard",
                        "Small Forward",
                        "Power Forward",
                        "Center",
                        "All"
                    ),
                    selectedPositions = emptyList(),
                    defaultShotLogsOptions = listOf(
                        "Has Shots",
                        "No Shots",
                        "Both"
                    ),
                    selectedShotLogsOptions = listOf("Both"),
                    minShots = playerFilter.minShots,
                    maxShots = playerFilter.maxShots,
                    filteredPlayerCount = 1
                )
            )
        }
    }

    @Test
    fun `buildAreYouSureYouWantToCancelFilters should return alert`() {
        val alert = viewModel.buildAreYouSureYouWantToCancelFilters()

        Assertions.assertEquals("Discard Filter Changes?", alert.title)
        Assertions.assertEquals("You have unsaved filter changes. Leaving now will discard all your current filter selections.", alert.description)
        Assertions.assertNotNull(alert.confirmButton)
        Assertions.assertEquals("Yes", alert.confirmButton?.buttonText)
        Assertions.assertNotNull(alert.dismissButton)
        Assertions.assertEquals("No", alert.dismissButton?.buttonText)
    }

    @Test
    fun `buildClearShotRangeAlert should return alert`() {
        val alert = viewModel.buildClearShotRangeAlert()

        Assertions.assertEquals("Clear Shot Range?", alert.title)
        Assertions.assertEquals("Are you sure you want to reset the minimum and maximum shot values to their defaults? This will set both values to 0.", alert.description)
        Assertions.assertNotNull(alert.confirmButton)
        Assertions.assertEquals("Yes", alert.confirmButton?.buttonText)
        Assertions.assertNotNull(alert.dismissButton)
        Assertions.assertEquals("No", alert.dismissButton?.buttonText)
    }

    @Test
    fun `onClearShotRangeClicked should call navigation alert`() {
        viewModel.onClearShotRangeClicked()

        verify { navigation.alert(alert = any()) }
    }

    @Test
    fun `onClearShotRangeConfirmed should update pendingFilter and state`() = runTest {
        val initialFilter = PlayerFilter(
            hasShotsLogged = HasShotsLoggedFilter.Both,
            minShots = 5,
            maxShots = 10,
            selectedPositions = emptyList()
        )
        val players = listOf(TestPlayer().create())

        coEvery { playersFilterRepository.fetchActiveFilter() } returns initialFilter
        coEvery { playersRepository.fetchAllPlayersWithFilter(filter = any()) } returns players

        viewModel = PlayerFiltersViewModel(
            scope = scope,
            application = application,
            navigation = navigation,
            playerFilterRepository = playersFilterRepository,
            playerRepository = playersRepository,
            dateExt = dateExt
        )

        viewModel.onClearShotRangeConfirmed()

        Assertions.assertNull(viewModel.pendingFilter.minShots)
        Assertions.assertNull(viewModel.pendingFilter.maxShots)
        Assertions.assertNull(viewModel.playerFiltersMutableStateFlow.value.minShots)
        Assertions.assertNull(viewModel.playerFiltersMutableStateFlow.value.maxShots)
    }

    @Test
    fun `buildResetFiltersAlert should return alert`() {
        val alert = viewModel.buildResetFiltersAlert()

        Assertions.assertEquals("Reset Filters", alert.title)
        Assertions.assertEquals("Are you sure you want to reset all filters to their default state?", alert.description)
        Assertions.assertNotNull(alert.confirmButton)
        Assertions.assertEquals("Yes", alert.confirmButton?.buttonText)
        Assertions.assertNotNull(alert.dismissButton)
        Assertions.assertEquals("No", alert.dismissButton?.buttonText)
    }

    @Test
    fun `on reset filters clicked should call alert`() {
        viewModel.onResetFiltersClicked()

        verify { navigation.alert(alert = any()) }
    }

    @Test
    fun `on reset filters confirmed should reset the filters and update state`() = runTest {
        val initialFilter = PlayerFilter(
            hasShotsLogged = HasShotsLoggedFilter.HasShots,
            minShots = 5,
            maxShots = 10,
            selectedPositions = listOf("Point Guard", "Shooting Guard")
        )
        val players = listOf(TestPlayer().create())
        val timestamp = 1000000L
        val expectedDateString = "Dec 31, 1969 06:16 PM"

        every { dateExt.now } returns timestamp
        coEvery { playersFilterRepository.fetchActiveFilter() } returns initialFilter
        coEvery { playersRepository.fetchAllPlayersWithFilter(filter = any()) } returns players
        coEvery { playersFilterRepository.saveActiveFilter(filter = any()) } returns Unit

        viewModel = PlayerFiltersViewModel(
            scope = scope,
            application = application,
            navigation = navigation,
            playerFilterRepository = playersFilterRepository,
            playerRepository = playersRepository,
            dateExt = dateExt
        )

        viewModel.onResetFiltersConfirmed()
        advanceUntilIdle()

        val expectedFilter = PlayerFilter(
            hasShotsLogged = HasShotsLoggedFilter.Both,
            minShots = null,
            maxShots = null,
            selectedPositions = emptyList(),
            lastUpdatedValue = expectedDateString
        )

        Assertions.assertEquals(expectedFilter.hasShotsLogged, viewModel.pendingFilter.hasShotsLogged)
        Assertions.assertNull(viewModel.pendingFilter.minShots)
        Assertions.assertNull(viewModel.pendingFilter.maxShots)
        Assertions.assertEquals(emptyList<String>(), viewModel.pendingFilter.selectedPositions)
        Assertions.assertEquals(expectedFilter.lastUpdatedValue, viewModel.pendingFilter.lastUpdatedValue)
        Assertions.assertEquals(expectedFilter, viewModel.initialFilter)
        Assertions.assertEquals(0, viewModel.playerFiltersMutableStateFlow.value.filterCount)
        Assertions.assertEquals(emptyList<String>(), viewModel.playerFiltersMutableStateFlow.value.selectedPositions)
        Assertions.assertNull(viewModel.playerFiltersMutableStateFlow.value.minShots)
        Assertions.assertNull(viewModel.playerFiltersMutableStateFlow.value.maxShots)
        Assertions.assertEquals(listOf("Both"), viewModel.playerFiltersMutableStateFlow.value.selectedShotLogsOptions)

        verify { navigation.enableProgress(progress = any<Progress>()) }
        verify { navigation.disableProgress() }
        coVerify { playersFilterRepository.saveActiveFilter(filter = any()) }
    }

    @Nested
    inner class OnToolbarMenuClicked {

        @Test
        fun `when initial filter and pending filter are equal should call navigation pop`() {
            val filter = TestPlayerFilter().create()

            viewModel.initialFilter = filter
            viewModel.pendingFilter = filter

            viewModel.onToolbarMenuClicked()

            verify { navigation.pop() }
            verify(exactly = 0) { navigation.alert(alert = any()) }
        }

        @Test
        fun `when initial filter and pending filter ar not equal should call alert`() {
            val filter = TestPlayerFilter().create()
            val filter2 = TestPlayerFilter().create().copy(minShots = 1, maxShots = 2)

            viewModel.initialFilter = filter
            viewModel.pendingFilter = filter2

            viewModel.onToolbarMenuClicked()

            verify { navigation.alert(alert = any()) }
            verify(exactly = 0) { navigation.pop() }
        }
    }

    @Nested
    inner class LoadInitialFilterFromDatabase() {

        @Test
        fun `when fetchActiveFilter returns null should update initialFilter and pendingFilter to default one`() = runTest {
            coEvery { playersFilterRepository.fetchActiveFilter() } returns null

            viewModel.loadInitialFilterFromDatabase()

            Assertions.assertEquals(PlayerFilter(hasShotsLogged = HasShotsLoggedFilter.Both), viewModel.initialFilter)
            Assertions.assertEquals(PlayerFilter(hasShotsLogged = HasShotsLoggedFilter.Both), viewModel.pendingFilter)
        }

        @Test
        fun `when fetchActiveFilter returns not null should update initialFilter and pendingFilter`() = runTest {
            val filter = TestPlayerFilter().create()

            coEvery { playersFilterRepository.fetchActiveFilter() } returns filter

            viewModel.loadInitialFilterFromDatabase()

            Assertions.assertEquals(filter, viewModel.initialFilter)
            Assertions.assertEquals(filter, viewModel.pendingFilter)
        }
    }

    @Test
    fun `buildDefaultPositions should return list of strings`() {
        val result = viewModel.buildDefaultPositions()

        Assertions.assertEquals(
            result,
            listOf(
                "Point Guard",
                "Shooting Guard",
                "Small Forward",
                "Power Forward",
                "Center",
                "All"
            )
        )
    }

    @Test
    fun `buildDefaultPositionNames should return list of strings`() {
        val result = viewModel.buildDefaultPositionNames()

        Assertions.assertEquals(
            result,
            listOf(
                "Point Guard",
                "Shooting Guard",
                "Small Forward",
                "Power Forward",
                "Center"
            )
        )
    }

    @Nested
    inner class IsSelectedAllChoosePositionsOption {

        @Test
        fun `should return false if title pass in is not set to All`() {
            val result = viewModel.isSelectedAllChoosePositionsOption(title = "Point Guard")

            Assertions.assertFalse(result)
        }

        @Test
        fun `should return true if title passed in is set to All`() {
            val result = viewModel.isSelectedAllChoosePositionsOption(title = "All")

            Assertions.assertTrue(result)
        }
    }

    @Nested
    inner class ShouldAddAllPositions {

        @Test
        fun `should return false when current position does not contain all default position names`() {
            val currentPositions = listOf("Point Guard", "Shooting Guard")

            val result = viewModel.shouldAddAllPosition(currentPositions = currentPositions)

            Assertions.assertFalse(result)
        }

        @Test
        fun `should return false when current positions contains all`() {
            val currentPositions = listOf(
                "Point Guard",
                "Shooting Guard",
                "Small Forward",
                "Power Forward",
                "Center",
                "All"
            )

            val result = viewModel.shouldAddAllPosition(currentPositions = currentPositions)

            Assertions.assertFalse(result)
        }

        @Test
        fun `should return true if all conditions are met`() {
            val currentPositions = listOf(
                "Point Guard",
                "Shooting Guard",
                "Small Forward",
                "Power Forward",
                "Center"
            )

            val result = viewModel.shouldAddAllPosition(currentPositions = currentPositions)

            Assertions.assertTrue(result)
        }
    }

    @Test
    fun `buildDefaultShotLogsOptions just return all options`() {
        val result = viewModel.buildDefaultShotLogsOptions()

        Assertions.assertEquals(
            result,
            listOf(
                "Has Shots",
                "No Shots",
                "Both"
            )
        )
    }

    @Nested
    inner class BuildDefaultSelectedShotLogOptions {

        @Test
        fun `when hasShotsLogged is set to has shots should return a list of has shots`() {
            val result = viewModel.buildDefaultSelectedShotLogOptions(hasShotsLogged = HasShotsLoggedFilter.HasShots)

            Assertions.assertEquals(result, listOf("Has Shots"))
        }

        @Test
        fun `when hasShotsLogged is set to no shots should return a list of no shots`() {
            val result = viewModel.buildDefaultSelectedShotLogOptions(hasShotsLogged = HasShotsLoggedFilter.NoShots)

            Assertions.assertEquals(result, listOf("No Shots"))
        }

        @Test
        fun `when hasShotsLogged is set to both should return a list of both`() {
            val result = viewModel.buildDefaultSelectedShotLogOptions(hasShotsLogged = HasShotsLoggedFilter.Both)

            Assertions.assertEquals(result, listOf("Both"))
        }

        @Test
        fun `when hasShotLogged is none should return empty list`() {
            val result = viewModel.buildDefaultSelectedShotLogOptions(hasShotsLogged = HasShotsLoggedFilter.None)

            Assertions.assertEquals(result, emptyList<String>())
        }

        @Test
        fun `when hasShotsLogged is null should return list of both`() {
            val result = viewModel.buildDefaultSelectedShotLogOptions(hasShotsLogged = null)

            Assertions.assertEquals(result, listOf("Both"))
        }
    }

    @Test
    fun `when initializePlayerFilterState is called should call functions`() = runTest {
        val players = listOf(TestPlayer().create())

        coEvery { playersFilterRepository.fetchActiveFilter() } returns null
        coEvery { playersRepository.fetchAllPlayersWithFilter(filter = any()) } returns players

        viewModel.initializePlayerFilterState()

        coVerify { playersFilterRepository.fetchActiveFilter() }
        coVerify { playersRepository.fetchAllPlayersWithFilter(filter = any()) }
    }

    @Test
    fun `updatePlayerFilterState should update state`() = runTest {
        val pendingFilter = PlayerFilter(
            hasShotsLogged = HasShotsLoggedFilter.HasShots,
            minShots = 5,
            maxShots = 10,
            selectedPositions = listOf("Point Guard", "Shooting Guard"),
            lastUpdatedValue = "Dec 31, 1969 06:16 PM"
        )
        val players = listOf(TestPlayer().create(), TestPlayer().create())

        coEvery { playersRepository.fetchAllPlayersWithFilter(filter = pendingFilter) } returns players

        viewModel.pendingFilter = pendingFilter
        viewModel.updatePlayerFilterState()

        val state = viewModel.playerFiltersMutableStateFlow.value

        Assertions.assertEquals(pendingFilter.getFilterCount(), state.filterCount)
        Assertions.assertEquals(pendingFilter.lastUpdatedValue, state.lastUpdatedFilterDateValue)
        Assertions.assertEquals(
            listOf("Point Guard", "Shooting Guard", "Small Forward", "Power Forward", "Center", "All"),
            state.defaultPositions
        )
        Assertions.assertEquals(pendingFilter.selectedPositions, state.selectedPositions)
        Assertions.assertEquals(
            listOf("Has Shots", "No Shots", "Both"),
            state.defaultShotLogsOptions
        )
        Assertions.assertEquals(listOf("Has Shots"), state.selectedShotLogsOptions)
        Assertions.assertEquals(pendingFilter.minShots, state.minShots)
        Assertions.assertEquals(pendingFilter.maxShots, state.maxShots)
        Assertions.assertEquals(players.size, state.filteredPlayerCount)

        coVerify { playersRepository.fetchAllPlayersWithFilter(filter = pendingFilter) }
    }

    @Nested
    inner class OnPositionToggled {

        @Test
        fun `when currentPosition contains the title should handle position deselection and update pending filter and state`() = runTest {
            val initialPositions = listOf("Point Guard", "Shooting Guard")
            val pendingFilter = PlayerFilter(
                hasShotsLogged = HasShotsLoggedFilter.Both,
                selectedPositions = initialPositions
            )
            val players = listOf(TestPlayer().create())

            coEvery { playersRepository.fetchAllPlayersWithFilter(filter = any()) } returns players

            viewModel.pendingFilter = pendingFilter
            viewModel.onPositionToggled(title = "Point Guard")

            val expectedPositions = listOf("Shooting Guard")
            Assertions.assertEquals(expectedPositions, viewModel.pendingFilter.selectedPositions)
            Assertions.assertEquals(expectedPositions, viewModel.playerFiltersMutableStateFlow.value.selectedPositions)
            coVerify { playersRepository.fetchAllPlayersWithFilter(filter = any()) }
        }

        @Test
        fun `when currentPosition does not contain the title should handle position selection and update pending filter and state`() = runTest {
            val initialPositions = listOf("Point Guard", "Shooting Guard")
            val pendingFilter = PlayerFilter(
                hasShotsLogged = HasShotsLoggedFilter.Both,
                selectedPositions = initialPositions
            )
            val players = listOf(TestPlayer().create())

            coEvery { playersRepository.fetchAllPlayersWithFilter(filter = any()) } returns players

            viewModel.pendingFilter = pendingFilter
            viewModel.onPositionToggled(title = "Small Forward")

            val expectedPositions = listOf("Point Guard", "Shooting Guard", "Small Forward")
            Assertions.assertEquals(expectedPositions, viewModel.pendingFilter.selectedPositions)
            Assertions.assertEquals(expectedPositions, viewModel.playerFiltersMutableStateFlow.value.selectedPositions)
            coVerify { playersRepository.fetchAllPlayersWithFilter(filter = any()) }
        }
    }

    @Nested
    inner class HandlePositionDeselection {

        @Test
        fun `when isSelectedAllChoosePositionsOption is true should clear all currentPositions`() {
            val currentPositions = mutableListOf("Point Guard", "Shooting Guard", "Small Forward", "All")

            viewModel.handlePositionDeselection(title = "All", currentPositions = currentPositions)

            Assertions.assertTrue(currentPositions.isEmpty())
        }

        @Test
        fun `when isSelectedAllChoosePositionsOption is false and current position does not contain all should remove the passed in title from currentPositions`() {
            val currentPositions = mutableListOf("Point Guard", "Shooting Guard", "Small Forward")

            viewModel.handlePositionDeselection(title = "Point Guard", currentPositions = currentPositions)

            Assertions.assertEquals(listOf("Shooting Guard", "Small Forward"), currentPositions)
        }

        @Test
        fun `when isSelectedAllChoosePositionsOption is false and current position contains all should remove the passed in title and all for currentPositions`() {
            val currentPositions = mutableListOf("Point Guard", "Shooting Guard", "Small Forward", "All")

            viewModel.handlePositionDeselection(title = "Point Guard", currentPositions = currentPositions)

            Assertions.assertEquals(listOf("Shooting Guard", "Small Forward"), currentPositions)
        }
    }

    @Nested
    inner class HandlePositionSelection {

        @Test
        fun `when isSelectedAllChoosePositionsOption is true should clear and add all default positions`() {
            val currentPositions = mutableListOf("Point Guard", "Shooting Guard")

            viewModel.handlePositionSelection(title = "All", currentPositions = currentPositions)

            Assertions.assertEquals(
                listOf("Point Guard", "Shooting Guard", "Small Forward", "Power Forward", "Center", "All"),
                currentPositions
            )
        }

        @Test
        fun `when isSelectedAllChoosePositionsOption is false and shouldAddAllPosition returns true should add title and all`() {
            val currentPositions = mutableListOf(
                "Point Guard",
                "Shooting Guard",
                "Small Forward",
                "Power Forward"
            )

            viewModel.handlePositionSelection(title = "Center", currentPositions = currentPositions)

            Assertions.assertEquals(
                listOf(
                    "Point Guard",
                    "Shooting Guard",
                    "Small Forward",
                    "Power Forward",
                    "Center",
                    "All"
                ),
                currentPositions
            )
        }

        @Test
        fun `when isSelectedAllChoosePositionsOption is false and shouldAddAllPosition returns false should add title but not all`() {
            val currentPositions = mutableListOf("Point Guard", "Shooting Guard")

            viewModel.handlePositionSelection(title = "Small Forward", currentPositions = currentPositions)

            Assertions.assertEquals(
                listOf("Point Guard", "Shooting Guard", "Small Forward"),
                currentPositions
            )
        }
    }

    @Nested
    inner class OnShotLogsToggled {

        @Test
        fun `when new filter is none should update pendingFilter and state`() = runTest {
            val pendingFilter = PlayerFilter(
                hasShotsLogged = HasShotsLoggedFilter.HasShots,
                selectedPositions = emptyList()
            )
            val players = listOf(TestPlayer().create())

            every { application.getString(StringsIds.hasShots) } returns "Has Shots"
            every { application.getString(StringsIds.noShots) } returns "No Shots"
            every { application.getString(StringsIds.both) } returns "Both"
            coEvery { playersRepository.fetchAllPlayersWithFilter(filter = any()) } returns players

            viewModel.pendingFilter = pendingFilter
            viewModel.onShotLogsToggled(title = "Unknown Title")

            Assertions.assertNull(viewModel.pendingFilter.hasShotsLogged)
            Assertions.assertEquals(listOf("Both"), viewModel.playerFiltersMutableStateFlow.value.selectedShotLogsOptions)
            coVerify { playersRepository.fetchAllPlayersWithFilter(filter = any()) }
        }

        @Test
        fun `when new filter is not none should update pendingFilter and state`() = runTest {
            val pendingFilter = PlayerFilter(
                hasShotsLogged = HasShotsLoggedFilter.Both,
                selectedPositions = emptyList()
            )
            val players = listOf(TestPlayer().create())

            every { application.getString(StringsIds.hasShots) } returns "Has Shots"
            every { application.getString(StringsIds.noShots) } returns "No Shots"
            every { application.getString(StringsIds.both) } returns "Both"
            coEvery { playersRepository.fetchAllPlayersWithFilter(filter = any()) } returns players

            viewModel.pendingFilter = pendingFilter
            viewModel.onShotLogsToggled(title = "No Shots")

            Assertions.assertEquals(HasShotsLoggedFilter.NoShots, viewModel.pendingFilter.hasShotsLogged)
            Assertions.assertEquals(listOf("No Shots"), viewModel.playerFiltersMutableStateFlow.value.selectedShotLogsOptions)
            coVerify { playersRepository.fetchAllPlayersWithFilter(filter = any()) }
        }
    }

    @Nested
    inner class OnMinShotsChanged {

        @Test
        fun `when minShotsValue is set to null should update state`() = runTest {
            val pendingFilter = PlayerFilter(
                hasShotsLogged = HasShotsLoggedFilter.Both,
                minShots = 5,
                maxShots = 10
            )
            val players = listOf(TestPlayer().create())

            coEvery { playersRepository.fetchAllPlayersWithFilter(filter = any()) } returns players

            viewModel.pendingFilter = pendingFilter
            viewModel.onMinShotsChanged(value = 0)

            Assertions.assertNull(viewModel.pendingFilter.minShots)
            Assertions.assertEquals(10, viewModel.pendingFilter.maxShots)
            Assertions.assertNull(viewModel.playerFiltersMutableStateFlow.value.minShots)
            Assertions.assertEquals(10, viewModel.playerFiltersMutableStateFlow.value.maxShots)
            coVerify { playersRepository.fetchAllPlayersWithFilter(filter = any()) }
        }

        @Test
        fun `when currentMaxShots is set to null should update state`() = runTest {
            val pendingFilter = PlayerFilter(
                hasShotsLogged = HasShotsLoggedFilter.Both,
                minShots = null,
                maxShots = null
            )
            val players = listOf(TestPlayer().create())

            coEvery { playersRepository.fetchAllPlayersWithFilter(filter = any()) } returns players

            viewModel.pendingFilter = pendingFilter
            viewModel.onMinShotsChanged(value = 5)

            Assertions.assertEquals(5, viewModel.pendingFilter.minShots)
            Assertions.assertEquals(6, viewModel.pendingFilter.maxShots)
            Assertions.assertEquals(5, viewModel.playerFiltersMutableStateFlow.value.minShots)
            Assertions.assertEquals(6, viewModel.playerFiltersMutableStateFlow.value.maxShots)
            coVerify { playersRepository.fetchAllPlayersWithFilter(filter = any()) }
        }

        @Test
        fun `when minShotsValue is larger then currentMaxShots should update state`() = runTest {
            val pendingFilter = PlayerFilter(
                hasShotsLogged = HasShotsLoggedFilter.Both,
                minShots = 3,
                maxShots = 5
            )
            val players = listOf(TestPlayer().create())

            coEvery { playersRepository.fetchAllPlayersWithFilter(filter = any()) } returns players

            viewModel.pendingFilter = pendingFilter
            viewModel.onMinShotsChanged(value = 7)

            Assertions.assertEquals(7, viewModel.pendingFilter.minShots)
            Assertions.assertEquals(8, viewModel.pendingFilter.maxShots)
            Assertions.assertEquals(7, viewModel.playerFiltersMutableStateFlow.value.minShots)
            Assertions.assertEquals(8, viewModel.playerFiltersMutableStateFlow.value.maxShots)
            coVerify { playersRepository.fetchAllPlayersWithFilter(filter = any()) }
        }

        @Test
        fun `when minShotsValue is smaller then currentMaxShots should update state`() = runTest {
            val pendingFilter = PlayerFilter(
                hasShotsLogged = HasShotsLoggedFilter.Both,
                minShots = 5,
                maxShots = 10
            )
            val players = listOf(TestPlayer().create())

            coEvery { playersRepository.fetchAllPlayersWithFilter(filter = any()) } returns players

            viewModel.pendingFilter = pendingFilter
            viewModel.onMinShotsChanged(value = 3)

            Assertions.assertEquals(3, viewModel.pendingFilter.minShots)
            Assertions.assertEquals(10, viewModel.pendingFilter.maxShots)
            Assertions.assertEquals(3, viewModel.playerFiltersMutableStateFlow.value.minShots)
            Assertions.assertEquals(10, viewModel.playerFiltersMutableStateFlow.value.maxShots)
            coVerify { playersRepository.fetchAllPlayersWithFilter(filter = any()) }
        }
    }

    @Nested
    inner class OnMaxShotsChanged {

        @Test
        fun `when maxShotsValue is set to null should update state`() = runTest {
            val pendingFilter = PlayerFilter(
                hasShotsLogged = HasShotsLoggedFilter.Both,
                minShots = 5,
                maxShots = 10
            )
            val players = listOf(TestPlayer().create())

            coEvery { playersRepository.fetchAllPlayersWithFilter(filter = any()) } returns players

            viewModel.pendingFilter = pendingFilter
            viewModel.onMaxShotsChanged(value = 0)

            Assertions.assertEquals(5, viewModel.pendingFilter.minShots)
            Assertions.assertNull(viewModel.pendingFilter.maxShots)
            Assertions.assertEquals(5, viewModel.playerFiltersMutableStateFlow.value.minShots)
            Assertions.assertNull(viewModel.playerFiltersMutableStateFlow.value.maxShots)
            coVerify { playersRepository.fetchAllPlayersWithFilter(filter = any()) }
        }

        @Test
        fun `when currentMinShots is set to null should update state`() = runTest {
            val pendingFilter = PlayerFilter(
                hasShotsLogged = HasShotsLoggedFilter.Both,
                minShots = null,
                maxShots = null
            )
            val players = listOf(TestPlayer().create())

            coEvery { playersRepository.fetchAllPlayersWithFilter(filter = any()) } returns players

            viewModel.pendingFilter = pendingFilter
            viewModel.onMaxShotsChanged(value = 10)

            Assertions.assertNull(viewModel.pendingFilter.minShots)
            Assertions.assertEquals(10, viewModel.pendingFilter.maxShots)
            Assertions.assertNull(viewModel.playerFiltersMutableStateFlow.value.minShots)
            Assertions.assertEquals(10, viewModel.playerFiltersMutableStateFlow.value.maxShots)
            coVerify { playersRepository.fetchAllPlayersWithFilter(filter = any()) }
        }

        @Test
        fun `when maxShotsValue is smaller then currentMinShots should update state`() = runTest {
            val pendingFilter = PlayerFilter(
                hasShotsLogged = HasShotsLoggedFilter.Both,
                minShots = 5,
                maxShots = 10
            )
            val players = listOf(TestPlayer().create())

            coEvery { playersRepository.fetchAllPlayersWithFilter(filter = any()) } returns players

            viewModel.pendingFilter = pendingFilter
            viewModel.onMaxShotsChanged(value = 3)

            Assertions.assertEquals(5, viewModel.pendingFilter.minShots)
            Assertions.assertEquals(6, viewModel.pendingFilter.maxShots)
            Assertions.assertEquals(5, viewModel.playerFiltersMutableStateFlow.value.minShots)
            Assertions.assertEquals(6, viewModel.playerFiltersMutableStateFlow.value.maxShots)
            coVerify { playersRepository.fetchAllPlayersWithFilter(filter = any()) }
        }

        @Test
        fun `when maxShotsValue equals currentMinShots should update state`() = runTest {
            val pendingFilter = PlayerFilter(
                hasShotsLogged = HasShotsLoggedFilter.Both,
                minShots = 5,
                maxShots = 10
            )
            val players = listOf(TestPlayer().create())

            coEvery { playersRepository.fetchAllPlayersWithFilter(filter = any()) } returns players

            viewModel.pendingFilter = pendingFilter
            viewModel.onMaxShotsChanged(value = 5)

            Assertions.assertEquals(5, viewModel.pendingFilter.minShots)
            Assertions.assertEquals(6, viewModel.pendingFilter.maxShots)
            Assertions.assertEquals(5, viewModel.playerFiltersMutableStateFlow.value.minShots)
            Assertions.assertEquals(6, viewModel.playerFiltersMutableStateFlow.value.maxShots)
            coVerify { playersRepository.fetchAllPlayersWithFilter(filter = any()) }
        }

        @Test
        fun `when maxShotsValue is larger then currentMinShots should update state`() = runTest {
            val pendingFilter = PlayerFilter(
                hasShotsLogged = HasShotsLoggedFilter.Both,
                minShots = 5,
                maxShots = 10
            )
            val players = listOf(TestPlayer().create())

            coEvery { playersRepository.fetchAllPlayersWithFilter(filter = any()) } returns players

            viewModel.pendingFilter = pendingFilter
            viewModel.onMaxShotsChanged(value = 15)

            Assertions.assertEquals(5, viewModel.pendingFilter.minShots)
            Assertions.assertEquals(15, viewModel.pendingFilter.maxShots)
            Assertions.assertEquals(5, viewModel.playerFiltersMutableStateFlow.value.minShots)
            Assertions.assertEquals(15, viewModel.playerFiltersMutableStateFlow.value.maxShots)
            coVerify { playersRepository.fetchAllPlayersWithFilter(filter = any()) }
        }
    }

    @Test
    fun `onSeeResultsClicked should save filter update initial filter update state and navigate`() = runTest {
        val pendingFilter = PlayerFilter(
            hasShotsLogged = HasShotsLoggedFilter.HasShots,
            minShots = 5,
            maxShots = 10,
            selectedPositions = listOf("Point Guard", "Shooting Guard"),
            lastUpdatedValue = "Old Date"
        )
        val players = listOf(TestPlayer().create())
        val timestamp = 1000000L
        val expectedDateString = "Dec 31, 1969 06:16 PM"

        every { dateExt.now } returns timestamp
        coEvery { playersFilterRepository.saveActiveFilter(filter = any()) } returns Unit
        coEvery { playersRepository.fetchAllPlayersWithFilter(filter = any()) } returns players

        viewModel.pendingFilter = pendingFilter
        viewModel.initialFilter = PlayerFilter(hasShotsLogged = HasShotsLoggedFilter.Both)
        viewModel.onSeeResultsClicked()
        advanceUntilIdle()

        val savedFilter = viewModel.pendingFilter.copy(lastUpdatedValue = expectedDateString)
        Assertions.assertEquals(expectedDateString, viewModel.pendingFilter.lastUpdatedValue)
        Assertions.assertEquals(savedFilter, viewModel.initialFilter)
        Assertions.assertEquals(savedFilter, viewModel.pendingFilter)
        Assertions.assertEquals(savedFilter.selectedPositions, viewModel.playerFiltersMutableStateFlow.value.selectedPositions)

        verify { navigation.enableProgress(progress = any<Progress>()) }
        coVerify { playersFilterRepository.saveActiveFilter(filter = savedFilter) }
        coVerify { playersRepository.fetchAllPlayersWithFilter(filter = any()) }
        verify { navigation.pop() }
        verify { navigation.disableProgress() }
    }
}
