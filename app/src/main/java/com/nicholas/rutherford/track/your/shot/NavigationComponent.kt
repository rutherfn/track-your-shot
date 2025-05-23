package com.nicholas.rutherford.track.your.shot

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.material.DrawerValue
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalDrawer
import androidx.compose.material.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.nicholas.rutherford.track.your.shot.base.vm.BaseViewModel
import com.nicholas.rutherford.track.your.shot.compose.components.dialogs.AlertDialog
import com.nicholas.rutherford.track.your.shot.compose.components.dialogs.CustomDatePickerDialog
import com.nicholas.rutherford.track.your.shot.compose.components.dialogs.ProgressDialog
import com.nicholas.rutherford.track.your.shot.compose.components.dialogs.ShotInputDialog
import com.nicholas.rutherford.track.your.shot.data.shared.InputInfo
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.alert.AlertConfirmAndDismissButton
import com.nicholas.rutherford.track.your.shot.data.shared.datepicker.DatePickerInfo
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.your.shot.feature.create.account.authentication.AuthenticationScreen
import com.nicholas.rutherford.track.your.shot.feature.create.account.createaccount.CreateAccountScreen
import com.nicholas.rutherford.track.your.shot.feature.create.account.createaccount.CreateAccountScreenParams
import com.nicholas.rutherford.track.your.shot.feature.forgot.password.ForgotPasswordScreen
import com.nicholas.rutherford.track.your.shot.feature.forgot.password.ForgotPasswordScreenParams
import com.nicholas.rutherford.track.your.shot.feature.login.LoginScreen
import com.nicholas.rutherford.track.your.shot.feature.login.LoginScreenParams
import com.nicholas.rutherford.track.your.shot.feature.players.playerlist.PlayersListScreen
import com.nicholas.rutherford.track.your.shot.feature.players.playerlist.PlayersListScreenParams
import com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot.LogShotParams
import com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot.LogShotScreen
import com.nicholas.rutherford.track.your.shot.feature.players.shots.selectshot.SelectShotParams
import com.nicholas.rutherford.track.your.shot.feature.players.shots.selectshot.SelectShotScreen
import com.nicholas.rutherford.track.your.shot.feature.settings.SettingsParams
import com.nicholas.rutherford.track.your.shot.feature.settings.SettingsScreen
import com.nicholas.rutherford.track.your.shot.feature.settings.accountinfo.AccountInfoParams
import com.nicholas.rutherford.track.your.shot.feature.settings.accountinfo.AccountInfoScreen
import com.nicholas.rutherford.track.your.shot.feature.settings.enabledpermissions.EnabledPermissionsParams
import com.nicholas.rutherford.track.your.shot.feature.settings.enabledpermissions.EnabledPermissionsScreen
import com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots.createeditdeclaredshot.CreateEditDeclaredShotScreen
import com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots.createeditdeclaredshot.CreateEditDeclaredShotScreenParams
import com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots.declaredshotslist.DeclaredShotsListScreen
import com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots.declaredshotslist.DeclaredShotsListScreenParams
import com.nicholas.rutherford.track.your.shot.feature.settings.onboardingeducation.OnboardingEducationParams
import com.nicholas.rutherford.track.your.shot.feature.settings.onboardingeducation.OnboardingEducationScreen
import com.nicholas.rutherford.track.your.shot.feature.settings.permissioneducation.PermissionEducationParams
import com.nicholas.rutherford.track.your.shot.feature.settings.permissioneducation.PermissionEducationScreen
import com.nicholas.rutherford.track.your.shot.feature.settings.termsconditions.TermsConditionsParams
import com.nicholas.rutherford.track.your.shot.feature.settings.termsconditions.TermsConditionsScreen
import com.nicholas.rutherford.track.your.shot.feature.shots.ShotsListScreen
import com.nicholas.rutherford.track.your.shot.feature.shots.ShotsListScreenParams
import com.nicholas.rutherford.track.your.shot.feature.splash.SplashScreen
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import com.nicholas.rutherford.track.your.shot.navigation.LogoutAction
import com.nicholas.rutherford.track.your.shot.navigation.NavigationDestinations
import com.nicholas.rutherford.track.your.shot.navigation.Navigator
import com.nicholas.rutherford.track.your.shot.navigation.PlayersListAction
import com.nicholas.rutherford.track.your.shot.navigation.ReportingAction
import com.nicholas.rutherford.track.your.shot.navigation.SettingsAction
import com.nicholas.rutherford.track.your.shot.navigation.ShotsAction
import com.nicholas.rutherford.track.your.shot.navigation.arguments.NamedArguments
import com.nicholas.rutherford.track.your.shot.navigation.arguments.NavArguments
import com.nicholas.rutherford.track.your.shot.navigation.asLifecycleAwareState
import kotlinx.coroutines.launch

