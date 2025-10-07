package com.nicholas.rutherford.track.your.shot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.nicholas.rutherford.voice.flow.core.VoiceCommand
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * The main entry point activity for the app
 *
 * Responsibilities:
 * - Sets up the Compose content view.
 * - Initializes edge-to-edge window display.
 * - Creates and injects all required ViewModels via Koin.
 * - Instantiates the [NavigationComponent] to handle all app view/navigation, dialogs, alerts, and modals.
 */
open class MainActivity : ComponentActivity() {

    /**
     * Main activity's [MainActivityViewModel] injected by Koin.
     */
    private val viewModel by viewModel<MainActivityViewModel>()

    /**
     * Called when the activity is first created.
     *
     * Sets up the Compose content, enables edge-to-edge display, and initializes
     * the [NavigationComponent] with:
     * - [androidx.navigation.NavHostController] for screen navigation
     * - [com.nicholas.rutherford.track.your.shot.navigation.Navigator] instance for navigation, dialogs, and system actions
     * - [ViewModels] container for all app ViewModels
     *
     * @param savedInstanceState The saved instance state bundle.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        VoiceCommand

        // Allows the app to draw behind system bars for edge-to-edge experience
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            // Enables edge-to-edge content rendering in Compose
            enableEdgeToEdge()

            // Initializes the NavigationComponent with required dependencies
            NavigationComponent(
                activity = this,
                navHostController = rememberNavController(),
                navigator = get(),
                viewModels = ViewModels(
                    mainActivityViewModel = viewModel,
                    splashViewModel = getViewModel(),
                    loginViewModel = getViewModel(),
                    playersListViewModel = getViewModel(),
                    createEditPlayerViewModel = getViewModel(),
                    forgotPasswordViewModel = getViewModel(),
                    createAccountViewModel = getViewModel(),
                    authenticationViewModel = getViewModel(),
                    selectShotViewModel = getViewModel(),
                    logShotViewModel = getViewModel(),
                    settingsViewModel = getViewModel(),
                    permissionEducationViewModel = getViewModel(),
                    termsConditionsViewModel = getViewModel(),
                    onboardingEducationViewModel = getViewModel(),
                    enabledPermissionsViewModel = getViewModel(),
                    accountInfoViewModel = getViewModel(),
                    reportListViewModel = getViewModel(),
                    createReportViewModel = getViewModel(),
                    shotsListViewModel = getViewModel(),
                    declaredShotsListViewModel = getViewModel(),
                    createEditDeclaredShotsViewModel = getViewModel(),
                    createVoiceCommandViewModel = getViewModel()
                )
            )
        }
    }
}
