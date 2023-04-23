package com.nicholas.rutherford.track.my.shot.feature.splash

import androidx.compose.ui.test.junit4.createComposeRule
import com.nicholas.rutherford.track.myshot.compose.content.test.rule.verifyTagIsDisplayed
import org.junit.Rule
import org.junit.Test

class SplashScreenTest {

    @get:Rule
    internal val composeTestRule = createComposeRule()

    @Test
    fun verifySplashImageIsDisplayed() {
        composeTestRule.setContent {
            SplashScreen {}
        }
        composeTestRule.verifyTagIsDisplayed(testTag = SplashTags.SPLASH_IMAGE)
    }
}
