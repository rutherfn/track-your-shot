package com.nicholas.rutherford.track.your.shot.feature.settings.debugtoggle

import com.nicholas.rutherford.track.your.shot.data.store.reader.DataStorePreferencesReader
import com.nicholas.rutherford.track.your.shot.data.store.writer.DataStorePreferencesWriter
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class DebugToggleViewModelTest {

    private lateinit var debugToggleViewModel: DebugToggleViewModel

    private val dataStorePreferencesReader = mockk<DataStorePreferencesReader>(relaxed = true)
    private val dataStoreWriterPreferencesWriter = mockk<DataStorePreferencesWriter>(relaxed = true)

    private var navigation = mockk<DebugToggleNavigation>(relaxed = true)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val dispatcher = StandardTestDispatcher()

    private val scope = CoroutineScope(SupervisorJob() + dispatcher)

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach()
    fun beforeEach() {
        Dispatchers.setMain(dispatcher)

        every { dataStorePreferencesReader.readVoiceToggledDebugEnabledFlow() } returns flowOf(false)
        every { dataStorePreferencesReader.readUploadVideoToggledDebugEnabled() } returns flowOf(false)

        debugToggleViewModel = DebugToggleViewModel(
            dataStorePreferencesReader = dataStorePreferencesReader,
            dataStoreWriterPreferencesWriter = dataStoreWriterPreferencesWriter,
            navigation = navigation,
            scope = scope
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterEach
    fun afterEach() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init should set initial state`() {
        dispatcher.scheduler.advanceUntilIdle()

        Assertions.assertEquals(
            debugToggleViewModel.debugToggleMutableStateFlow.value,
            DebugToggleState(
                voiceToggledState = false,
                videoUploadToggleState = false
            )
        )
    }

    @Test
    fun `onBackClicked should call navigation pop`() {
        debugToggleViewModel.onBackClicked()

        verify { navigation.pop() }
    }

    @Nested
    inner class StateFlowUpdates {

        @Test
        fun `should update state when voice toggle flow emits true`() {
            every { dataStorePreferencesReader.readVoiceToggledDebugEnabledFlow() } returns flowOf(true)
            every { dataStorePreferencesReader.readUploadVideoToggledDebugEnabled() } returns flowOf(false)

            debugToggleViewModel.collectToggleFlows()
            dispatcher.scheduler.advanceUntilIdle()

            Assertions.assertEquals(
                debugToggleViewModel.debugToggleMutableStateFlow.value,
                DebugToggleState(
                    voiceToggledState = true,
                    videoUploadToggleState = false
                )
            )
        }

        @Test
        fun `should update state when video upload toggle flow emits true`() {
            every { dataStorePreferencesReader.readVoiceToggledDebugEnabledFlow() } returns flowOf(false)
            every { dataStorePreferencesReader.readUploadVideoToggledDebugEnabled() } returns flowOf(true)

            debugToggleViewModel.collectToggleFlows()
            dispatcher.scheduler.advanceUntilIdle()

            Assertions.assertEquals(
                debugToggleViewModel.debugToggleMutableStateFlow.value,
                DebugToggleState(
                    voiceToggledState = false,
                    videoUploadToggleState = true
                )
            )
        }

        @Test
        fun `should update state when both toggle flows emit true`() {
            every { dataStorePreferencesReader.readVoiceToggledDebugEnabledFlow() } returns flowOf(true)
            every { dataStorePreferencesReader.readUploadVideoToggledDebugEnabled() } returns flowOf(true)

            debugToggleViewModel.collectToggleFlows()
            dispatcher.scheduler.advanceUntilIdle()

            Assertions.assertEquals(
                debugToggleViewModel.debugToggleMutableStateFlow.value,
                DebugToggleState(
                    voiceToggledState = true,
                    videoUploadToggleState = true
                )
            )
        }
    }

    @Nested
    inner class OnVoiceDebugToggled {

        @Test
        fun `should save voice debug toggle state to dataStore`() = runTest {
            coEvery { dataStoreWriterPreferencesWriter.saveVoiceToggledDebugEnabled(any()) } returns Unit

            debugToggleViewModel.onVoiceDebugToggled(true)
            dispatcher.scheduler.advanceUntilIdle()

            coVerify { dataStoreWriterPreferencesWriter.saveVoiceToggledDebugEnabled(value = true) }
        }

        @Test
        fun `should save false voice debug toggle state to dataStore`() = runTest {
            coEvery { dataStoreWriterPreferencesWriter.saveVoiceToggledDebugEnabled(any()) } returns Unit

            debugToggleViewModel.onVoiceDebugToggled(false)
            dispatcher.scheduler.advanceUntilIdle()

            coVerify { dataStoreWriterPreferencesWriter.saveVoiceToggledDebugEnabled(value = false) }
        }
    }

    @Nested
    inner class OnVideoUploadDebugToggled {

        @Test
        fun `should save video upload debug toggle state to dataStore`() = runTest {
            coEvery { dataStoreWriterPreferencesWriter.saveUploadVideoToggledDebugEnabled(any()) } returns Unit

            debugToggleViewModel.onVideoUploadDebugToggled(true)
            dispatcher.scheduler.advanceUntilIdle()

            coVerify { dataStoreWriterPreferencesWriter.saveUploadVideoToggledDebugEnabled(value = true) }
        }

        @Test
        fun `should save false video upload debug toggle state to dataStore`() = runTest {
            coEvery { dataStoreWriterPreferencesWriter.saveUploadVideoToggledDebugEnabled(any()) } returns Unit

            debugToggleViewModel.onVideoUploadDebugToggled(false)
            dispatcher.scheduler.advanceUntilIdle()

            coVerify { dataStoreWriterPreferencesWriter.saveUploadVideoToggledDebugEnabled(value = false) }
        }
    }
}
