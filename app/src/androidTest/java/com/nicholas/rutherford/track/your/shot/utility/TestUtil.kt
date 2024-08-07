package com.nicholas.rutherford.track.your.shot.utility

import android.content.Context
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.navigation.compose.rememberNavController
import androidx.test.core.app.ApplicationProvider
import com.nicholas.rutherford.track.your.shot.AppModule
import com.nicholas.rutherford.track.your.shot.MainActivityViewModel
import com.nicholas.rutherford.track.your.shot.MainActivityWrapper
import com.nicholas.rutherford.track.your.shot.NavigationComponent
import com.nicholas.rutherford.track.your.shot.ViewModels
import com.nicholas.rutherford.track.your.shot.feature.create.account.authentication.AuthenticationViewModel
import com.nicholas.rutherford.track.your.shot.feature.create.account.createaccount.CreateAccountViewModel
import com.nicholas.rutherford.track.your.shot.feature.forgot.password.ForgotPasswordViewModel
import com.nicholas.rutherford.track.your.shot.feature.login.LoginViewModel
import com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer.CreateEditPlayerViewModel
import com.nicholas.rutherford.track.your.shot.feature.players.playerlist.PlayersListViewModel
import com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot.LogShotViewModel
import com.nicholas.rutherford.track.your.shot.feature.players.shots.selectshot.SelectShotViewModel
import com.nicholas.rutherford.track.your.shot.feature.splash.SplashViewModel
import com.nicholas.rutherford.track.your.shot.navigation.Navigator
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.compose.get
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module

class TestUtil(private val composeRule: ComposeContentTestRule) {

    fun setupKoinModules() {
        val mockAndroidContext = ApplicationProvider.getApplicationContext<Context>()
        // [stopKoin] gets called to stop the production instance of startKoin
        // todo further investigation -> Can we override the production koin instance?
        stopKoin()
        startKoin {
            androidContext(mockAndroidContext)
            modules(AppModule().modules)
        }
    }

    fun setContentAndLoadOptionalModule(koinModule: Module? = null) {
        koinModule?.let { module ->
            loadKoinModules(module = module)
        }
        setDefaultComposeContent()
    }

    private fun setDefaultComposeContent() {
        composeRule.setContent {
            val navHostController = rememberNavController()
            val navigator = get<Navigator>()

            val mainActivityViewModel = get<MainActivityViewModel>()
            val splashViewModel = get<SplashViewModel>()
            val loginViewModel = get<LoginViewModel>()
            val forgotPasswordViewModel = get<ForgotPasswordViewModel>()
            val playersListViewModel = get<PlayersListViewModel>()
            val createEditPlayerViewModel = get<CreateEditPlayerViewModel>()
            val createAccountViewModel = get<CreateAccountViewModel>()
            val authenticationViewModel = get<AuthenticationViewModel>()
            val selectShotViewModel = get<SelectShotViewModel>()
            val logShotViewModel = get<LogShotViewModel>()

            NavigationComponent(
                activity = MainActivityWrapper(),
                navHostController = navHostController,
                navigator = navigator,
                viewModels = ViewModels(
                    mainActivityViewModel = mainActivityViewModel,
                    splashViewModel = splashViewModel,
                    loginViewModel = loginViewModel,
                    forgotPasswordViewModel = forgotPasswordViewModel,
                    createAccountViewModel = createAccountViewModel,
                    playersListViewModel = playersListViewModel,
                    createEditPlayerViewModel = createEditPlayerViewModel,
                    authenticationViewModel = authenticationViewModel,
                    selectShotViewModel = selectShotViewModel,
                    logShotViewModel = logShotViewModel
                )
            )
        }
    }
}
