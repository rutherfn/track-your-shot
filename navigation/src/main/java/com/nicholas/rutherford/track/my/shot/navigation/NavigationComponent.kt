package com.nicholas.rutherford.track.my.shot.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun NavigationComponent(
    navHostController: NavHostController,
    navigator: Navigator
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
    }
}
