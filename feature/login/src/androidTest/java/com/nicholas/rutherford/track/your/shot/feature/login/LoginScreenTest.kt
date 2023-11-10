package com.nicholas.rutherford.track.your.shot.feature.login

import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.test.junit4.createComposeRule
import com.nicholas.rutherford.track.your.shot.feature.splash.DrawablesIds
import com.nicholas.rutherford.track.yourshot.compose.content.test.rule.verifyTagIsNotDisplayed
import com.nicholas.rutherford.track.yourshot.compose.content.test.rule.verifyTagWithImageResIsDisplayed
import com.nicholas.rutherford.track.yourshot.compose.content.test.rule.verifyTagWithTextIsDisplayed
import org.junit.Rule
import org.junit.Test

class LoginScreenTest {

    @get:Rule
    internal val composeTestRule = createComposeRule()

    private val validLoginState = LoginState(
        launcherDrawableId = DrawablesIds.launcherRound,
        email = "emailtest@yahoo.com",
        password = "PasswordTest124"
    )

    @Test
    fun verifyContentWithValidStateContent() {
        composeTestRule.setContent {
            val coroutineScope = rememberCoroutineScope()

            LoginScreen(
                loginScreenParams = LoginScreenParams(
                    state = validLoginState,
                    onEmailValueChanged = { },
                    onPasswordValueChanged = { },
                    onLoginButtonClicked = {},
                    onForgotPasswordClicked = { },
                    onCreateAccountClicked = { },
                    coroutineScope = coroutineScope
                )
            )
        }

        composeTestRule.verifyTagWithImageResIsDisplayed(id = DrawablesIds.launcherRound, testTag = LoginTags.LOGIN_APP_IMAGE)
        composeTestRule.verifyTagWithTextIsDisplayed(text = "Proceed With Your Account", testTag = LoginTags.PROCEED_WITH_YOUR_ACCOUNT_TEXT)
        composeTestRule.verifyTagWithTextIsDisplayed(text = "Login", testTag = LoginTags.LOGIN_TEXT)
        composeTestRule.verifyTagWithTextIsDisplayed(text = "Email", testTag = LoginTags.EMAIL_TEXT_FIELD)
        composeTestRule.verifyTagWithTextIsDisplayed(text = "emailtest@yahoo.com", testTag = LoginTags.EMAIL_TEXT_FIELD)
        composeTestRule.verifyTagWithTextIsDisplayed(text = "Login", testTag = LoginTags.LOGIN_BUTTON)
        composeTestRule.verifyTagWithTextIsDisplayed(text = "Forgot Password", testTag = LoginTags.FORGOT_PASSWORD_TEXT)
        composeTestRule.verifyTagWithTextIsDisplayed(text = "Click me to create account", testTag = LoginTags.CLICK_ME_TO_CREATE_ACCOUNT_TEXT)
    }

    @Test
    fun verifyContentwithValidStateWithLauncherDrawableIdSetToNull() {
        composeTestRule.setContent {
            val coroutineScope = rememberCoroutineScope()

            LoginScreen(
                loginScreenParams = LoginScreenParams(
                    state = validLoginState.copy(launcherDrawableId = null),
                    onEmailValueChanged = { },
                    onPasswordValueChanged = { },
                    onLoginButtonClicked = {},
                    onForgotPasswordClicked = { },
                    onCreateAccountClicked = { },
                    coroutineScope = coroutineScope
                )
            )
        }

        composeTestRule.verifyTagIsNotDisplayed(testTag = LoginTags.LOGIN_APP_IMAGE)
        composeTestRule.verifyTagWithTextIsDisplayed(text = "Proceed With Your Account", testTag = LoginTags.PROCEED_WITH_YOUR_ACCOUNT_TEXT)
        composeTestRule.verifyTagWithTextIsDisplayed(text = "Login", testTag = LoginTags.LOGIN_TEXT)
        composeTestRule.verifyTagWithTextIsDisplayed(text = "Email", testTag = LoginTags.EMAIL_TEXT_FIELD)
        composeTestRule.verifyTagWithTextIsDisplayed(text = "emailtest@yahoo.com", testTag = LoginTags.EMAIL_TEXT_FIELD)
        composeTestRule.verifyTagWithTextIsDisplayed(text = "Login", testTag = LoginTags.LOGIN_BUTTON)
        composeTestRule.verifyTagWithTextIsDisplayed(text = "Forgot Password", testTag = LoginTags.FORGOT_PASSWORD_TEXT)
        composeTestRule.verifyTagWithTextIsDisplayed(text = "Click me to create account", testTag = LoginTags.CLICK_ME_TO_CREATE_ACCOUNT_TEXT)
    }
}
