package com.nicholas.rutherford.track.my.shot.feature.splash

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import org.junit.Rule
import org.junit.Test

class SplashScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun verifySplashImageIsDisplayed() {
        composeTestRule.setContent {
            SplashScreen {}
        }
        composeTestRule.onNodeWithTag(SplashTags.SPLASH_IMAGE).assertIsDisplayed()
    }
}
