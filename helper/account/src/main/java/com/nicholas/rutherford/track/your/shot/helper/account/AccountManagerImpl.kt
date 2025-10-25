package com.nicholas.rutherford.track.your.shot.helper.account

import android.app.Application
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.data.room.repository.ActiveUserRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.DeclaredShotRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.IndividualPlayerReportRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.PendingPlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.SavedVoiceCommandRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.ShotIgnoringRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.UserRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.ActiveUser
import com.nicholas.rutherford.track.your.shot.data.room.response.DeclaredShot
import com.nicholas.rutherford.track.your.shot.data.room.response.IndividualPlayerReport
import com.nicholas.rutherford.track.your.shot.data.room.response.Player
import com.nicholas.rutherford.track.your.shot.data.room.response.PlayerPositions
import com.nicholas.rutherford.track.your.shot.data.room.response.SavedVoiceCommand
import com.nicholas.rutherford.track.your.shot.data.room.response.ShotLogged
import com.nicholas.rutherford.track.your.shot.data.room.response.VoiceCommandTypes
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.alert.AlertConfirmAndDismissButton
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.your.shot.data.store.writer.DataStorePreferencesWriter
import com.nicholas.rutherford.track.your.shot.firebase.core.read.ReadFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.firebase.util.existinguser.ExistingUserFirebase
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import com.nicholas.rutherford.track.your.shot.navigation.NavigationActions
import com.nicholas.rutherford.track.your.shot.navigation.Navigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Implementation of [AccountManager] that handles account creation, login, logout,
 * and synchronization with Firebase and the local database.
 *
 * @param scope Coroutine scope used for launching asynchronous tasks.
 * @param application Android application context.
 * @param navigator Navigation controller to manage progress, alerts, and screen navigation.
 * @param activeUserRepository Repository for active user data.
 * @param declaredShotRepository Repository for declared shot data.
 * @param playerRepository Repository for player data.
 * @param individualPlayerReportRepository Repository for individual player reports.
 * @param pendingPlayerRepository Repository for pending player data.
 * @param shotIgnoringRepository Repository for ignored shots.
 * @param userRepository Repository for user data.
 * @param readFirebaseUserInfo Firebase helper to read user and player info.
 * @param existingUserFirebase Firebase helper for login/logout.
 * @param dataStorePreferencesWriter Writer for DataStore preferences.
 *
 * TODO: Come back and write unit tests for this implementation.
 */
