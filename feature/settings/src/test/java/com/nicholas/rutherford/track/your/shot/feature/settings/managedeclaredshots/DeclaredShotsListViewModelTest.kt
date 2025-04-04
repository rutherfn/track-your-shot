package com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots

import com.nicholas.rutherford.track.your.shot.data.room.repository.DeclaredShotRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.DeclaredShot
import com.nicholas.rutherford.track.your.shot.data.test.room.TestDeclaredShot
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class DeclaredShotsListViewModelTest {

    private lateinit var viewModel: DeclaredShotsListViewModel

    private val declaredShotRepository = mockk<DeclaredShotRepository>(relaxed = true)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val dispatcher = UnconfinedTestDispatcher()

    private val scope = CoroutineScope(SupervisorJob() + dispatcher)

    val vs = DeclaredShotsListState()

    @BeforeEach
    fun beforeEach() {
        viewModel = DeclaredShotsListViewModel(
            scope = scope,
            declaredShotRepository = declaredShotRepository
        )
    }

    @Nested
    inner class OnNavigateTo {

        @Test
        fun `when fetchAllDeclaredShots returns a empty list should not update value and state`() = runTest {
            val emptyDeclaredShotArrayList: ArrayList<DeclaredShot> = arrayListOf()

            coEvery { declaredShotRepository.fetchAllDeclaredShots() } returns emptyList()

            viewModel.onNavigatedTo()

            Assertions.assertEquals(viewModel.currentDeclaredShotArrayList, emptyDeclaredShotArrayList)
            Assertions.assertEquals(viewModel.declaredShotsListMutableStateFlow.value, DeclaredShotsListState())
        }

        @Test
        fun `when fetchAllDeclaredShots returns list should update value and state`() = runTest {
            val declaredShotArrayList = listOf(TestDeclaredShot.build())

            coEvery { declaredShotRepository.fetchAllDeclaredShots() } returns declaredShotArrayList

            viewModel.onNavigatedTo()

            Assertions.assertEquals(viewModel.currentDeclaredShotArrayList, declaredShotArrayList)
            Assertions.assertEquals(viewModel.declaredShotsListMutableStateFlow.value, DeclaredShotsListState(declaredShotsList = declaredShotArrayList.toList()))
        }
    }
}