package com.nicholas.rutherford.track.your.shot.feature.splash

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class SplashTagsTest {

    @Nested
    inner class Constants {

        @Test
        fun `splash image`() {
            Assertions.assertEquals(SplashTags.SPLASH_IMAGE, "splash_image")
        }
    }
}
