package com.nicholas.rutherford.track.your.shot.feature.voice.commands.voicecommandlist

import androidx.lifecycle.LifecycleOwner
import com.nicholas.rutherford.track.your.shot.base.test.BaseTest
import com.nicholas.rutherford.track.your.shot.data.room.repository.SavedVoiceCommandRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.SavedVoiceCommand
import com.nicholas.rutherford.track.your.shot.data.room.response.VoiceCommandTypes
import com.nicholas.rutherford.track.your.shot.feature.voice.commands.VoiceCommandFilter
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class VoiceCommandListViewModelTest : BaseTest() {

    private lateinit var viewModel: VoiceCommandListViewModel

    private val lifecycleOwner = mockk<LifecycleOwner>(relaxed = true)

    internal val navigation = mockk<VoiceCommandListNavigation>(relaxed = true)

    internal val savedVoiceCommandRepository = mockk<SavedVoiceCommandRepository>(relaxed = true)

    val dispatcher = StandardTestDispatcher()

    private val scope = CoroutineScope(SupervisorJob() + dispatcher)

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun beforeEach() {
        Dispatchers.setMain(dispatcher)

        viewModel = VoiceCommandListViewModel(
            scope = scope,
            navigation = navigation,
            savedVoiceCommandRepository = savedVoiceCommandRepository
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterEach
    fun afterEach() {
        Dispatchers.resetMain()
    }

    @Nested
    inner class OnCreate {

        @Test
        fun `when get all voice commands returns a empty list should not update state`() = runTest {
            val emptySavedVoiceCommandList: List<SavedVoiceCommand> = listOf()

            coEvery { savedVoiceCommandRepository.getAllVoiceCommands() } returns emptySavedVoiceCommandList

            viewModel = VoiceCommandListViewModel(
                scope = scope,
                navigation = navigation,
                savedVoiceCommandRepository = savedVoiceCommandRepository
            )

            viewModel.onCreate(owner = lifecycleOwner)

            dispatcher.scheduler.advanceUntilIdle()

            val state = viewModel.voiceCommandMutableStateFlow.value

            val expectedState = VoiceCommandListState(
                startCommands = emptySavedVoiceCommandList,
                stopCommands = emptySavedVoiceCommandList,
                makeCommands = emptySavedVoiceCommandList,
                missCommands = emptySavedVoiceCommandList,
                selectedFilter = VoiceCommandFilter.START,
                filteredCommands = emptySavedVoiceCommandList
            )

            Assertions.assertEquals(expectedState, state)
        }

        @Test
        fun `when get all voice commands returns a list of saved voice commands should update state`() = runTest {
            val savedCommands = listOf(
                SavedVoiceCommand(id = 0, name = "Start", firebaseKey = "key", type = VoiceCommandTypes.Start),
                SavedVoiceCommand(id = 1, name = "Stop", firebaseKey = "key", type = VoiceCommandTypes.Stop),
                SavedVoiceCommand(id = 2, name = "Make", firebaseKey = "key", type = VoiceCommandTypes.Make),
                SavedVoiceCommand(id = 3, name = "Miss", firebaseKey = "key", type = VoiceCommandTypes.Miss)
            )

            coEvery { savedVoiceCommandRepository.getAllVoiceCommands() } returns savedCommands

            viewModel = VoiceCommandListViewModel(
                scope = scope,
                navigation = navigation,
                savedVoiceCommandRepository = savedVoiceCommandRepository
            )

            viewModel.onCreate(owner = lifecycleOwner)

            dispatcher.scheduler.advanceUntilIdle()

            val state = viewModel.voiceCommandMutableStateFlow.value

            val expectedState = VoiceCommandListState(
                startCommands = listOf(SavedVoiceCommand(id = 0, name = "Start", firebaseKey = "key", type = VoiceCommandTypes.Start)),
                stopCommands = listOf(SavedVoiceCommand(id = 1, name = "Stop", firebaseKey = "key", type = VoiceCommandTypes.Stop)),
                makeCommands = listOf(SavedVoiceCommand(id = 2, name = "Make", firebaseKey = "key", type = VoiceCommandTypes.Make)),
                missCommands = listOf(SavedVoiceCommand(id = 3, name = "Miss", firebaseKey = "key", type = VoiceCommandTypes.Miss)),
                selectedFilter = VoiceCommandFilter.START,
                filteredCommands = listOf(SavedVoiceCommand(id = 0, name = "Start", firebaseKey = "key", type = VoiceCommandTypes.Start))
            )

            Assertions.assertEquals(expectedState, state)
        }
    }

    @Test
    fun `on toolbar menu clicked should call open navigation drawer`() {
        viewModel.onToolbarMenuClicked()

        verify { navigation.openNavigationDrawer() }
    }

    @Test
    fun `on filter selected should update state`() {
        val selectedFilter = VoiceCommandFilter.START

        viewModel.voiceCommandMutableStateFlow.value = VoiceCommandListState(
            selectedFilter = VoiceCommandFilter.START,
            startCommands = listOf(SavedVoiceCommand(id = 0, name = "Start", firebaseKey = "key", type = VoiceCommandTypes.Start))
        )

        viewModel.onFilterSelected(filter = selectedFilter)

        val state = viewModel.voiceCommandMutableStateFlow.value

        Assertions.assertEquals(
            state,
            VoiceCommandListState(
                selectedFilter = VoiceCommandFilter.START,
                startCommands = listOf(SavedVoiceCommand(id = 0, name = "Start", firebaseKey = "key", type = VoiceCommandTypes.Start)),
                filteredCommands = listOf(SavedVoiceCommand(id = 0, name = "Start", firebaseKey = "key", type = VoiceCommandTypes.Start))
            )
        )
    }

    @Nested
    inner class BuildFilteredSavedCommands {

        @Test
        fun `when selectedFilter is START should return startCommands`() {
            val startCommands = listOf(
                SavedVoiceCommand(id = 1, name = "Start Command 1", firebaseKey = "key", type = VoiceCommandTypes.Start),
                SavedVoiceCommand(id = 2, name = "Start Command 2", firebaseKey = "key", type = VoiceCommandTypes.Start)
            )
            val stopCommands = listOf(
                SavedVoiceCommand(id = 3, name = "Stop Command 1", firebaseKey = "key", type = VoiceCommandTypes.Stop)
            )
            val makeCommands = listOf(
                SavedVoiceCommand(id = 4, name = "Make Command 1", firebaseKey = "key", type = VoiceCommandTypes.Make)
            )
            val missCommands = listOf(
                SavedVoiceCommand(id = 5, name = "Miss Command 1", firebaseKey = "key", type = VoiceCommandTypes.Miss)
            )

            val result = viewModel.buildFilteredSavedCommands(
                selectedFilter = VoiceCommandFilter.START,
                startCommands = startCommands,
                stopCommands = stopCommands,
                makeCommands = makeCommands,
                missCommands = missCommands
            )

            Assertions.assertEquals(startCommands, result)
        }

        @Test
        fun `when selectedFilter is STOP should return stopCommands`() {
            val startCommands = listOf(
                SavedVoiceCommand(id = 1, name = "Start Command 1", firebaseKey = "key", type = VoiceCommandTypes.Start)
            )
            val stopCommands = listOf(
                SavedVoiceCommand(id = 2, name = "Stop Command 1", firebaseKey = "key", type = VoiceCommandTypes.Stop),
                SavedVoiceCommand(id = 3, name = "Stop Command 2", firebaseKey = "key", type = VoiceCommandTypes.Stop)
            )
            val makeCommands = listOf(
                SavedVoiceCommand(id = 4, name = "Make Command 1", firebaseKey = "key", type = VoiceCommandTypes.Make)
            )
            val missCommands = listOf(
                SavedVoiceCommand(id = 5, name = "Miss Command 1", firebaseKey = "key", type = VoiceCommandTypes.Miss)
            )

            val result = viewModel.buildFilteredSavedCommands(
                selectedFilter = VoiceCommandFilter.STOP,
                startCommands = startCommands,
                stopCommands = stopCommands,
                makeCommands = makeCommands,
                missCommands = missCommands
            )

            Assertions.assertEquals(stopCommands, result)
        }

        @Test
        fun `when selectedFilter is MAKE should return makeCommands`() {
            val startCommands = listOf(
                SavedVoiceCommand(id = 1, name = "Start Command 1", firebaseKey = "key", type = VoiceCommandTypes.Start)
            )
            val stopCommands = listOf(
                SavedVoiceCommand(id = 2, name = "Stop Command 1", firebaseKey = "key", type = VoiceCommandTypes.Stop)
            )
            val makeCommands = listOf(
                SavedVoiceCommand(id = 3, name = "Make Command 1", firebaseKey = "key", type = VoiceCommandTypes.Make),
                SavedVoiceCommand(id = 4, name = "Make Command 2", firebaseKey = "key", type = VoiceCommandTypes.Make),
                SavedVoiceCommand(id = 5, name = "Make Command 3", firebaseKey = "key", type = VoiceCommandTypes.Make)
            )
            val missCommands = listOf(
                SavedVoiceCommand(id = 6, name = "Miss Command 1", firebaseKey = "key", type = VoiceCommandTypes.Miss)
            )

            val result = viewModel.buildFilteredSavedCommands(
                selectedFilter = VoiceCommandFilter.MAKE,
                startCommands = startCommands,
                stopCommands = stopCommands,
                makeCommands = makeCommands,
                missCommands = missCommands
            )

            Assertions.assertEquals(makeCommands, result)
        }

        @Test
        fun `when selectedFilter is MISS should return missCommands`() {
            val startCommands = listOf(
                SavedVoiceCommand(id = 1, name = "Start Command 1", firebaseKey = "key", type = VoiceCommandTypes.Start)
            )
            val stopCommands = listOf(
                SavedVoiceCommand(id = 2, name = "Stop Command 1", firebaseKey = "key", type = VoiceCommandTypes.Stop)
            )
            val makeCommands = listOf(
                SavedVoiceCommand(id = 3, name = "Make Command 1", firebaseKey = "key", type = VoiceCommandTypes.Make)
            )
            val missCommands = listOf(
                SavedVoiceCommand(id = 4, name = "Miss Command 1", firebaseKey = "key", type = VoiceCommandTypes.Miss),
                SavedVoiceCommand(id = 5, name = "Miss Command 2", firebaseKey = "key", type = VoiceCommandTypes.Miss)
            )

            val result = viewModel.buildFilteredSavedCommands(
                selectedFilter = VoiceCommandFilter.MISS,
                startCommands = startCommands,
                stopCommands = stopCommands,
                makeCommands = makeCommands,
                missCommands = missCommands
            )

            Assertions.assertEquals(missCommands, result)
        }

        @Test
        fun `when selectedFilter is MISS should return missCommands for else case`() {
            val startCommands = listOf(
                SavedVoiceCommand(id = 1, name = "Start Command 1", firebaseKey = "key", type = VoiceCommandTypes.Start)
            )
            val stopCommands = listOf(
                SavedVoiceCommand(id = 2, name = "Stop Command 1", firebaseKey = "key", type = VoiceCommandTypes.Stop)
            )
            val makeCommands = listOf(
                SavedVoiceCommand(id = 3, name = "Make Command 1", firebaseKey = "key", type = VoiceCommandTypes.Make)
            )
            val missCommands = listOf(
                SavedVoiceCommand(id = 4, name = "Miss Command 1", firebaseKey = "key", type = VoiceCommandTypes.Miss),
                SavedVoiceCommand(id = 5, name = "Miss Command 2", firebaseKey = "key", type = VoiceCommandTypes.Miss)
            )

            // Test the else case by using MISS filter (which goes to the else branch)
            val result = viewModel.buildFilteredSavedCommands(
                selectedFilter = VoiceCommandFilter.MISS,
                startCommands = startCommands,
                stopCommands = stopCommands,
                makeCommands = makeCommands,
                missCommands = missCommands
            )

            Assertions.assertEquals(missCommands, result)
        }

        @Test
        fun `when only startCommands has items should return only startCommands for START filter`() {
            val startCommands = listOf(
                SavedVoiceCommand(id = 1, name = "Start Command 1", firebaseKey = "key", type = VoiceCommandTypes.Start)
            )
            val emptyList: List<SavedVoiceCommand> = emptyList()

            val result = viewModel.buildFilteredSavedCommands(
                selectedFilter = VoiceCommandFilter.START,
                startCommands = startCommands,
                stopCommands = emptyList,
                makeCommands = emptyList,
                missCommands = emptyList
            )

            Assertions.assertEquals(startCommands, result)
        }

        @Test
        fun `when only stopCommands has items should return only stopCommands for STOP filter`() {
            val stopCommands = listOf(
                SavedVoiceCommand(id = 1, name = "Stop Command 1", firebaseKey = "key", type = VoiceCommandTypes.Stop)
            )
            val emptyList: List<SavedVoiceCommand> = emptyList()

            val result = viewModel.buildFilteredSavedCommands(
                selectedFilter = VoiceCommandFilter.STOP,
                startCommands = emptyList,
                stopCommands = stopCommands,
                makeCommands = emptyList,
                missCommands = emptyList
            )

            Assertions.assertEquals(stopCommands, result)
        }

        @Test
        fun `when only makeCommands has items should return only makeCommands for MAKE filter`() {
            val makeCommands = listOf(
                SavedVoiceCommand(id = 1, name = "Make Command 1", firebaseKey = "key", type = VoiceCommandTypes.Make)
            )
            val emptyList: List<SavedVoiceCommand> = emptyList()

            val result = viewModel.buildFilteredSavedCommands(
                selectedFilter = VoiceCommandFilter.MAKE,
                startCommands = emptyList,
                stopCommands = emptyList,
                makeCommands = makeCommands,
                missCommands = emptyList
            )

            Assertions.assertEquals(makeCommands, result)
        }

        @Test
        fun `when only missCommands has items should return only missCommands for MISS filter`() {
            val missCommands = listOf(
                SavedVoiceCommand(id = 1, name = "Miss Command 1", firebaseKey = "key", type = VoiceCommandTypes.Miss)
            )
            val emptyList: List<SavedVoiceCommand> = emptyList()

            val result = viewModel.buildFilteredSavedCommands(
                selectedFilter = VoiceCommandFilter.MISS,
                startCommands = emptyList,
                stopCommands = emptyList,
                makeCommands = emptyList,
                missCommands = missCommands
            )

            Assertions.assertEquals(missCommands, result)
        }
    }

    @Test
    fun `on create edit command type clicked should call navigateToCreateEditVoiceCommand`() = runTest {
        val type = 2
        val phrase = "Hello World"

        viewModel.onCreateEditCommandTypeClicked(type = type, phrase = phrase)

        verify { navigation.navigateToCreateEditVoiceCommand(type = type, phrase = phrase) }
    }
}
