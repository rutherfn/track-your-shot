package com.nicholas.rutherford.track.my.shot.navigation

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

/**
 * Navigation Component that initiates content that's being passed in
 * todo: Need to look into adding params here:https://proandroiddev.com/how-to-make-jetpack-compose-navigation-easier-and-testable-b4b19fd5f2e4
 */
@Composable
fun NavigationComponent(
    navHostController: NavHostController,
    navigator: Navigator,
    splashContent: @Composable (navController: Navigator) -> Unit,
    loginContent: @Composable (navController: Navigator) -> Unit,
    homeContent: @Composable (navController: Navigator) -> Unit,
    forgotPasswordContent: @Composable (navController: Navigator) -> Unit,
    createAccountContent: @Composable (navController: Navigator) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val alertState by navigator.alertActions.asLifecycleAwareState(
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

    LaunchedEffect(alertState) {
        alertState?.let { newAlert ->
            alert = newAlert
        }
    }
    LaunchedEffect(navigatorState) {
        navigatorState?.let {
            it.parcelableArguments.forEach { arg ->
                navHostController.currentBackStackEntry?.arguments?.putParcelable(arg.key, arg.value)
            }
            navHostController.navigate(it.destination, it.navOptions)
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
            splashContent.invoke(navigator)
        }
        composable(route = NavigationDestinations.LOGIN_SCREEN) {
            loginContent.invoke(navigator)
        }
        composable(route = NavigationDestinations.HOME_SCREEN) {
            homeContent.invoke(navigator)
        }
        composable(route = NavigationDestinations.FORGOT_PASSWORD_SCREEN) {
            forgotPasswordContent.invoke(navigator)
        }
        composable(route = NavigationDestinations.CREATE_ACCOUNT_SCREEN) {
            createAccountContent.invoke(navigator)
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
