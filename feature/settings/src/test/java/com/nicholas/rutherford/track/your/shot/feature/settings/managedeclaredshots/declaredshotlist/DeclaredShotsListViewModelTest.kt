package com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots.declaredshotlist

import com.nicholas.rutherford.track.your.shot.data.room.repository.DeclaredShotRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.DeclaredShot
import com.nicholas.rutherford.track.your.shot.data.test.room.TestDeclaredShot
import com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots.declaredshotslist.DeclaredShotsListNavigation
import com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots.declaredshotslist.DeclaredShotsListState
import com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots.declaredshotslist.DeclaredShotsListViewModel
import com.nicholas.rutherford.track.your.shot.shared.preference.create.CreateSharedPreferences
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
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

    private val createSharedPreferences = mockk<CreateSharedPreferences>(relaxed = true)
    private val navigation = mockk<DeclaredShotsListNavigation>(relaxed = true)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val dispatcher = UnconfinedTestDispatcher()

    private val scope = CoroutineScope(SupervisorJob() + dispatcher)

    val state = DeclaredShotsListState()

    @BeforeEach
    fun beforeEach() {
        viewModel = DeclaredShotsListViewModel(
            declaredShotRepository = declaredShotRepository,
            createSharedPreferences = createSharedPreferences,
            navigation = navigation,
            scope = scope
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
            Assertions.assertEquals(viewModel.declaredShotsListMutableStateFlow.value, state)
        }

        @Test
        fun `when fetchAllDeclaredShots returns list should update value and state`() = runTest {
            val declaredShotArrayList = listOf(TestDeclaredShot.build())

            coEvery { declaredShotRepository.fetchAllDeclaredShots() } returns declaredShotArrayList

            viewModel.onNavigatedTo()

            Assertions.assertEquals(viewModel.currentDeclaredShotArrayList, declaredShotArrayList)
            Assertions.assertEquals(viewModel.declaredShotsListMutableStateFlow.value, state.copy(declaredShotsList = declaredShotArrayList.toList()))
        }
    }

    @Test
    fun `on toolbar menu clicked should call pop`() {
        viewModel.onToolbarMenuClicked()

        verify { navigation.pop() }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `on declared shot clicked should create declared shot id and navigate to create edit declared shot`() = runTest {
        val id = 2

        viewModel.onDeclaredShotClicked(id = id)

        verify { createSharedPreferences.createDeclaredShotId(value = id) }
        dispatcher.scheduler.apply { advanceTimeBy(9000); runCurrent() }
        verify { navigation.createEditDeclaredShot() }
    }

    @Test
    fun `on add declared shot clicked should navigate to create edit declared shot`() {
        viewModel.onAddDeclaredShotClicked()

        verify { navigation.createEditDeclaredShot() }
    }
}
