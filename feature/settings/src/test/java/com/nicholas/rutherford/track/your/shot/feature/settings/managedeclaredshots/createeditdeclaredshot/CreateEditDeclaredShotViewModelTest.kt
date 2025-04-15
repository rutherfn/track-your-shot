package com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots.createeditdeclaredshot

import android.app.Application
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.data.room.entities.toShotIgnoring
import com.nicholas.rutherford.track.your.shot.data.room.repository.DeclaredShotRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.ShotIgnoringRepository
import com.nicholas.rutherford.track.your.shot.data.test.room.TestDeclaredShot
import com.nicholas.rutherford.track.your.shot.data.test.room.TestShotIgnoringEntity
import com.nicholas.rutherford.track.your.shot.firebase.core.create.CreateFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.shared.preference.create.CreateSharedPreferences
import com.nicholas.rutherford.track.your.shot.shared.preference.read.ReadSharedPreferences
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
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
    private val shotIgnoringRepository = mockk<ShotIgnoringRepository>(relaxed = true)

    private val createFirebaseUserInfo = mockk<CreateFirebaseUserInfo>(relaxed = true)

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
            shotIgnoringRepository = shotIgnoringRepository,
            createFirebaseUserInfo = createFirebaseUserInfo,
            createSharedPreferences = createSharedPreferences,
            readSharedPreferences = readSharedPreferences,
            navigation = navigation,
            scope = scope
        )
    }

    @Test
    fun constants() {
        Assertions.assertEquals(DEFAULT_ID, 0)
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

    @Nested
    inner class AttemptToUpdateDeclaredShotState {

        @Test
        fun `when fetch declared shot from id returns null should not update state`() {
            val id = 2

            coEvery { declaredShotRepository.fetchDeclaredShotFromId(id = id) } returns null

            viewModel.attemptToUpdateDeclaredShotState(id = id)

            Assertions.assertEquals(viewModel.currentDeclaredShot, null)
            Assertions.assertEquals(viewModel.createEditDeclaredShotMutableStateFlow.value, state)
            verify { createSharedPreferences.createDeclaredShotId(value = 0) }
        }

        @Test
        fun `when fetch declared shot from id returns declared shot should update state`() {
            val id = 2
            val declaredShot = TestDeclaredShot.build()
            val viewShotName = "View ${declaredShot.title}"

            every { application.getString(StringsIds.viewX, declaredShot.title) } returns viewShotName
            coEvery { declaredShotRepository.fetchDeclaredShotFromId(id = id) } returns declaredShot

            viewModel.attemptToUpdateDeclaredShotState(id = id)

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

    @Nested
    inner class OnYesDeleteShot {
        private val shotName = "shotName"
        private val id = 1

        @Test
        fun `when attemptToCreateDefaultShotIdsToIgnoreFirebaseRealTimeDatabaseResponseFlow returns the first value as false should disable progress and show alert`() = runTest {
            val currentIdsToIgnore = listOf(22, 44, 1)

            coEvery { shotIgnoringRepository.fetchAllIgnoringShots() } returns listOf(TestShotIgnoringEntity.build().toShotIgnoring(), TestShotIgnoringEntity.build().toShotIgnoring().copy(shotId = 44))
            coEvery { createFirebaseUserInfo.attemptToCreateDefaultShotIdsToIgnoreFirebaseRealTimeDatabaseResponseFlow(defaultShotIdsToIgnore = currentIdsToIgnore) } returns flowOf(Pair(false, currentIdsToIgnore))

            viewModel.onYesDeleteShot(shotName = shotName, id = id)

            coVerify(exactly = 0) { declaredShotRepository.deleteShotById(id = id) }
            coVerify(exactly = 0) { shotIgnoringRepository.createShotIgnoring(shotId = id) }
            coVerify(exactly = 0) { navigation.pop() }
            verify { navigation.disableProgress() }
            verify { navigation.alert(alert = any()) }
        }

        @Test
        fun `when attemptToCreateDefaultShotIdsToIgnoreFirebaseRealTimeDatabaseResponseFlow returns the first value as true should pop and disable progress`() = runTest {
            val currentIdsToIgnore = listOf(22, 44, 1)

            coEvery { shotIgnoringRepository.fetchAllIgnoringShots() } returns listOf(TestShotIgnoringEntity.build().toShotIgnoring(), TestShotIgnoringEntity.build().toShotIgnoring().copy(shotId = 44))
            coEvery { createFirebaseUserInfo.attemptToCreateDefaultShotIdsToIgnoreFirebaseRealTimeDatabaseResponseFlow(defaultShotIdsToIgnore = currentIdsToIgnore) } returns flowOf(Pair(true, currentIdsToIgnore))

            viewModel.onYesDeleteShot(shotName = shotName, id = id)

            verify(exactly = 0) { navigation.alert(alert = any()) }

            coVerify { declaredShotRepository.deleteShotById(id = id) }
            coVerify { shotIgnoringRepository.createShotIgnoring(shotId = id) }
            verify { navigation.pop() }
            verify { navigation.disableProgress() }
        }
    }

    @Test
    fun `build could not delete shot alert`() {
        val shotName = "shotName"

        every { application.getString(StringsIds.unableToDeleteShot) } returns "Unable To Delete Shot"
        every { application.getString(StringsIds.weCouldNotDeleteXShot, shotName) } returns "We could not delete $shotName. Please try again later."
        every { application.getString(StringsIds.gotIt) } returns "Got It"

        val alert = viewModel.buildCouldNotDeleteShotAlert(shotName = shotName)

        Assertions.assertEquals(alert.title, "Unable To Delete Shot")
        Assertions.assertEquals(alert.description, "We could not delete $shotName. Please try again later.")
        Assertions.assertEquals(alert.confirmButton!!.buttonText, "Got It")
    }

    @Test
    fun `build delete shot alert should return alert`() {
        val shotName = "shotName"
        val id = 22

        every { application.getString(StringsIds.no) } returns "No"
        every { application.getString(StringsIds.yes) } returns "Yes"
        every { application.getString(StringsIds.deleteShot) } returns "Delete Shot"
        every { application.getString(StringsIds.areYouSureYouWantToDeleteXShot, shotName) } returns "Are you sure you want to delete $shotName?"

        val alert = viewModel.buildDeleteShotAlert(shotName = shotName, id = id)

        Assertions.assertEquals(alert.title, "Delete Shot")
        Assertions.assertEquals(alert.description, "Are you sure you want to delete $shotName?")
        Assertions.assertEquals(alert.confirmButton!!.buttonText, "Yes")
        Assertions.assertEquals(alert.dismissButton!!.buttonText, "No")
    }

    @Test
    fun `build shot has been created alert should return alert`() {
        val shotName = "shotName"

        val shotTitle = "Shot $shotName has been created"
        val shotDescription = "The shot $shotName has been created. You can now select it when logging shots for a player."
        val gotIt = "Got It"

        every { application.getString(StringsIds.shotXHasBeenCreated, shotName) } returns shotTitle
        every { application.getString(StringsIds.shotXHasBeenCreatedDescription, shotName) } returns shotDescription
        every { application.getString(StringsIds.gotIt) } returns "Got It"

        val alert = viewModel.buildShotHasBeenCreatedAlert(shotName = shotName)

        Assertions.assertEquals(alert.title, shotTitle)
        Assertions.assertEquals(alert.description, shotDescription)
        Assertions.assertEquals(alert.confirmButton!!.buttonText, gotIt)
    }

    @Test
    fun `build shot has been edited alert should return alert`() {
        val shotName = "shotName"

        val shotTitle = "Shot $shotName has been edited"
        val shotDescription = "The shot $shotName has been edited. You can now select it when logging shots for a player."
        val gotIt = "Got It"

        every { application.getString(StringsIds.shotXHasBeenEdited, shotName) } returns shotTitle
        every { application.getString(StringsIds.shotXHasBeenEditedDescription, shotName) } returns shotDescription
        every { application.getString(StringsIds.gotIt) } returns gotIt

        val alert = viewModel.buildShotHasBeenEditedAlert(shotName = shotName)

        Assertions.assertEquals(alert.title, shotTitle)
        Assertions.assertEquals(alert.description, shotDescription)
        Assertions.assertEquals(alert.confirmButton!!.buttonText, gotIt)
    }

    @Test
    fun `build shot error alert`() {
        val shotName = "shotName"

        val shotTitle = "Shot $shotName Had Issues Saving Details"
        val shotDescription = "There was an issue saving the shot $shotName details. Please try again or contact support if the problem continues."
        val gotIt = "Got It"

        every { application.getString(StringsIds.shotXHadIssueSavingDetails, shotName) } returns shotTitle
        every { application.getString(StringsIds.shotXHadIssueSavingDetailsDescription, shotName) } returns shotDescription
        every { application.getString(StringsIds.gotIt) } returns gotIt

        val alert = viewModel.buildShotErrorAlert(shotName = shotName)

        Assertions.assertEquals(alert.title, shotTitle)
        Assertions.assertEquals(alert.description, shotDescription)
        Assertions.assertEquals(alert.confirmButton!!.buttonText, gotIt)
    }

    @Nested
    inner class BuildSubmitShotAlert {

        @Test
        fun `when hasShotBeenCreated is set to true should return buildShotHasBeenCreatedAlert`() {
            val shotName = "shotName"

            val shotTitle = "Shot $shotName has been created"
            val shotDescription = "The shot $shotName has been created. You can now select it when logging shots for a player."
            val gotIt = "Got It"

            every { application.getString(StringsIds.shotXHasBeenCreated, shotName) } returns shotTitle
            every { application.getString(StringsIds.shotXHasBeenCreatedDescription, shotName) } returns shotDescription
            every { application.getString(StringsIds.gotIt) } returns "Got It"

            val alert = viewModel.buildSubmitShotAlert(hasShotBeenCreated = true, shotName = shotName)

            Assertions.assertEquals(alert.title, shotTitle)
            Assertions.assertEquals(alert.description, shotDescription)
            Assertions.assertEquals(alert.confirmButton!!.buttonText, gotIt)
        }

        @Test
        fun `when hasShotBeenCreated is set to false should return buildShotHasBeenEditedAlert`() {
            val shotName = "shotName"

            val shotTitle = "Shot $shotName has been edited"
            val shotDescription = "The shot $shotName has been edited. You can now select it when logging shots for a player."
            val gotIt = "Got It"

            every { application.getString(StringsIds.shotXHasBeenEdited, shotName) } returns shotTitle
            every { application.getString(StringsIds.shotXHasBeenEditedDescription, shotName) } returns shotDescription
            every { application.getString(StringsIds.gotIt) } returns gotIt

            val alert = viewModel.buildSubmitShotAlert(hasShotBeenCreated = false, shotName = shotName)

            Assertions.assertEquals(alert.title, shotTitle)
            Assertions.assertEquals(alert.description, shotDescription)
            Assertions.assertEquals(alert.confirmButton!!.buttonText, gotIt)
        }
    }

    @Test
    fun `on delete shot clicked`() {
        val id = 22

        viewModel.onDeleteShotClicked(id = id)

        verify { navigation.alert(alert = any()) }
    }

    @Test
    fun `on edit pencil clicked`() {
        val declaredShot = TestDeclaredShot.build()
        val editShotValue = "Edit ${declaredShot.title}"

        every { application.getString(StringsIds.editX, declaredShot.title) } returns editShotValue

        viewModel.currentDeclaredShot = declaredShot  // <-- Important

        viewModel.onEditShotPencilClicked()

        Assertions.assertEquals(viewModel.currentDeclaredShot, declaredShot)
        Assertions.assertEquals(
            viewModel.createEditDeclaredShotMutableStateFlow.value,
            CreateEditDeclaredShotState(
                declaredShotState = DeclaredShotState.EDITING,
                toolbarTitle = editShotValue
            )
        )
    }

    @Nested
    inner class OnEditShotNameValueChanged {
        private val shotName = "shot"

        @Test
        fun `when newDeclaredShot is null should not update the new declared shot with passed in shot name`() {
            viewModel.newDeclaredShot = null

            viewModel.onEditShotNameValueChanged(shotName = shotName)

            Assertions.assertEquals(viewModel.newDeclaredShot, null)
        }

        @Test
        fun `when newDeclaredShot is not null should update new declared shot with passed in shot name`() {
            val declaredShot = TestDeclaredShot.build()

            viewModel.newDeclaredShot = declaredShot

            Assertions.assertEquals(viewModel.newDeclaredShot, declaredShot)

            viewModel.onEditShotNameValueChanged(shotName = shotName)

            Assertions.assertEquals(viewModel.newDeclaredShot, declaredShot.copy(title = shotName))
        }
    }

    @Nested
    inner class OnEditShotCategoryValueChanged {
        private val shotCategory = "category"

        @Test
        fun `when newDeclaredShot is null should not update the new declared shot with passed in shot category`() {
            viewModel.newDeclaredShot = null

            viewModel.onEditShotCategoryValueChanged(shotCategory = shotCategory)

            Assertions.assertEquals(viewModel.newDeclaredShot, null)
        }

        @Test
        fun `when newDeclaredShot is not null should update new declared shot with passed in shot category`() {
            val declaredShot = TestDeclaredShot.build()

            viewModel.newDeclaredShot = declaredShot

            Assertions.assertEquals(viewModel.newDeclaredShot, declaredShot)

            viewModel.onEditShotCategoryValueChanged(shotCategory = shotCategory)

            Assertions.assertEquals(viewModel.newDeclaredShot, declaredShot.copy(shotCategory = shotCategory))
        }
    }

    @Nested
    inner class OnEditShotDescriptionValueChanged {
        private val shotDescription = "description"

        @Test
        fun `when newDeclaredShot is null should not update the new declared shot with passed in shot description`() {
            viewModel.newDeclaredShot = null

            viewModel.onEditShotDescriptionValueChanged(description = shotDescription)

            Assertions.assertEquals(viewModel.newDeclaredShot, null)
        }

        @Test
        fun `when newDeclaredShot is not null should update new declared shot with passed in shot description`() {
            val declaredShot = TestDeclaredShot.build()

            viewModel.newDeclaredShot = declaredShot

            Assertions.assertEquals(viewModel.newDeclaredShot, declaredShot)

            viewModel.onEditShotDescriptionValueChanged(description = shotDescription)

            Assertions.assertEquals(viewModel.newDeclaredShot, declaredShot.copy(description = shotDescription))
        }
    }

    @Nested
    inner class OnEditOrCreateNewShot {
        private val declaredShot = TestDeclaredShot.build()

        @Test
        fun `when declaredShot state is editing and newDeclaredShot is null should show error alert`() = runTest {
            viewModel.newDeclaredShot = null
            viewModel.createEditDeclaredShotMutableStateFlow.update { state -> state.copy(declaredShotState = DeclaredShotState.EDITING) }

            viewModel.onEditOrCreateNewShot()

            verify { navigation.enableProgress(progress = any()) }
            verify { navigation.disableProgress() }
            verify { navigation.alert(alert = any()) }

            verify(exactly = 0) { navigation.pop() }
            coVerify(exactly = 0) { declaredShotRepository.deleteShotById(declaredShot.id) }
        }

        @Test
        fun `when declaredShot state is not editing and newDeclaredShot is null should show error alert`() = runTest {
            viewModel.newDeclaredShot = null
            viewModel.createEditDeclaredShotMutableStateFlow.update { state -> state.copy(declaredShotState = DeclaredShotState.CREATING) }

            viewModel.onEditOrCreateNewShot()

            verify { navigation.enableProgress(progress = any()) }
            verify { navigation.disableProgress() }
            verify { navigation.alert(alert = any()) }

            verify(exactly = 0) { navigation.pop() }
            coVerify(exactly = 0) { declaredShotRepository.deleteShotById(declaredShot.id) }
        }

        @Test
        fun `when declaredShot state is editing, newDeclaredShot is not null, and attemptToCreateDeclaredShotFirebaseRealtimeDatabaseResponseFlow returns false should show error alert`() = runTest {
            viewModel.newDeclaredShot = declaredShot
            viewModel.createEditDeclaredShotMutableStateFlow.update { state -> state.copy(declaredShotState = DeclaredShotState.EDITING) }

            coEvery { createFirebaseUserInfo.attemptToCreateDeclaredShotFirebaseRealtimeDatabaseResponseFlow(declaredShot = declaredShot) } returns flowOf(Pair(false, null))

            viewModel.onEditOrCreateNewShot()

            coVerify { declaredShotRepository.deleteShotById(declaredShot.id) }

            verify { navigation.enableProgress(progress = any()) }
            verify { navigation.disableProgress() }
            verify { navigation.alert(alert = any()) }

            verify(exactly = 0) { navigation.pop() }
        }

        @Test
        fun `when declaredShot state is not editing, newDeclaredShot is not null, and attemptToCreateDeclaredShotFirebaseRealtimeDatabaseResponseFlow returns false should show error alert`() = runTest {
            viewModel.newDeclaredShot = declaredShot
            viewModel.createEditDeclaredShotMutableStateFlow.update { state -> state.copy(declaredShotState = DeclaredShotState.CREATING) }

            coEvery { createFirebaseUserInfo.attemptToCreateDeclaredShotFirebaseRealtimeDatabaseResponseFlow(declaredShot = declaredShot) } returns flowOf(Pair(false, null))

            viewModel.onEditOrCreateNewShot()

            verify { navigation.enableProgress(progress = any()) }
            verify { navigation.disableProgress() }
            verify { navigation.alert(alert = any()) }

            coVerify(exactly = 0) { declaredShotRepository.deleteShotById(declaredShot.id) }
            verify(exactly = 0) { navigation.pop() }
        }

        @Test
        fun `when declaredShot state is editing, newDeclaredShot is not null, and attemptToCreateDeclaredShotFirebaseRealtimeDatabaseResponseFlow returns true should pop and show alert`() = runTest {
            viewModel.newDeclaredShot = declaredShot
            viewModel.createEditDeclaredShotMutableStateFlow.update { state -> state.copy(declaredShotState = DeclaredShotState.EDITING) }

            coEvery { createFirebaseUserInfo.attemptToCreateDeclaredShotFirebaseRealtimeDatabaseResponseFlow(declaredShot = declaredShot) } returns flowOf(Pair(true, declaredShot))

            viewModel.onEditOrCreateNewShot()

            verify { navigation.enableProgress(progress = any()) }
            verify { navigation.disableProgress() }
            verify { navigation.pop() }
            verify { navigation.alert(alert = any()) }

            coVerify { declaredShotRepository.deleteShotById(declaredShot.id) }
        }

        @Test
        fun `when declaredShot state is creating, newDeclaredShot is not null, and attemptToCreateDeclaredShotFirebaseRealtimeDatabaseResponseFlow returns true should pop and show alert`() = runTest {
            viewModel.newDeclaredShot = declaredShot
            viewModel.createEditDeclaredShotMutableStateFlow.update { state -> state.copy(declaredShotState = DeclaredShotState.CREATING) }

            coEvery { createFirebaseUserInfo.attemptToCreateDeclaredShotFirebaseRealtimeDatabaseResponseFlow(declaredShot = declaredShot) } returns flowOf(Pair(true, declaredShot))

            viewModel.onEditOrCreateNewShot()

            verify { navigation.enableProgress(progress = any()) }
            verify { navigation.disableProgress() }
            verify { navigation.pop() }
            verify { navigation.alert(alert = any()) }

            coVerify(exactly = 0) { declaredShotRepository.deleteShotById(declaredShot.id) }
        }
    }

}
