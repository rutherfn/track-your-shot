package com.nicholas.rutherford.track.your.shot.feature.players.shots.selectshot

import com.nicholas.rutherford.track.your.shot.data.room.repository.DeclaredShotRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.DeclaredShot
import com.nicholas.rutherford.track.your.shot.data.test.room.TestDeclaredShot
import com.nicholas.rutherford.track.your.shot.helper.account.AccountManager
import com.nicholas.rutherford.track.your.shot.shared.preference.create.CreateSharedPreferences
import com.nicholas.rutherford.track.your.shot.shared.preference.read.ReadSharedPreferences
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SelectShotViewModelTest {

    private lateinit var selectShotViewModel: SelectShotViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    private val scope = CoroutineScope(SupervisorJob() + testDispatcher)

    private val navigation = mockk<SelectShotNavigation>(relaxed = true)

    private val declaredShotRepository = mockk<DeclaredShotRepository>(relaxed = true)

    private val accountManager = mockk<AccountManager>(relaxed = true)

    private val createSharedPreferences = mockk<CreateSharedPreferences>(relaxed = true)
    private val readSharedPreferences = mockk<ReadSharedPreferences>(relaxed = true)

    @BeforeEach
    fun beforeEach() {
        selectShotViewModel = SelectShotViewModel(
            scope = scope,
            navigation = navigation,
            declaredShotRepository = declaredShotRepository,
            accountManager = accountManager,
            createSharedPreferences = createSharedPreferences,
            readSharedPreferences = readSharedPreferences
        )
    }

    @Test
    fun `update is existing player and player id should update internal fields`() {
        Assertions.assertEquals(selectShotViewModel.isExistingPlayer, null)
        Assertions.assertEquals(selectShotViewModel.playerId, null)

        selectShotViewModel.updateIsExistingPlayerAndPlayerId(
            isExistingPlayerArgument = true,
            playerIdArgument = 1
        )

        Assertions.assertEquals(selectShotViewModel.isExistingPlayer, true)
        Assertions.assertEquals(selectShotViewModel.playerId, 1)
    }

    @Test
    fun `fetch declared shots and update state state should update state property`() {
        val shotDeclaredList = listOf(TestDeclaredShot.build())

        coEvery { declaredShotRepository.fetchAllDeclaredShots() } returns shotDeclaredList

        selectShotViewModel.fetchDeclaredShotsAndUpdateState()

        Assertions.assertEquals(
            selectShotViewModel.selectShotMutableStateFlow.value,
            SelectShotState(declaredShotList = shotDeclaredList)
        )
        Assertions.assertEquals(
            selectShotViewModel.currentDeclaredShotArrayList.toList(),
            shotDeclaredList
        )
    }

    @Nested
    inner class CollectLoggedInDeclaredShotsStateFlow {

        @Test
        fun `when loggedInDeclaredShotListStateFloe returns a empty list should not update state`() {
            val declaredShotList: List<DeclaredShot> = emptyList()

            every { readSharedPreferences.shouldUpdateLoggedInDeclaredShotListState() } returns true
            every { accountManager.loggedInDeclaredShotListStateFlow } returns MutableStateFlow(declaredShotList)

            selectShotViewModel = SelectShotViewModel(
                scope = scope,
                navigation = navigation,
                declaredShotRepository = declaredShotRepository,
                accountManager = accountManager,
                createSharedPreferences = createSharedPreferences,
                readSharedPreferences = readSharedPreferences
            )

            Assertions.assertEquals(
                selectShotViewModel.selectShotMutableStateFlow.value,
                SelectShotState(declaredShotList = declaredShotList)
            )
            Assertions.assertEquals(
                selectShotViewModel.currentDeclaredShotArrayList.toList(),
                declaredShotList
            )
        }

        @Test
        fun `when shouldUpdateLoggedInDeclaredShotListState returns false should not update state`() {
            val emptyDeclaredShotList: List<DeclaredShot> = emptyList()
            val declaredShotList: List<DeclaredShot> = listOf(TestDeclaredShot.build())

            every { readSharedPreferences.shouldUpdateLoggedInDeclaredShotListState() } returns false
            every { accountManager.loggedInDeclaredShotListStateFlow } returns MutableStateFlow(declaredShotList)

            selectShotViewModel = SelectShotViewModel(
                scope = scope,
                navigation = navigation,
                declaredShotRepository = declaredShotRepository,
                accountManager = accountManager,
                createSharedPreferences = createSharedPreferences,
                readSharedPreferences = readSharedPreferences
            )

            Assertions.assertEquals(
                selectShotViewModel.selectShotMutableStateFlow.value,
                SelectShotState(declaredShotList = emptyDeclaredShotList)
            )
            Assertions.assertEquals(
                selectShotViewModel.currentDeclaredShotArrayList.toList(),
                emptyDeclaredShotList
            )
        }

        @Test
        fun `when all conditions are met should update state and set preference value back to false`() {
            val declaredShotList: List<DeclaredShot> = listOf(TestDeclaredShot.build())

            every { readSharedPreferences.shouldUpdateLoggedInDeclaredShotListState() } returns true
            every { accountManager.loggedInDeclaredShotListStateFlow } returns MutableStateFlow(declaredShotList)

            selectShotViewModel = SelectShotViewModel(
                scope = scope,
                navigation = navigation,
                declaredShotRepository = declaredShotRepository,
                accountManager = accountManager,
                createSharedPreferences = createSharedPreferences,
                readSharedPreferences = readSharedPreferences
            )

            Assertions.assertEquals(
                selectShotViewModel.selectShotMutableStateFlow.value,
                SelectShotState(declaredShotList = declaredShotList)
            )
            Assertions.assertEquals(
                selectShotViewModel.currentDeclaredShotArrayList.toList(),
                declaredShotList
            )

            verify(exactly = 1) { createSharedPreferences.createShouldUpdateLoggedInDeclaredShotListPreference(value = false) }
        }
    }

    @Nested
    inner class ShouldUpdateStateFromLoggedIn {

        @Test
        fun `should return false if declaredShotList is empty`() {
            val result = selectShotViewModel.shouldUpdateStateFromLoggedIn(
                declaredShotList = emptyList(),
                shouldUpdateLoggedInDeclaredShotListState = false
            )

            Assertions.assertEquals(result, false)
        }

        @Test
        fun `should return false if shouldUpdateLoggedInDeclaredShotListState is set to false`() {
            val declaredShotList: List<DeclaredShot> = listOf(TestDeclaredShot.build())

            val result = selectShotViewModel.shouldUpdateStateFromLoggedIn(
                declaredShotList = declaredShotList,
                shouldUpdateLoggedInDeclaredShotListState = false
            )

            Assertions.assertEquals(result, false)
        }

        @Test
        fun `should return true when all conditions are met`() {
            val declaredShotList: List<DeclaredShot> = listOf(TestDeclaredShot.build())

            val result = selectShotViewModel.shouldUpdateStateFromLoggedIn(
                declaredShotList = declaredShotList,
                shouldUpdateLoggedInDeclaredShotListState = true
            )

            Assertions.assertEquals(result, true)
        }
    }

    @Test
    fun `on search value changed should update state`() {
        val title = TestDeclaredShot.build().title
        val shotDeclaredList = listOf(TestDeclaredShot.build())

        coEvery { declaredShotRepository.fetchDeclaredShotsBySearchQuery(searchQuery = title) } returns shotDeclaredList

        selectShotViewModel.onSearchValueChanged(newSearchQuery = title)

        Assertions.assertEquals(
            selectShotViewModel.selectShotMutableStateFlow.value,
            SelectShotState(declaredShotList = shotDeclaredList, searchQuery = title)
        )
        Assertions.assertEquals(
            selectShotViewModel.currentDeclaredShotArrayList.toList(),
            shotDeclaredList
        )
    }

    @Nested
    inner class OnCancelIconClicked {

        @Test
        fun `when search query is not empty should update state`() {
            val searchQuery = "searchQuery"

            selectShotViewModel.selectShotMutableStateFlow.update {
                it.copy(searchQuery = searchQuery)
            }

            val shotDeclaredList = listOf(TestDeclaredShot.build())

            coEvery { declaredShotRepository.fetchAllDeclaredShots() } returns shotDeclaredList

            selectShotViewModel.onCancelIconClicked()

            Assertions.assertEquals(
                selectShotViewModel.selectShotMutableStateFlow.value,
                SelectShotState(declaredShotList = shotDeclaredList)
            )
            Assertions.assertEquals(
                selectShotViewModel.currentDeclaredShotArrayList.toList(),
                shotDeclaredList
            )
        }

        @Test
        fun `when search query is empty should not update state`() {
            val emptyShotDeclaredList: List<DeclaredShot> = emptyList()
            selectShotViewModel.selectShotMutableStateFlow.update {
                it.copy(searchQuery = "")
            }

            selectShotViewModel.onCancelIconClicked()

            Assertions.assertEquals(
                selectShotViewModel.selectShotMutableStateFlow.value,
                SelectShotState(declaredShotList = emptyShotDeclaredList)
            )
            Assertions.assertEquals(
                selectShotViewModel.currentDeclaredShotArrayList.toList(),
                emptyShotDeclaredList
            )
        }
    }

    @Nested
    inner class OnBackButtonClicked {

        @Test
        fun `when isExistingPlayer set to false should call pop from create player`() {
            selectShotViewModel.isExistingPlayer = false

            selectShotViewModel.onBackButtonClicked()

            verify { navigation.popFromCreatePlayer() }
        }

        @Test
        fun `when isExistingPlayer set to true should call pop from edit player`() {
            selectShotViewModel.isExistingPlayer = true

            selectShotViewModel.onBackButtonClicked()

            verify { navigation.popFromEditPlayer() }
        }
    }
}
