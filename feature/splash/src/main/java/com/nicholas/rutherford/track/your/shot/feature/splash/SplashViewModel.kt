package com.nicholas.rutherford.track.your.shot.feature.splash

import androidx.lifecycle.LifecycleOwner
import com.nicholas.rutherford.track.your.shot.base.vm.BaseViewModel
import com.nicholas.rutherford.track.your.shot.data.room.repository.ActiveUserRepository
import com.nicholas.rutherford.track.your.shot.data.store.reader.DataStorePreferencesReader
import com.nicholas.rutherford.track.your.shot.data.store.writer.DataStorePreferencesWriter
import com.nicholas.rutherford.track.your.shot.firebase.core.read.ReadFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.helper.account.AccountManager
import kotlinx.coroutines.CoroutineScope

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * ViewModel responsible for the splash screen logic and navigation flow based on
 * user state.
 *
 * This ViewModel handles:
 * - Determining if the app is being launched for the first time.
 * - Navigating to the appropriate screen depending on:
 *   - Whether the user is logged in.
 *   - Whether their email is verified(Although this is broken due to Firebase Authentication)
 *   - Whether they have an active user profile set up.
 *
 * @param navigation Defines navigation actions available from the splash screen.
 * @param readFirebaseUserInfo Provides Firebase authentication state.
 * @param activeUserRepository Interface for accessing locally stored active user data.
 * @param accountManager Handles app-level account management logic (e.g., forced logout).
 * @param dataStorePreferencesReader Reads data store preferences.
 * @param dataStorePreferencesWriter Writes to data store preferences.
 */
class SplashViewModel(
    private val navigation: SplashNavigation,
    private val readFirebaseUserInfo: ReadFirebaseUserInfo,
    private val activeUserRepository: ActiveUserRepository,
    private val accountManager: AccountManager,
    private val dataStorePreferencesReader: DataStorePreferencesReader,
    private val dataStorePreferencesWriter: DataStorePreferencesWriter,
    private val scope: CoroutineScope
) : BaseViewModel() {

    // Override to provide the injected scope
    override fun getScope(): CoroutineScope? = scope

    // Override to start collecting flows in onStart
    override fun getFlowCollectionTrigger(): FlowCollectionTrigger = FlowCollectionTrigger.START

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
    internal suspend fun checkIfAppHasBeenLaunchedBefore(appHasBeenLaunched: Boolean) {
        if (!appHasBeenLaunched) {
            accountManager.checkIfWeNeedToLogoutOnLaunch()
            dataStorePreferencesWriter.saveAppHasLaunched(value = true)
        }
    }

    /**
     * Determines the appropriate navigation destination after splash screen.
     *
     * Logic includes:
     * - Checking if app has been launched before.
     * - Checking Firebase login and email verification states(this is skipped over due to a Firebase AAuthentication issue).
     * - Fetching active user from local database.
     * - Using stored preferences as fallback to determine authentication state.
     */
    internal fun navigateToPlayersListLoginOrAuthentication() {
        collectFlows(
            flow1 = readFirebaseUserInfo.isLoggedInFlow(),
            flow2 = dataStorePreferencesReader.readAppHasBeenLaunchedFlow(),
            flow3 = dataStorePreferencesReader.readIsLoggedInFlow(),
            flow4 = dataStorePreferencesReader.readShouldShowTermsAndConditionsFlow()
        ) { loggedInValue, appHasBeenLaunched, userLoggedInLocally, shouldShowTermsAndConditions ->

            checkIfAppHasBeenLaunchedBefore(appHasBeenLaunched = appHasBeenLaunched)

            val activeUser = activeUserRepository.fetchActiveUser()
            val isLoggedIn = loggedInValue || userLoggedInLocally

            if (isLoggedIn) {
                navigatePostAuthDestination(
                    shouldShowTermAndConditions = shouldShowTermsAndConditions,
                    isLoggedIn = true,
                    email = activeUser?.email ?: ""
                )

                // TODO: Uncomment this block once Firebase Authentication issues are resolved
                    /*
                    val isVerified = emailVerifiedValue || readSharedPreferences.hasAccountBeenAuthenticated()
                    if (isVerified && activeUser != null && activeUser.accountHasBeenCreated) {
                        navigatePostAuthDestination(isLoggedIn = true, email = activeUser.email)
                    } else {
                        activeUser?.let { user ->
                            navigation.navigateToAuthentication(
                                username = user.username,
                                email = user.email
                            )
                        }
                    }
                    */
            } else {
                navigatePostAuthDestination(
                    shouldShowTermAndConditions = shouldShowTermsAndConditions,
                    isLoggedIn = false,
                    email = activeUser?.email
                )
            }
        }
    }

    /**
     * Navigates to either the players list, login screen, or terms and conditions
     * depending on login state and app preferences.
     *
     * @param shouldShowTermAndConditions Whether to show the terms and conditions screen.
     * @param isLoggedIn Whether the user is considered logged in.
     * @param email Optional email address of the logged-in user.
     */
    internal fun navigatePostAuthDestination(shouldShowTermAndConditions: Boolean, isLoggedIn: Boolean, email: String?) {
        if (shouldShowTermAndConditions && isLoggedIn) {
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
