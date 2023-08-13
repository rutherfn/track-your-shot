package com.nicholas.rutherford.track.my.shot

import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.nicholas.rutherford.track.my.shot.compose.components.AlertDialog
import com.nicholas.rutherford.track.my.shot.compose.components.ProgressDialog
import com.nicholas.rutherford.track.my.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.my.shot.data.shared.alert.AlertConfirmAndDismissButton
import com.nicholas.rutherford.track.my.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.my.shot.feature.create.account.authentication.AuthenticationScreen
import com.nicholas.rutherford.track.my.shot.feature.create.account.createaccount.CreateAccountScreen
import com.nicholas.rutherford.track.my.shot.feature.create.account.createaccount.CreateAccountScreenParams
import com.nicholas.rutherford.track.my.shot.feature.forgot.password.ForgotPasswordScreen
import com.nicholas.rutherford.track.my.shot.feature.forgot.password.ForgotPasswordScreenParams
import com.nicholas.rutherford.track.my.shot.feature.home.HomeScreen
import com.nicholas.rutherford.track.my.shot.feature.login.LoginScreen
import com.nicholas.rutherford.track.my.shot.feature.login.LoginScreenParams
import com.nicholas.rutherford.track.my.shot.feature.splash.SplashScreen
import com.nicholas.rutherford.track.my.shot.navigation.NavigationDestinations
import com.nicholas.rutherford.track.my.shot.navigation.Navigator
import com.nicholas.rutherford.track.my.shot.navigation.arguments.NamedArguments
import com.nicholas.rutherford.track.my.shot.navigation.arguments.NavArguments
import com.nicholas.rutherford.track.my.shot.navigation.asLifecycleAwareState
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

    val alertState by navigator.alertActions.asLifecycleAwareState(
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

    var alert: Alert? by remember { mutableStateOf(value = null) }
    var progress: Progress? by remember { mutableStateOf(value = null) }

    val createAccountViewModel = viewModels.createAccountViewModel
    val loginViewModel = viewModels.loginViewModel
    val forgotPasswordViewModel = viewModels.forgotPasswordViewModel

    LaunchedEffect(alertState) {
        alertState?.let { newAlert ->
            alert = newAlert
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

    NavHost(
        navController = navHostController,
        startDestination = NavigationDestinations.SPLASH_SCREEN
    ) {
        composable(route = NavigationDestinations.SPLASH_SCREEN) {
            SplashScreen(navigateToHomeLoginOrAuthentication = {
                viewModels.splashViewModel.navigateToHomeLoginOrAuthentication()
            })
        }
        composable(route = NavigationDestinations.LOGIN_SCREEN) {
            LoginScreen(
                loginScreenParams = LoginScreenParams(
                    state = loginViewModel.loginStateFlow.collectAsState().value,
                    onEmailValueChanged = { newEmail -> loginViewModel.onEmailValueChanged(newEmail = newEmail) },
                    onPasswordValueChanged = { newPassword -> loginViewModel.onPasswordValueChanged(newPassword = newPassword) },
                    onLoginButtonClicked = {
                        coroutineScope.launch {
                            loginViewModel.onLoginButtonClicked()
                        }
                    },
                    onForgotPasswordClicked = { loginViewModel.onForgotPasswordClicked() },
                    onCreateAccountClicked = { loginViewModel.onCreateAccountClicked() },
                    coroutineScope = coroutineScope
                )
            )
        }
        composable(route = NavigationDestinations.HOME_SCREEN) {
            HomeScreen(viewModel = viewModels.homeViewModel)
        }
        composable(route = NavigationDestinations.FORGOT_PASSWORD_SCREEN) {
            ForgotPasswordScreen(
                forgotPasswordScreenParams = ForgotPasswordScreenParams(
                    state = forgotPasswordViewModel.forgotPasswordStateFlow.collectAsState().value,
                    onEmailValueChanged = { newEmail -> forgotPasswordViewModel.onEmailValueChanged(newEmail = newEmail) },
                    onSendPasswordResetButtonClicked = { newEmail ->
                        coroutineScope.launch { forgotPasswordViewModel.onSendPasswordResetButtonClicked(newEmail = newEmail) }
                    },
                    onBackButtonClicked = { forgotPasswordViewModel.onBackButtonClicked() }
                )
            )
        }
        composable(route = NavigationDestinations.CREATE_ACCOUNT_SCREEN) {
            CreateAccountScreen(
                createAccountScreenParams = CreateAccountScreenParams(
                    state = createAccountViewModel.createAccountStateFlow.collectAsState().value,
                    onUsernameValueChanged = { newUsername -> createAccountViewModel.onUsernameValueChanged(newUsername = newUsername) },
                    onEmailValueChanged = { newEmail -> createAccountViewModel.onEmailValueChanged(newEmail = newEmail) },
                    onPasswordValueChanged = { newPassword -> createAccountViewModel.onPasswordValueChanged(newPassword = newPassword) },
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

    alert?.let { newAlert ->
        AlertDialog(
            onDismissClicked = {
                navigator.alert(alertAction = null)
                alert = null
                newAlert.onDismissClicked.invoke()
            },
            title = newAlert.title,
            confirmButton = newAlert.confirmButton?.let { confirmButton ->
                AlertConfirmAndDismissButton(
                    onButtonClicked = {
                        navigator.alert(alertAction = null)
                        alert = null
                        confirmButton.onButtonClicked.invoke()
                    },
                    buttonText = confirmButton.buttonText
                )
            } ?: run { null },
            dismissButton = newAlert.dismissButton?.let { dismissButton ->
                AlertConfirmAndDismissButton(
                    onButtonClicked = {
                        navigator.alert(alertAction = null)
                        alert = null
                        dismissButton.onButtonClicked.invoke()
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
                newProgress.onDismissClicked.invoke()
            },
            title = newProgress.title
        )
    }
}
