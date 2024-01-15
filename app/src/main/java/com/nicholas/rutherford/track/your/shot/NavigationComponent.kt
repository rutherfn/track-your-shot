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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.nicholas.rutherford.track.your.shot.compose.components.AlertDialog
import com.nicholas.rutherford.track.your.shot.compose.components.ProgressDialog
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.alert.AlertConfirmAndDismissButton
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
import com.nicholas.rutherford.track.your.shot.feature.players.shots.SelectShotParams
import com.nicholas.rutherford.track.your.shot.feature.players.shots.SelectShotScreen
import com.nicholas.rutherford.track.your.shot.feature.splash.SplashScreen
import com.nicholas.rutherford.track.your.shot.navigation.ComparePlayersStatsAction
import com.nicholas.rutherford.track.your.shot.navigation.LogoutAction
import com.nicholas.rutherford.track.your.shot.navigation.NavigationDestinations
import com.nicholas.rutherford.track.your.shot.navigation.Navigator
import com.nicholas.rutherford.track.your.shot.navigation.PlayersListAction
import com.nicholas.rutherford.track.your.shot.navigation.SettingsAction
import com.nicholas.rutherford.track.your.shot.navigation.StatsAction
import com.nicholas.rutherford.track.your.shot.navigation.VoiceCommandsAction
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
    val emailState by navigator.emailActions.asLifecycleAwareState(
        lifecycleOwner = lifecycleOwner,
        initialState = null
    )
    val finishState by navigator.finishActions.asLifecycleAwareState(
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

    var alert: Alert? by remember { mutableStateOf(value = null) }
    var progress: Progress? by remember { mutableStateOf(value = null) }

    val screenContents = ScreenContents()

    val mainActivityViewModel = viewModels.mainActivityViewModel
    val createAccountViewModel = viewModels.createAccountViewModel
    val loginViewModel = viewModels.loginViewModel
    val playersListViewModel = viewModels.playersListViewModel
    val createEditPlayerViewModel = viewModels.createEditPlayerViewModel
    val forgotPasswordViewModel = viewModels.forgotPasswordViewModel
    val selectShotViewModel = viewModels.selectShotViewModel

    LaunchedEffect(alertState) {
        alertState?.let { newAlert ->
            alert = newAlert
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
                    println("$ex")
                }
            }
        }
    }
    LaunchedEffect(navigatorState) {
        navigatorState?.let {
            println("get here navigate")
            navHostController.navigate(it.destination, it.navOptions)
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
            navHostController.popBackStack(route = route, inclusive = false)
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

    ModalDrawer(
        drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen,
        drawerShape = MaterialTheme.shapes.medium,
        drawerContent = {
            DrawerContent(
                actions = listOf(
                    PlayersListAction,
                    StatsAction,
                    ComparePlayersStatsAction,
                    VoiceCommandsAction,
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
                        navHostController.navigate(route, navOptions)
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
                        onEditPlayerClicked = { player -> playersListViewModel.onEditPlayerClicked(player = player) },
                        onDeletePlayerClicked = { player -> playersListViewModel.onDeletePlayerClicked(player = player) }
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
                    firstNameArgument = firstNameArgument,
                    lastNameArgument = lastNameArgument,
                    createEditPlayerViewModel = createEditPlayerViewModel
                )(entry)
            }
            composable(route = NavigationDestinations.CREATE_EDIT_PLAYER_SCREEN) { entry ->
                screenContents.createEditPlayerContent(
                    firstNameArgument = null,
                    lastNameArgument = null,
                    createEditPlayerViewModel = createEditPlayerViewModel
                )(entry)
            }
            composable(
                route = NavigationDestinations.SELECT_SHOT_SCREEN
            ) {
                SelectShotScreen(
                    selectShotParams = SelectShotParams(
                        state = selectShotViewModel.selectShotStateFlow.collectAsState().value,
                        onSearchValueChanged = { newSearchQuery -> selectShotViewModel.onSearchValueChanged(newSearchQuery = newSearchQuery) },
                        onBackButtonClicked = { selectShotViewModel.onBackButtonClicked() },
                        onCancelIconClicked = { selectShotViewModel.onCancelIconClicked() },
                        onnDeclaredShotItemClicked = {},
                        onHelpIconClicked = {}
                    )
                )
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
                        onCreateAccountButtonClicked = { createAccountViewModel.onCreateAccountButtonClicked() },
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
