package com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots.createeditdeclaredshot

import android.app.Application
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.data.room.repository.DeclaredShotRepository
import com.nicholas.rutherford.track.your.shot.data.test.room.TestDeclaredShot
import com.nicholas.rutherford.track.your.shot.shared.preference.create.CreateSharedPreferences
import com.nicholas.rutherford.track.your.shot.shared.preference.read.ReadSharedPreferences
import io.mockk.coEvery
import io.mockk.every
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

class CreateEditDeclaredShotViewModelTest {

    private lateinit var viewModel: CreateEditDeclaredShotViewModel

    private var application = mockk<Application>(relaxed = true)

    private val declaredShotRepository = mockk<DeclaredShotRepository>(relaxed = true)

    private val createSharedPreferences = mockk<CreateSharedPreferences>(relaxed = true)
    private val readSharedPreferences = mockk<ReadSharedPreferences>(relaxed = true)

    private val navigation = mockk<CreateEditDeclaredShotNavigation>(relaxed = true)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    private val scope = CoroutineScope(SupervisorJob() + testDispatcher)

    private val state = CreateEditDeclaredShotState()

    @BeforeEach
    fun beforeEach() {
        viewModel = CreateEditDeclaredShotViewModel(
            application = application,
            declaredShotRepository = declaredShotRepository,
            createSharedPreferences = createSharedPreferences,
            readSharedPreferences = readSharedPreferences,
            navigation = navigation,
            scope = scope
        )
    }

    @Nested
    inner class OnNavigateTo {

        @Test
        fun `when declaredShotId returns back 0 should update state`() {
            val toolbarTitle = "Create Shot"

            every { application.getString(StringsIds.createShot) } returns toolbarTitle
            every { readSharedPreferences.declaredShotId() } returns 0

            viewModel.onNavigatedTo()

            Assertions.assertEquals(
                viewModel.createEditDeclaredShotMutableStateFlow.value,
                state.copy(
                    declaredShotState = DeclaredShotState.CREATING,
                    toolbarTitle = toolbarTitle
                )
            )
        }

        @Test
        fun `when declaredShotId is not set to 0 and fetch declared shot from id returns null should not update state`() = runTest {
            val id = 4

            every { readSharedPreferences.declaredShotId() } returns id
            coEvery { declaredShotRepository.fetchDeclaredShotFromId(id = id) } returns null

            viewModel.onNavigatedTo()

            Assertions.assertEquals(viewModel.currentDeclaredShot, null)
            Assertions.assertEquals(viewModel.createEditDeclaredShotMutableStateFlow.value, state)
            verify { createSharedPreferences.createDeclaredShotId(value = 0) }
        }

        @Test
        fun `when declaredShotId is not set to 0 and fetch declared shot from id returns value should update state`() = runTest {
            val id = 4
            val declaredShot = TestDeclaredShot.build()
            val viewShotName = "View ${declaredShot.title}"

            every { application.getString(StringsIds.viewX, declaredShot.title) } returns viewShotName
            every { readSharedPreferences.declaredShotId() } returns id
            coEvery { declaredShotRepository.fetchDeclaredShotFromId(id = id) } returns declaredShot

            viewModel.onNavigatedTo()

            Assertions.assertEquals(viewModel.currentDeclaredShot, declaredShot)
            Assertions.assertEquals(
                viewModel.createEditDeclaredShotMutableStateFlow.value,
                state.copy(
                    currentDeclaredShot = declaredShot,
                    declaredShotState = DeclaredShotState.VIEWING,
                    toolbarTitle = viewShotName
                )
            )
            verify { createSharedPreferences.createDeclaredShotId(value = 0) }
        }
    }

    @Test
    fun `on toolbar menu clicked`() {
        viewModel.onToolbarMenuClicked()

        verify { navigation.pop() }
    }
}
