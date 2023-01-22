package com.nicholas.rutherford.track.my.shot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.nicholas.rutherford.track.my.shot.feature.create.account.CreateAccountScreen
import com.nicholas.rutherford.track.my.shot.feature.create.account.authentication.AuthenticationScreen
import com.nicholas.rutherford.track.my.shot.feature.forgot.password.ForgotPasswordScreen
import com.nicholas.rutherford.track.my.shot.feature.home.HomeScreen
import com.nicholas.rutherford.track.my.shot.feature.login.LoginScreen
import com.nicholas.rutherford.track.my.shot.feature.splash.SplashScreen
import com.nicholas.rutherford.track.my.shot.navigation.NavigationComponent
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel by viewModel<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.initAppCenter()
        setContent {
            NavigationComponent(
                navHostController = rememberNavController(),
                navigator = get(),
                splashContent = { SplashScreen(viewModel = getViewModel()) },
                loginContent = { LoginScreen(viewModel = getViewModel()) },
                homeContent = { HomeScreen(viewModel = getViewModel()) },
                forgotPasswordContent = { ForgotPasswordScreen(viewModel = getViewModel()) },
                createAccountContent = { CreateAccountScreen(viewModel = getViewModel()) },
                authenticationContent = { AuthenticationScreen(viewModel = getViewModel()) }
            )
        }
    }
}
