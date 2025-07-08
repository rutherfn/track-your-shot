package com.nicholas.rutherford.track.your.shot.feature.splash

import androidx.lifecycle.LifecycleOwner
import com.nicholas.rutherford.track.your.shot.base.vm.BaseViewModel
import com.nicholas.rutherford.track.your.shot.data.room.repository.ActiveUserRepository
import com.nicholas.rutherford.track.your.shot.firebase.core.read.ReadFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.helper.account.AccountManager
import com.nicholas.rutherford.track.your.shot.shared.preference.create.CreateSharedPreferences
import com.nicholas.rutherford.track.your.shot.shared.preference.read.ReadSharedPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

/**
 * ViewModel responsible for the splash screen logic and navigation flow based on
 * authentication and user state.
 *
 * This ViewModel handles:
 * - Determining if the app is being launched for the first time.
 * - Navigating to the appropriate screen depending on:
 *   - Whether the user is logged in.
 *   - Whether their email is verified.
 *   - Whether they have an active user profile set up.
 *
 * @param navigation Defines navigation actions available from the splash screen.
 * @param readFirebaseUserInfo Provides Firebase authentication state.
 * @param activeUserRepository Interface for accessing locally stored active user data.
 * @param accountManager Handles app-level account management logic (e.g., forced logout).
 * @param readSharedPreferences Interface for reading app preferences.
 * @param createSharedPreferences Interface for writing app preferences.
 * @param scope Coroutine scope used for background operations.
 */
class SplashViewModel(
    private val navigation: SplashNavigation,
    private val readFirebaseUserInfo: ReadFirebaseUserInfo,
    private val activeUserRepository: ActiveUserRepository,
    private val accountManager: AccountManager,
    private val readSharedPreferences: ReadSharedPreferences,
    private val createSharedPreferences: CreateSharedPreferences,
    private val scope: CoroutineScope
) : BaseViewModel() {

    /**
     * Called when the lifecycle owner's `onStart()` is triggered.
     * Begins navigation decision flow from the splash screen.
     */
    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        navigateToPlayersListLoginOrAuthentication()
    }

    /**
     * Checks whether this is the first app launch.
     * If so, initiates any required logout and updates the flag.
     */
    internal fun checkIfAppHasBeenLaunchedBefore() {
        if (!readSharedPreferences.appHasBeenLaunched()) {
            accountManager.checkIfWeNeedToLogoutOnLaunch()
            createSharedPreferences.createAppHasLaunchedPreference(value = true)
        }
    }

    /**
     * Determines the appropriate navigation destination after splash screen.
     *
     * Logic includes:
     * - Checking if app has been launched before.
     * - Checking Firebase login and email verification states.
     * - Fetching active user from local database.
     * - Using stored preferences as fallback to determine authentication state.
     */
    internal fun navigateToPlayersListLoginOrAuthentication() {
        checkIfAppHasBeenLaunchedBefore()

        scope.launch {
            combine(
                readFirebaseUserInfo.isEmailVerifiedFlow(),
                readFirebaseUserInfo.isLoggedInFlow()
            ) { emailVerifiedValue, loggedInValue ->
                val activeUser = activeUserRepository.fetchActiveUser()
                val isVerified = emailVerifiedValue || readSharedPreferences.hasAccountBeenAuthenticated()
                val isLoggedIn = loggedInValue || readSharedPreferences.isLoggedIn()

                if (isLoggedIn) {
                    if (isVerified && activeUser != null && activeUser.accountHasBeenCreated) {
                        navigateToLoginOrPlayersList(isLoggedIn = true, email = activeUser.email)
                    } else {
                        activeUser?.let { user ->
                            navigation.navigateToAuthentication(
                                username = user.username,
                                email = user.email
                            )
                        }
                    }
                } else {
                    navigateToLoginOrPlayersList(isLoggedIn = false, email = activeUser?.email)
                }
            }.collectLatest { }
        }
    }

    /**
     * Navigates to either the players list, login screen, or terms and conditions
     * depending on login state and app preferences.
     *
     * @param isLoggedIn Whether the user is considered logged in.
     * @param email Optional email address of the logged-in user.
     */
    internal fun navigateToLoginOrPlayersList(isLoggedIn: Boolean, email: String?) {
        if (readSharedPreferences.shouldShowTermsAndConditions()) {
            navigation.navigateToTermsAndConditions()
        } else if (isLoggedIn) {
            email?.let {
                navigation.navigateToPlayersList()
            } ?: navigation.navigateToLogin()
        } else {
            navigation.navigateToLogin()
        }
    }
}

