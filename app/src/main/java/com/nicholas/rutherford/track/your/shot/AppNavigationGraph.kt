package com.nicholas.rutherford.track.your.shot

import androidx.compose.runtime.collectAsState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.nicholas.rutherford.track.your.shot.base.vm.ObserveLifecycle
import com.nicholas.rutherford.track.your.shot.feature.create.account.authentication.AuthenticationScreen
import com.nicholas.rutherford.track.your.shot.feature.create.account.authentication.AuthenticationViewModel
import com.nicholas.rutherford.track.your.shot.feature.create.account.createaccount.CreateAccountScreen
import com.nicholas.rutherford.track.your.shot.feature.create.account.createaccount.CreateAccountScreenParams
import com.nicholas.rutherford.track.your.shot.feature.create.account.createaccount.CreateAccountViewModel
import com.nicholas.rutherford.track.your.shot.feature.forgot.password.ForgotPasswordScreen
import com.nicholas.rutherford.track.your.shot.feature.forgot.password.ForgotPasswordScreenParams
import com.nicholas.rutherford.track.your.shot.feature.forgot.password.ForgotPasswordViewModel
import com.nicholas.rutherford.track.your.shot.feature.login.LoginScreen
import com.nicholas.rutherford.track.your.shot.feature.login.LoginScreenParams
import com.nicholas.rutherford.track.your.shot.feature.login.LoginViewModel
import com.nicholas.rutherford.track.your.shot.feature.settings.onboardingeducation.OnboardingEducationParams
import com.nicholas.rutherford.track.your.shot.feature.settings.onboardingeducation.OnboardingEducationScreen
import com.nicholas.rutherford.track.your.shot.feature.settings.onboardingeducation.OnboardingEducationViewModel
import com.nicholas.rutherford.track.your.shot.feature.settings.termsconditions.TermsConditionsParams
import com.nicholas.rutherford.track.your.shot.feature.settings.termsconditions.TermsConditionsScreen
import com.nicholas.rutherford.track.your.shot.feature.settings.termsconditions.TermsConditionsViewModel
import com.nicholas.rutherford.track.your.shot.feature.splash.SplashScreen
import com.nicholas.rutherford.track.your.shot.feature.splash.SplashViewModel
import com.nicholas.rutherford.track.your.shot.navigation.NavigationDestinations
import com.nicholas.rutherford.track.your.shot.navigation.arguments.NavArguments
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

    /**
     * Adds the Create Account Screen destination to the NavGraph.
     * Retrieves [CreateAccountViewModel] via Koin and observes its lifecycle.
     * Collects UI state from the ViewModel and passes event callbacks to [CreateAccountScreen].
     * Displays the [CreateAccountScreen] composable
     *
     * @param isConnectedToInternet [Boolean] to determine if we are connected to the internet or not.
     */
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

    /**
     * Adds the Authentication Screen destination to the NavGraph.
     * Grabs navigation arguments declared in [NavArguments.authentication]
     * Retrieves [AuthenticationViewModel] via Koin and observes its lifecycle.
     * Displays the [AuthenticationScreen] composable
     */
    fun NavGraphBuilder.authenticationScreen() {
        composable(
            route = NavigationDestinations.AUTHENTICATION_SCREEN_WITH_PARAMS,
            arguments = NavArguments.authentication
        ) { entry ->
            val authenticationScreenViewModel: AuthenticationViewModel = koinViewModel()

            ObserveLifecycle(viewModel = authenticationScreenViewModel)

            if (entry.arguments != null) {
                AuthenticationScreen(viewModel = authenticationScreenViewModel)
            }
        }
    }

    /**
     * Adds the Terms And Condition Screen destination to the NavGraph.
     * Grabs navigation arguments declared in [NavArguments.termsConditions]
     * Retrieves [TermsConditionsViewModel] via Koin and observes its lifecycle.
     * Displays the [TermsConditionsScreen] composable
     */

    fun NavGraphBuilder.termsAndConditionScreen() {
        composable(
            route = NavigationDestinations.TERMS_CONDITIONS_WITH_PARAMS,
            arguments = NavArguments.termsConditions
        ) { entry ->
            val termsConditionsViewModel: TermsConditionsViewModel = koinViewModel()

            ObserveLifecycle(viewModel = termsConditionsViewModel)

            if (entry.arguments != null) {
                TermsConditionsScreen(
                    params = TermsConditionsParams(
                        onBackClicked = { termsConditionsViewModel.onBackClicked() },
                        onCloseAcceptButtonClicked = { termsConditionsViewModel.onCloseAcceptButtonClicked() },
                        onDevEmailClicked = { termsConditionsViewModel.onDevEmailClicked() },
                        state = termsConditionsViewModel.termsConditionsStateFlow.collectAsState().value
                    )
                )
            }
        }
    }

    /**
     * Adds the Onboarding Education Screen destination to the NavGraph.
     * Grabs navigation arguments declared in [NavArguments.onBoardingEducation]
     * Retrieves [OnboardingEducationViewModel] via Koin and observes its lifecycle.
     * Displays the [OnboardingEducationScreen] composable
     */

    fun NavGraphBuilder.onBoardingEducationScreen() {
        composable(
            route = NavigationDestinations.ONBOARDING_EDUCATION_SCREEN_WITH_PARAMS,
            arguments = NavArguments.onBoardingEducation
        ) { entry ->
            val onboardingEducationViewModel: OnboardingEducationViewModel = koinViewModel()

            ObserveLifecycle(viewModel = onboardingEducationViewModel)

            if (entry.arguments != null) {
                OnboardingEducationScreen(
                    onboardingEducationParams = OnboardingEducationParams(
                        onGotItButtonClicked = { onboardingEducationViewModel.onGotItButtonClicked() },
                        state = onboardingEducationViewModel.onboardingEducationStateFlow.collectAsState().value
                    )
                )
            }
        }
    }
}
