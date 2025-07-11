package com.nicholas.rutherford.track.your.shot

import androidx.navigation.NavGraphBuilder
import com.nicholas.rutherford.track.your.shot.AppNavigationGraph.authenticationScreen
import com.nicholas.rutherford.track.your.shot.AppNavigationGraph.createAccountScreen
import com.nicholas.rutherford.track.your.shot.AppNavigationGraph.createOrEditPlayerScreen
import com.nicholas.rutherford.track.your.shot.AppNavigationGraph.forgotPasswordScreen
import com.nicholas.rutherford.track.your.shot.AppNavigationGraph.loginScreen
import com.nicholas.rutherford.track.your.shot.AppNavigationGraph.onBoardingEducationScreen
import com.nicholas.rutherford.track.your.shot.AppNavigationGraph.playersListScreen
import com.nicholas.rutherford.track.your.shot.AppNavigationGraph.splashScreen
import com.nicholas.rutherford.track.your.shot.AppNavigationGraph.termsAndConditionScreen

/**
 * Central registry for all navigation destinations in the app.
 *
 * Screens are grouped into two categories:
 * - Static screens that do not require parameters at build time.
 * - Dynamic screens that require runtime values (e.g., network connection).
 *
 * Use [registerAll] to add all screens to the NavGraphBuilder.
 */
object AppNavigationRegistry {

    /**
     * Represents a screen that can be registered with the navigation graph.
     */
    sealed class Screen() {

        class StaticScreen(
            val builder: NavGraphBuilder.() -> Unit
        ) : Screen()

        class DynamicScreen(
            val createBuilder: (Boolean) -> NavGraphBuilder.() -> Unit
        ) : Screen() {
            fun build(isConnectedToInternet: Boolean): NavGraphBuilder.() -> Unit {
                return createBuilder(isConnectedToInternet)
            }
        }
    }

    // Static screens that don't need parameters like internet connection
    val staticScreens: List<Screen.StaticScreen> = listOf(
        Screen.StaticScreen { splashScreen() },
        Screen.StaticScreen { loginScreen() },
        Screen.StaticScreen { forgotPasswordScreen() },
        Screen.StaticScreen { authenticationScreen() },
        Screen.StaticScreen { termsAndConditionScreen() },
        Screen.StaticScreen { onBoardingEducationScreen() }
    )

    // Dynamic screens that require runtime parameters
    val dynamicScreens: List<Screen.DynamicScreen> = listOf(
        Screen.DynamicScreen { isConnected ->
            { createAccountScreen(isConnected) }
        },
        Screen.DynamicScreen { isConnected ->
            { playersListScreen(isConnected) }
        },
        Screen.DynamicScreen { isConnected ->
            { createOrEditPlayerScreen(isConnected) }
        }
    )

    /**
     * Registers all screens to the NavGraphBuilder.
     *
     * @param navGraphBuilder The NavGraphBuilder to register the screens to.
     * @param isConnectedToInternet Flag to provide to dynamic screens that require it.
     */
    fun registerAll(navGraphBuilder: NavGraphBuilder, isConnectedToInternet: Boolean) {
        staticScreens.forEach { screen ->
            screen.builder.invoke(navGraphBuilder)
        }

        dynamicScreens.forEach { screen ->
            screen.build(isConnectedToInternet).invoke(navGraphBuilder)
        }
    }
}


