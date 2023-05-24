package com.nicholas.rutherford.track.my.shot

import android.content.Context
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.navigation.compose.rememberNavController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.idling.CountingIdlingResource
import com.nicholas.rutherford.track.my.shot.feature.create.account.authentication.AuthenticationViewModel
import com.nicholas.rutherford.track.my.shot.feature.create.account.createaccount.CreateAccountViewModel
import com.nicholas.rutherford.track.my.shot.feature.forgot.password.ForgotPasswordViewModel
import com.nicholas.rutherford.track.my.shot.feature.home.HomeViewModel
import com.nicholas.rutherford.track.my.shot.feature.login.LoginViewModel
import com.nicholas.rutherford.track.my.shot.feature.splash.SplashViewModel
import com.nicholas.rutherford.track.my.shot.navigation.Navigator
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.compose.get
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

class TestUtil {

    private val countingIdlingResource = CountingIdlingResource("DelayIdlingResource")

    fun setupKoinModules() {
        val mockAndroidContext = ApplicationProvider.getApplicationContext<Context>()
        // we need to call this because by default koin is starting in the production code
        // todo fruther investigation -> Can we override the production koin instance?
        stopKoin()
        startKoin {
            androidContext(mockAndroidContext)
            modules(AppModule().modules)
        }
    }

    fun registerAndStartDelayCallback(delayMillis: Long) {
        IdlingRegistry.getInstance().register(countingIdlingResource)
        countingIdlingResource.increment()
        Thread.sleep(delayMillis)
        countingIdlingResource.decrement()
    }

    fun unregisterDelayCallback() {
        IdlingRegistry.getInstance().unregister(countingIdlingResource)
    }

    fun setDefaultComposeContent(composeRule: ComposeContentTestRule) {
        composeRule.setContent {
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
    }
}
