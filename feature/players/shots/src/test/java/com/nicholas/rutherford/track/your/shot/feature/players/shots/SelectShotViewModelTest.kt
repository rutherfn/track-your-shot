package com.nicholas.rutherford.track.your.shot.feature.players.shots

import com.nicholas.rutherford.track.your.shot.data.room.repository.DeclaredShotRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.DeclaredShot
import com.nicholas.rutherford.track.your.shot.data.test.room.TestDeclaredShot
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
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

    @BeforeEach
    fun beforeEach() {
        selectShotViewModel = SelectShotViewModel(
            scope = scope,
            navigation = navigation,
            declaredShotRepository = declaredShotRepository
        )
    }

    @Test
    fun `update declared shot list state should update state property`() {
        val shotDeclaredList = listOf(TestDeclaredShot.build())

        coEvery { declaredShotRepository.fetchAllDeclaredShots() } returns shotDeclaredList

        selectShotViewModel.updateDeclaredShotListState()

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
        fun `when search query is not empty should update state`()  {
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
        fun `when fromCreatePlayer set to true should call pop from create player`() {
            selectShotViewModel.onBackButtonClicked(fromCreatePlayer = true)

            verify { navigation.popFromCreatePlayer() }
        }

        @Test
        fun `when fromCreatePlayer set to false should call pop from edit player`() {
            selectShotViewModel.onBackButtonClicked(fromCreatePlayer = false)

            verify { navigation.popFromEditPlayer() }
        }
    }
}
