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
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module

class TestUtil(private val composeRule: ComposeContentTestRule) {
    private val countingIdlingResource = CountingIdlingResource("DelayIdlingResource")

    fun setupKoinModules() {
        val mockAndroidContext = ApplicationProvider.getApplicationContext<Context>()
        // [stopKoin] gets called to stop the priduction instanc eof startKoin
        // todo further investigation -> Can we override the production koin instance?
        stopKoin()
        startKoin {
            androidContext(mockAndroidContext)
            modules(AppModule().modules)
        }
    }

    fun registerAndStartDelayCallback() {
        IdlingRegistry.getInstance().register(countingIdlingResource)
        countingIdlingResource.increment()
        Thread.sleep(5000L) // 5000L Represents 5 seconds, the current time showing splash
        countingIdlingResource.decrement()
    }

    fun unregisterDelayCallback() {
        IdlingRegistry.getInstance().unregister(countingIdlingResource)
    }

    fun setContentAndLoadOptionalModule(koinModule: Module? = null) {
        koinModule?.let { module ->
            loadKoinModules(module = module)
        }
        setDefaultComposeContent()
        //   registerAndStartDelayCallback()
    }

    fun breakdownSetupsForTest() {
        unregisterDelayCallback()
    }

    private fun setDefaultComposeContent() {
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
