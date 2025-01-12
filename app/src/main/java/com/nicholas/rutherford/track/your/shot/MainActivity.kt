package com.nicholas.rutherford.track.your.shot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

open class MainActivity : ComponentActivity() {

    private val viewModel by viewModel<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NavigationComponent(
                activity = this,
                navHostController = rememberNavController(),
                navigator = get(),
                viewModels = ViewModels(
                    mainActivityViewModel = viewModel,
                    splashViewModel = getViewModel(),
                    loginViewModel = getViewModel(),
                    playersListViewModel = getViewModel(),
                    createEditPlayerViewModel = getViewModel(),
                    forgotPasswordViewModel = getViewModel(),
                    createAccountViewModel = getViewModel(),
                    authenticationViewModel = getViewModel(),
                    selectShotViewModel = getViewModel(),
                    logShotViewModel = getViewModel(),
                    settingsViewModel = getViewModel(),
                    permissionEducationViewModel = getViewModel(),
                    termsConditionsViewModel = getViewModel(),
                    onboardingEducationViewModel = getViewModel(),
                    enabledPermissionsViewModel = getViewModel(),
                    accountInfoViewModel = getViewModel(),
                    reportListViewModel = getViewModel(),
                    createReportViewModel = getViewModel(),
                    viewPlayerReportsViewModel = getViewModel()
                )
            )
        }
    }
}
