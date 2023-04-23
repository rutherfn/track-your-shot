package com.nicholas.rutherford.track.my.shot.feature.login

import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.test.junit4.createComposeRule
import com.nicholas.rutherford.track.my.shot.feature.splash.DrawablesIds
import com.nicholas.rutherford.track.myshot.compose.content.test.rule.verifyTagWithImageResIsDisplayed
import com.nicholas.rutherford.track.myshot.compose.content.test.rule.verifyTagWithTextIsDisplayed
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

        // verify rest of the elements here for testing
    }
}
