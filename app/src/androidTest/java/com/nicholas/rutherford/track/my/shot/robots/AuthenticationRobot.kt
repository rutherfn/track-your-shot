package com.nicholas.rutherford.track.my.shot.robots

import androidx.compose.ui.test.junit4.ComposeContentTestRule
import com.nicholas.rutherford.track.my.shot.feature.create.account.authentication.AuthenticationTags
import com.nicholas.rutherford.track.my.shot.feature.splash.DrawablesIds
import com.nicholas.rutherford.track.myshot.compose.content.test.rule.verifyTagWithImageResIsDisplayed
import com.nicholas.rutherford.track.myshot.compose.content.test.rule.verifyTagWithTextIsDisplayed

class AuthenticationRobot(private val composeRule: ComposeContentTestRule) {
    fun verifyAuthenticationContent() {
        composeRule.verifyTagWithImageResIsDisplayed(id = DrawablesIds.email, testTag = AuthenticationTags.EMAIL_IMAGE)
        composeRule.verifyTagWithTextIsDisplayed(text = "Check if account has been verified", testTag = AuthenticationTags.CHECK_IF_ACCOUNT_HAS_BEEN_VERIFIED_TEXT)
    }
}
