package com.nicholas.rutherford.track.my.shot.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun NavigationComponent(
    navHostController: NavHostController,
    navigator: Navigator,
    splashContent: @Composable () -> Unit,
    homeContent: @Composable () -> Unit
) {
    LaunchedEffect("navigation") {
        navigator.sharedFlow.onEach {
            navHostController.navigate(it.label)
        }.launchIn(this)
    }

    NavHost(
        navController = navHostController,
        startDestination = Navigator.NavTarget.SPLASH.label
    ) {
        composable(route = Navigator.NavTarget.SPLASH.label) {
            splashContent.invoke()
        }
        composable(route = Navigator.NavTarget.HOME.label) {
            homeContent.invoke()
        }
    }
}
