package com.nicholas.rutherford.track.my.shot.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

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
    forgotPasswordContent: @Composable (navController: Navigator) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val navigatorState by navigator.navActions.asLifecycleAwareState(
        lifecycleOwner = lifecycleOwner,
        initialState = null
    )
    val popRouteState by navigator.popRouteActions.asLifecycleAwareState(
        lifecycleOwner = lifecycleOwner,
        initialState = null
    )
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
            navigator.updatePopRouteActionToNull()
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
    }
}
