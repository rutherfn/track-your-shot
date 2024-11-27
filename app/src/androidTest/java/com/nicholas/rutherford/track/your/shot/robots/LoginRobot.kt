package com.nicholas.rutherford.track.your.shot.robots

import androidx.compose.ui.test.junit4.ComposeContentTestRule
import com.nicholas.rutherford.track.your.shot.base.resources.DrawablesIds
import com.nicholas.rutherford.track.your.shot.feature.login.LoginTags
import com.nicholas.rutherford.track.yourshot.compose.content.test.rule.verifyTagWithImageResIsDisplayed
import com.nicholas.rutherford.track.yourshot.compose.content.test.rule.verifyTagWithTextIsDisplayed

class LoginRobot(private val composeRule: ComposeContentTestRule) {
    fun verifyLoginTestContent() {
        composeRule.verifyTagWithImageResIsDisplayed(id = DrawablesIds.launcherRoundTest, testTag = LoginTags.LOGIN_APP_IMAGE)
        composeRule.verifyTagWithTextIsDisplayed(text = "Proceed With Your Account", testTag = LoginTags.PROCEED_WITH_YOUR_ACCOUNT_TEXT)
        composeRule.verifyTagWithTextIsDisplayed(text = "Login", testTag = LoginTags.LOGIN_TEXT)
        composeRule.verifyTagWithTextIsDisplayed(text = "Email", testTag = LoginTags.EMAIL_TEXT_FIELD)
        composeRule.verifyTagWithTextIsDisplayed(text = "Login", testTag = LoginTags.LOGIN_BUTTON)
        composeRule.verifyTagWithTextIsDisplayed(text = "Forgot Password", testTag = LoginTags.FORGOT_PASSWORD_TEXT)
        composeRule.verifyTagWithTextIsDisplayed(text = "Click me to create account", testTag = LoginTags.CLICK_ME_TO_CREATE_ACCOUNT_TEXT)
    }

    fun verifyStateContent() {
        // todo
    }

    fun verifyProdContent() {
        // todo
    }
}
