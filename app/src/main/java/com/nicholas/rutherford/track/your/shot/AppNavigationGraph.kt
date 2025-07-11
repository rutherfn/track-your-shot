package com.nicholas.rutherford.track.your.shot

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.nicholas.rutherford.track.your.shot.base.vm.ObserveLifecycle
import com.nicholas.rutherford.track.your.shot.compose.components.AppBar2
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
import com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer.CreateEditPlayerParams
import com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer.CreateEditPlayerScreen
import com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer.CreateEditPlayerViewModel
import com.nicholas.rutherford.track.your.shot.feature.players.playerlist.PlayersListScreen
import com.nicholas.rutherford.track.your.shot.feature.players.playerlist.PlayersListScreenParams
import com.nicholas.rutherford.track.your.shot.feature.players.playerlist.PlayersListViewModel
import com.nicholas.rutherford.track.your.shot.feature.settings.onboardingeducation.OnboardingEducationParams
import com.nicholas.rutherford.track.your.shot.feature.settings.onboardingeducation.OnboardingEducationScreen
import com.nicholas.rutherford.track.your.shot.feature.settings.onboardingeducation.OnboardingEducationViewModel
import com.nicholas.rutherford.track.your.shot.feature.settings.termsconditions.TermsConditionsParams
import com.nicholas.rutherford.track.your.shot.feature.settings.termsconditions.TermsConditionsScreen
import com.nicholas.rutherford.track.your.shot.feature.settings.termsconditions.TermsConditionsViewModel
import com.nicholas.rutherford.track.your.shot.feature.splash.SplashScreen
import com.nicholas.rutherford.track.your.shot.feature.splash.SplashViewModel
import com.nicholas.rutherford.track.your.shot.navigation.NavigationDestinations
import com.nicholas.rutherford.track.your.shot.navigation.arguments.NamedArguments
import com.nicholas.rutherford.track.your.shot.navigation.arguments.NavArguments
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

/**
 * Defines navigation destinations and composable screens for the app.
 *
 * This object holds navigation functions used to add screens to the navigation graph.
 * It also manages the dynamic AppBar for each screen via a shared [AppBar2] instance.
 *
 * ## App Bar Management
 * - [currentAppBar] is a [mutableStateOf] object that holds the current top app bar ([AppBar2]).
 * - [updateAppBar] allows any screen to update the visible app bar.
 *
 * The current app bar is meant to be rendered at the top of your app layout (e.g., in your `MainScreen()`).
 * Each screen is responsible for setting the appropriate app bar using an [AppBarFactory] implementation.
 *
 * Example usage from a screen:
 *
 * ```kotlin
 * val viewModel: MyViewModel = koinViewModel()
 * val appBarFactory: AppBarFactory = koinInject()
 *
 * ObserveLifecycle(viewModel)
 * updateAppBar(appBarFactory.createXAppBar(viewModel))
 * ```
 */
object AppNavigationGraph {

    /**
     * Holds the currently displayed AppBar.
     * Should be observed and rendered from the top-level UI (e.g. [NavigationComponent]).
     */
    var currentAppBar by mutableStateOf<AppBar2?>(null)
        private set

    /**
     * Call this function from within a screen to set or update the AppBar.
     *
     * @param appBar The [AppBar2] to display or null to hide the app bar.
     */
    fun updateAppBar(appBar: AppBar2?) {
        currentAppBar = appBar
    }

    /**
     * Adds the Splash Screen destination to the NavGraph.
     * Retrieves [SplashViewModel] via Koin and observes its lifecycle.
     * Displays the [SplashScreen] composable.
     */
    fun NavGraphBuilder.splashScreen() {
        composable(route = NavigationDestinations.SPLASH_SCREEN) {
            val splashViewModel: SplashViewModel = koinViewModel()

            ObserveLifecycle(viewModel = splashViewModel)
            updateAppBar(appBar = null)

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
            val appBarFactory: AppBarFactory = koinInject()

            ObserveLifecycle(viewModel = loginViewModel)
            updateAppBar(appBar = appBarFactory.createLoginAppBar())

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
            val appBarFactory: AppBarFactory = koinInject()

            ObserveLifecycle(viewModel = forgotPasswordViewModel)
            updateAppBar(appBar = appBarFactory.createForgotPasswordAppBar(viewModel = forgotPasswordViewModel))

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
            val appBarFactory: AppBarFactory = koinInject()

            ObserveLifecycle(viewModel = createAccountViewModel)
            updateAppBar(appBar = appBarFactory.createAccountAppBar(viewModel = createAccountViewModel))

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
            val appBarFactory: AppBarFactory = koinInject()

            ObserveLifecycle(viewModel = authenticationScreenViewModel)
            updateAppBar(appBar = appBarFactory.createAuthenticationAppBar(viewModel = authenticationScreenViewModel))

            AuthenticationScreen(viewModel = authenticationScreenViewModel)
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
            val appBarFactory: AppBarFactory = koinInject()

            ObserveLifecycle(viewModel = termsConditionsViewModel)
            updateAppBar(appBar = appBarFactory.createTermsAndConditionsAppBar())

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
            val isFirstTimeLaunched = entry.arguments?.getBoolean(NamedArguments.IS_FIRST_TIME_LAUNCHED) ?: false
            val onboardingEducationViewModel: OnboardingEducationViewModel = koinViewModel()
            val appBarFactory: AppBarFactory = koinInject()

            ObserveLifecycle(viewModel = onboardingEducationViewModel)
            updateAppBar(appBar = appBarFactory.createOnboardingEducationAppBar(viewModel = onboardingEducationViewModel, isFirstTimeLaunched = isFirstTimeLaunched))

            OnboardingEducationScreen(
                onboardingEducationParams = OnboardingEducationParams(
                    onGotItButtonClicked = { onboardingEducationViewModel.onGotItButtonClicked() },
                    state = onboardingEducationViewModel.onboardingEducationStateFlow.collectAsState().value
                )
            )
        }
    }

