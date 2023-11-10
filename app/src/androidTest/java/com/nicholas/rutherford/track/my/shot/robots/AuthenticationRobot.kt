package com.nicholas.rutherford.track.your.shot.robots

import androidx.compose.ui.test.junit4.ComposeContentTestRule
import com.nicholas.rutherford.track.your.shot.compose.components.TopAppBarTestTags
import com.nicholas.rutherford.track.your.shot.feature.create.account.authentication.AuthenticationTags
import com.nicholas.rutherford.track.your.shot.feature.splash.DrawablesIds
import com.nicholas.rutherford.track.yourshot.compose.content.test.rule.verifyTagIsDisplayed
import com.nicholas.rutherford.track.yourshot.compose.content.test.rule.verifyTagWithImageResIsDisplayed
import com.nicholas.rutherford.track.yourshot.compose.content.test.rule.verifyTagWithTextIsDisplayed

class AuthenticationRobot(private val composeRule: ComposeContentTestRule) {
    fun verifyAuthenticationContent() {
        composeRule.verifyTagWithTextIsDisplayed(text = "Verify Account", testTag = TopAppBarTestTags.TOOLBAR_TITLE)
        composeRule.verifyTagIsDisplayed(testTag = TopAppBarTestTags.TOOLBAR_BUTTON_ICON)
        composeRule.verifyTagWithImageResIsDisplayed(id = DrawablesIds.email, testTag = AuthenticationTags.EMAIL_IMAGE)
        composeRule.verifyTagWithTextIsDisplayed(text = "Email has been sent to verify account. Please open sent email to verify account.", testTag = AuthenticationTags.EMAIL_HAS_BEEN_SENT_TEXT)
        composeRule.verifyTagWithTextIsDisplayed(text = "Check if account has been verified", testTag = AuthenticationTags.CHECK_IF_ACCOUNT_HAS_BEEN_VERIFIED_TEXT)
        composeRule.verifyTagWithTextIsDisplayed(text = "Open Email", testTag = AuthenticationTags.OPEN_EMAIL_BUTTON)
        composeRule.verifyTagWithTextIsDisplayed(text = "Resend Email", testTag = AuthenticationTags.RESEND_EMAIL_BUTTON)
    }
}
