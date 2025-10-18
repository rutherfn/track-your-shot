package com.nicholas.rutherford.track.your.shot.room.response

import android.app.Application
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.data.room.response.VoiceCommandTypes
import com.nicholas.rutherford.track.your.shot.data.room.response.VoiceCommandTypes.Companion.examplePhrases
import com.nicholas.rutherford.track.your.shot.data.room.response.VoiceCommandTypes.Companion.toCreateCommandLabel
import com.nicholas.rutherford.track.your.shot.data.room.response.VoiceCommandTypes.Companion.toDisplayLabel
import com.nicholas.rutherford.track.your.shot.data.room.response.VoiceCommandTypes.Companion.toEditVoiceCommandLabel
import com.nicholas.rutherford.track.your.shot.data.room.response.VoiceCommandTypes.Companion.toPlayVoiceCommandsTypes
import com.nicholas.rutherford.track.your.shot.data.room.response.VoiceCommandTypes.Companion.toVoiceCommandValue
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class VoiceCommandTypesTest {

    private var application = mockk<Application>(relaxed = true)

    @Nested
    inner class TypesValidation {

        @Test
        fun `when voice command type is start should contain start value`() {
            val value = VoiceCommandTypes.Start.value

            Assertions.assertEquals(value, Constants.VOICE_COMMAND_START_VALUE)
            Assertions.assertEquals(value, 0)
        }

        @Test
        fun `when voice command type is stop should contain stop value`() {
            val value = VoiceCommandTypes.Stop.value

            Assertions.assertEquals(value, Constants.VOICE_COMMAND_STOP_VALUE)
            Assertions.assertEquals(value, 1)
        }

        @Test
        fun `when voice command is make should contain make value`() {
            val value = VoiceCommandTypes.Make.value

            Assertions.assertEquals(value, Constants.VOICE_COMMAND_MAKE_VALUE)
            Assertions.assertEquals(value, 2)
        }

        @Test
        fun `when voice command is miss should contain make value`() {
            val value = VoiceCommandTypes.Miss.value

            Assertions.assertEquals(value, Constants.VOICE_COMMAND_MISS_VALUE)
            Assertions.assertEquals(value, 3)
        }

        @Test
        fun `when voice command is none should contain none value`() {
            val value = VoiceCommandTypes.None.value

            Assertions.assertEquals(value, Constants.VOICE_COMMAND_NONE_VALUE)
            Assertions.assertEquals(value, 4)
        }
    }

    @Nested
    inner class FromValue {

        @Test
        fun `when value passed in is start value should return start`() {
            val value = VoiceCommandTypes.Companion.fromValue(value = Constants.VOICE_COMMAND_START_VALUE)

            Assertions.assertEquals(value, VoiceCommandTypes.Start)
        }

        @Test
        fun `when value passed in is stop value should return stop`() {
            val value = VoiceCommandTypes.Companion.fromValue(value = Constants.VOICE_COMMAND_STOP_VALUE)

            Assertions.assertEquals(value, VoiceCommandTypes.Stop)
        }

        @Test
        fun `when value passed in is make value should return make`() {
            val value = VoiceCommandTypes.Companion.fromValue(value = Constants.VOICE_COMMAND_MAKE_VALUE)

            Assertions.assertEquals(value, VoiceCommandTypes.Make)
        }

        @Test
        fun `when value passed in is miss value should return miss`() {
            val value = VoiceCommandTypes.Companion.fromValue(value = Constants.VOICE_COMMAND_MISS_VALUE)

            Assertions.assertEquals(value, VoiceCommandTypes.Miss)
        }

        @Test
        fun `when value passed in is not any of the default type values should return none`() {
            val value = VoiceCommandTypes.Companion.fromValue(value = Constants.VOICE_COMMAND_NONE_VALUE)

            Assertions.assertEquals(value, VoiceCommandTypes.None)
        }
    }

    @Nested
    inner class ToDisplayValue {

        @Test
        fun `when voice command types is start should return start value`() {
            val result = VoiceCommandTypes.Start.toDisplayLabel()

            Assertions.assertEquals(result, "Start")
        }

        @Test
        fun `when voice command types is stop should return stop value`() {
            val result = VoiceCommandTypes.Stop.toDisplayLabel()

            Assertions.assertEquals(result, "Stop")
        }

        @Test
        fun `when voice command types is make should return make value`() {
            val result = VoiceCommandTypes.Make.toDisplayLabel()

            Assertions.assertEquals(result, "Make")
        }

        @Test
        fun `when voice command types is miss should return miss value`() {
            val result = VoiceCommandTypes.Miss.toDisplayLabel()

            Assertions.assertEquals(result, "Miss")
        }

        @Test
        fun `when voice command types is none should return none value`() {
            val result = VoiceCommandTypes.None.toDisplayLabel()

            Assertions.assertEquals(result, "None")
        }
    }

    @Nested
    inner class ToCreateCommandLabel {

        @Test
        fun `when voice command types is start should return start command value`() {
            val result = VoiceCommandTypes.Start.toCreateCommandLabel()

            Assertions.assertEquals(result, "Start Voice Command")
        }

        @Test
        fun `when voice command types is stop should return stop command value`() {
            val result = VoiceCommandTypes.Stop.toCreateCommandLabel()

            Assertions.assertEquals(result, "Stop Voice Command")
        }

        @Test
        fun `when voice command types is make should return make command value`() {
            val result = VoiceCommandTypes.Make.toCreateCommandLabel()

            Assertions.assertEquals(result, "Make Voice Command")
        }

        @Test
        fun `when voice command types is miss should return miss command value`() {
            val result = VoiceCommandTypes.Miss.toCreateCommandLabel()

            Assertions.assertEquals(result, "Miss Voice Command")
        }

        @Test
        fun `when voice command types is none should return none command value`() {
            val result = VoiceCommandTypes.None.toCreateCommandLabel()

            Assertions.assertEquals(result, "None")
        }
    }

    @Nested
    inner class ToEditVoiceCommandLabel {

        @Test
        fun `when voice command types is start should return edit command value`() {
            val result = VoiceCommandTypes.Start.toEditVoiceCommandLabel()

            Assertions.assertEquals(result, "Edit Start Voice Command")
        }

        @Test
        fun `when voice command types is stop should return edit command value`() {
            val result = VoiceCommandTypes.Stop.toEditVoiceCommandLabel()

            Assertions.assertEquals(result, "Edit Stop Voice Command")
        }

        @Test
        fun `when voice command types is make should return edit command value`() {
            val result = VoiceCommandTypes.Make.toEditVoiceCommandLabel()

            Assertions.assertEquals(result, "Edit Make Voice Command")
        }

        @Test
        fun `when voice command types is miss should return edit command value`() {
            val result = VoiceCommandTypes.Miss.toEditVoiceCommandLabel()

            Assertions.assertEquals(result, "Edit Miss Voice Command")
        }

        @Test
        fun `when voice command types is none should return none value`() {
            val result = VoiceCommandTypes.None.toEditVoiceCommandLabel()

            Assertions.assertEquals(result, "None")
        }
    }

    @Nested
    inner class ToValueCommandValue {

        @BeforeEach
        fun beforeEach() {
            every { application.getString(StringsIds.start) } returns "Start"
            every { application.getString(StringsIds.stop) } returns "Stop"
            every { application.getString(StringsIds.make) } returns "Make"
            every { application.getString(StringsIds.miss) } returns "Miss"
            every { application.getString(StringsIds.none) } returns "None"
        }

        @Test
        fun `when voice command types is start should return command value`() {
            val result = VoiceCommandTypes.Start.toVoiceCommandValue(application = application)

            Assertions.assertEquals(result, "Start")
        }

        @Test
        fun `when voice command types is stop should return command value`() {
            val result = VoiceCommandTypes.Stop.toVoiceCommandValue(application = application)

            Assertions.assertEquals(result, "Stop")
        }

        @Test
        fun `when voice command types is make should return command value`() {
            val result = VoiceCommandTypes.Make.toVoiceCommandValue(application = application)

            Assertions.assertEquals(result, "Make")
        }

        @Test
        fun `when voice command types is miss should return command value`() {
            val result = VoiceCommandTypes.Miss.toVoiceCommandValue(application = application)

            Assertions.assertEquals(result, "Miss")
        }

        @Test
        fun `when voice command types is none should return command value`() {
            val result = VoiceCommandTypes.None.toVoiceCommandValue(application = application)

            Assertions.assertEquals(result, "None")
        }
    }

    @Nested
    inner class ToPlayVoiceCommandsTypes {

        @BeforeEach
        fun beforeEach() {
            every { application.getString(StringsIds.start) } returns "Start"
            every { application.getString(StringsIds.stop) } returns "Stop"
            every { application.getString(StringsIds.make) } returns "Make"
            every { application.getString(StringsIds.miss) } returns "Miss"
            every { application.getString(StringsIds.none) } returns "None"
        }

        @Test
        fun `when value is start should return desired voice command type`() {
            val result = "Start".toPlayVoiceCommandsTypes(application = application)

            Assertions.assertEquals(result, VoiceCommandTypes.Start)
        }

        @Test
        fun `when value is stop should return desired voice command type`() {
            val result = "Stop".toPlayVoiceCommandsTypes(application = application)

            Assertions.assertEquals(result, VoiceCommandTypes.Stop)
        }

        @Test
        fun `when value is make should return desired voice command type`() {
            val result = "Make".toPlayVoiceCommandsTypes(application = application)

            Assertions.assertEquals(result, VoiceCommandTypes.Make)
        }

        @Test
        fun `when value is miss should return desired voice command type`() {
            val result = "Miss".toPlayVoiceCommandsTypes(application = application)

            Assertions.assertEquals(result, VoiceCommandTypes.Miss)
        }

        @Test
        fun `when value is none should return desired voice command type`() {
            val result = "None".toPlayVoiceCommandsTypes(application = application)

            Assertions.assertEquals(result, VoiceCommandTypes.None)
        }
    }

    @Nested
    inner class ExamplePhrases {

        @Test
        fun `when voice command type is start should return start example phrases`() {
            val result = VoiceCommandTypes.Start.examplePhrases()

            Assertions.assertEquals(result, "\"start\", \"begin\", \"go\"")
        }

        @Test
        fun `when voice command type is stop should return stop example phrases`() {
            val result = VoiceCommandTypes.Stop.examplePhrases()

            Assertions.assertEquals(result, "\"stop\", \"end\", \"done\"")
        }

        @Test
        fun `when voice command type is make should return make example phrases`() {
            val result = VoiceCommandTypes.Make.examplePhrases()

            Assertions.assertEquals(result, "\"swish\", \"money\", \"bucket\"")
        }

        @Test
        fun `when voice command type is miss should return miss example phrases`() {
            val result = VoiceCommandTypes.Miss.examplePhrases()

            Assertions.assertEquals(result, "\"brick\", \"airball\", \"miss\"")
        }

        @Test
        fun `when voice command type is none should return empty string`() {
            val result = VoiceCommandTypes.None.examplePhrases()

            Assertions.assertEquals(result, "")
        }
    }
}