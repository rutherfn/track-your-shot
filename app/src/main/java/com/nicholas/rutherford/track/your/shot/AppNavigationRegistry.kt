package com.nicholas.rutherford.track.your.shot

import androidx.navigation.NavGraphBuilder
import com.nicholas.rutherford.track.your.shot.AppNavigationGraph.accountInfoScreen
import com.nicholas.rutherford.track.your.shot.AppNavigationGraph.authenticationScreen
import com.nicholas.rutherford.track.your.shot.AppNavigationGraph.createAccountScreen
import com.nicholas.rutherford.track.your.shot.AppNavigationGraph.createEditDeclaredScreen
import com.nicholas.rutherford.track.your.shot.AppNavigationGraph.createOrEditPlayerScreen
import com.nicholas.rutherford.track.your.shot.AppNavigationGraph.createReportScreen
import com.nicholas.rutherford.track.your.shot.AppNavigationGraph.debugToggleScreen
import com.nicholas.rutherford.track.your.shot.AppNavigationGraph.declaredShotsListScreen
import com.nicholas.rutherford.track.your.shot.AppNavigationGraph.enabledPermissionScreen
import com.nicholas.rutherford.track.your.shot.AppNavigationGraph.forgotPasswordScreen
import com.nicholas.rutherford.track.your.shot.AppNavigationGraph.logShotScreen
import com.nicholas.rutherford.track.your.shot.AppNavigationGraph.loginScreen
import com.nicholas.rutherford.track.your.shot.AppNavigationGraph.onBoardingEducationScreen
import com.nicholas.rutherford.track.your.shot.AppNavigationGraph.permissionEducationScreen
import com.nicholas.rutherford.track.your.shot.AppNavigationGraph.playersListScreen
import com.nicholas.rutherford.track.your.shot.AppNavigationGraph.reportListScreen
import com.nicholas.rutherford.track.your.shot.AppNavigationGraph.selectShotScreen
import com.nicholas.rutherford.track.your.shot.AppNavigationGraph.settingsScreen
import com.nicholas.rutherford.track.your.shot.AppNavigationGraph.shotListScreen
import com.nicholas.rutherford.track.your.shot.AppNavigationGraph.splashScreen
import com.nicholas.rutherford.track.your.shot.AppNavigationGraph.termsAndConditionScreen
import com.nicholas.rutherford.track.your.shot.AppNavigationGraph.voiceCommandsScreen

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
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

    // Static screens that don't need parameters directly linked to it
    val staticScreens: List<Screen.StaticScreen> = listOf(
        Screen.StaticScreen { splashScreen() },
        Screen.StaticScreen { loginScreen() },
        Screen.StaticScreen { forgotPasswordScreen() },
        Screen.StaticScreen { authenticationScreen() },
        Screen.StaticScreen { termsAndConditionScreen() },
        Screen.StaticScreen { onBoardingEducationScreen() },
        Screen.StaticScreen { logShotScreen() },
        Screen.StaticScreen { selectShotScreen() },
        Screen.StaticScreen { reportListScreen() },
        Screen.StaticScreen { shotListScreen() },
        Screen.StaticScreen { createReportScreen() },
        Screen.StaticScreen { voiceCommandsScreen() },
        Screen.StaticScreen { settingsScreen() },
        Screen.StaticScreen { accountInfoScreen() },
        Screen.StaticScreen { permissionEducationScreen() },
        Screen.StaticScreen { enabledPermissionScreen() },
        Screen.StaticScreen { debugToggleScreen() },
        Screen.StaticScreen { createEditDeclaredScreen() },
        Screen.StaticScreen { declaredShotsListScreen() }
    )

    // Dynamic screens that require runtime parameters
    val dynamicScreens: List<Screen.DynamicScreen> = listOf(
        Screen.DynamicScreen { isConnected ->
            { createAccountScreen(isConnectedToInternet = isConnected) }
        },
        Screen.DynamicScreen { isConnected ->
            { playersListScreen(isConnectedToInternet = isConnected) }
        },
        Screen.DynamicScreen { isConnected ->
            { createOrEditPlayerScreen(isConnectedToInternet = isConnected) }
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
