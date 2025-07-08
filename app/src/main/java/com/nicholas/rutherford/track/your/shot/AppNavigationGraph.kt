package com.nicholas.rutherford.track.your.shot

import androidx.compose.runtime.collectAsState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.nicholas.rutherford.track.your.shot.base.vm.ObserveLifecycle
import com.nicholas.rutherford.track.your.shot.feature.create.account.createaccount.CreateAccountScreen
import com.nicholas.rutherford.track.your.shot.feature.create.account.createaccount.CreateAccountScreenParams
import com.nicholas.rutherford.track.your.shot.feature.create.account.createaccount.CreateAccountViewModel
import com.nicholas.rutherford.track.your.shot.feature.forgot.password.ForgotPasswordScreen
import com.nicholas.rutherford.track.your.shot.feature.forgot.password.ForgotPasswordScreenParams
import com.nicholas.rutherford.track.your.shot.feature.forgot.password.ForgotPasswordViewModel
import com.nicholas.rutherford.track.your.shot.feature.login.LoginScreen
import com.nicholas.rutherford.track.your.shot.feature.login.LoginScreenParams
import com.nicholas.rutherford.track.your.shot.feature.login.LoginViewModel
import com.nicholas.rutherford.track.your.shot.feature.splash.SplashScreen
import com.nicholas.rutherford.track.your.shot.feature.splash.SplashViewModel
import com.nicholas.rutherford.track.your.shot.navigation.NavigationDestinations
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

/**
 * Defines navigation destinations and composable screens for the app.
 *
 * Contains functions to add screens to the navigation graph.
 */
object AppNavigationGraph {

    /**
     * Adds the Splash Screen destination to the NavGraph.
     * Retrieves [SplashViewModel] via Koin and observes its lifecycle.
     * Displays the [SplashScreen] composable.
     */
    fun NavGraphBuilder.splashScreen() {
        composable(route = NavigationDestinations.SPLASH_SCREEN) {
            val splashViewModel: SplashViewModel = koinViewModel()

            ObserveLifecycle(viewModel = splashViewModel)

            SplashScreen()
        }
    }

    /**
     * Adds the Login Screen destination to the NavGraph.
     * Retrieves [LoginViewModel] via Koin and observes its lifecycle.
     * Collects UI state from the ViewModel and passes event callbacks to [LoginScreen].
     * Displays the [LoginScreen] composable
     */
    fun NavGraphBuilder.loginScreen() {
        composable(route = NavigationDestinations.LOGIN_SCREEN) {
            val loginViewModel: LoginViewModel = koinViewModel()

            ObserveLifecycle(viewModel = loginViewModel)

            LoginScreen(
                loginScreenParams = LoginScreenParams(
                    state = loginViewModel.loginStateFlow.collectAsState().value,
                    onEmailValueChanged = { newEmail ->
                        loginViewModel.onEmailValueChanged(newEmail = newEmail)
                    },
                    onPasswordValueChanged = { newPassword ->
                        loginViewModel.onPasswordValueChanged(newPassword = newPassword)
                    },
                    onLoginButtonClicked = {
                        loginViewModel.onLoginButtonClicked()
                    },
                    onForgotPasswordClicked = {
                        loginViewModel.onForgotPasswordClicked()
                    },
                    onCreateAccountClicked = {
                        loginViewModel.onCreateAccountClicked()
                    }
                )
            )
        }
    }

    /**
     * Adds the Forgot Password Screen destination to the NavGraph.
     * Retrieves [ForgotPasswordViewModel] via Koin and observes its lifecycle.
     * Collects UI state from the ViewModel and passes event callbacks to [ForgotPasswordScreen].
     * Displays the [ForgotPasswordScreen] composable
     */
    fun NavGraphBuilder.forgotPasswordScreen() {
        composable(route = NavigationDestinations.FORGOT_PASSWORD_SCREEN) {
            val forgotPasswordViewModel: ForgotPasswordViewModel = koinViewModel()

            ObserveLifecycle(viewModel = forgotPasswordViewModel)

            ForgotPasswordScreen(
                forgotPasswordScreenParams = ForgotPasswordScreenParams(
                    state = forgotPasswordViewModel.forgotPasswordStateFlow.collectAsState().value,
                    onEmailValueChanged = { newEmail ->
                        forgotPasswordViewModel.onEmailValueChanged(
                            newEmail = newEmail
                        )
                    },
                    onSendPasswordResetButtonClicked = { newEmail ->
                        forgotPasswordViewModel.onSendPasswordResetButtonClicked(newEmail = newEmail)
                    },
                    onBackButtonClicked = { forgotPasswordViewModel.onBackButtonClicked() }
                )
            )
        }
    }

    fun NavGraphBuilder.createAccountScreen(isConnectedToInternet: Boolean) {
        composable(route = NavigationDestinations.CREATE_ACCOUNT_SCREEN) {
            val createAccountViewModel: CreateAccountViewModel = koinViewModel()

            ObserveLifecycle(viewModel = createAccountViewModel)

            CreateAccountScreen(
                createAccountScreenParams = CreateAccountScreenParams(
                    state = createAccountViewModel.createAccountStateFlow.collectAsState().value,
                    onUsernameValueChanged = { newUsername ->
                        createAccountViewModel.onUsernameValueChanged(
                            newUsername = newUsername
                        )
                    },
                    onEmailValueChanged = { newEmail ->
                        createAccountViewModel.onEmailValueChanged(
                            newEmail = newEmail
                        )
                    },
                    onPasswordValueChanged = { newPassword ->
                        createAccountViewModel.onPasswordValueChanged(
                            newPassword = newPassword
                        )
                    },
                    onCreateAccountButtonClicked = {
                        createAccountViewModel.onCreateAccountButtonClicked(
                            isConnectedToInternet = isConnectedToInternet
                        )
                    },
                    onBackButtonClicked = { createAccountViewModel.onBackButtonClicked() }
                )
            )
        }
    }
}
