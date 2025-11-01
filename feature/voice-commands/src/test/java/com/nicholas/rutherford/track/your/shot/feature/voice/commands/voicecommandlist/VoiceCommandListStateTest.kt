package com.nicholas.rutherford.track.your.shot.feature.voice.commands.voicecommandlist

import com.nicholas.rutherford.track.your.shot.base.test.BaseTest
import com.nicholas.rutherford.track.your.shot.data.room.response.SavedVoiceCommand
import com.nicholas.rutherford.track.your.shot.data.room.response.VoiceCommandTypes
import com.nicholas.rutherford.track.your.shot.feature.voice.commands.VoiceCommandFilter
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class VoiceCommandListStateTest : BaseTest() {

    private lateinit var state: VoiceCommandListState

    @BeforeEach
    fun beforeEach() {
        state = VoiceCommandListState()
    }

    @Nested
    inner class HasSingleStartCommand {

        @Test
        fun `when start commands list entry size is not 1 should return false`() {
            state = state.copy(startCommands = emptyList())

            Assertions.assertEquals(state.hasSingleStartCommand, false)
        }

        @Test
        fun `when start commands list entry size is 1 should return true`() {
            state = state.copy(
                startCommands = listOf(
                    SavedVoiceCommand(
                        id = 1,
                        name = "Let's Go",
                        firebaseKey = "key",
                        type = VoiceCommandTypes.Start
                    )
                )
            )

            Assertions.assertEquals(state.hasSingleStartCommand, true)
        }
    }

    @Nested
    inner class HasSingleStopCommand {

        @Test
        fun `when stop commands list entry size is not 1 should return false`() {
            state = state.copy(stopCommands = emptyList())

            Assertions.assertEquals(state.hasSingleStopCommand, false)
        }

        @Test
        fun `when stop commands list entry size is 1 should return true`() {
            state = state.copy(
                stopCommands = listOf(
                    SavedVoiceCommand(
                        id = 1,
                        name = "Let's Go",
                        firebaseKey = "key",
                        type = VoiceCommandTypes.Start
                    )
                )
            )

            Assertions.assertEquals(state.hasSingleStopCommand, true)
        }
    }

    @Nested
    inner class HasSingleMakeCommand {

        @Test
        fun `when make commands list entry size is not 1 should return false`() {
            state = state.copy(makeCommands = emptyList())

            Assertions.assertEquals(state.hasSingleMakeCommand, false)
        }

        @Test
        fun `when make commands entry size is 1 should return true`() {
            state = state.copy(
                makeCommands = listOf(
                    SavedVoiceCommand(
                        id = 1,
                        name = "Let's Go",
                        firebaseKey = "key",
                        type = VoiceCommandTypes.Start
                    )
                )
            )

            Assertions.assertEquals(state.hasSingleMakeCommand, true)
        }
    }

    @Nested
    inner class HasSingleMissCommand {

        @Test
        fun `when miss commands list entry size is not 1 should return false`() {
            state = state.copy(missCommands = emptyList())

            Assertions.assertEquals(state.hasSingleMissCommand, false)
        }

        @Test
        fun `when miss commands list entry size is 1 should return true`() {
            state = state.copy(
                missCommands = listOf(
                    SavedVoiceCommand(
                        id = 1,
                        name = "Let's Go",
                        firebaseKey = "key",
                        type = VoiceCommandTypes.Start
                    )
                )
            )

            Assertions.assertEquals(state.hasSingleMissCommand, true)
        }
    }

    @Nested
    inner class HasSingleCommandForSelectedFilter {

        @Test
        fun `when voice filter type is start should return false if hasSingleStartCommand returns false`() {
            state = state.copy(
                selectedFilter = VoiceCommandFilter.START,
                startCommands = emptyList()
            )

            Assertions.assertEquals(state.hasSingleCommandForSelectedFilter, false)
        }

        @Test
        fun `when voice filter type is start should return true if hasSingleStartCommand returns true`() {
            state = state.copy(
                selectedFilter = VoiceCommandFilter.START,
                startCommands = listOf(
                    SavedVoiceCommand(
                        id = 1,
                        name = "Let's Go",
                        firebaseKey = "key",
                        type = VoiceCommandTypes.Start
                    )
                )
            )

            Assertions.assertEquals(state.hasSingleCommandForSelectedFilter, true)
        }

        @Test
        fun `when voice filter type is stop should return false if hasSingleStopCommand returns false`() {
            state = state.copy(
                selectedFilter = VoiceCommandFilter.STOP,
                stopCommands = emptyList()
            )

            Assertions.assertEquals(state.hasSingleCommandForSelectedFilter, false)
        }

        @Test
        fun `when voice filter type is stop should return true if hasSingleStopCommand returns true`() {
            state = state.copy(
                selectedFilter = VoiceCommandFilter.STOP,
                stopCommands = listOf(
                    SavedVoiceCommand(
                        id = 1,
                        name = "Let's Go",
                        firebaseKey = "key",
                        type = VoiceCommandTypes.Start
                    )
                )
            )

            Assertions.assertEquals(state.hasSingleCommandForSelectedFilter, true)
        }

        @Test
        fun `when voice filter type is make should return false if hasSingleMakeCommand returns false`() {
            state = state.copy(
                selectedFilter = VoiceCommandFilter.MAKE,
                makeCommands = emptyList()
            )

            Assertions.assertEquals(state.hasSingleCommandForSelectedFilter, false)
        }

        @Test
        fun `when voice filter type is make should return true if hasSingleMakeCommand returns true`() {
            state = state.copy(
                selectedFilter = VoiceCommandFilter.MAKE,
                makeCommands = listOf(
                    SavedVoiceCommand(
                        id = 1,
                        name = "Let's Go",
                        firebaseKey = "key",
                        type = VoiceCommandTypes.Start
                    )
                )
            )

            Assertions.assertEquals(state.hasSingleCommandForSelectedFilter, true)
        }

        @Test
        fun `when voice filter type is miss should return false if hasSingleMissCommand returns false`() {
            state = state.copy(
                selectedFilter = VoiceCommandFilter.MISS,
                missCommands = emptyList()
            )

            Assertions.assertEquals(state.hasSingleCommandForSelectedFilter, false)
        }

        @Test
        fun `when voice filter type is miss should return true if hasSingleMissCommand returns true`() {
            state = state.copy(
                selectedFilter = VoiceCommandFilter.MISS,
                missCommands = listOf(
                    SavedVoiceCommand(
                        id = 1,
                        name = "Let's Go",
                        firebaseKey = "key",
                        type = VoiceCommandTypes.Start
                    )
                )
            )

            Assertions.assertEquals(state.hasSingleCommandForSelectedFilter, true)
        }
    }
}
