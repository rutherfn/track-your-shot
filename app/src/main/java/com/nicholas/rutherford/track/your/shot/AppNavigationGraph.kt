package com.nicholas.rutherford.track.your.shot

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.nicholas.rutherford.track.your.shot.AppNavigationGraph.currentAppBar
import com.nicholas.rutherford.track.your.shot.AppNavigationGraph.updateAppBar
import com.nicholas.rutherford.track.your.shot.base.vm.ObserveLifecycle
import com.nicholas.rutherford.track.your.shot.compose.components.AppBar
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
import com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot.LogShotParams
import com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot.LogShotScreen
import com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot.LogShotViewModel
import com.nicholas.rutherford.track.your.shot.feature.players.shots.selectshot.SelectShotParams
import com.nicholas.rutherford.track.your.shot.feature.players.shots.selectshot.SelectShotScreen
import com.nicholas.rutherford.track.your.shot.feature.players.shots.selectshot.SelectShotViewModel
import com.nicholas.rutherford.track.your.shot.feature.reports.createreport.CreateReportParams
import com.nicholas.rutherford.track.your.shot.feature.reports.createreport.CreateReportScreen
import com.nicholas.rutherford.track.your.shot.feature.reports.createreport.CreateReportViewModel
import com.nicholas.rutherford.track.your.shot.feature.reports.reportlist.ReportListParams
import com.nicholas.rutherford.track.your.shot.feature.reports.reportlist.ReportListScreen
import com.nicholas.rutherford.track.your.shot.feature.reports.reportlist.ReportListViewModel
import com.nicholas.rutherford.track.your.shot.feature.settings.SettingsParams
import com.nicholas.rutherford.track.your.shot.feature.settings.SettingsScreen
import com.nicholas.rutherford.track.your.shot.feature.settings.SettingsViewModel
import com.nicholas.rutherford.track.your.shot.feature.settings.accountinfo.AccountInfoParams
import com.nicholas.rutherford.track.your.shot.feature.settings.accountinfo.AccountInfoScreen
import com.nicholas.rutherford.track.your.shot.feature.settings.accountinfo.AccountInfoViewModel
import com.nicholas.rutherford.track.your.shot.feature.settings.debugtoggle.DebugToggleParams
import com.nicholas.rutherford.track.your.shot.feature.settings.debugtoggle.DebugToggleScreen
import com.nicholas.rutherford.track.your.shot.feature.settings.debugtoggle.DebugToggleViewModel
import com.nicholas.rutherford.track.your.shot.feature.settings.enabledpermissions.EnabledPermissionsParams
import com.nicholas.rutherford.track.your.shot.feature.settings.enabledpermissions.EnabledPermissionsScreen
import com.nicholas.rutherford.track.your.shot.feature.settings.enabledpermissions.EnabledPermissionsViewModel
import com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots.createeditdeclaredshot.CreateEditDeclaredShotScreen
import com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots.createeditdeclaredshot.CreateEditDeclaredShotScreenParams
import com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots.createeditdeclaredshot.CreateEditDeclaredShotViewModel
import com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots.declaredshotslist.DeclaredShotsListScreen
import com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots.declaredshotslist.DeclaredShotsListScreenParams
import com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots.declaredshotslist.DeclaredShotsListViewModel
import com.nicholas.rutherford.track.your.shot.feature.settings.onboardingeducation.OnboardingEducationParams
import com.nicholas.rutherford.track.your.shot.feature.settings.onboardingeducation.OnboardingEducationScreen
import com.nicholas.rutherford.track.your.shot.feature.settings.onboardingeducation.OnboardingEducationViewModel
import com.nicholas.rutherford.track.your.shot.feature.settings.permissioneducation.PermissionEducationParams
import com.nicholas.rutherford.track.your.shot.feature.settings.permissioneducation.PermissionEducationScreen
import com.nicholas.rutherford.track.your.shot.feature.settings.permissioneducation.PermissionEducationViewModel
import com.nicholas.rutherford.track.your.shot.feature.settings.termsconditions.TermsConditionsParams
import com.nicholas.rutherford.track.your.shot.feature.settings.termsconditions.TermsConditionsScreen
import com.nicholas.rutherford.track.your.shot.feature.settings.termsconditions.TermsConditionsViewModel
import com.nicholas.rutherford.track.your.shot.feature.shots.ShotsListScreen
import com.nicholas.rutherford.track.your.shot.feature.shots.ShotsListScreenParams
import com.nicholas.rutherford.track.your.shot.feature.shots.ShotsListViewModel
import com.nicholas.rutherford.track.your.shot.feature.splash.SplashScreen
import com.nicholas.rutherford.track.your.shot.feature.splash.SplashViewModel
import com.nicholas.rutherford.track.your.shot.feature.voice.commands.VoiceCommandListScreen
import com.nicholas.rutherford.track.your.shot.feature.voice.commands.createeditvoicecommand.CreateEditVoiceCommandParams
import com.nicholas.rutherford.track.your.shot.feature.voice.commands.createeditvoicecommand.CreateEditVoiceCommandScreen
import com.nicholas.rutherford.track.your.shot.feature.voice.commands.createeditvoicecommand.CreateEditVoiceCommandViewModel
import com.nicholas.rutherford.track.your.shot.feature.voice.commands.voicecommandlist.VoiceCommandListParams
import com.nicholas.rutherford.track.your.shot.feature.voice.commands.voicecommandlist.VoiceCommandListViewModel
import com.nicholas.rutherford.track.your.shot.navigation.NavigationDestinations
import com.nicholas.rutherford.track.your.shot.navigation.arguments.NamedArguments
import com.nicholas.rutherford.track.your.shot.navigation.arguments.NavArguments
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Defines navigation destinations and composable screens for the app.
 *
 * This object holds navigation functions used to add screens to the navigation graph.
 * It also manages the dynamic AppBar for each screen via a shared [AppBar] instance.
 *
 * ## App Bar Management
 * - [currentAppBar] is a [mutableStateOf] object that holds the current top app bar ([AppBar]).
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
    var currentAppBar by mutableStateOf<AppBar?>(null)
        private set

    /**
     * Call this function from within a screen to set or update the AppBar.
     *
     * @param appBar The [AppBar] to display or null to hide the app bar.
     */
    fun updateAppBar(appBar: AppBar?) {
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
        ) {
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
        ) {
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
            val isFirstTimeLaunched =
                entry.arguments?.getBoolean(NamedArguments.IS_FIRST_TIME_LAUNCHED) ?: false
            val onboardingEducationViewModel: OnboardingEducationViewModel = koinViewModel()
            val appBarFactory: AppBarFactory = koinInject()

            ObserveLifecycle(viewModel = onboardingEducationViewModel)
            updateAppBar(
                appBar = appBarFactory.createOnboardingEducationAppBar(
                    viewModel = onboardingEducationViewModel,
                    isFirstTimeLaunched = isFirstTimeLaunched
                )
            )

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

    /**
     * Adds the Create Or Edit Player Screen destination to the NavGraph.
     * Retrieves [CreateEditPlayerViewModel] via Koin and observes its lifecycle.
     * Collects UI state from the ViewModel and passes event callbacks to [CreateEditPlayerScreen].
     * Displays the [CreateEditPlayerScreen] composable
     */
    fun NavGraphBuilder.createOrEditPlayerScreen(isConnectedToInternet: Boolean) {
        composable(
            route = NavigationDestinations.CREATE_EDIT_PLAYER_SCREEN_WITH_PARAMS,
            arguments = NavArguments.createEditPlayer
        ) { entry ->
            val firstName = entry.arguments?.getString(NamedArguments.FIRST_NAME) ?: ""
            val isEditable = firstName.isNotEmpty()
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
                onCreatePlayerClicked = {
                    createEditPlayerViewModel.onCreatePlayerClicked(
                        isConnectedToInternet
                    )
                },
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
            updateAppBar(
                appBar = appBarFactory.createEditPlayerAppBar(
                    params = createEditPlayerParams,
                    isEditable = isEditable
                )
            )

            CreateEditPlayerScreen(createEditPlayerParams = createEditPlayerParams)
        }
    }

    /**
     * Adds the Select Shot Screen destination to the NavGraph.
     * Retrieves [SelectShotViewModel] via Koin and observes its lifecycle.
     * Collects UI state from the ViewModel and passes event callbacks to [SelectShotScreen].
     * Displays the [SelectShotScreen] composable
     */
    fun NavGraphBuilder.selectShotScreen() {
        composable(
            route = NavigationDestinations.SELECT_SHOT_SCREEN_WITH_PARAMS,
            arguments = NavArguments.selectShot
        ) {
            val selectShotViewModel: SelectShotViewModel = koinViewModel()
            val appBarFactory: AppBarFactory = koinInject()
            val selectShotParams = SelectShotParams(
                state = selectShotViewModel.selectShotStateFlow.collectAsState().value,
                onSearchValueChanged = { newSearchQuery ->
                    selectShotViewModel.onSearchValueChanged(
                        newSearchQuery = newSearchQuery
                    )
                },
                onBackButtonClicked = { selectShotViewModel.onBackButtonClicked() },
                onCancelIconClicked = { query ->
                    selectShotViewModel.onCancelIconClicked(
                        query
                    )
                },
                onnDeclaredShotItemClicked = {},
                onHelpIconClicked = { selectShotViewModel.onHelpIconClicked() },
                onItemClicked = { shotType ->
                    selectShotViewModel.onDeclaredShotItemClicked(shotType = shotType)
                }
            )

            ObserveLifecycle(viewModel = selectShotViewModel)
            updateAppBar(appBar = appBarFactory.createSelectShotAppBar(selectShotParams = selectShotParams))

            SelectShotScreen(selectShotParams = selectShotParams)
        }
    }

    /**
     * Adds the Log Shot Screen destination to the NavGraph.
     * Retrieves [LogShotViewModel] via Koin and observes its lifecycle.
     * Collects UI state from the ViewModel and passes event callbacks to [LogShotScreen].
     * Displays the [LogShotScreen] composable
     */
    fun NavGraphBuilder.logShotScreen() {
        composable(
            route = NavigationDestinations.LOG_SHOT_WITH_PARAMS,
            arguments = NavArguments.logShot
        ) {
            val logShotViewModel: LogShotViewModel = koinViewModel()
            val appBarFactory: AppBarFactory = koinInject()
            val logShotParams = LogShotParams(
                state = logShotViewModel.logShotStateFlow.collectAsState().value,
                onBackButtonClicked = { logShotViewModel.onBackClicked() },
                onDateShotsTakenClicked = { logShotViewModel.onDateShotsTakenClicked() },
                onShotsMadeUpwardClicked = { value ->
                    logShotViewModel.onShotsMadeUpwardOrDownwardClicked(
                        shots = value
                    )
                },
                onShotsMadeDownwardClicked = { value ->
                    logShotViewModel.onShotsMadeUpwardOrDownwardClicked(
                        shots = value
                    )
                },
                onShotsMissedUpwardClicked = { value ->
                    logShotViewModel.onShotsMissedUpwardOrDownwardClicked(
                        shots = value
                    )
                },
                onShotsMissedDownwardClicked = { value ->
                    logShotViewModel.onShotsMissedUpwardOrDownwardClicked(
                        shots = value
                    )
                },
                onSaveClicked = { logShotViewModel.onSaveClicked() },
                onDeleteShotClicked = { logShotViewModel.onDeleteShotClicked() }
            )

            ObserveLifecycle(viewModel = logShotViewModel)
            updateAppBar(appBar = appBarFactory.createLogShotAppBar(params = logShotParams))

            LogShotScreen(logShotParams = logShotParams)
        }
    }

    /**
     * Adds the Report List Screen destination to the NavGraph.
     * Retrieves [ReportListViewModel] via Koin and observes its lifecycle.
     * Collects UI state from the ViewModel and passes event callbacks to [ReportListScreen].
     * Displays the [ReportListScreen] composable
     */
    fun NavGraphBuilder.reportListScreen() {
        composable(
            route = NavigationDestinations.REPORTS_LIST_SCREEN
        ) {
            val reportListViewModel: ReportListViewModel = koinViewModel()
            val appBarFactory: AppBarFactory = koinInject()

            val reportListParams = ReportListParams(
                state = reportListViewModel.reportListStateFlow.collectAsState().value,
                onToolbarMenuClicked = { reportListViewModel.onToolbarMenuClicked() },
                onAddReportClicked = { reportListViewModel.onCreatePlayerReportClicked() },
                onViewReportClicked = { url -> reportListViewModel.onViewReportClicked(url = url) },
                onDeletePlayerReportClicked = { individualPlayerReport ->
                    reportListViewModel.onDeletePlayerReportClicked(
                        individualPlayerReport = individualPlayerReport
                    )
                },
                onDownloadPlayerReportClicked = { individualPlayerReport ->
                    reportListViewModel.onDownloadPlayerReportClicked(
                        individualPlayerReport = individualPlayerReport
                    )
                },
                buildDateTimeStamp = { value -> reportListViewModel.buildDateTimeStamp(value) }
            )

            ObserveLifecycle(viewModel = reportListViewModel)

            updateAppBar(appBar = appBarFactory.createReportListAppBar(params = reportListParams))
            ReportListScreen(params = reportListParams)
        }
    }

    /**
     * Adds the Permission Education Screen destination to the NavGraph.
     * Retrieves [PermissionEducationViewModel] via Koin and observes its lifecycle.
     * Collects UI state from the ViewModel and passes event callbacks to [PermissionEducationScreen].
     * Displays the [PermissionEducationScreen] composable
     */
    fun NavGraphBuilder.permissionEducationScreen() {
        composable(
            route = NavigationDestinations.PERMISSION_EDUCATION_SCREEN
        ) {
            val permissionEducationViewModel: PermissionEducationViewModel = koinViewModel()
            val appBarFactory: AppBarFactory = koinInject()

            val permissionEducationParams = PermissionEducationParams(
                onGotItButtonClicked = { permissionEducationViewModel.onGotItButtonClicked() },
                onMoreInfoClicked = { permissionEducationViewModel.onMoreInfoClicked() },
                state = permissionEducationViewModel.permissionEducationStateFlow.collectAsState().value
            )

            ObserveLifecycle(viewModel = permissionEducationViewModel)
            updateAppBar(appBar = appBarFactory.createPermissionEducationAppBar(viewModel = permissionEducationViewModel))

            PermissionEducationScreen(permissionEducationParams = permissionEducationParams)
        }
    }

    /**
     * Adds the Enabled Permissions Screen destination to the NavGraph.
     * Retrieves [EnabledPermissionsViewModel] via Koin and observes its lifecycle.
     * Collects UI state from the ViewModel and passes event callbacks to [EnabledPermissionsScreen].
     * Displays the [EnabledPermissionsScreen] composable
     */
    fun NavGraphBuilder.enabledPermissionScreen() {
        composable(
            route = NavigationDestinations.ENABLED_PERMISSIONS_SCREEN
        ) {
            val enabledPermissionsViewModel: EnabledPermissionsViewModel = koinViewModel()
            val appBarFactory: AppBarFactory = koinInject()

            val enabledPermissionsParams = EnabledPermissionsParams(
                onToolbarMenuClicked = { enabledPermissionsViewModel.onToolbarMenuClicked() },
                onSwitchChangedToTurnOffPermission = { enabledPermissionsViewModel.onSwitchChangedToTurnOffPermission() },
                permissionNotGrantedForCameraAlert = { enabledPermissionsViewModel.permissionNotGrantedForCameraAlert() }
            )

            ObserveLifecycle(viewModel = enabledPermissionsViewModel)
            updateAppBar(appBar = appBarFactory.createEnabledPermissionsAppBar(params = enabledPermissionsParams))

            EnabledPermissionsScreen(params = enabledPermissionsParams)
        }
    }

    /**
     * Adds the Debug Toggle Screen destination to the NavGraph.
     * Retrieves [DebugToggleViewModel] via Koin and observes its lifecycle.
     * Collects UI state from the ViewModel and passes event callbacks to [DebugToggleScreen].
     * Displays the [DebugToggleScreen] composable
     */
    fun NavGraphBuilder.debugToggleScreen() {
        composable(
            route = NavigationDestinations.DEBUG_TOGGLE_SCREEN
        ) {
            val debugToggleViewModel: DebugToggleViewModel = koinViewModel()
            val appBarFactory: AppBarFactory = koinInject()

            val debugToggleParams = DebugToggleParams(
                state = debugToggleViewModel.debugToggleStateFlow.collectAsState().value,
                onToolbarMenuClicked = { debugToggleViewModel.onBackClicked() },
                onVoiceDebugToggled = { value -> debugToggleViewModel.onVoiceDebugToggled(value = value) },
                onVideoUploadDebugToggled = { value -> debugToggleViewModel.onVideoUploadDebugToggled(value = value) }
            )

            ObserveLifecycle(viewModel = debugToggleViewModel)
            updateAppBar(appBar = appBarFactory.createDebugToggleAppBar(params = debugToggleParams))

            DebugToggleScreen(params = debugToggleParams)
        }
    }

    /**
     * Adds the Create Edit Declared Shot Screen destination to the NavGraph.
     * Retrieves [CreateEditDeclaredShotViewModel] via Koin and observes its lifecycle.
     * Collects UI state from the ViewModel and passes event callbacks to [CreateEditDeclaredShotScreen].
     * Displays the [CreateEditDeclaredShotScreen] composable
     */
    fun NavGraphBuilder.createEditDeclaredScreen() {
        composable(
            route = NavigationDestinations.CREATE_EDIT_DECLARED_SHOTS_SCREEN_PARAMS,
            arguments = NavArguments.createEditDeclaredShot
        ) { entry ->
            val createEditDeclaredShotViewModel: CreateEditDeclaredShotViewModel = koinViewModel()
            val appBarFactory: AppBarFactory = koinInject()

            val shotName = entry.arguments?.getString(NamedArguments.SHOT_NAME) ?: ""

            val createEditDeclaredShotParams = CreateEditDeclaredShotScreenParams(
                state = createEditDeclaredShotViewModel.createEditDeclaredShotStateFlow.collectAsState().value,
                onToolbarMenuClicked = { createEditDeclaredShotViewModel.onToolbarMenuClicked() },
                onDeleteShotClicked = { id ->
                    createEditDeclaredShotViewModel.onDeleteShotClicked(
                        id = id
                    )
                },
                onEditShotPencilClicked = { createEditDeclaredShotViewModel.onEditShotPencilClicked() },
                onEditShotNameValueChanged = { name ->
                    createEditDeclaredShotViewModel.onEditShotNameValueChanged(
                        shotName = name
                    )
                },
                onEditShotCategoryValueChanged = { shotCategory ->
                    createEditDeclaredShotViewModel.onEditShotCategoryValueChanged(
                        shotCategory = shotCategory
                    )
                },
                onEditShotDescriptionValueChanged = { description ->
                    createEditDeclaredShotViewModel.onEditShotDescriptionValueChanged(
                        description = description
                    )
                },
                onCreateShotNameValueChanged = { name ->
                    createEditDeclaredShotViewModel.onCreateShotNameValueChanged(
                        shotName = name
                    )
                },
                onCreateShotDescriptionValueChanged = { shotDescription ->
                    createEditDeclaredShotViewModel.onCreateShotDescriptionValueChanged(
                        shotDescription = shotDescription
                    )
                },
                onCreateShotCategoryValueChanged = { shotCategory ->
                    createEditDeclaredShotViewModel.onCreateShotCategoryValueChanged(
                        shotCategory = shotCategory
                    )
                },
                onEditOrCreateNewShot = { createEditDeclaredShotViewModel.onEditOrCreateNewShot() }
            )

            ObserveLifecycle(viewModel = createEditDeclaredShotViewModel)
            updateAppBar(appBar = appBarFactory.createCreateEditDeclaredShotAppBar(declaredShotName = shotName, params = createEditDeclaredShotParams))

            CreateEditDeclaredShotScreen(params = createEditDeclaredShotParams)
        }
    }

    /**
     * Adds the Account Info Screen destination to the NavGraph.
     * Retrieves [AccountInfoViewModel] via Koin and observes its lifecycle.
     * Collects UI state from the ViewModel and passes event callbacks to [AccountInfoScreen].
     * Displays the [AccountInfoScreen] composable
     */
    fun NavGraphBuilder.accountInfoScreen() {
        composable(
            route = NavigationDestinations.ACCOUNT_INFO_SCREEN_WITH_PARAMS,
            arguments = NavArguments.accountInfo
        ) { entry ->
            val accountInfoViewModel: AccountInfoViewModel = koinViewModel()
            val appBarFactory: AppBarFactory = koinInject()
            val username = entry.arguments?.getString(NamedArguments.USERNAME) ?: ""
            val email = entry.arguments?.getString(NamedArguments.EMAIL) ?: ""

            val accountInfoParams = AccountInfoParams(
                onToolbarMenuClicked = { accountInfoViewModel.onToolbarMenuClicked() },
                usernameArgument = username,
                emailArgument = email
            )
            ObserveLifecycle(viewModel = accountInfoViewModel)

            updateAppBar(appBar = appBarFactory.createAccountInfoAppBar(viewModel = accountInfoViewModel))

            AccountInfoScreen(params = accountInfoParams)
        }
    }

    /**
     * Adds the Shots List Screen destination to the NavGraph.
     * Retrieves [ShotsListViewModel] via Koin and observes its lifecycle.
     * Collects UI state from the ViewModel and passes event callbacks to [ShotsListScreen].
     * Displays the [ShotsListScreen] composable
     */
    fun NavGraphBuilder.shotListScreen() {
        composable(
            route = NavigationDestinations.SHOTS_LIST_SCREEN_WITH_PARAMS,
            arguments = NavArguments.shotsList
        ) { entry ->
            val shotsListViewModel: ShotsListViewModel = koinViewModel()
            val appBarFactory: AppBarFactory = koinInject()
            val shouldShowAllPlayerShotsArgument =
                entry.arguments?.getBoolean(NamedArguments.SHOULD_SHOW_ALL_PLAYERS_SHOTS)
                    ?: false

            val shotsListParams = ShotsListScreenParams(
                state = shotsListViewModel.shotListStateFlow.collectAsState().value,
                onHelpClicked = { shotsListViewModel.onHelpClicked() },
                onToolbarMenuClicked = { shotsListViewModel.onToolbarMenuClicked() },
                onShotItemClicked = { shotLoggedWithPlayer ->
                    shotsListViewModel.onShotItemClicked(
                        shotLoggedWithPlayer
                    )
                },
                shouldShowAllPlayerShots = shouldShowAllPlayerShotsArgument
            )
            ObserveLifecycle(viewModel = shotsListViewModel)

            updateAppBar(appBar = appBarFactory.createShotsListAppBar(params = shotsListParams))

            ShotsListScreen(params = shotsListParams)
        }
    }

    /**
     * Adds the Create Report Screen destination to the NavGraph.
     * Retrieves [CreateReportViewModel] via Koin and observes its lifecycle.
     * Collects UI state from the ViewModel and passes event callbacks to [CreateReportScreen].
     * Displays the [CreateReportScreen] composable
     */
    fun NavGraphBuilder.createReportScreen() {
        composable(
            route = NavigationDestinations.CREATE_REPORT_SCREEN
        ) {
            val createReportViewModel: CreateReportViewModel = koinViewModel()
            val appBarFactory: AppBarFactory = koinInject()

            val createReportParams = CreateReportParams(
                onToolbarMenuClicked = { createReportViewModel.onToolbarMenuClicked() },
                onPlayerChanged = { playerName ->
                    createReportViewModel.onPlayerChanged(
                        playerName = playerName
                    )
                },
                attemptToGeneratePlayerReport = { createReportViewModel.attemptToGeneratePlayerReport() },
                state = createReportViewModel.createReportStateFlow.collectAsState().value
            )

            ObserveLifecycle(viewModel = createReportViewModel)

            updateAppBar(appBar = appBarFactory.createReportScreenAppBar(params = createReportParams))

            CreateReportScreen(params = createReportParams)
        }
    }

    /**
     * Adds the Voice Command List Screen destination to the NavGraph.
     * Retrieves [VoiceCommandListViewModel] via Koin and observes its lifecycle.
     * Collects UI state from the ViewModel and passes event callbacks to [VoiceCommandListScreen].
     * Displays the [VoiceCommandListScreen] composable
     */
    fun NavGraphBuilder.voiceCommandListScreen() {
        composable(
            route = NavigationDestinations.VOICE_COMMANDS_SCREEN
        ) {
            val voiceCommandListViewModel: VoiceCommandListViewModel = koinViewModel()
            val appBarFactory: AppBarFactory = koinInject()

            val params = VoiceCommandListParams(
                state = voiceCommandListViewModel.voiceCommandsStateFlow.collectAsState().value,
                onToolbarMenuClicked = { voiceCommandListViewModel.onToolbarMenuClicked() },
                onFilterSelected = { filter -> voiceCommandListViewModel.onFilterSelected(filter) },
                onCreateCommandTypeClicked = { type, phrase -> voiceCommandListViewModel.onCreateCommandTypeClicked(type = type, phrase = phrase) }
            )

            ObserveLifecycle(viewModel = voiceCommandListViewModel)

            updateAppBar(appBar = appBarFactory.createVoiceCommandListScreenAppBar(voiceCommandListViewModel = voiceCommandListViewModel))

            VoiceCommandListScreen(params = params)
        }
    }

    /**
     * Adds the Create Edit Voice Command Screen destination to the NavGraph.
     * Retrieves [CreateEditVoiceCommandViewModel] via Koin and observes its lifecycle.
     * Collects UI state from the ViewModel and passes event callbacks to [CreateEditVoiceCommandScreen].
     * Displays the [CreateEditVoiceCommandScreen] composable
     */
    fun NavGraphBuilder.createEditVoiceCommandScreen() {
        composable(
            route = NavigationDestinations.CREATE_EDIT_VOICE_COMMAND_SCREEN_WITH_PARAMS,
            arguments = NavArguments.createEditVoiceCommand
        ) { entry ->
            val createEditVoiceCommandViewModel: CreateEditVoiceCommandViewModel = koinViewModel()
            val appBarFactory: AppBarFactory = koinInject()

            val params = CreateEditVoiceCommandParams(
                state = createEditVoiceCommandViewModel.createEditVoiceCommandStateFlow.collectAsState().value,
                onRecordClicked = {},
                onSaveClicked = {},
                onToolbarMenuClicked = { createEditVoiceCommandViewModel.onToolbarMenuClicked() }
            )

            ObserveLifecycle(viewModel = createEditVoiceCommandViewModel)

            updateAppBar(appBar = appBarFactory.createEditVoiceCommandCreateScreenAppBar(createEditVoiceCommandViewModel = createEditVoiceCommandViewModel))

            CreateEditVoiceCommandScreen(params = params)
        }
    }

    /**
     * Adds the Settings Screen destination to the NavGraph.
     * Retrieves [SettingsViewModel] via Koin and observes its lifecycle.
     * Collects UI state from the ViewModel and passes event callbacks to [SettingsScreen].
     * Displays the [SettingsScreen] composable
     */
    fun NavGraphBuilder.settingsScreen() {
        composable(
            route = NavigationDestinations.SETTINGS_SCREEN
        ) {
            val settingsViewModel: SettingsViewModel = koinViewModel()
            val appBarFactory: AppBarFactory = koinInject()

            val settingsParams = SettingsParams(
                onToolbarMenuClicked = {
                    settingsViewModel.onToolbarMenuClicked()
                },
                onHelpClicked = {
                    settingsViewModel.onHelpClicked()
                },
                onSettingItemClicked = { value ->
                    settingsViewModel.onSettingItemClicked(value = value)
                },
                state = settingsViewModel.settingsStateFlow.collectAsState().value
            )

            ObserveLifecycle(viewModel = settingsViewModel)

            updateAppBar(appBar = appBarFactory.createSettingsAppBar(params = settingsParams))

            SettingsScreen(params = settingsParams)
        }
    }

    /**
     * Adds the Declared Shots List Screen destination to the NavGraph.
     * Retrieves [DeclaredShotsListViewModel] via Koin and observes its lifecycle.
     * Collects UI state from the ViewModel and passes event callbacks to [DeclaredShotsListScreen].
     * Displays the [DeclaredShotsListScreen] composable
     */
    fun NavGraphBuilder.declaredShotsListScreen() {
        composable(
            route = NavigationDestinations.DECLARED_SHOTS_LIST_SCREEN
        ) {
            val declaredShotsListViewModel: DeclaredShotsListViewModel = koinViewModel()
            val appBarFactory: AppBarFactory = koinInject()

            val declaredShotsListScreenParams = DeclaredShotsListScreenParams(
                state = declaredShotsListViewModel.declaredShotsListStateFlow.collectAsState().value,
                onDeclaredShotClicked = { title ->
                    declaredShotsListViewModel.onDeclaredShotClicked(title = title)
                },
                onToolbarMenuClicked = { declaredShotsListViewModel.onToolbarMenuClicked() },
                onAddDeclaredShotClicked = { declaredShotsListViewModel.onAddDeclaredShotClicked() }
            )

            ObserveLifecycle(viewModel = declaredShotsListViewModel)

            updateAppBar(appBar = appBarFactory.createDeclaredShotsListAppBar(params = declaredShotsListScreenParams))

            DeclaredShotsListScreen(declaredShotsListScreenParams = declaredShotsListScreenParams)
        }
    }
}
