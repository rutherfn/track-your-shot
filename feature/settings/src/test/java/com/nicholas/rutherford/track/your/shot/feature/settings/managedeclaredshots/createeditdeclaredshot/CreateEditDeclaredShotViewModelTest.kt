package com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots.createeditdeclaredshot

import android.app.Application
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.data.room.entities.toShotIgnoring
import com.nicholas.rutherford.track.your.shot.data.room.repository.DeclaredShotRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.ShotIgnoringRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.DeclaredShot
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

        @BeforeEach
        fun beforeEach() {
            coEvery { declaredShotRepository.fetchAllDeclaredShots() } returns listOf(TestDeclaredShot.build())
        }

        @Test
        fun `when declaredShotId returns back 0 should update state`() {
            val toolbarTitle = "Create Shot"

            every { application.getString(StringsIds.createShot) } returns toolbarTitle
            every { readSharedPreferences.declaredShotId() } returns 0

            viewModel.onNavigatedTo()

            Assertions.assertEquals(viewModel.allDeclaredShotNames, listOf("Hook Shot"))
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

            Assertions.assertEquals(viewModel.allDeclaredShotNames, listOf("Hook Shot"))
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

            Assertions.assertEquals(viewModel.allDeclaredShotNames, listOf("Hook Shot"))
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

    @Test
    fun `build shot name not added alert should return alert`() {
        val shotNameMissing = "Shot Name Missing"
        val shotNameMissingDescription = "Shot name is required and currently missing. Please enter a shot name to proceed with defining the shot details."
        val gotIt = "Got It"

        every { application.getString(StringsIds.shotNameMissing) } returns shotNameMissing
        every { application.getString(StringsIds.shotNameMissingDescription) } returns shotNameMissingDescription
        every { application.getString(StringsIds.gotIt) } returns gotIt

        val alert = viewModel.buildShotNameNotAddedAlert()

        Assertions.assertEquals(alert.title, shotNameMissing)
        Assertions.assertEquals(alert.description, shotNameMissingDescription)
        Assertions.assertEquals(alert.confirmButton!!.buttonText, gotIt)
    }

    @Test
    fun `build shot name already exists should return alert`() {
        val shotWithThatNameAlreadyExists = "Shot With That Name Already Exists"
        val shotWithThatNameAlreadyExistsDescription = "A shot with that name already exists. Please choose a different name to continue."
        val gotIt = "Got It"

        every { application.getString(StringsIds.shotWithThatNameAlreadyExists) } returns shotWithThatNameAlreadyExists
        every { application.getString(StringsIds.shotWithThatNameAlreadyExistsDescription) } returns  shotWithThatNameAlreadyExistsDescription
        every { application.getString(StringsIds.gotIt) } returns gotIt

        val alert = viewModel.buildShotNameAlreadyExistAlert()

        Assertions.assertEquals(alert.title, shotWithThatNameAlreadyExists)
        Assertions.assertEquals(alert.description,  shotWithThatNameAlreadyExistsDescription)
        Assertions.assertEquals(alert.confirmButton!!.buttonText, gotIt)
    }

    @Test
    fun `build shot category not added alert should return alert`() {
        val shotCategoryMissing = "Shot Category Missing"
        val shotCategoryMissingDescription = "Shot category is required and currently missing. Please enter a shot category to proceed with defining the shot details."
        val gotIt = "Got It"

        every { application.getString(StringsIds.shotCategoryMissing) } returns shotCategoryMissing
        every { application.getString(StringsIds.shotCategoryMissingDescription) } returns shotCategoryMissingDescription

        every { application.getString(StringsIds.gotIt) } returns gotIt

        val alert = viewModel.buildShotCategoryNotAddedAlert()

        Assertions.assertEquals(alert.title, shotCategoryMissing)
        Assertions.assertEquals(alert.description, shotCategoryMissingDescription)
        Assertions.assertEquals(alert.confirmButton!!.buttonText, gotIt)
    }

    @Test
    fun `build shot description not added alert should return alert`() {
        val shotDescriptionMissing = "Shot Description Missing"
        val shotDescriptionMissingDesc = "Shot description is required and currently missing. Please enter a shot description to proceed with defining the shot details."
        val gotIt = "Got It"

        every { application.getString(StringsIds.shotDescriptionMissing) } returns shotDescriptionMissing
        every { application.getString(StringsIds.shotDescriptionMissingDesc) } returns shotDescriptionMissingDesc

        every { application.getString(StringsIds.gotIt) } returns gotIt

        val alert = viewModel.buildShotDescriptionNotAddedAlert()

        Assertions.assertEquals(alert.title, shotDescriptionMissing)
        Assertions.assertEquals(alert.description, shotDescriptionMissingDesc)
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
        fun `when editedDeclaredShot is null should not update the new declared shot with passed in shot name`() {
            viewModel.editedDeclaredShot = null

            viewModel.onEditShotNameValueChanged(shotName = shotName)

            Assertions.assertEquals(viewModel.editedDeclaredShot, null)
        }

        @Test
        fun `when editedDeclaredShot is not null should update new declared shot with passed in shot name`() {
            val declaredShot = TestDeclaredShot.build()

            viewModel.editedDeclaredShot = declaredShot

            Assertions.assertEquals(viewModel.editedDeclaredShot, declaredShot)

            viewModel.onEditShotNameValueChanged(shotName = shotName)

            Assertions.assertEquals(viewModel.editedDeclaredShot, declaredShot.copy(title = shotName))
        }
    }

    @Nested
    inner class OnEditShotCategoryValueChanged {
        private val shotCategory = "category"

        @Test
        fun `when editedDeclaredShot is null should not update the new declared shot with passed in shot category`() {
            viewModel.editedDeclaredShot = null

            viewModel.onEditShotCategoryValueChanged(shotCategory = shotCategory)

            Assertions.assertEquals(viewModel.editedDeclaredShot, null)
        }

        @Test
        fun `when editedDeclaredShot is not null should update new declared shot with passed in shot category`() {
            val declaredShot = TestDeclaredShot.build()

            viewModel.editedDeclaredShot = declaredShot

            Assertions.assertEquals(viewModel.editedDeclaredShot, declaredShot)

            viewModel.onEditShotCategoryValueChanged(shotCategory = shotCategory)

            Assertions.assertEquals(viewModel.editedDeclaredShot, declaredShot.copy(shotCategory = shotCategory))
        }
    }

    @Nested
    inner class OnEditShotDescriptionValueChanged {
        private val shotDescription = "description"

        @Test
        fun `when editedDeclaredShot is null should not update the new declared shot with passed in shot description`() {
            viewModel.editedDeclaredShot = null

            viewModel.onEditShotDescriptionValueChanged(description = shotDescription)

            Assertions.assertEquals(viewModel.editedDeclaredShot, null)
        }

        @Test
        fun `when editedDeclaredShot is not null should update new declared shot with passed in shot description`() {
            val declaredShot = TestDeclaredShot.build()

            viewModel.editedDeclaredShot = declaredShot

            Assertions.assertEquals(viewModel.editedDeclaredShot, declaredShot)

            viewModel.onEditShotDescriptionValueChanged(description = shotDescription)

            Assertions.assertEquals(viewModel.editedDeclaredShot, declaredShot.copy(description = shotDescription))
        }
    }

    @Test
    fun `on create shot name value changed`() {
        val shotName = "shotName"

        Assertions.assertEquals(viewModel.createdShotInfo, CreateShotInfo())

        viewModel.onCreateShotNameValueChanged(shotName = shotName)

        Assertions.assertEquals(viewModel.createdShotInfo, CreateShotInfo(name = shotName))
    }

    @Test
    fun `on create shot description value changed`() {
        val shotDescription = "desc"

        Assertions.assertEquals(viewModel.createdShotInfo, CreateShotInfo())

        viewModel.onCreateShotDescriptionValueChanged(shotDescription = shotDescription)

        Assertions.assertEquals(viewModel.createdShotInfo, CreateShotInfo(description = shotDescription))

    }

    @Test
    fun `on create shot category value changed`() {
        val shotCategory = "category"

        Assertions.assertEquals(viewModel.createdShotInfo, CreateShotInfo())

        viewModel.onCreateShotCategoryValueChanged(shotCategory = shotCategory)

        Assertions.assertEquals(viewModel.createdShotInfo, CreateShotInfo(category = shotCategory))
    }

    @Nested
    inner class OnEditOrCreateNewShot {
        private val declaredShot = TestDeclaredShot.build()

        @Test
        fun `when editedDeclaredShot state is editing and editedDeclaredShot is null should show error alert`() = runTest {
            viewModel.editedDeclaredShot = null
            viewModel.createEditDeclaredShotMutableStateFlow.update { state -> state.copy(declaredShotState = DeclaredShotState.EDITING) }

            viewModel.onEditOrCreateNewShot()

            verify { navigation.enableProgress(progress = any()) }
            verify { navigation.disableProgress() }
            verify { navigation.alert(alert = any()) }

            verify(exactly = 0) { navigation.pop() }
            coVerify(exactly = 0) { declaredShotRepository.deleteShotById(declaredShot.id) }
        }

        @Test
        fun `when declaredShot state is not editing and editedDeclaredShot is null should show error alert`() = runTest {
            viewModel.editedDeclaredShot = null
            viewModel.createEditDeclaredShotMutableStateFlow.update { state -> state.copy(declaredShotState = DeclaredShotState.CREATING) }

            viewModel.onEditOrCreateNewShot()

            verify { navigation.enableProgress(progress = any()) }
            verify { navigation.disableProgress() }
            verify { navigation.alert(alert = any()) }

            verify(exactly = 0) { navigation.pop() }
            coVerify(exactly = 0) { declaredShotRepository.deleteShotById(declaredShot.id) }
        }

        @Test
        fun `when editedDeclaredShot state is editing, editedDeclaredShot is not null, and attemptToCreateDeclaredShotFirebaseRealtimeDatabaseResponseFlow returns false should show error alert`() = runTest {
            viewModel.editedDeclaredShot = declaredShot
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
        fun `when editedDeclaredShot state is editing, editedDeclaredShot is not null, but title is empty should show alert`() = runTest {
            viewModel.editedDeclaredShot = declaredShot.copy(title = "")
            viewModel.createEditDeclaredShotMutableStateFlow.update { state -> state.copy(declaredShotState = DeclaredShotState.EDITING) }

            viewModel.onEditOrCreateNewShot()

            verify { navigation.enableProgress(progress = any()) }
            verify { navigation.disableProgress() }
            verify { navigation.alert(alert = any()) }

            coVerify(exactly = 0) { declaredShotRepository.deleteShotById(declaredShot.id) }
            verify(exactly = 0) { navigation.pop() }
        }

        @Test
        fun `when editedDeclaredShot state is editing, editedDeclaredShot is not null, but category is empty should show alert`() = runTest {
            viewModel.editedDeclaredShot = declaredShot.copy(shotCategory = "")
            viewModel.createEditDeclaredShotMutableStateFlow.update { state -> state.copy(declaredShotState = DeclaredShotState.EDITING) }

            viewModel.onEditOrCreateNewShot()

            verify { navigation.enableProgress(progress = any()) }
            verify { navigation.disableProgress() }
            verify { navigation.alert(alert = any()) }

            coVerify(exactly = 0) { declaredShotRepository.deleteShotById(declaredShot.id) }
            verify(exactly = 0) { navigation.pop() }
        }

        @Test
        fun `when editDeclaredShot state is editing, and editedDeclaredShot name contains shot names should show alert`() = runTest {
            viewModel.editedDeclaredShot = declaredShot
            viewModel.createEditDeclaredShotMutableStateFlow.update { state -> state.copy(declaredShotState = DeclaredShotState.EDITING) }

            viewModel.allDeclaredShotNames = listOf(declaredShot.title)

            viewModel.onEditOrCreateNewShot()

            verify { navigation.enableProgress(progress = any()) }
            verify { navigation.disableProgress() }
            verify { navigation.alert(alert = any()) }

            coVerify(exactly = 0) { declaredShotRepository.deleteShotById(declaredShot.id) }
            verify(exactly = 0) { navigation.pop() }
        }

        @Test
        fun `when editedDeclaredShot state is editing, editedDeclaredShot is not null, but description is empty should show alert`() = runTest {
            viewModel.editedDeclaredShot = declaredShot.copy(description = "")
            viewModel.createEditDeclaredShotMutableStateFlow.update { state -> state.copy(declaredShotState = DeclaredShotState.EDITING) }

            viewModel.onEditOrCreateNewShot()

            verify { navigation.enableProgress(progress = any()) }
            verify { navigation.disableProgress() }
            verify { navigation.alert(alert = any()) }

            coVerify(exactly = 0) { declaredShotRepository.deleteShotById(declaredShot.id) }
            verify(exactly = 0) { navigation.pop() }
        }

        @Test
        fun `when editedDeclaredShot state is not editing, editDeclaredShot name is empty should return alert`() = runTest {
            viewModel.editedDeclaredShot = declaredShot.copy(title = "")
            viewModel.createEditDeclaredShotMutableStateFlow.update { state -> state.copy(declaredShotState = DeclaredShotState.CREATING) }

            viewModel.onEditOrCreateNewShot()

            verify { navigation.enableProgress(progress = any()) }
            verify { navigation.disableProgress() }
            verify { navigation.alert(alert = any()) }

            coVerify(exactly = 0) { declaredShotRepository.deleteShotById(declaredShot.id) }
            verify(exactly = 0) { navigation.pop() }
        }

        @Test
        fun `when editedDeclaredShot state is not editing, editDeclaredShot category is empty should return alert`() = runTest {
            viewModel.editedDeclaredShot = declaredShot.copy(shotCategory = "")
            viewModel.createEditDeclaredShotMutableStateFlow.update { state -> state.copy(declaredShotState = DeclaredShotState.CREATING) }

            viewModel.onEditOrCreateNewShot()

            verify { navigation.enableProgress(progress = any()) }
            verify { navigation.disableProgress() }
            verify { navigation.alert(alert = any()) }

            coVerify(exactly = 0) { declaredShotRepository.deleteShotById(declaredShot.id) }
            verify(exactly = 0) { navigation.pop() }
        }

        @Test
        fun `when editedDeclaredShot state is not editing, editDeclaredShot description is empty should return alert`() = runTest {
            viewModel.editedDeclaredShot = declaredShot.copy(description = "")
            viewModel.createEditDeclaredShotMutableStateFlow.update { state -> state.copy(declaredShotState = DeclaredShotState.CREATING) }

            viewModel.onEditOrCreateNewShot()

            verify { navigation.enableProgress(progress = any()) }
            verify { navigation.disableProgress() }
            verify { navigation.alert(alert = any()) }

            coVerify(exactly = 0) { declaredShotRepository.deleteShotById(declaredShot.id) }
            verify(exactly = 0) { navigation.pop() }
        }

        @Test
        fun `when editedDeclaredShot state is not editing, editDeclaredShot name contains in declared shot names should return alert`() = runTest {
            viewModel.editedDeclaredShot = declaredShot
            viewModel.allDeclaredShotNames = listOf(declaredShot.title)
            viewModel.createEditDeclaredShotMutableStateFlow.update { state -> state.copy(declaredShotState = DeclaredShotState.CREATING) }

            viewModel.onEditOrCreateNewShot()

            verify { navigation.enableProgress(progress = any()) }
            verify { navigation.disableProgress() }
            verify { navigation.alert(alert = any()) }

            coVerify(exactly = 0) { declaredShotRepository.deleteShotById(declaredShot.id) }
            verify(exactly = 0) { navigation.pop() }
        }

        @Test
        fun `when editedDeclaredShot state is not editing,editedDeclaredShot is not null, and attemptToCreateDeclaredShotFirebaseRealtimeDatabaseResponseFlow returns false should show error alert`() = runTest {
            viewModel.editedDeclaredShot = declaredShot
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
        fun `when editedDeclaredShot state is editing, editedDeclaredShot is not null, and attemptToCreateDeclaredShotFirebaseRealtimeDatabaseResponseFlow returns true should pop and show alert`() = runTest {
            viewModel.editedDeclaredShot = declaredShot
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
        fun `when editedDeclaredShot state is creating, newDeclaredShot is not empty, and attemptToCreateDeclaredShotFirebaseRealtimeDatabaseResponseFlow returns true should pop and show alert`() = runTest {
            val newDeclaredShot = DeclaredShot(
                id = 444,
                shotCategory = "category",
                title = "name",
                description = "description"
            )

            viewModel.allDeclaredShotNames = emptyList()
            viewModel.createdShotInfo = CreateShotInfo(name = "name", "description", "category")
            viewModel.createEditDeclaredShotMutableStateFlow.update { state -> state.copy(declaredShotState = DeclaredShotState.CREATING) }

            coEvery { declaredShotRepository.fetchMaxId() } returns 444
            coEvery { createFirebaseUserInfo.attemptToCreateDeclaredShotFirebaseRealtimeDatabaseResponseFlow(declaredShot = newDeclaredShot.copy(id = 445)) } returns flowOf(Pair(true, newDeclaredShot.copy(id = 445)))

            viewModel.onEditOrCreateNewShot()

            coVerify { declaredShotRepository.createNewDeclaredShot(newDeclaredShot.copy(id = 445)) }
            verify { navigation.enableProgress(progress = any()) }
            verify { navigation.disableProgress() }
            verify { navigation.pop() }
            verify { navigation.alert(alert = any()) }

            coVerify(exactly = 0) { declaredShotRepository.deleteShotById(445) }
        }
    }

}