class AccountManagerImpl(
    private val scope: CoroutineScope,
    private val application: Application,
    private val navigator: Navigator,
    private val activeUserRepository: ActiveUserRepository,
    private val declaredShotRepository: DeclaredShotRepository,
    private val playerRepository: PlayerRepository,
    private val individualPlayerReportRepository: IndividualPlayerReportRepository,
    private val pendingPlayerRepository: PendingPlayerRepository,
    private val shotIgnoringRepository: ShotIgnoringRepository,
    private val userRepository: UserRepository,
    private val savedVoiceCommandRepository: SavedVoiceCommandRepository,
    private val readFirebaseUserInfo: ReadFirebaseUserInfo,
    private val existingUserFirebase: ExistingUserFirebase,
    private val dataStorePreferencesWriter: DataStorePreferencesWriter
) : AccountManager {

    /** Tracks declared shot IDs locally */
    private var declaredShotIds: List<Int> = emptyList()

    /**
     * Creates a new active user in the local database.
     *
     * @param username The username of the new active user.
     * @param email The email of the new active user.
     */
    override suspend fun createActiveUser(username: String, email: String) {
        activeUserRepository.createActiveUser(
            activeUser = ActiveUser(
                id = Constants.ACTIVE_USER_ID,
                accountHasBeenCreated = true,
                username = username,
                email = email,
                firebaseAccountInfoKey = ""
            )
        )
    }

    /**
     * Logs out the current user, clears the local database, resets shared preferences,
     * and navigates back to the login screen.
     */
    override fun logout() {
        scope.launch {
            navigator.progress(progressAction = Progress())

            existingUserFirebase.logout()

            dataStorePreferencesWriter.saveShouldShowTermsAndConditions(value = false)
            dataStorePreferencesWriter.saveIsLoggedIn(value = false)
            clearOutDatabase()

            declaredShotIds = emptyList()

            delay(Constants.DELAY_IN_MILLISECONDS_TO_SHOW_PROGRESS_MASK_ON_LOG_OUT)

            navigator.progress(progressAction = null)
            navigator.navigate(navigationAction = NavigationActions.DrawerScreen.logout())
        }
    }

    /**
     * Logs in a user with [email] and [password], updates the local database
     * with Firebase account and player data, and navigates to the player list.
     */
    override fun login(email: String, password: String) {
        navigator.progress(progressAction = Progress())

        scope.launch {
            existingUserFirebase.loginFlow(
                email = email,
                password = password
            ).collectLatest { isSuccessful ->
                if (isSuccessful) {
                    dataStorePreferencesWriter.saveShouldShowTermsAndConditions(value = false)
                    readFirebaseUserInfo.getAccountInfoFlow()
                        .collectLatest { accountInfoRealtimeResponse ->
                            accountInfoRealtimeResponse?.let { accountInfo ->
                                updateActiveUserFromLoggedInUser(
                                    email = accountInfo.email,
                                    username = accountInfo.userName
                                )
                            } ?: disableProgressAndShowUnableToLoginAlert(isLoggedIn = true)
                        }
                } else {
                    disableProgressAndShowUnableToLoginAlert()
                }
            }
        }
    }

    /** Checks if the app should log out the user on launch */
    override fun checkIfWeNeedToLogoutOnLaunch() {
        if (existingUserFirebase.isLoggedIn()) {
            scope.launch {
                existingUserFirebase.logout()
                clearOutDatabase()
            }
        }
    }

    /** Clears all relevant tables in the local database */
    private suspend fun clearOutDatabase() {
        activeUserRepository.deleteActiveUser()
        playerRepository.deleteAllPlayers()
        pendingPlayerRepository.deleteAllPendingPlayers()
        userRepository.deleteAllUsers()
        individualPlayerReportRepository.deleteAllReports()
        declaredShotRepository.deleteAllDeclaredShots()
        shotIgnoringRepository.deleteAllShotsIgnoring()
        savedVoiceCommandRepository.deleteAllCommands()
        deleteAllPendingShotsFromPlayers()
    }

    /** Removes pending shots from all players in the database */
    private fun deleteAllPendingShotsFromPlayers() {
        scope.launch {
            playerRepository.fetchAllPlayers().forEach { player ->
                val pendingShots = player.shotsLoggedList.filter { shot -> shot.isPending }

                if (pendingShots.isNotEmpty()) {
                    val newPlayer = Player(
                        firstName = player.firstName,
                        lastName = player.lastName,
                        position = player.position,
                        firebaseKey = player.firebaseKey,
                        imageUrl = player.imageUrl,
                        shotsLoggedList = player.shotsLoggedList.filter { shot -> !shot.isPending }
                    )
                    playerRepository.updatePlayer(currentPlayer = player, newPlayer = newPlayer)
                }
            }
        }
    }

    /** Checks for and deletes active user and players if they exist */
    private suspend fun checkForActiveUserAndPlayers() {
        if (activeUserRepository.fetchActiveUser() != null) {
            activeUserRepository.deleteActiveUser()
        }
        if (playerRepository.fetchAllPlayers().isNotEmpty()) {
            playerRepository.deleteAllPlayers()
        }
    }

    /**
     * Updates the active user in the local database from Firebase login info.
     *
     * @param email The logged-in user's email.
     * @param username The logged-in user's username.
     */
    private suspend fun updateActiveUserFromLoggedInUser(email: String, username: String) {
        readFirebaseUserInfo.getAccountInfoKeyFlow().collectLatest { key ->
            key?.let { firebaseAccountInfoKey ->
                checkForActiveUserAndPlayers()

                activeUserRepository.createActiveUser(
                    activeUser = ActiveUser(
                        id = Constants.ACTIVE_USER_ID,
                        accountHasBeenCreated = true,
                        username = username,
                        email = email,
                        firebaseAccountInfoKey = firebaseAccountInfoKey
                    )
                )

                collectPlayerInfoList()
            } ?: disableProgressAndShowUnableToLoginAlert(isLoggedIn = true)
        }
    }

    /** Collects player info list from Firebase and updates the local database */
    private suspend fun collectPlayerInfoList() {
        readFirebaseUserInfo.getPlayerInfoList()
            .collectLatest { playerInfoRealtimeWithKeyResponseList ->
                if (playerInfoRealtimeWithKeyResponseList.isNotEmpty()) {
                    val playerList =
                        playerInfoRealtimeWithKeyResponseList.map { player ->
                            Player(
                                firstName = player.playerInfo.firstName,
                                lastName = player.playerInfo.lastName,
                                position = PlayerPositions.fromValue(player.playerInfo.positionValue),
                                firebaseKey = player.playerFirebaseKey,
                                imageUrl = player.playerInfo.imageUrl,
                                shotsLoggedList = player.playerInfo.shotsLogged.map { shotLoggedRealtimeResponse ->
                                    ShotLogged(
                                        id = shotLoggedRealtimeResponse.id,
                                        shotName = shotLoggedRealtimeResponse.shotName,
                                        shotType = shotLoggedRealtimeResponse.shotType,
                                        shotsAttempted = shotLoggedRealtimeResponse.shotsAttempted,
                                        shotsMade = shotLoggedRealtimeResponse.shotsMade,
                                        shotsMissed = shotLoggedRealtimeResponse.shotsMissed,
                                        shotsMadePercentValue = shotLoggedRealtimeResponse.shotsMadePercentValue,
                                        shotsMissedPercentValue = shotLoggedRealtimeResponse.shotsMissedPercentValue,
                                        shotsAttemptedMillisecondsValue = shotLoggedRealtimeResponse.shotsAttemptedMillisecondsValue,
                                        shotsLoggedMillisecondsValue = shotLoggedRealtimeResponse.shotsLoggedMillisecondsValue,
                                        isPending = shotLoggedRealtimeResponse.isPending
                                    )
                                }
                            )
                        }

                    playerRepository.createListOfPlayers(playerList = playerList)
                    collectReportList()
                } else {
                    collectReportList()
                }
            }
    }

    /** Collects individual player reports from Firebase and updates local database */
    private suspend fun collectReportList() {
        readFirebaseUserInfo.getReportList()
            .collectLatest { individualPlayerReportWithKeyRealtimeResponse ->
                if (individualPlayerReportWithKeyRealtimeResponse.isNotEmpty()) {
                    val individualPlayerReportList =
                        individualPlayerReportWithKeyRealtimeResponse.mapIndexed { index, report ->
                            IndividualPlayerReport(
                                id = index + 1,
                                loggedDateValue = report.playerReport.loggedDateValue,
                                playerName = report.playerReport.playerName,
                                firebaseKey = report.reportFirebaseKey,
                                pdfUrl = report.playerReport.pdfUrl
                            )
                        }
                    individualPlayerReportRepository.createReports(individualPlayerReports = individualPlayerReportList)
                    collectDeletedShotIds()
                } else {
                    collectDeletedShotIds()
                }
            }
    }

    private suspend fun collectSavedVoiceCommands() {
        readFirebaseUserInfo.getSavedVoiceCommandList()
            .collectLatest { savedVoiceCommandList ->
                if (savedVoiceCommandList.isNotEmpty()) {
                    val commands =
                        savedVoiceCommandList.mapIndexed { index, savedVoiceCommand ->
                            SavedVoiceCommand(
                                id = index + 1,
                                name = savedVoiceCommand.savedVoiceCommandInfo.name,
                                firebaseKey = savedVoiceCommand.savedVoiceCommandKey,
                                type = VoiceCommandTypes.fromValue(value = savedVoiceCommand.savedVoiceCommandInfo.typeValue)
                            )
                        }

                    val currentSavedCommands = savedVoiceCommandRepository.getAllVoiceCommands()

                    println("full list of commands $commands")
                    println("commands saved in the db $currentSavedCommands")

                    savedVoiceCommandRepository.createAllSavedVoiceCommands(commands = commands)
                    collectDeclaredShots()
                } else {
                    collectDeclaredShots()
                }
            }
    }

    /** Collects deleted shot IDs from Firebase and updates local database */
    private suspend fun collectDeletedShotIds() {
        readFirebaseUserInfo.getDeletedShotIdsFlow()
            .collectLatest { shotIds ->
                if (shotIds.isNotEmpty()) {
                    declaredShotIds = shotIds
                    shotIds.forEach { shotId ->
                        shotIgnoringRepository.createShotIgnoring(shotId = shotId)
                    }
                    collectSavedVoiceCommands()
                } else {
                    collectSavedVoiceCommands()
                }
            }
    }

    /** Collects declared shots from Firebase and updates local database */
    private suspend fun collectDeclaredShots() {
        readFirebaseUserInfo.getCreatedDeclaredShotsFlow()
            .collectLatest { declaredShots ->
                //todo comeback and fix this logic trello link: https://trello.com/c/io2ILw2m/271-bug-look-into-ability-to-edit-updating-existing-shots-with-logging-into-a-account
//                declaredShots.forEach { shot ->
//                    if (!declaredShotIds.contains(shot.declaredShotRealtimeResponse.id)) {
//                        declaredShotRepository.createNewDeclaredShot(
//                            declaredShot = DeclaredShot(
//                                id = shot.declaredShotRealtimeResponse.id,
//                                shotCategory = shot.declaredShotRealtimeResponse.shotCategory,
//                                title = shot.declaredShotRealtimeResponse.title,
//                                description = shot.declaredShotRealtimeResponse.description,
//                                firebaseKey = shot.declaredShotFirebaseKey
//                            )
//                        )
//                    }
//                }
                declaredShotRepository.createDeclaredShots(shotIdsToFilterOut = declaredShotIds)

                dataStorePreferencesWriter.saveIsLoggedIn(value = true)

                declaredShotIds = emptyList()
                disableProcessAndNavigateToPlayersList()
            }
    }

    /** Disables progress indicator and navigates to players list */
    private fun disableProcessAndNavigateToPlayersList() {
        navigator.progress(progressAction = null)
        navigator.navigate(navigationAction = NavigationActions.DrawerScreen.playersList())
    }

    /**
     * Disables progress and shows an alert indicating that login failed.
     *
     * @param isLoggedIn Whether the user was partially logged in.
     */
    private suspend fun disableProgressAndShowUnableToLoginAlert(isLoggedIn: Boolean = false) {
        if (isLoggedIn) {
            existingUserFirebase.logout()
            clearOutDatabase()
        }
        navigator.progress(progressAction = null)
        navigator.alert(alertAction = null)
        navigator.alert(alertAction = unableToLoginToAccountAlert())
    }

    /** Creates an alert for being unable to login to account */
    private fun unableToLoginToAccountAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.unableToLoginToAccount),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.gotIt)
            ),
            description = application.getString(StringsIds.havingTroubleLoggingIntoYourAccountPleaseTryAgainAndEnsureCredentialsExistAndAreValid)
        )
    }
}