@Composable
fun NavigationComponent(
    activity: MainActivity,
    navHostController: NavHostController,
    navigator: Navigator,
    viewModels: ViewModels
) {
    val coroutineScope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    val scope = rememberCoroutineScope()

    val alertState by navigator.alertActions.asLifecycleAwareState(
        lifecycleOwner = lifecycleOwner,
        initialState = null
    )
    val appSettingsState by navigator.appSettingsActions.asLifecycleAwareState(
        lifecycleOwner = lifecycleOwner,
        initialState = null
    )
    val datePickerState by navigator.datePickerActions.asLifecycleAwareState(
        lifecycleOwner = lifecycleOwner,
        initialState = null
    )
    val emailState by navigator.emailActions.asLifecycleAwareState(
        lifecycleOwner = lifecycleOwner,
        initialState = null
    )
    val emailDevState by navigator.emailDevActions.asLifecycleAwareState(
        lifecycleOwner = lifecycleOwner,
        initialState = null
    )
    val finishState by navigator.finishActions.asLifecycleAwareState(
        lifecycleOwner = lifecycleOwner,
        initialState = null
    )
    val inputInfoState by navigator.inputInfoActions.asLifecycleAwareState(
        lifecycleOwner = lifecycleOwner,
        initialState = null
    )
    val navigatorState by navigator.navActions.asLifecycleAwareState(
        lifecycleOwner = lifecycleOwner,
        initialState = null
    )
    val popRouteState by navigator.popRouteActions.asLifecycleAwareState(
        lifecycleOwner = lifecycleOwner,
        initialState = null
    )
    val progressState by navigator.progressActions.asLifecycleAwareState(
        lifecycleOwner = lifecycleOwner,
        initialState = null
    )
    val navigationDrawerState by navigator.navigationDrawerAction.asLifecycleAwareState(
        lifecycleOwner = lifecycleOwner,
        initialState = null
    )
    val urlState by navigator.urlAction.asLifecycleAwareState(
        lifecycleOwner = lifecycleOwner,
        initialState = null
    )

    var alert: Alert? by remember { mutableStateOf(value = null) }
    var datePicker: DatePickerInfo? by remember { mutableStateOf(value = null) }
    var inputInfo: InputInfo? by remember { mutableStateOf(value = null) }
    var progress: Progress? by remember { mutableStateOf(value = null) }

    val screenContents = ScreenContents()

    val mainActivityViewModel = viewModels.mainActivityViewModel
    val createAccountViewModel = viewModels.createAccountViewModel
    val loginViewModel = viewModels.loginViewModel
    val playersListViewModel = viewModels.playersListViewModel
    val createEditPlayerViewModel = viewModels.createEditPlayerViewModel
    val forgotPasswordViewModel = viewModels.forgotPasswordViewModel
    val selectShotViewModel = viewModels.selectShotViewModel
    val logShotViewModel = viewModels.logShotViewModel
    val settingsViewModel = viewModels.settingsViewModel
    val permissionEducationViewModel = viewModels.permissionEducationViewModel
    val termsConditionsViewModel = viewModels.termsConditionsViewModel
    val onboardingEducationViewModel = viewModels.onboardingEducationViewModel
    val enabledPermissionsViewModel = viewModels.enabledPermissionsViewModel
    val accountInfoViewModel = viewModels.accountInfoViewModel
    val reportListViewModel = viewModels.reportListViewModel
    val createReportViewModel = viewModels.createReportViewModel
    val shotsListViewModel = viewModels.shotsListViewModel
    val declaredShotsListViewModel = viewModels.declaredShotsListViewModel
    val createEditDeclaredShotViewModel = viewModels.createEditDeclaredShotsViewModel

    val isConnectedToInternet = mainActivityViewModel.isConnected.collectAsState().value

    LaunchedEffect(alertState) {
        alertState?.let { newAlert ->
            alert = newAlert
        }
    }
    LaunchedEffect(datePickerState) {
        datePickerState?.let { newDatePicker ->
            datePicker = newDatePicker
        }
    }
    LaunchedEffect(inputInfoState) {
        inputInfoState?.let { newInputInfo ->
            inputInfo = newInputInfo
        }
    }
    LaunchedEffect(appSettingsState) {
        appSettingsState?.let { shouldOpenAppSettings ->
            if (shouldOpenAppSettings) {
                val intent = Intent()

                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri = Uri.fromParts("package", activity.packageName, null)
                intent.data = uri

                activity.startActivity(intent)

                navigator.appSettings(appSettingsAction = null)
            }
        }
    }
    LaunchedEffect(emailState) {
        emailState?.let { shouldAttemptToOpenEmail ->
            if (shouldAttemptToOpenEmail) {
                try {
                    val intent = Intent(Intent.ACTION_MAIN)

                    intent.addCategory(Intent.CATEGORY_APP_EMAIL)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                    activity.startActivity(intent)
                    navigator.emailAction(emailAction = null)
                } catch (ex: ActivityNotFoundException) {
                    ex.printStackTrace()
                }
            }
        }
    }
    LaunchedEffect(emailDevState) {
        emailDevState?.let { devEmail ->
            try {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = "mailto:$devEmail".toUri()
                }

                activity.startActivity(intent)
                navigator.emailDevAction(emailDevAction = null)
            } catch (ex: ActivityNotFoundException) {
                ex.printStackTrace()
            }
        }
    }

    fun findViewModelByDestination(destination: String): BaseViewModel? {
        return when {
            destination.contains(NavigationDestinations.REPORTS_LIST_SCREEN) -> reportListViewModel
            destination.contains(NavigationDestinations.CREATE_REPORT_SCREEN) -> createReportViewModel
            destination.contains(NavigationDestinations.PLAYERS_LIST_SCREEN) -> playersListViewModel
            destination.contains(NavigationDestinations.SELECT_SHOT_SCREEN) -> selectShotViewModel
            destination.contains(NavigationDestinations.SHOTS_LIST_SCREEN) -> shotsListViewModel
            destination.contains(NavigationDestinations.DECLARED_SHOTS_LIST_SCREEN) -> declaredShotsListViewModel
            destination.contains(NavigationDestinations.CREATE_EDIT_DECLARED_SHOTS_SCREEN) -> createEditDeclaredShotViewModel
            else -> null
        }
    }

    LaunchedEffect(navigatorState) {
        navigatorState?.let {
            navHostController.navigate(it.destination, it.navOptions)
            findViewModelByDestination(destination = it.destination)?.onNavigatedTo()
        }
    }

    LaunchedEffect(finishState) {
        finishState?.let { shouldFinish ->
            if (shouldFinish) {
                activity.finish()
                navigator.finish(finishAction = null)
            }
        }
    }

    LaunchedEffect(popRouteState) {
        popRouteState?.let { route ->
            if (route == Constants.POP_DEFAULT_ACTION) {
                navHostController.popBackStack()
            } else {
                navHostController.popBackStack(route = route, inclusive = false)
                findViewModelByDestination(destination = route)?.onNavigatedTo()
            }
            navigator.pop(popRouteAction = null) // need to set this to null to listen to next pop action
        }
    }

    LaunchedEffect(progressState) {
        progressState?.let { newProgress ->
            progress = newProgress
        } ?: run {
            progress = null
        }
    }

    LaunchedEffect(navigationDrawerState) {
        navigationDrawerState?.let { shouldOpenNavigationDrawer ->
            scope.launch {
                if (shouldOpenNavigationDrawer) {
                    drawerState.open()
                } else {
                    drawerState.close()
                }
                navigator.showNavigationDrawer(navigationDrawerAction = null)
            }
        }
    }

    LaunchedEffect(urlState) {
        urlState?.let { url ->
            val intent = Intent(Intent.ACTION_VIEW, url.toUri())
            activity.startActivity(intent)
            navigator.url(url = null)
        }
    }

    ModalDrawer(
        drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen,
        drawerShape = MaterialTheme.shapes.medium,
        drawerContent = {
            DrawerContent(
                actions = listOf(
                    PlayersListAction,
                    ShotsAction,
                    ReportingAction,
                    SettingsAction,
                    LogoutAction
                ),
                onDestinationClicked = { route, navOptions, titleId ->
                    scope.launch {
                        drawerState.close()
                    }
                    if (route.isEmpty()) {
                        mainActivityViewModel.logout(titleId = titleId)
                    } else {
                        val currentRoute = navHostController.currentDestination?.route ?: ""
                        if (route != currentRoute) {
                            navHostController.navigate(route, navOptions)
                            findViewModelByDestination(destination = route)?.onNavigatedTo()
                        }
                    }
                }
            )
        }
    ) {
        NavHost(
            navController = navHostController,
            startDestination = NavigationDestinations.SPLASH_SCREEN
        ) {
            composable(route = NavigationDestinations.SPLASH_SCREEN) {
                SplashScreen(navigateToPlayersListLoginOrAuthentication = {
                    viewModels.splashViewModel.navigateToPlayersListLoginOrAuthentication()
                })
            }
            composable(route = NavigationDestinations.LOGIN_SCREEN) {
                LoginScreen(
                    loginScreenParams = LoginScreenParams(
                        state = loginViewModel.loginStateFlow.collectAsState().value,
                        onEmailValueChanged = { newEmail ->
                            loginViewModel.onEmailValueChanged(
                                newEmail = newEmail
                            )
                        },
                        onPasswordValueChanged = { newPassword ->
                            loginViewModel.onPasswordValueChanged(
                                newPassword = newPassword
                            )
                        },
                        onLoginButtonClicked = {
                            loginViewModel.onLoginButtonClicked()
                        },
                        onForgotPasswordClicked = { loginViewModel.onForgotPasswordClicked() },
                        onCreateAccountClicked = { loginViewModel.onCreateAccountClicked() },
                        coroutineScope = coroutineScope
                    )
                )
            }
            composable(route = NavigationDestinations.PLAYERS_LIST_SCREEN) {
                PlayersListScreen(
                    playerListScreenParams = PlayersListScreenParams(
                        state = playersListViewModel.playerListStateFlow.collectAsState().value,
                        onToolbarMenuClicked = { playersListViewModel.onToolbarMenuClicked() },
                        updatePlayerListState = { playersListViewModel.updatePlayerListState() },
                        onAddPlayerClicked = { playersListViewModel.onAddPlayerClicked() },
                        onPlayerClicked = { player -> playersListViewModel.onPlayerClicked(player = player) },
                        onSheetItemClicked = { index -> playersListViewModel.onSheetItemClicked(isConnectedToInternet = isConnectedToInternet, index = index) }
                    )
                )
            }
            composable(
                route = NavigationDestinations.SHOTS_LIST_SCREEN_WITH_PARAMS,
                arguments = NavArguments.shotsList
            ) { entry ->
                ShotsListScreen(
                    params = ShotsListScreenParams(
                        state = shotsListViewModel.shotListStateFlow.collectAsState().value,
                        onHelpClicked = { shotsListViewModel.onHelpClicked() },
                        onToolbarMenuClicked = { shotsListViewModel.onToolbarMenuClicked() },
                        onShotItemClicked = { shotLoggedWithPlayer -> shotsListViewModel.onShotItemClicked(shotLoggedWithPlayer) },
                        shouldShowAllPlayerShots = entry.arguments?.getBoolean(NamedArguments.SHOULD_SHOW_ALL_PLAYERS_SHOTS) ?: false
                    )
                )
            }
            composable(
                route = NavigationDestinations.CREATE_EDIT_PLAYER_SCREEN_WITH_PARAMS,
                arguments = NavArguments.createEditPlayer
            ) { entry ->
                val firstNameArgument = entry.arguments?.getString(NamedArguments.FIRST_NAME)
                val lastNameArgument = entry.arguments?.getString(NamedArguments.LAST_NAME)
                screenContents.createEditPlayerContent(
                    isConnectedToInternet = isConnectedToInternet,
                    firstNameArgument = firstNameArgument,
                    lastNameArgument = lastNameArgument,
                    createEditPlayerViewModel = createEditPlayerViewModel
                )(entry)
            }
            composable(route = NavigationDestinations.CREATE_EDIT_PLAYER_SCREEN) { entry ->
                screenContents.createEditPlayerContent(
                    isConnectedToInternet = isConnectedToInternet,
                    firstNameArgument = null,
                    lastNameArgument = null,
                    createEditPlayerViewModel = createEditPlayerViewModel
                )(entry)
            }
            composable(
                route = NavigationDestinations.SELECT_SHOT_SCREEN_WITH_PARAMS,
                arguments = NavArguments.selectShot
            ) { entry ->
                SelectShotScreen(
                    selectShotParams = SelectShotParams(
                        state = selectShotViewModel.selectShotStateFlow.collectAsState().value,
                        onSearchValueChanged = { newSearchQuery -> selectShotViewModel.onSearchValueChanged(newSearchQuery = newSearchQuery) },
                        onBackButtonClicked = { selectShotViewModel.onBackButtonClicked() },
                        onCancelIconClicked = { query -> selectShotViewModel.onCancelIconClicked(query) },
                        onnDeclaredShotItemClicked = {},
                        onHelpIconClicked = { selectShotViewModel.onHelpIconClicked() },
                        updateIsExistingPlayerAndPlayerId = {
                            selectShotViewModel.updateIsExistingPlayerAndPlayerId(
                                isExistingPlayerArgument = entry.arguments?.getBoolean(NamedArguments.IS_EXISTING_PLAYER),
                                playerIdArgument = entry.arguments?.getInt(NamedArguments.PLAYER_ID)
                            )
                        },
                        onItemClicked = { shotType ->
                            selectShotViewModel.onDeclaredShotItemClicked(shotType = shotType)
                        }
                    )
                )
            }
            composable(
                route = NavigationDestinations.LOG_SHOT_WITH_PARAMS,
                arguments = NavArguments.logShot
            ) { entry ->
                entry.arguments?.let { bundle ->
                    LogShotScreen(
                        logShotParams = LogShotParams(
                            state = logShotViewModel.logShotStateFlow.collectAsState().value,
                            onBackButtonClicked = { logShotViewModel.onBackClicked() },
                            onDateShotsTakenClicked = { logShotViewModel.onDateShotsTakenClicked() },
                            updateIsExistingPlayerAndPlayerId = {
                                logShotViewModel.updateIsExistingPlayerAndId(
                                    isExistingPlayerArgument = bundle.getBoolean(NamedArguments.IS_EXISTING_PLAYER),
                                    playerIdArgument = bundle.getInt(NamedArguments.PLAYER_ID),
                                    shotTypeArgument = bundle.getInt(NamedArguments.SHOT_TYPE),
                                    shotIdArgument = bundle.getInt(NamedArguments.SHOT_ID),
                                    viewCurrentExistingShotArgument = bundle.getBoolean(NamedArguments.VIEW_CURRENT_EXISTING_SHOT),
                                    viewCurrentPendingShotArgument = bundle.getBoolean(NamedArguments.VIEW_CURRENT_PENDING_SHOT),
                                    fromShotListArgument = bundle.getBoolean(NamedArguments.FROM_SHOT_LIST)
                                )
                            },
                            onShotsMadeUpwardClicked = { value -> logShotViewModel.onShotsMadeUpwardOrDownwardClicked(shots = value) },
                            onShotsMadeDownwardClicked = { value -> logShotViewModel.onShotsMadeUpwardOrDownwardClicked(shots = value) },
                            onShotsMissedUpwardClicked = { value -> logShotViewModel.onShotsMissedUpwardOrDownwardClicked(shots = value) },
                            onShotsMissedDownwardClicked = { value -> logShotViewModel.onShotsMissedUpwardOrDownwardClicked(shots = value) },
                            onSaveClicked = { logShotViewModel.onSaveClicked() },
                            onDeleteShotClicked = { logShotViewModel.onDeleteShotClicked() }
                        )
                    )
                }
            }
            composable(
                route = NavigationDestinations.FORGOT_PASSWORD_SCREEN
            ) {
                ForgotPasswordScreen(
                    forgotPasswordScreenParams = ForgotPasswordScreenParams(
                        state = forgotPasswordViewModel.forgotPasswordStateFlow.collectAsState().value,
                        onEmailValueChanged = { newEmail ->
                            forgotPasswordViewModel.onEmailValueChanged(
                                newEmail = newEmail
                            )
                        },
                        onSendPasswordResetButtonClicked = { newEmail ->
                            coroutineScope.launch {
                                forgotPasswordViewModel.onSendPasswordResetButtonClicked(
                                    newEmail = newEmail
                                )
                            }
                        },
                        onBackButtonClicked = { forgotPasswordViewModel.onBackButtonClicked() }
                    )
                )
            }
            composable(
                route = NavigationDestinations.SETTINGS_SCREEN
            ) {
                SettingsScreen(
                    params = SettingsParams(
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
                )
            }
            composable(route = NavigationDestinations.REPORTS_LIST_SCREEN) {
                screenContents.reportListContent(reportListViewModel = reportListViewModel).invoke()
            }
            composable(
                route = NavigationDestinations.CREATE_REPORT_SCREEN
            ) {
                screenContents.createReportContent(createReportViewModel = createReportViewModel).invoke()
            }
            composable(
                route = NavigationDestinations.PERMISSION_EDUCATION_SCREEN
            ) {
                PermissionEducationScreen(
                    permissionEducationParams = PermissionEducationParams(
                        onGotItButtonClicked = { permissionEducationViewModel.onGotItButtonClicked() },
                        onMoreInfoClicked = { permissionEducationViewModel.onMoreInfoClicked() },
                        state = permissionEducationViewModel.permissionEducationStateFlow.collectAsState().value
                    )
                )
            }
            composable(
                route = NavigationDestinations.ENABLED_PERMISSIONS_SCREEN
            ) {
                EnabledPermissionsScreen(
                    params = EnabledPermissionsParams(
                        onToolbarMenuClicked = { enabledPermissionsViewModel.onToolbarMenuClicked() },
                        onSwitchChangedToTurnOffPermission = { enabledPermissionsViewModel.onSwitchChangedToTurnOffPermission() },
                        permissionNotGrantedForCameraAlert = { enabledPermissionsViewModel.permissionNotGrantedForCameraAlert() }
                    )
                )
            }
            composable(
                route = NavigationDestinations.DECLARED_SHOTS_LIST_SCREEN
            ) {
                DeclaredShotsListScreen(
                    declaredShotsListScreenParams = DeclaredShotsListScreenParams(
                        state = declaredShotsListViewModel.declaredShotsListStateFlow.collectAsState().value,
                        onDeclaredShotClicked = { id -> declaredShotsListViewModel.onDeclaredShotClicked(id = id) },
                        onToolbarMenuClicked = { declaredShotsListViewModel.onToolbarMenuClicked() },
                        onAddDeclaredShotClicked = { declaredShotsListViewModel.onAddDeclaredShotClicked() }
                    )
                )
            }
            composable(
                route = NavigationDestinations.CREATE_EDIT_DECLARED_SHOTS_SCREEN
            ) {
                CreateEditDeclaredShotScreen(
                    params = CreateEditDeclaredShotScreenParams(
                        state = createEditDeclaredShotViewModel.createEditDeclaredShotStateFlow.collectAsState().value,
                        onToolbarMenuClicked = { createEditDeclaredShotViewModel.onToolbarMenuClicked() },
                        onDeleteShotClicked = { id -> createEditDeclaredShotViewModel.onDeleteShotClicked(id = id) },
                        onEditShotPencilClicked = { createEditDeclaredShotViewModel.onEditShotPencilClicked() },
                        onEditShotNameValueChanged = { shotName -> createEditDeclaredShotViewModel.onEditShotNameValueChanged(shotName = shotName) },
                        onEditShotCategoryValueChanged = { shotCategory -> createEditDeclaredShotViewModel.onEditShotCategoryValueChanged(shotCategory = shotCategory) },
                        onEditShotDescriptionValueChanged = { description -> createEditDeclaredShotViewModel.onEditShotDescriptionValueChanged(description = description) },
                        onCreateShotNameValueChanged = { shotName -> createEditDeclaredShotViewModel.onCreateShotNameValueChanged(shotName = shotName) },
                        onCreateShotDescriptionValueChanged = { shotDescription -> createEditDeclaredShotViewModel.onCreateShotDescriptionValueChanged(shotDescription = shotDescription) },
                        onCreateShotCategoryValueChanged = { shotCategory -> createEditDeclaredShotViewModel.onCreateShotCategoryValueChanged(shotCategory = shotCategory) },
                        onEditOrCreateNewShot = { createEditDeclaredShotViewModel.onEditOrCreateNewShot() }
                    )
                )
            }
            composable(
                route = NavigationDestinations.TERMS_CONDITIONS_WITH_PARAMS,
                arguments = NavArguments.termsConditions
            ) { entry ->
                entry.arguments?.let { bundle ->
                    val isAcknowledgeConditions = bundle.getBoolean(NamedArguments.IS_ACKNOWLEDGE_CONDITIONS)
                    TermsConditionsScreen(
                        params = TermsConditionsParams(
                            updateButtonTextState = { termsConditionsViewModel.updateButtonTextState(isAcknowledgeConditions = isAcknowledgeConditions) },
                            onCloseAcceptButtonClicked = { termsConditionsViewModel.onCloseAcceptButtonClicked(isAcknowledgeConditions = isAcknowledgeConditions) },
                            onDevEmailClicked = { termsConditionsViewModel.onDevEmailClicked() },
                            state = termsConditionsViewModel.termsConditionsStateFlow.collectAsState().value,
                            isAcknowledgeConditions = isAcknowledgeConditions
                        )
                    )
                }
            }
            composable(
                route = NavigationDestinations.ONBOARDING_EDUCATION_SCREEN
            ) {
                OnboardingEducationScreen(
                    onboardingEducationParams = OnboardingEducationParams(
                        onGotItButtonClicked = { onboardingEducationViewModel.onGotItButtonClicked() },
                        state = onboardingEducationViewModel.onboardingEducationStateFlow.collectAsState().value
                    )
                )
            }
            composable(route = NavigationDestinations.CREATE_ACCOUNT_SCREEN) {
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
                        onCreateAccountButtonClicked = { createAccountViewModel.onCreateAccountButtonClicked(isConnectedToInternet = isConnectedToInternet) },
                        onBackButtonClicked = { createAccountViewModel.onBackButtonClicked() }
                    )
                )
            }
            composable(
                route = NavigationDestinations.AUTHENTICATION_SCREEN_WITH_PARAMS,
                arguments = NavArguments.authentication
            ) {
                AuthenticationScreen(
                    viewModel = viewModels.authenticationViewModel,
                    usernameArgument = it.arguments?.getString(NamedArguments.USERNAME),
                    emailArgument = it.arguments?.getString(NamedArguments.EMAIL)
                )
            }
            composable(
                route = NavigationDestinations.ACCOUNT_INFO_SCREEN_PARAMS,
                arguments = NavArguments.accountInfo
            ) { entry ->
                entry.arguments?.let { bundle ->
                    val usernameArgument = bundle.getString(NamedArguments.USERNAME) ?: ""
                    val emailArgument = bundle.getString(NamedArguments.EMAIL) ?: ""

                    AccountInfoScreen(
                        params = AccountInfoParams(
                            onToolbarMenuClicked = { accountInfoViewModel.onToolbarMenuClicked() },
                            usernameArgument = usernameArgument,
                            emailArgument = emailArgument
                        )
                    )
                }
            }
        }
    }

    inputInfo?.let { info ->
        ShotInputDialog(
            inputInfo = InputInfo(
                titleResId = info.titleResId,
                confirmButtonResId = info.confirmButtonResId,
                dismissButtonResId = info.dismissButtonResId,
                placeholderResId = info.placeholderResId,
                startingInputAmount = info.startingInputAmount,
                onConfirmButtonClicked = { value ->
                    navigator.inputInfo(inputInfoAction = null)
                    info.onConfirmButtonClicked.invoke(value)
                    inputInfo = null
                },
                onDismissButtonClicked = {
                    navigator.inputInfo(inputInfoAction = null)
                    info.onDismissButtonClicked?.invoke()
                    inputInfo = null
                }
            )
        )
    }

    datePicker?.let { newDatePicker ->
        TrackMyShotTheme {
            CustomDatePickerDialog(
                datePickerInfo = DatePickerInfo(
                    onDateOkClicked = { value ->
                        navigator.datePicker(datePickerAction = null)
                        datePicker = null
                        newDatePicker.onDateOkClicked.invoke(value)
                    },
                    onDismissClicked = {
                        navigator.datePicker(datePickerAction = null)
                        datePicker = null
                        newDatePicker.onDismissClicked?.invoke()
                    },
                    dateValue = newDatePicker.dateValue
                )
            )
        }
    }

    alert?.let { newAlert ->
        AlertDialog(
            onDismissClicked = {
                navigator.alert(alertAction = null)
                alert = null
                newAlert.onDismissClicked?.invoke()
            },
            title = newAlert.title,
            confirmButton = newAlert.confirmButton?.let { confirmButton ->
                AlertConfirmAndDismissButton(
                    onButtonClicked = {
                        navigator.alert(alertAction = null)
                        alert = null
                        confirmButton.onButtonClicked?.invoke()
                    },
                    buttonText = confirmButton.buttonText
                )
            } ?: run { null },
            dismissButton = newAlert.dismissButton?.let { dismissButton ->
                AlertConfirmAndDismissButton(
                    onButtonClicked = {
                        navigator.alert(alertAction = null)
                        alert = null
                        dismissButton.onButtonClicked?.invoke()
                    },
                    buttonText = dismissButton.buttonText
                )
            } ?: run { null },
            description = newAlert.description
        )
    }

    progress?.let { newProgress ->
        ProgressDialog(
            onDismissClicked = {
                if (newProgress.shouldBeAbleToBeDismissed) {
                    navigator.progress(progressAction = null)
                    progress = null
                }
                newProgress.onDismissClicked?.invoke()
            },
            title = newProgress.title
        )
    }
}
