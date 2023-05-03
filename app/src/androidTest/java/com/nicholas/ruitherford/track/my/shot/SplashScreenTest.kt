package com.nicholas.ruitherford.track.my.shot

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import com.nicholas.rutherford.track.my.shot.MainActivityWrapper
import com.nicholas.rutherford.track.my.shot.NavigationComponent
import com.nicholas.rutherford.track.my.shot.ViewModels
import com.nicholas.rutherford.track.my.shot.feature.create.account.authentication.AuthenticationViewModel
import com.nicholas.rutherford.track.my.shot.feature.create.account.createaccount.CreateAccountViewModel
import com.nicholas.rutherford.track.my.shot.feature.forgot.password.ForgotPasswordViewModel
import com.nicholas.rutherford.track.my.shot.feature.home.HomeViewModel
import com.nicholas.rutherford.track.my.shot.feature.login.LoginViewModel
import com.nicholas.rutherford.track.my.shot.feature.splash.SplashTags
import com.nicholas.rutherford.track.my.shot.feature.splash.SplashViewModel
import com.nicholas.rutherford.track.my.shot.helper.constants.SharedPreferencesConstants
import com.nicholas.rutherford.track.my.shot.navigation.Navigator
import com.nicholas.rutherford.track.myshot.compose.content.test.rule.verifyTagIsDisplayed
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.androidx.compose.get

class SplashScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    private val testSetup = TestSetup()

    @Before
    fun setUp() = testSetup.loadAllKoinModules()

    @After
    fun tearDown() {
        testSetup.tearDownKoin()
    }

    @Test
    fun verify_splash_screen_content() {

        // Build a Composable that depends on MyViewModel and MyDependency
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

        Thread.sleep(5000)
    }

    private fun getSharedPreferences(androidApplication: Application): android.content.SharedPreferences {
        return androidApplication.getSharedPreferences(SharedPreferencesConstants.Core.TRACK_MY_SHOT_PREFERENCES, android.content.Context.MODE_PRIVATE)
    }
}
