package com.nicholas.rutherford.track.my.shot.tests

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nicholas.rutherford.track.my.shot.MainActivity
import com.nicholas.rutherford.track.my.shot.TestUtil
import com.nicholas.rutherford.track.my.shot.fakes.FakeReadFirebaseUserInfo
import com.nicholas.rutherford.track.my.shot.fakes.FakeReadSharedPreferences
import com.nicholas.rutherford.track.my.shot.feature.home.HomeTags
import com.nicholas.rutherford.track.my.shot.firebase.read.ReadFirebaseUserInfo
import com.nicholas.rutherford.track.my.shot.robots.AuthenticationRobot
import com.nicholas.rutherford.track.my.shot.robots.LoginRobot
import com.nicholas.rutherford.track.my.shot.robots.SplashRobot
import com.nicholas.rutherford.track.my.shot.shared.preference.read.ReadSharedPreferences
import com.nicholas.rutherford.track.myshot.compose.content.test.rule.verifyTagIsDisplayed
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

    private val authenticationRobot = AuthenticationRobot(composeRule = composeRule)
    private val splashRobot = SplashRobot(composeRule = composeRule)
    private val loginRobot = LoginRobot(composeRule = composeRule)

    private val testUtil = TestUtil(composeRule = composeRule)

    @Before
    fun setUp() {
        testUtil.setupKoinModules()
    }

    @Test
    fun verify_splash_screen_content_navigating_to_login() {
        testUtil.setContentAndLoadOptionalModule()

        splashRobot.verifySplashImageIsDisplayed()
        loginRobot.verifyLoginTestContent()
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

        // todo verify home content once created
        composeRule.verifyTagIsDisplayed(testTag = HomeTags.TEST_BUTTON)
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
        authenticationRobot.verifyAuthenticationContent()
    }
}