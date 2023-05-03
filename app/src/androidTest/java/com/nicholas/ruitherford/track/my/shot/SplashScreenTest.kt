package com.nicholas.ruitherford.track.my.shot

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.nicholas.rutherford.track.my.shot.MainActivity
import com.nicholas.rutherford.track.my.shot.MainActivityWrapper
import com.nicholas.rutherford.track.my.shot.NavigationComponent
import com.nicholas.rutherford.track.my.shot.ViewModels
import com.nicholas.rutherford.track.my.shot.feature.create.account.authentication.AuthenticationViewModel
import com.nicholas.rutherford.track.my.shot.feature.create.account.createaccount.CreateAccountViewModel
import com.nicholas.rutherford.track.my.shot.feature.forgot.password.ForgotPasswordViewModel
import com.nicholas.rutherford.track.my.shot.feature.home.HomeViewModel
import com.nicholas.rutherford.track.my.shot.feature.login.LoginTags
import com.nicholas.rutherford.track.my.shot.feature.login.LoginViewModel
import com.nicholas.rutherford.track.my.shot.feature.splash.DrawablesIds
import com.nicholas.rutherford.track.my.shot.feature.splash.SplashTags
import com.nicholas.rutherford.track.my.shot.feature.splash.SplashViewModel
import com.nicholas.rutherford.track.my.shot.navigation.Navigator
import com.nicholas.rutherford.track.myshot.compose.content.test.rule.verifyTagIsDisplayed
import com.nicholas.rutherford.track.myshot.compose.content.test.rule.verifyTagWithImageResIsDisplayed
import com.nicholas.rutherford.track.myshot.compose.content.test.rule.verifyTagWithTextIsDisplayed
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.androidx.compose.get

class SplashScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    private val testUtil = TestUtil()

    @Before
    fun setUp() = testUtil.loadAllKoinModules()

    @After
    fun tearDown() {
        testUtil.tearDownKoin()
    }

    @Test
    fun verify_splash_screen_content_normal_state() {
        val content = @Composable {
            val navHostController = rememberNavController()
            val navigator = get<Navigator>()

            val splashViewModel = get<SplashViewModel>()
            val loginViewModel = get<LoginViewModel>()
            val homeViewModel = get<HomeViewModel>()
            val forgotPasswordViewModel = get<ForgotPasswordViewModel>()
            val createAccountViewModel = get<CreateAccountViewModel>()
            val authenticationViewModel = get<AuthenticationViewModel>()

            NavigationComponent(
                activity = MainActivityWrapper(),
                navHostController = navHostController,
                navigator = navigator,
                viewModels = ViewModels(
                    splashViewModel = splashViewModel,
                    loginViewModel = loginViewModel,
                    homeViewModel = homeViewModel,
                    forgotPasswordViewModel = forgotPasswordViewModel,
                    createAccountViewModel = createAccountViewModel,
                    authenticationViewModel = authenticationViewModel
                )
            )
        }

        composeRule.setContent {
            content()
        }

        composeRule.verifyTagIsDisplayed(testTag = SplashTags.SPLASH_IMAGE)

        testUtil.registerAndStartDelayCallback(delayMillis = 5000L)

        composeRule.verifyTagWithImageResIsDisplayed(id = DrawablesIds.launcherRoundTest, testTag = LoginTags.LOGIN_APP_IMAGE)
        composeRule.verifyTagWithTextIsDisplayed(text = "Proceed With Your Account", testTag = LoginTags.PROCEED_WITH_YOUR_ACCOUNT_TEXT)
        composeRule.verifyTagWithTextIsDisplayed(text = "Login", testTag = LoginTags.LOGIN_TEXT)
        composeRule.verifyTagWithTextIsDisplayed(text = "Email", testTag = LoginTags.EMAIL_TEXT_FIELD)
        composeRule.verifyTagWithTextIsDisplayed(text = "Login", testTag = LoginTags.LOGIN_BUTTON)
        composeRule.verifyTagWithTextIsDisplayed(text = "Forgot Password", testTag = LoginTags.FORGOT_PASSWORD_TEXT)
        composeRule.verifyTagWithTextIsDisplayed(text = "Click me to create account", testTag = LoginTags.CLICK_ME_TO_CREATE_ACCOUNT_TEXT)

        testUtil.unregisterDelayCallback()
    }
}
