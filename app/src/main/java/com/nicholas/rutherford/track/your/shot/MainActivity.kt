package com.nicholas.rutherford.track.your.shot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.alert.AlertConfirmAndDismissButton
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import com.nicholas.rutherford.track.your.shot.helper.reviews.ReviewManager
import com.nicholas.rutherford.track.your.shot.helper.reviews.ReviewPromptManager
import kotlinx.coroutines.launch
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
     * Review prompt manager for periodic review prompts.
     */
    private val reviewPromptManager: ReviewPromptManager = get()

    /**
     * Review manager for launching review flow.
     */
    private val reviewManager: ReviewManager = get()

    /**
     * Navigator for showing alerts.
     */
    private val navigator = get<com.nicholas.rutherford.track.your.shot.navigation.Navigator>()

    override fun onStart() {
        super.onStart()
        checkAndShowReviewPrompt()
    }

    /**
     * Checks if a review prompt should be shown and displays it if conditions are met.
     */
    private fun checkAndShowReviewPrompt() {
        lifecycleScope.launch {
            if (reviewPromptManager.shouldShowReviewPrompt()) {
                showReviewPromptDialog()
            }
        }
    }

    /**
     * Shows the review prompt dialog asking the user if they want to rate the app.
     */
    private fun showReviewPromptDialog() {
        lifecycleScope.launch {
            reviewPromptManager.recordReviewPromptShown()
            
            val alert = Alert(
                title = getString(StringsIds.rateAppPromptTitle),
                description = getString(StringsIds.rateAppPromptDescription),
                confirmButton = AlertConfirmAndDismissButton(
                    buttonText = getString(StringsIds.rateNow),
                    onButtonClicked = {
                        lifecycleScope.launch {
                            reviewManager.requestReview(this@MainActivity)
                        }
                    }
                ),
                dismissButton = AlertConfirmAndDismissButton(
                    buttonText = getString(StringsIds.maybeLater),
                    onButtonClicked = {
                        lifecycleScope.launch {
                            reviewPromptManager.recordUserDeclinedReview()
                        }
                    }
                )
            )
            navigator.alert(alert)
        }
    }

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
                    createEditVoiceCommandViewModel = getViewModel()
                )
            )
        }
    }
}
