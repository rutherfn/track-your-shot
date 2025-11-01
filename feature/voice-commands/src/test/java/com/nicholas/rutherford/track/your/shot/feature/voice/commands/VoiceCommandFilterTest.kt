package com.nicholas.rutherford.track.your.shot.feature.voice.commands

import com.nicholas.rutherford.track.your.shot.data.room.response.VoiceCommandTypes
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class VoiceCommandFilterTest {

    @Nested
    inner class ToDisplayLabel {

        @Test
        fun `when filter is start should return start value`() {
            val result = VoiceCommandFilter.START.toDisplayLabel()

            Assertions.assertEquals(result, "Start")
        }

        @Test
        fun `when filter is stop should return stop value`() {
            val result = VoiceCommandFilter.STOP.toDisplayLabel()

            Assertions.assertEquals(result, "Stop")
        }

        @Test
        fun `when filter is make should return make value`() {
            val result = VoiceCommandFilter.MAKE.toDisplayLabel()

            Assertions.assertEquals(result, "Make")
        }

        @Test
        fun `when filter is miss should return miss value`() {
            val result = VoiceCommandFilter.MISS.toDisplayLabel()

            Assertions.assertEquals(result, "Miss")
        }
    }

    @Nested
    inner class ToType {

        @Test
        fun `when filter is start should return voice command types start`() {
            val result = VoiceCommandFilter.START.toType()

            Assertions.assertEquals(result, VoiceCommandTypes.Start)
        }

        @Test
        fun `when filter is stop should return voice command types stop`() {
            val result = VoiceCommandFilter.STOP.toType()

            Assertions.assertEquals(result, VoiceCommandTypes.Stop)
        }

        @Test
        fun `when filter is make should return voice command types make`() {
            val result = VoiceCommandFilter.MAKE.toType()

            Assertions.assertEquals(result, VoiceCommandTypes.Make)
        }

        @Test
        fun `when filter is miss should return voice command types miss`() {
            val result = VoiceCommandFilter.MISS.toType()

            Assertions.assertEquals(result, VoiceCommandTypes.Miss)
        }
    }
}
