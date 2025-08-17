package com.nicholas.rutherford.track.your.shot.helper.account

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Interface defining account-related operations.
 * Implementations are responsible for handling user creation, login, logout,
 * and checking account state on app launch.
 */
interface AccountManager {

    /**
     * Creates a new active user in the local database.
     *
     * @param username The username of the new active user.
     * @param email The email address of the new active user.
     */
    suspend fun createActiveUser(username: String, email: String)

    /**
     * Logs out the current user, clears local data, and updates any relevant
     * shared preferences or application state.
     */
    fun logout()

    /**
     * Checks if the user should be automatically logged out when the app launches.
     * Typically used to ensure local state consistency if a previous session exists.
     */
    fun checkIfWeNeedToLogoutOnLaunch()

    /**
     * Logs in a user using the provided credentials.
     * Should update local state, synchronize with remote sources, and handle
     * any progress indicators or error alerts.
     *
     * @param email The user's email address.
     * @param password The user's password.
     */
    fun login(email: String, password: String)
}
