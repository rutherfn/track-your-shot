package com.nicholas.rutherford.track.your.shot.feature.voice.commands.createeditvoicecommand

import androidx.lifecycle.SavedStateHandle
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.base.test.BaseTest
import com.nicholas.rutherford.track.your.shot.data.room.repository.SavedVoiceCommandRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.SavedVoiceCommand
import com.nicholas.rutherford.track.your.shot.data.room.response.VoiceCommandTypes
import com.nicholas.rutherford.track.your.shot.data.room.response.VoiceCommandTypes.Companion.toDisplayLabel
import com.nicholas.rutherford.track.your.shot.data.test.room.TestSavedVoiceCommand
import com.nicholas.rutherford.track.your.shot.feature.voice.commands.VoiceCommandState
import com.nicholas.rutherford.track.your.shot.firebase.core.create.CreateFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.firebase.core.delete.DeleteFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.firebase.core.update.UpdateFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.firebase.realtime.SavedVoiceCommandRealtimeResponse
import com.nicholas.rutherford.track.your.shot.firebase.realtime.SavedVoiceCommandRealtimeWithKeyResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CreateEditVoiceCommandViewModelTest : BaseTest() {

    private lateinit var viewModel: CreateEditVoiceCommandViewModel

    private var savedStateHandle = mockk<SavedStateHandle>(relaxed = true)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()
    private val scope = CoroutineScope(SupervisorJob() + testDispatcher)

    private val createFirebaseUserInfo = mockk<CreateFirebaseUserInfo>(relaxed = true)
    private val updateFirebaseUserInfo = mockk<UpdateFirebaseUserInfo>(relaxed = true)
    private val deleteFirebaseUserInfo = mockk<DeleteFirebaseUserInfo>(relaxed = true)
    private val savedVoiceCommandRepository = mockk<SavedVoiceCommandRepository>(relaxed = true)
    private val navigation = mockk<CreateEditVoiceCommandNavigation>(relaxed = true)

    private val recordedPhraseParam = "recorded phrase"
    private val voiceCommandTypeParam = 2

    @BeforeEach
    fun beforeEach() {
        every { savedStateHandle.get<Int>("voiceCommandTypeValueParam") } returns voiceCommandTypeParam
        every { savedStateHandle.get<String>("recordedPhraseParam") } returns recordedPhraseParam

        viewModel = CreateEditVoiceCommandViewModel(
            savedStateHandle = savedStateHandle,
            application = application,
            scope = scope,
            createFirebaseUserInfo = createFirebaseUserInfo,
            updateFirebaseUserInfo = updateFirebaseUserInfo,
            deleteFirebaseUserInfo = deleteFirebaseUserInfo,
            savedVoiceCommandRepository = savedVoiceCommandRepository,
            navigation = navigation
        )
    }

    @Nested
    inner class IsCurrentlyEditingSavedVoiceCommand {

        @Test
        fun `when type is set to null should return false`() {
            val result = viewModel.isCurrentlyEditingSavedVoiceCommand(
                phrase = "phrase",
                type = null
            )

            Assertions.assertEquals(result, false)
        }

        @Test
        fun `when type is set to none and phrase is not null should return fals `() {
            val result = viewModel.isCurrentlyEditingSavedVoiceCommand(
                phrase = "phrase",
                type = VoiceCommandTypes.None
            )

            Assertions.assertEquals(result, false)
        }

        @Test
        fun `when type is not set to none and phrase is always null null should return false`() {
            val commandTypes = listOf(
                VoiceCommandTypes.Start,
                VoiceCommandTypes.Stop,
                VoiceCommandTypes.Make,
                VoiceCommandTypes.Miss
            )

            commandTypes.forEach { type ->
                val result = viewModel.isCurrentlyEditingSavedVoiceCommand(
                    phrase = null,
                    type = type
                )

                Assertions.assertEquals(result, false)
            }
        }

        @Test
        fun `when type is not set to none and phrase is not null should return true`() {
            val commandTypes = listOf(
                VoiceCommandTypes.Start,
                VoiceCommandTypes.Stop,
                VoiceCommandTypes.Make,
                VoiceCommandTypes.Miss
            )

            commandTypes.forEach { type ->
                val result = viewModel.isCurrentlyEditingSavedVoiceCommand(
                    phrase = "phrase",
                    type = type
                )

                Assertions.assertEquals(result, true)
            }
        }
    }

    @Nested
    inner class SetInfoFromParamsAndUpdateState {
        private val phrase = recordedPhraseParam

        @Test
        fun `when isEditingShot is set to true and data returns should update state`() = runTest {
            val type = VoiceCommandTypes.fromValue(2)
            val savedVoiceCommand = TestSavedVoiceCommand.build().copy(name = phrase, type = type)

            coEvery { savedVoiceCommandRepository.getVoiceCommandByName(name = phrase) } returns savedVoiceCommand

            viewModel.setInfoFromParamsAndUpdateState()

            Assertions.assertEquals(
                viewModel.createEditVoiceCommandStateFlow.value,
                CreateEditVoiceCommandState(
                    type = type,
                    recordedPhrase = phrase,
                    voiceCommandState = VoiceCommandState.EDITING_EXISTING,
                    isCommandAlreadyCreated = true
                )
            )
        }

        @Test
        fun `when isEditingShot is set to false and data returns should update state`() = runTest {
            val noneType = VoiceCommandTypes.fromValue(4)
            val savedVoiceCommand = TestSavedVoiceCommand.build().copy(name = phrase, type = noneType)

            every { savedStateHandle.get<Int>("voiceCommandTypeValueParam") } returns noneType.value
            every { savedStateHandle.get<String>("recordedPhraseParam") } returns phrase

            viewModel = CreateEditVoiceCommandViewModel(
                savedStateHandle = savedStateHandle,
                application = application,
                scope = scope,
                createFirebaseUserInfo = createFirebaseUserInfo,
                updateFirebaseUserInfo = updateFirebaseUserInfo,
                deleteFirebaseUserInfo = deleteFirebaseUserInfo,
                savedVoiceCommandRepository = savedVoiceCommandRepository,
                navigation = navigation
            )

            coEvery { savedVoiceCommandRepository.getVoiceCommandByName(name = phrase) } returns savedVoiceCommand

            viewModel.setInfoFromParamsAndUpdateState()

            Assertions.assertEquals(
                viewModel.createEditVoiceCommandStateFlow.value,
                CreateEditVoiceCommandState(
                    type = noneType,
                    recordedPhrase = phrase,
                    voiceCommandState = VoiceCommandState.CREATING,
                    isCommandAlreadyCreated = true
                )
            )
        }
    }

    @Nested
    inner class BuildIsCommandAlreadyCreated {
        private val voicePhrase = "voicePhrase"
        private val voiceType = VoiceCommandTypes.Make

        @Test
        fun `when getVoiceCommandByName returns null should return false`() = runTest {
            coEvery { savedVoiceCommandRepository.getVoiceCommandByName(name = voicePhrase) } returns null

            val result = viewModel.buildIsCommandAlreadyCreated(
                voicePhrase = voicePhrase,
                voiceType = voiceType
            )

            Assertions.assertEquals(result, false)
        }

        @Test
        fun `when getVoiceCommandByName does not return null, and type is not equal to the passed in type should return false`() = runTest {
            val savedVoiceCommand = TestSavedVoiceCommand.build().copy(type = VoiceCommandTypes.Miss)

            coEvery { savedVoiceCommandRepository.getVoiceCommandByName(name = voicePhrase) } returns savedVoiceCommand

            val result = viewModel.buildIsCommandAlreadyCreated(
                voicePhrase = voicePhrase,
                voiceType = voiceType
            )

            Assertions.assertEquals(result, false)
        }

        @Test
        fun `when getVoiceCommandByName does not return null, and type is equal to the passed in type should return false`() = runTest {
            val savedVoiceCommand = TestSavedVoiceCommand.build().copy(type = voiceType)

            coEvery { savedVoiceCommandRepository.getVoiceCommandByName(name = voicePhrase) } returns savedVoiceCommand

            val result = viewModel.buildIsCommandAlreadyCreated(
                voicePhrase = voicePhrase,
                voiceType = voiceType
            )

            Assertions.assertEquals(result, true)
        }
    }

    @Nested
    inner class BuildHasOverriddenExistingCommand {
        val phrase = "newPhrase"

        @Test
        fun `when recordedPhraseParam is set to null should return false`() {
            every { savedStateHandle.get<String>("recordedPhraseParam") } returns null

            viewModel = CreateEditVoiceCommandViewModel(
                savedStateHandle = savedStateHandle,
                application = application,
                scope = scope,
                createFirebaseUserInfo = createFirebaseUserInfo,
                updateFirebaseUserInfo = updateFirebaseUserInfo,
                deleteFirebaseUserInfo = deleteFirebaseUserInfo,
                savedVoiceCommandRepository = savedVoiceCommandRepository,
                navigation = navigation
            )

            val result = viewModel.buildHasOverriddenExistingCommand(phrase = phrase)

            Assertions.assertEquals(result, false)
        }

        @Test
        fun `when recordedPhraseParam is not set to null but does equal the passed in phrase should return false`() {
            every { savedStateHandle.get<String>("recordedPhraseParam") } returns phrase

            viewModel = CreateEditVoiceCommandViewModel(
                savedStateHandle = savedStateHandle,
                application = application,
                scope = scope,
                createFirebaseUserInfo = createFirebaseUserInfo,
                updateFirebaseUserInfo = updateFirebaseUserInfo,
                deleteFirebaseUserInfo = deleteFirebaseUserInfo,
                savedVoiceCommandRepository = savedVoiceCommandRepository,
                navigation = navigation
            )

            val result = viewModel.buildHasOverriddenExistingCommand(phrase = phrase)

            Assertions.assertEquals(result, false)
        }

        @Test
        fun `when recordedPhraseParam is not set to null but does not equal the passed in phrase should return true`() {
            every { savedStateHandle.get<String>("recordedPhraseParam") } returns recordedPhraseParam

            viewModel = CreateEditVoiceCommandViewModel(
                savedStateHandle = savedStateHandle,
                application = application,
                scope = scope,
                createFirebaseUserInfo = createFirebaseUserInfo,
                updateFirebaseUserInfo = updateFirebaseUserInfo,
                deleteFirebaseUserInfo = deleteFirebaseUserInfo,
                savedVoiceCommandRepository = savedVoiceCommandRepository,
                navigation = navigation
            )

            val result = viewModel.buildHasOverriddenExistingCommand(phrase = phrase)

            Assertions.assertEquals(result, true)
        }
    }

    @Nested
    inner class BuildSavedNewCommandDescriptionAlert {

        @Test
        fun `when command type is start should return correct description`() {
            val type = VoiceCommandTypes.Start

            val result = viewModel.buildSavedNewCommandDescriptionAlert(type = type)

            Assertions.assertEquals(result, application.getString(StringsIds.savedNewStartCommandDescription))
        }

        @Test
        fun `when command type is stop should return correct description`() {
            val type = VoiceCommandTypes.Stop

            val result = viewModel.buildSavedNewCommandDescriptionAlert(type = type)

            Assertions.assertEquals(result, application.getString(StringsIds.savedNewStopCommandDescription))
        }

        @Test
        fun `when command type is make should return correct description`() {
            val type = VoiceCommandTypes.Make

            val result = viewModel.buildSavedNewCommandDescriptionAlert(type = type)

            Assertions.assertEquals(result, application.getString(StringsIds.savedNewMakeCommandDescription))
        }

        @Test
        fun `when command type is not start, stop, or make should return correct default description`() {
            val types = listOf(
                VoiceCommandTypes.Miss,
                VoiceCommandTypes.None
            )

            types.forEach { type ->
                val result = viewModel.buildSavedNewCommandDescriptionAlert(type = type)

                Assertions.assertEquals(result, application.getString(StringsIds.savedNewMissCommandDescription))
            }
        }
    }

    @Nested
    inner class BuildEditCommandDescriptionAlert {

        @Test
        fun `when command type is start should return correct description`() {
            val type = VoiceCommandTypes.Start

            val result = viewModel.buildEditCommandDescriptionAlert(type = type)

            Assertions.assertEquals(result, application.getString(StringsIds.editedNewStartCommandDescription))
        }

        @Test
        fun `when command type is stop should return correct description`() {
            val type = VoiceCommandTypes.Stop

            val result = viewModel.buildEditCommandDescriptionAlert(type = type)

            Assertions.assertEquals(result, application.getString(StringsIds.editedNewStopCommandDescription))
        }

        @Test
        fun `when command type is make should return correct description`() {
            val type = VoiceCommandTypes.Make

            val result = viewModel.buildEditCommandDescriptionAlert(type = type)

            Assertions.assertEquals(result, application.getString(StringsIds.editedNewMakeCommandDescription))
        }

        @Test
        fun `when command type is not start, stop, or make should return correct default description`() {
            val types = listOf(
                VoiceCommandTypes.Miss,
                VoiceCommandTypes.None
            )

            types.forEach { type ->
                val result = viewModel.buildEditCommandDescriptionAlert(type = type)

                Assertions.assertEquals(
                    result,
                    application.getString(StringsIds.editedNewMissCommandDescription)
                )
            }
        }
    }

    @Test
    fun `on toolbar menu clicked should call pop`() {
        viewModel.onToolbarMenuClicked()

        verify { navigation.pop() }
    }

    @Test
    fun `on record phrase clicked should update state`() {
        Assertions.assertEquals(
            viewModel.createEditVoiceCommandMutableStateFlow.value,
            CreateEditVoiceCommandState()
        )

        // Execute method that calls Android APIs (SpeechRecognizer) - handled gracefully by BaseTest
        // This utility method catches expected Android API exceptions and logs them for debugging
        executeWithAndroidApiHandling(
            operationName = "onRecordPhraseClicked",
            block = { viewModel.onRecordPhraseClicked() }
        )

        Assertions.assertEquals(
            viewModel.createEditVoiceCommandMutableStateFlow.value,
            CreateEditVoiceCommandState(
                voiceCommandState = VoiceCommandState.RECORDING_NEW,
                isRecording = true
            )
        )
    }

    @Test
    fun `on phrase captured should update state`() {
        val phrase = "phraseTest"

        Assertions.assertEquals(
            viewModel.createEditVoiceCommandMutableStateFlow.value,
            CreateEditVoiceCommandState()
        )

        executeWithAndroidApiHandling(
            operationName = "onPhraseCaptured",
            block = { viewModel.onPhraseCaptured(phrase = phrase) }
        )

        Assertions.assertEquals(
            viewModel.createEditVoiceCommandMutableStateFlow.value,
            CreateEditVoiceCommandState(
                recordedPhrase = phrase,
                isRecording = false,
                voiceCommandState = VoiceCommandState.EDITING_EXISTING,
                hasOverriddenExistingCommand = true
            )
        )
    }

    @Test
    fun `on error phrase captured should update state`() {
        val error = "error"

        Assertions.assertEquals(
            viewModel.createEditVoiceCommandMutableStateFlow.value,
            CreateEditVoiceCommandState()
        )

        viewModel.onErrorPhraseCaptured(error = error)

        Assertions.assertEquals(
            viewModel.createEditVoiceCommandMutableStateFlow.value,
            CreateEditVoiceCommandState(
                isRecording = false,
                voiceCommandState = VoiceCommandState.RECORDING_ERROR,
                voiceCapturedErrorDescription = error,
                hasOverriddenExistingCommand = false
            )
        )
    }

    @Test
    fun `on dismiss error clicked should update state`() {
        Assertions.assertEquals(
            viewModel.createEditVoiceCommandMutableStateFlow.value,
            CreateEditVoiceCommandState()
        )

        viewModel.onDismissErrorClicked()

        Assertions.assertEquals(
            viewModel.createEditVoiceCommandMutableStateFlow.value,
            CreateEditVoiceCommandState(
                voiceCommandState = VoiceCommandState.CREATING,
                voiceCapturedErrorDescription = null
            )
        )
    }

    @Test
    fun `on try again clicked should update state`() {
        Assertions.assertEquals(
            viewModel.createEditVoiceCommandMutableStateFlow.value,
            CreateEditVoiceCommandState()
        )

        executeWithAndroidApiHandling(
            operationName = "onTryAgainClicked",
            block = { viewModel.onTryAgainClicked() }
        )

        Assertions.assertEquals(
            viewModel.createEditVoiceCommandMutableStateFlow.value,
            CreateEditVoiceCommandState(
                voiceCommandState = VoiceCommandState.RECORDING_NEW,
                isRecording = true
            )
        )
    }

    @Test
    fun `on record again clicked should update state`() {
        Assertions.assertEquals(
            viewModel.createEditVoiceCommandMutableStateFlow.value,
            CreateEditVoiceCommandState()
        )

        executeWithAndroidApiHandling(
            operationName = "onRecordAgainClicked",
            block = { viewModel.onRecordAgainClicked() }
        )

        Assertions.assertEquals(
            viewModel.createEditVoiceCommandMutableStateFlow.value,
            CreateEditVoiceCommandState(
                voiceCommandState = VoiceCommandState.EDITING_EXISTING,
                isRecording = true
            )
        )
    }

    @Test
    fun `dismissProgressAndShowNoCommandEnteredAlert should call disable progress and alert`() {
        viewModel.dismissProgressAndShowNoCommandEnteredAlert()

        verify { navigation.disableProgress() }
        verify { navigation.alert(alert = any()) }
    }

    @Test
    fun `dismissProgressAndShowCouldNotDeleteCommandAlert should call disable progress and alert`() {
        viewModel.dismissProgressAndShowCouldNotDeleteCommandAlert()

        verify { navigation.disableProgress() }
        verify { navigation.alert(alert = any()) }
    }

    @Test
    fun `areYouSureYouWantToDeleteCommandAlert should return expected alert info`() {
        val result = viewModel.areYouSureYouWantToDeleteCommandAlert()

        Assertions.assertEquals(
            result.title,
            application.getString(StringsIds.deletingCommand)
        )
        Assertions.assertEquals(
            result.confirmButton!!.buttonText,
            application.getString(StringsIds.yes)
        )

        Assertions.assertEquals(
            result.dismissButton!!.buttonText,
            application.getString(StringsIds.no)
        )
        Assertions.assertEquals(
            result.description,
            application.getString(StringsIds.areYouSureYouWantToDeleteCommand)
        )
    }

    @Test
    fun `savedNewCommandAlert should return expected alert info`() {
        val type = VoiceCommandTypes.Start

        val result = viewModel.savedNewCommandAlert(type = type)

        Assertions.assertEquals(
            result.title,
            application.getString(StringsIds.newCommandSavedSuccessfully)
        )
        Assertions.assertEquals(
            result.dismissButton!!.buttonText,
            application.getString(StringsIds.gotIt)
        )
        Assertions.assertEquals(
            result.description,
            application.getString(StringsIds.savedNewStartCommandDescription)
        )
    }

    @Test
    fun `editNewCommandAlert should return expected alert info`() {
        val type = VoiceCommandTypes.Start

        val result = viewModel.editNewCommandAlert(type = type)

        Assertions.assertEquals(
            result.title,
            application.getString(StringsIds.editCommandSuccessfully)
        )
        Assertions.assertEquals(
            result.dismissButton!!.buttonText,
            application.getString(StringsIds.gotIt)
        )
        Assertions.assertEquals(
            result.description,
            application.getString(StringsIds.editedNewStartCommandDescription)
        )
    }

    @Test
    fun `deleteCommandAlert should return expected alert info`() {
        val type = VoiceCommandTypes.Start

        val result = viewModel.deleteCommandAlert(type = type)

        Assertions.assertEquals(
            result.title,
            application.getString(StringsIds.deleteCommandSuccessfully)
        )
        Assertions.assertEquals(
            result.dismissButton!!.buttonText,
            application.getString(StringsIds.gotIt)
        )
        Assertions.assertEquals(
            result.description,
            application.getString(StringsIds.deleteCommandSuccessfullyDescription, type.toDisplayLabel())
        )
    }

    @Test
    fun `errorNoCommandEnteredAlert should return expected alert info`() {
        val result = viewModel.errorNoCommandEnteredAlert()

        Assertions.assertEquals(
            result.title,
            application.getString(StringsIds.noCommandEntered)
        )
        Assertions.assertEquals(
            result.dismissButton!!.buttonText,
            application.getString(StringsIds.gotIt)
        )
        Assertions.assertEquals(
            result.description,
            application.getString(StringsIds.noCommandEnteredDescription)
        )
    }

    @Test
    fun `errorSavingNewVoiceCommandAlert should return expected alert info`() {
        val result = viewModel.errorSavingNewVoiceCommandAlert()

        Assertions.assertEquals(
            result.title,
            application.getString(StringsIds.cannotSaveVoiceCommand)
        )
        Assertions.assertEquals(
            result.dismissButton!!.buttonText,
            application.getString(StringsIds.gotIt)
        )
        Assertions.assertEquals(
            result.description,
            application.getString(StringsIds.errorVoiceCommandSavedFailure)
        )
    }

    @Test
    fun `errorEditingExistingVoiceCommandAlert should return expected alert info`() {
        val result = viewModel.errorEditingExistingVoiceCommandAlert()

        Assertions.assertEquals(
            result.title,
            application.getString(StringsIds.cannotEditVoiceCommand)
        )
        Assertions.assertEquals(
            result.dismissButton!!.buttonText,
            application.getString(StringsIds.gotIt)
        )
        Assertions.assertEquals(
            result.description,
            application.getString(StringsIds.errorVoiceCommandEditFailure)
        )
    }

    @Test
    fun `errorCannotEditSameVoiceCommandAlert should return expected alert info`() {
        val result = viewModel.errorCannotEditSameVoiceCommandAlert()

        Assertions.assertEquals(
            result.title,
            application.getString(StringsIds.cannotEditVoiceCommand)
        )
        Assertions.assertEquals(
            result.dismissButton!!.buttonText,
            application.getString(StringsIds.gotIt)
        )
        Assertions.assertEquals(
            result.description,
            application.getString(StringsIds.errorVoiceCommandEditFailedWithSameName)
        )
    }

    @Test
    fun `errorCouldNotDeleteCommandAlert should return expected alert info`() {
        val result = viewModel.errorCouldNotDeleteCommandAlert()

        Assertions.assertEquals(
            result.title,
            application.getString(StringsIds.couldNotDeleteCommand)
        )
        Assertions.assertEquals(
            result.dismissButton!!.buttonText,
            application.getString(StringsIds.gotIt)
        )
        Assertions.assertEquals(
            result.description,
            application.getString(StringsIds.couldNotDeleteCommandDescription)
        )
    }

    @Test
    fun `on delete voice command should call navigation alert with areYouSureYouWantToDeleteCommandAlert`() {
        viewModel.onDeleteVoiceCommand()

        verify { navigation.alert(alert = any()) }
    }

    @Nested
    inner class AttemptToDeleteVoiceCommand {

        private val savedVoiceCommand = TestSavedVoiceCommand.build().copy(name = recordedPhraseParam)

        @Test
        fun `when recordedPhraseParam is set to null should dismiss progress and show alert`() = runTest {
            every { savedStateHandle.get<String>("recordedPhraseParam") } returns null

            viewModel = CreateEditVoiceCommandViewModel(
                savedStateHandle = savedStateHandle,
                application = application,
                scope = scope,
                createFirebaseUserInfo = createFirebaseUserInfo,
                updateFirebaseUserInfo = updateFirebaseUserInfo,
                deleteFirebaseUserInfo = deleteFirebaseUserInfo,
                savedVoiceCommandRepository = savedVoiceCommandRepository,
                navigation = navigation
            )

            viewModel.attemptToDeleteVoiceCommand()

            coVerify(exactly = 0) { savedVoiceCommandRepository.deleteSavedVoiceCommand(savedVoiceCommand = savedVoiceCommand) }

            verify { navigation.enableProgress(progress = any()) }
            verify { navigation.disableProgress() }
            verify { navigation.alert(alert = any()) }
        }

        @Test
        fun `when recordedPhraseParam is not set to null and getVoiceCommandByName returns null should dismiss progress and show alert`() = runTest {
            every { savedStateHandle.get<String>("recordedPhraseParam") } returns recordedPhraseParam

            viewModel = CreateEditVoiceCommandViewModel(
                savedStateHandle = savedStateHandle,
                application = application,
                scope = scope,
                createFirebaseUserInfo = createFirebaseUserInfo,
                updateFirebaseUserInfo = updateFirebaseUserInfo,
                deleteFirebaseUserInfo = deleteFirebaseUserInfo,
                savedVoiceCommandRepository = savedVoiceCommandRepository,
                navigation = navigation
            )

            coEvery { savedVoiceCommandRepository.getVoiceCommandByName(name = recordedPhraseParam) } returns null

            viewModel.attemptToDeleteVoiceCommand()

            coVerify(exactly = 0) { savedVoiceCommandRepository.deleteSavedVoiceCommand(savedVoiceCommand = savedVoiceCommand) }

            verify { navigation.enableProgress(progress = any()) }
            verify { navigation.disableProgress() }
            verify { navigation.alert(alert = any()) }
        }

        @Test
        fun `when recordedPhraseParam and getVoiceCommandByName does not return null and deleteSavedVoiceCommand returns false should dismiss progress and show alert`() = runTest {
            every { savedStateHandle.get<String>("recordedPhraseParam") } returns recordedPhraseParam

            viewModel = CreateEditVoiceCommandViewModel(
                savedStateHandle = savedStateHandle,
                application = application,
                scope = scope,
                createFirebaseUserInfo = createFirebaseUserInfo,
                updateFirebaseUserInfo = updateFirebaseUserInfo,
                deleteFirebaseUserInfo = deleteFirebaseUserInfo,
                savedVoiceCommandRepository = savedVoiceCommandRepository,
                navigation = navigation
            )

            coEvery { savedVoiceCommandRepository.getVoiceCommandByName(name = recordedPhraseParam) } returns savedVoiceCommand
            coEvery { deleteFirebaseUserInfo.deleteSavedVoiceCommand(savedVoiceCommandKey = savedVoiceCommand.firebaseKey) } returns flowOf(value = false)

            viewModel.attemptToDeleteVoiceCommand()

            coVerify(exactly = 0) { savedVoiceCommandRepository.deleteSavedVoiceCommand(savedVoiceCommand = savedVoiceCommand) }

            verify { navigation.enableProgress(progress = any()) }
            verify { navigation.disableProgress() }
            verify { navigation.alert(alert = any()) }
        }

        @Test
        fun `when recordedPhraseParam and getVoiceCommandByName does not return null and deleteSavedVoiceCommand returns true should delete saved voice command, dismiss progress, and show alert`() = runTest {
            every { savedStateHandle.get<String>("recordedPhraseParam") } returns recordedPhraseParam

            viewModel = CreateEditVoiceCommandViewModel(
                savedStateHandle = savedStateHandle,
                application = application,
                scope = scope,
                createFirebaseUserInfo = createFirebaseUserInfo,
                updateFirebaseUserInfo = updateFirebaseUserInfo,
                deleteFirebaseUserInfo = deleteFirebaseUserInfo,
                savedVoiceCommandRepository = savedVoiceCommandRepository,
                navigation = navigation
            )

            coEvery { savedVoiceCommandRepository.getVoiceCommandByName(name = recordedPhraseParam) } returns savedVoiceCommand
            coEvery { deleteFirebaseUserInfo.deleteSavedVoiceCommand(savedVoiceCommandKey = savedVoiceCommand.firebaseKey) } returns flowOf(value = true)

            viewModel.attemptToDeleteVoiceCommand()

            coVerify { savedVoiceCommandRepository.deleteSavedVoiceCommand(savedVoiceCommand = savedVoiceCommand) }

            verify { navigation.enableProgress(progress = any()) }
            verify { navigation.disableProgress() }
            verify { navigation.alert(alert = any()) }
        }
    }

    @Nested
    inner class OnEditVoiceCommand {
        val savedVoiceCommand = TestSavedVoiceCommand.build()

        @Test
        fun `when recordedPhraseParam returns back null should dismiss progress and show alert`() = runTest {
            val phrase = "phrase"
            val type = VoiceCommandTypes.Stop

            every { savedStateHandle.get<String>("recordedPhraseParam") } returns null

            viewModel = CreateEditVoiceCommandViewModel(
                savedStateHandle = savedStateHandle,
                application = application,
                scope = scope,
                createFirebaseUserInfo = createFirebaseUserInfo,
                updateFirebaseUserInfo = updateFirebaseUserInfo,
                deleteFirebaseUserInfo = deleteFirebaseUserInfo,
                savedVoiceCommandRepository = savedVoiceCommandRepository,
                navigation = navigation
            )

            viewModel.onEditVoiceCommand(phrase = phrase, type = type)

            coVerify(exactly = 0) {
                savedVoiceCommandRepository.updateSavedVoiceCommand(
                    savedVoiceCommand = SavedVoiceCommand(
                        id = savedVoiceCommand.id,
                        name = phrase,
                        firebaseKey = savedVoiceCommand.firebaseKey,
                        type = type
                    )
                )
            }
            verify(exactly = 0) { navigation.pop() }

            verify { navigation.enableProgress(progress = any()) }
            verify { navigation.disableProgress() }
            verify { navigation.alert(alert = any()) }
        }

        @Test
        fun `when recordedPhraseParam returns back not null and matches param should dismiss progress and show alert`() = runTest {
            val phrase = "phrase"
            val type = VoiceCommandTypes.Stop

            every { savedStateHandle.get<String>("recordedPhraseParam") } returns phrase

            viewModel = CreateEditVoiceCommandViewModel(
                savedStateHandle = savedStateHandle,
                application = application,
                scope = scope,
                createFirebaseUserInfo = createFirebaseUserInfo,
                updateFirebaseUserInfo = updateFirebaseUserInfo,
                deleteFirebaseUserInfo = deleteFirebaseUserInfo,
                savedVoiceCommandRepository = savedVoiceCommandRepository,
                navigation = navigation
            )

            viewModel.onEditVoiceCommand(phrase = phrase, type = type)

            coVerify(exactly = 0) {
                savedVoiceCommandRepository.updateSavedVoiceCommand(
                    savedVoiceCommand = SavedVoiceCommand(
                        id = savedVoiceCommand.id,
                        name = phrase,
                        firebaseKey = savedVoiceCommand.firebaseKey,
                        type = type
                    )
                )
            }
            verify(exactly = 0) { navigation.pop() }

            verify { navigation.enableProgress(progress = any()) }
            verify { navigation.disableProgress() }
            verify { navigation.alert(alert = any()) }
        }

        @Test
        fun `when getVoiceCommandByName returns back null should dismiss progress and show alert`() = runTest {
            val phrase = "phrase"
            val phraseTwo = "phrase2"
            val type = VoiceCommandTypes.Stop

            every { savedStateHandle.get<String>("recordedPhraseParam") } returns phraseTwo

            viewModel = CreateEditVoiceCommandViewModel(
                savedStateHandle = savedStateHandle,
                application = application,
                scope = scope,
                createFirebaseUserInfo = createFirebaseUserInfo,
                updateFirebaseUserInfo = updateFirebaseUserInfo,
                deleteFirebaseUserInfo = deleteFirebaseUserInfo,
                savedVoiceCommandRepository = savedVoiceCommandRepository,
                navigation = navigation
            )

            coEvery { savedVoiceCommandRepository.getVoiceCommandByName(name = phraseTwo) } returns savedVoiceCommand
            coEvery { savedVoiceCommandRepository.getVoiceCommandByName(name = phraseTwo) } returns null

            viewModel.onEditVoiceCommand(phrase = phrase, type = type)

            coVerify(exactly = 0) {
                savedVoiceCommandRepository.updateSavedVoiceCommand(
                    savedVoiceCommand = SavedVoiceCommand(
                        id = savedVoiceCommand.id,
                        name = phrase,
                        firebaseKey = savedVoiceCommand.firebaseKey,
                        type = type
                    )
                )
            }
            verify(exactly = 0) { navigation.pop() }

            verify { navigation.enableProgress(progress = any()) }
            verify { navigation.disableProgress() }
            verify { navigation.alert(alert = any()) }
        }

        @Test
        fun `when updateSavedVoiceCommand returns back false should dismiss progress and show alert`() = runTest {
            val phrase = "phrase"
            val phraseTwo = "phrase2"
            val type = VoiceCommandTypes.Stop

            every { savedStateHandle.get<String>("recordedPhraseParam") } returns phraseTwo

            viewModel = CreateEditVoiceCommandViewModel(
                savedStateHandle = savedStateHandle,
                application = application,
                scope = scope,
                createFirebaseUserInfo = createFirebaseUserInfo,
                updateFirebaseUserInfo = updateFirebaseUserInfo,
                deleteFirebaseUserInfo = deleteFirebaseUserInfo,
                savedVoiceCommandRepository = savedVoiceCommandRepository,
                navigation = navigation
            )

            coEvery { savedVoiceCommandRepository.getVoiceCommandByName(name = phraseTwo) } returns savedVoiceCommand
            coEvery { savedVoiceCommandRepository.getVoiceCommandByName(name = phraseTwo) } returns savedVoiceCommand
            coEvery {
                updateFirebaseUserInfo.updateSavedVoiceCommand(
                    savedVoiceCommandRealtimeWithKeyResponse = SavedVoiceCommandRealtimeWithKeyResponse(
                        savedVoiceCommandKey = savedVoiceCommand.firebaseKey,
                        savedVoiceCommandInfo = SavedVoiceCommandRealtimeResponse(
                            name = phrase,
                            typeValue = savedVoiceCommand.type.value
                        )

                    )
                )
            } returns flowOf(value = false)

            viewModel.onEditVoiceCommand(phrase = phrase, type = type)

            coVerify(exactly = 0) {
                savedVoiceCommandRepository.updateSavedVoiceCommand(
                    savedVoiceCommand = SavedVoiceCommand(
                        id = savedVoiceCommand.id,
                        name = phrase,
                        firebaseKey = savedVoiceCommand.firebaseKey,
                        type = type
                    )
                )
            }
            verify(exactly = 0) { navigation.pop() }

            verify { navigation.enableProgress(progress = any()) }
            verify { navigation.disableProgress() }
            verify { navigation.alert(alert = any()) }
        }

        @Test
        fun `when updateSavedVoiceCommand returns back true should dismiss progress, pop, and show alert`() = runTest {
            val phrase = "phrase"
            val phraseTwo = "phrase2"
            val type = VoiceCommandTypes.Stop

            every { savedStateHandle.get<String>("recordedPhraseParam") } returns phraseTwo

            viewModel = CreateEditVoiceCommandViewModel(
                savedStateHandle = savedStateHandle,
                application = application,
                scope = scope,
                createFirebaseUserInfo = createFirebaseUserInfo,
                updateFirebaseUserInfo = updateFirebaseUserInfo,
                deleteFirebaseUserInfo = deleteFirebaseUserInfo,
                savedVoiceCommandRepository = savedVoiceCommandRepository,
                navigation = navigation
            )

            coEvery { savedVoiceCommandRepository.getVoiceCommandByName(name = phraseTwo) } returns savedVoiceCommand
            coEvery { savedVoiceCommandRepository.getVoiceCommandByName(name = phraseTwo) } returns savedVoiceCommand
            coEvery {
                updateFirebaseUserInfo.updateSavedVoiceCommand(
                    savedVoiceCommandRealtimeWithKeyResponse = SavedVoiceCommandRealtimeWithKeyResponse(
                        savedVoiceCommandKey = savedVoiceCommand.firebaseKey,
                        savedVoiceCommandInfo = SavedVoiceCommandRealtimeResponse(
                            name = phrase,
                            typeValue = savedVoiceCommand.type.value
                        )

                    )
                )
            } returns flowOf(value = true)

            viewModel.onEditVoiceCommand(phrase = phrase, type = type)

            coVerify {
                savedVoiceCommandRepository.updateSavedVoiceCommand(
                    savedVoiceCommand = SavedVoiceCommand(
                        id = savedVoiceCommand.id,
                        name = phrase,
                        firebaseKey = savedVoiceCommand.firebaseKey,
                        type = type
                    )
                )
            }
            verify { navigation.pop() }
            verify { navigation.enableProgress(progress = any()) }
            verify { navigation.disableProgress() }
            verify { navigation.alert(alert = any()) }
        }
    }

    @Nested
    inner class OnSaveNewVoiceCommand {

        @Test
        fun `when phrase is set to null should dismiss progress and show alert`() = runTest {
            every { savedStateHandle.get<Int>("voiceCommandTypeValueParam") } returns voiceCommandTypeParam
            every { savedStateHandle.get<String>("recordedPhraseParam") } returns null

            viewModel = CreateEditVoiceCommandViewModel(
                savedStateHandle = savedStateHandle,
                application = application,
                scope = scope,
                createFirebaseUserInfo = createFirebaseUserInfo,
                updateFirebaseUserInfo = updateFirebaseUserInfo,
                deleteFirebaseUserInfo = deleteFirebaseUserInfo,
                savedVoiceCommandRepository = savedVoiceCommandRepository,
                navigation = navigation
            )

            viewModel.onSaveNewVoiceCommand()

            verify(exactly = 0) { navigation.pop() }

            verify { navigation.enableProgress(progress = any()) }
            verify { navigation.disableProgress() }
            verify { navigation.alert(alert = any()) }
        }

        @Test
        fun `when phrase is not set to null and attemptToCreateSavedVoiceCommandFirebaseRealtimeDatabaseResponseFlow returns back false should show dismiss progress and show alert`() = runTest {
            every { savedStateHandle.get<Int>("voiceCommandTypeValueParam") } returns voiceCommandTypeParam
            every { savedStateHandle.get<String>("recordedPhraseParam") } returns recordedPhraseParam

            viewModel = CreateEditVoiceCommandViewModel(
                savedStateHandle = savedStateHandle,
                application = application,
                scope = scope,
                createFirebaseUserInfo = createFirebaseUserInfo,
                updateFirebaseUserInfo = updateFirebaseUserInfo,
                deleteFirebaseUserInfo = deleteFirebaseUserInfo,
                savedVoiceCommandRepository = savedVoiceCommandRepository,
                navigation = navigation
            )

            coEvery {
                createFirebaseUserInfo.attemptToCreateSavedVoiceCommandFirebaseRealtimeDatabaseResponseFlow(
                    savedVoiceCommandRealtimeResponse = SavedVoiceCommandRealtimeResponse(
                        name = recordedPhraseParam,
                        typeValue = voiceCommandTypeParam
                    )
                )
            } returns flowOf(value = Pair(false, "firebaseKey"))

            viewModel.onSaveNewVoiceCommand()

            verify(exactly = 0) { navigation.pop() }

            verify { navigation.enableProgress(progress = any()) }
            verify { navigation.disableProgress() }
            verify { navigation.alert(alert = any()) }
        }

        @Test
        fun `when phrase is not set to null and attemptToCreateSavedVoiceCommandFirebaseRealtimeDatabaseResponseFlow returns back empty firebase key should show dismiss progress and show alert`() = runTest {
            every { savedStateHandle.get<Int>("voiceCommandTypeValueParam") } returns voiceCommandTypeParam
            every { savedStateHandle.get<String>("recordedPhraseParam") } returns recordedPhraseParam

            viewModel = CreateEditVoiceCommandViewModel(
                savedStateHandle = savedStateHandle,
                application = application,
                scope = scope,
                createFirebaseUserInfo = createFirebaseUserInfo,
                updateFirebaseUserInfo = updateFirebaseUserInfo,
                deleteFirebaseUserInfo = deleteFirebaseUserInfo,
                savedVoiceCommandRepository = savedVoiceCommandRepository,
                navigation = navigation
            )

            coEvery {
                createFirebaseUserInfo.attemptToCreateSavedVoiceCommandFirebaseRealtimeDatabaseResponseFlow(
                    savedVoiceCommandRealtimeResponse = SavedVoiceCommandRealtimeResponse(
                        name = recordedPhraseParam,
                        typeValue = voiceCommandTypeParam
                    )
                )
            } returns flowOf(value = Pair(true, ""))

            viewModel.onSaveNewVoiceCommand()

            verify(exactly = 0) { navigation.pop() }

            verify { navigation.enableProgress(progress = any()) }
            verify { navigation.disableProgress() }
            verify { navigation.alert(alert = any()) }
        }

        @Test
        fun `when phrase is not set to null and attemptToCreateSavedVoiceCommandFirebaseRealtimeDatabaseResponseFlow returns back null firebase key should show dismiss progress and show alert`() = runTest {
            every { savedStateHandle.get<Int>("voiceCommandTypeValueParam") } returns voiceCommandTypeParam
            every { savedStateHandle.get<String>("recordedPhraseParam") } returns recordedPhraseParam

            viewModel = CreateEditVoiceCommandViewModel(
                savedStateHandle = savedStateHandle,
                application = application,
                scope = scope,
                createFirebaseUserInfo = createFirebaseUserInfo,
                updateFirebaseUserInfo = updateFirebaseUserInfo,
                deleteFirebaseUserInfo = deleteFirebaseUserInfo,
                savedVoiceCommandRepository = savedVoiceCommandRepository,
                navigation = navigation
            )

            coEvery {
                createFirebaseUserInfo.attemptToCreateSavedVoiceCommandFirebaseRealtimeDatabaseResponseFlow(
                    savedVoiceCommandRealtimeResponse = SavedVoiceCommandRealtimeResponse(
                        name = recordedPhraseParam,
                        typeValue = voiceCommandTypeParam
                    )
                )
            } returns flowOf(value = Pair(true, null))

            viewModel.onSaveNewVoiceCommand()

            verify(exactly = 0) { navigation.pop() }

            verify { navigation.enableProgress(progress = any()) }
            verify { navigation.disableProgress() }
            verify { navigation.alert(alert = any()) }
        }

        @Test
        fun `when phrase is not set to null and attemptToCreateSavedVoiceCommandFirebaseRealtimeDatabaseResponseFlow returns back true and firebase key should show dismiss progress, pop and show alert`() = runTest {
            every { savedStateHandle.get<Int>("voiceCommandTypeValueParam") } returns voiceCommandTypeParam
            every { savedStateHandle.get<String>("recordedPhraseParam") } returns recordedPhraseParam

            viewModel = CreateEditVoiceCommandViewModel(
                savedStateHandle = savedStateHandle,
                application = application,
                scope = scope,
                createFirebaseUserInfo = createFirebaseUserInfo,
                updateFirebaseUserInfo = updateFirebaseUserInfo,
                deleteFirebaseUserInfo = deleteFirebaseUserInfo,
                savedVoiceCommandRepository = savedVoiceCommandRepository,
                navigation = navigation
            )

            viewModel.onPhraseCaptured(phrase = recordedPhraseParam)

            coEvery {
                createFirebaseUserInfo.attemptToCreateSavedVoiceCommandFirebaseRealtimeDatabaseResponseFlow(
                    savedVoiceCommandRealtimeResponse = SavedVoiceCommandRealtimeResponse(
                        name = recordedPhraseParam,
                        typeValue = VoiceCommandTypes.None.value
                    )
                )
            } returns flowOf(value = Pair(true, "firebasekey"))
            coEvery { savedVoiceCommandRepository.getVoiceCommandSize() } returns 2

            viewModel.onSaveNewVoiceCommand()

            coVerify { savedVoiceCommandRepository.createSavedVoiceCommand(
                savedVoiceCommand = SavedVoiceCommand(
                    id = 3,
                    name = recordedPhraseParam,
                    firebaseKey = "firebasekey",
                    type = VoiceCommandTypes.None
                )
            )
            }
            verify { navigation.pop() }
            verify { navigation.enableProgress(progress = any()) }
            verify { navigation.disableProgress() }
            verify { navigation.alert(alert = any()) }
        }
    }
}