        /**
         * Adds the Players List Screen destination to the NavGraph.
         *
         * Retrieves [PlayersListViewModel] via Koin and observes its lifecycle.
         * Collects UI state from the ViewModel and passes event callbacks to [PlayersListScreen].
         * Displays the [PlayersListScreen] composable.
         *
         * @param isConnectedToInternet [Boolean] to determine if the device is currently connected to the internet.
         */
        fun NavGraphBuilder.playersListScreen(isConnectedToInternet: Boolean) {
            composable(route = NavigationDestinations.PLAYERS_LIST_SCREEN) {
                val playersListViewModel: PlayersListViewModel = koinViewModel()
                val appBarFactory: AppBarFactory = koinInject()

                ObserveLifecycle(viewModel = playersListViewModel)
                updateAppBar(appBar = appBarFactory.createPlayersListAppBar(viewModel = playersListViewModel))

                PlayersListScreen(
                    playerListScreenParams = PlayersListScreenParams(
                        state = playersListViewModel.playerListStateFlow.collectAsState().value,
                        onToolbarMenuClicked = { playersListViewModel.onToolbarMenuClicked() },
                        updatePlayerListState = { playersListViewModel.updatePlayerListState() },
                        onAddPlayerClicked = { playersListViewModel.onAddPlayerClicked() },
                        onPlayerClicked = { player ->
                            playersListViewModel.onPlayerClicked(
                                player = player
                            )
                        },
                        onSheetItemClicked = { index ->
                            playersListViewModel.onSheetItemClicked(
                                isConnectedToInternet = isConnectedToInternet,
                                index = index
                            )
                        }
                    )
                )
            }
        }

    fun NavGraphBuilder.createOrEditPlayerScreen(isConnectedToInternet: Boolean) {
        composable(
            route = NavigationDestinations.CREATE_EDIT_PLAYER_SCREEN_WITH_PARAMS,
            arguments = NavArguments.createEditPlayer
        ) { entry ->
            val firstName = entry.arguments?.getString(NamedArguments.FIRST_NAME) ?: ""
            val lastName = entry.arguments?.getString(NamedArguments.LAST_NAME) ?: ""
            val isEditable = firstName.isNotEmpty() && lastName.isNotEmpty()
            val createEditPlayerViewModel: CreateEditPlayerViewModel = koinViewModel()
            val appBarFactory: AppBarFactory = koinInject()
            val createEditPlayerParams = CreateEditPlayerParams(
                state = createEditPlayerViewModel.createEditPlayerStateFlow.collectAsState().value,
                updateImageUriState = { uri -> createEditPlayerViewModel.updateImageUriState(uri = uri) },
                onClearImageState = { createEditPlayerViewModel.onClearImageState() },
                onToolbarMenuClicked = { createEditPlayerViewModel.onToolbarMenuClicked() },
                onLogShotsClicked = { createEditPlayerViewModel.onLogShotsClicked() },
                onFirstNameValueChanged = { newFirstName ->
                    createEditPlayerViewModel.onFirstNameValueChanged(
                        newFirstName = newFirstName
                    )
                },
                onLastNameValueChanged = { newLastName ->
                    createEditPlayerViewModel.onLastNameValueChanged(
                        newLastName = newLastName
                    )
                },
                onPlayerPositionStringChanged = { newPositionStringResId ->
                    createEditPlayerViewModel.onPlayerPositionStringChanged(
                        newPositionStringResId
                    )
                },
                onImageUploadClicked = { uri -> createEditPlayerViewModel.onImageUploadClicked(uri) },
                onCreatePlayerClicked = { createEditPlayerViewModel.onCreatePlayerClicked(isConnectedToInternet) },
                permissionNotGrantedForCameraAlert = { createEditPlayerViewModel.permissionNotGrantedForCameraAlert() },
                onSelectedCreateEditImageOption = { option ->
                    createEditPlayerViewModel.onSelectedCreateEditImageOption(option)
                },
                onViewShotClicked = { shotType, shotId ->
                    createEditPlayerViewModel.onViewShotClicked(
                        shotType = shotType,
                        shotId = shotId
                    )
                },
                onViewPendingShotClicked = { shotType, shotId ->
                    createEditPlayerViewModel.onViewPendingShotClicked(
                        shotType = shotType,
                        shotId = shotId
                    )
                }
            )

            ObserveLifecycle(viewModel = createEditPlayerViewModel)
            updateAppBar(appBar = appBarFactory.createEditPlayerAppBar(params = createEditPlayerParams, isEditable = isEditable))

            CreateEditPlayerScreen(createEditPlayerParams = createEditPlayerParams)
        }
    }
}
