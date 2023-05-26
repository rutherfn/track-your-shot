package com.nicholas.rutherford.track.my.shot.robots

import androidx.compose.ui.test.junit4.ComposeContentTestRule
import com.nicholas.rutherford.track.my.shot.feature.splash.SplashTags
import com.nicholas.rutherford.track.myshot.compose.content.test.rule.verifyTagIsDisplayed

class SplashRobot(private val composeRule: ComposeContentTestRule) {

    internal fun verifySplashImageIsDisplayed() {
        composeRule.verifyTagIsDisplayed(
            testTag = SplashTags.SPLASH_IMAGE
        )
        Thread.sleep(5000)
    }
}
