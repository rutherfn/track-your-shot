package com.nicholas.rutherford.track.my.shot.tests

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nicholas.rutherford.track.my.shot.MainActivity
import com.nicholas.rutherford.track.my.shot.TestUtil
import com.nicholas.rutherford.track.my.shot.fakes.FakeReadFirebaseUserInfo
import com.nicholas.rutherford.track.my.shot.fakes.FakeReadSharedPreferences
import com.nicholas.rutherford.track.my.shot.feature.home.HomeTags
import com.nicholas.rutherford.track.my.shot.feature.login.LoginTags
import com.nicholas.rutherford.track.my.shot.feature.splash.DrawablesIds
import com.nicholas.rutherford.track.my.shot.firebase.read.ReadFirebaseUserInfo
import com.nicholas.rutherford.track.my.shot.robots.SplashRobot
import com.nicholas.rutherford.track.my.shot.shared.preference.read.ReadSharedPreferences
import com.nicholas.rutherford.track.myshot.compose.content.test.rule.verifyTagIsDisplayed
import com.nicholas.rutherford.track.myshot.compose.content.test.rule.verifyTagWithImageResIsDisplayed
import com.nicholas.rutherford.track.myshot.compose.content.test.rule.verifyTagWithTextIsDisplayed
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.dsl.module

@RunWith(AndroidJUnit4::class)
class SplashUiTest {

    @get:Rule
    val composeRule = createComposeRule()

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    private val splashRobot = SplashRobot(composeRule = composeRule)

    private val testUtil = TestUtil(composeRule = composeRule)

    @Before
    fun setUp() {
        testUtil.setupKoinModules()
    }

    @Test
    fun verify_splash_screen_content_navigating_to_login() {
        testUtil.setContentAndLoadOptionalModule()

        splashRobot.verifySplashImageIsDisplayed()

        testUtil.registerAndStartDelayCallback()

        composeRule.verifyTagWithImageResIsDisplayed(id = DrawablesIds.launcherRoundTest, testTag = LoginTags.LOGIN_APP_IMAGE)
        composeRule.verifyTagWithTextIsDisplayed(text = "Proceed With Your Account", testTag = LoginTags.PROCEED_WITH_YOUR_ACCOUNT_TEXT)
        composeRule.verifyTagWithTextIsDisplayed(text = "Login", testTag = LoginTags.LOGIN_TEXT)
        composeRule.verifyTagWithTextIsDisplayed(text = "Email", testTag = LoginTags.EMAIL_TEXT_FIELD)
        composeRule.verifyTagWithTextIsDisplayed(text = "Login", testTag = LoginTags.LOGIN_BUTTON)
        composeRule.verifyTagWithTextIsDisplayed(text = "Forgot Password", testTag = LoginTags.FORGOT_PASSWORD_TEXT)
        composeRule.verifyTagWithTextIsDisplayed(text = "Click me to create account", testTag = LoginTags.CLICK_ME_TO_CREATE_ACCOUNT_TEXT)

        testUtil.breakdownSetupsForTest()
    }

    @Test
    fun verify_splash_screen_content_navigating_to_home() {
        testUtil.setContentAndLoadOptionalModule(
            koinModule = module {
                single<ReadSharedPreferences> { FakeReadSharedPreferences(accountHasBeenCreated = true) }
                single<ReadFirebaseUserInfo> { FakeReadFirebaseUserInfo(isLoggedIn = true, isEmailVerified = true) }
            }
        )

        splashRobot.verifySplashImageIsDisplayed()

        testUtil.registerAndStartDelayCallback()

        composeRule.verifyTagIsDisplayed(testTag = HomeTags.TEST_BUTTON)

        testUtil.breakdownSetupsForTest()
    }

    @Test
    fun verify_splash_screen_content_navigating_to_authentication() {
        testUtil.setContentAndLoadOptionalModule(
            koinModule = module {
                single<ReadSharedPreferences> { FakeReadSharedPreferences(unverifiedEmail = "unverifiedemail@gmail.com", unverifiedUsername = "unverifiedUsername") }
                single<ReadFirebaseUserInfo> { FakeReadFirebaseUserInfo(isLoggedIn = true) }
            }
        )

        splashRobot.verifySplashImageIsDisplayed()
    }
}
