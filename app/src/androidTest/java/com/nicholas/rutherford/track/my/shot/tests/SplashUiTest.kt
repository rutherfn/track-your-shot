package com.nicholas.rutherford.track.your.shot.tests

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nicholas.rutherford.track.your.shot.MainActivity
import com.nicholas.rutherford.track.your.shot.TestUtil
import com.nicholas.rutherford.track.your.shot.fakes.FakeReadFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.fakes.FakeReadSharedPreferences
import com.nicholas.rutherford.track.your.shot.firebase.core.read.ReadFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.robots.AuthenticationRobot
import com.nicholas.rutherford.track.your.shot.robots.LoginRobot
import com.nicholas.rutherford.track.your.shot.robots.SplashRobot
import com.nicholas.rutherford.track.your.shot.shared.preference.read.ReadSharedPreferences
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
