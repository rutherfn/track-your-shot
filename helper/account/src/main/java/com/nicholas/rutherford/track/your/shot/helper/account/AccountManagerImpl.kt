package com.nicholas.rutherford.track.your.shot.helper.account

import android.app.Application
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.data.room.repository.ActiveUserRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.DeclaredShotRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.IndividualPlayerReportRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.PendingPlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.UserRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.ActiveUser
import com.nicholas.rutherford.track.your.shot.data.room.response.DeclaredShot
import com.nicholas.rutherford.track.your.shot.data.room.response.IndividualPlayerReport
import com.nicholas.rutherford.track.your.shot.data.room.response.Player
import com.nicholas.rutherford.track.your.shot.data.room.response.PlayerPositions
import com.nicholas.rutherford.track.your.shot.data.room.response.ShotLogged
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.alert.AlertConfirmAndDismissButton
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.your.shot.firebase.core.read.ReadFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.firebase.util.existinguser.ExistingUserFirebase
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import com.nicholas.rutherford.track.your.shot.navigation.NavigationActions
import com.nicholas.rutherford.track.your.shot.navigation.Navigator
import com.nicholas.rutherford.track.your.shot.shared.preference.create.CreateSharedPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AccountManagerImpl(
    private val scope: CoroutineScope,
    private val application: Application,
    private val navigator: Navigator,
    private val activeUserRepository: ActiveUserRepository,
    private val declaredShotRepository: DeclaredShotRepository,
    private val playerRepository: PlayerRepository,
    private val individualPlayerReportRepository: IndividualPlayerReportRepository,
    private val pendingPlayerRepository: PendingPlayerRepository,
    private val userRepository: UserRepository,
    private val readFirebaseUserInfo: ReadFirebaseUserInfo,
    private val existingUserFirebase: ExistingUserFirebase,
    private val createSharedPreferences: CreateSharedPreferences
) : AccountManager {

    private val _loggedInPlayerListStateFlow: MutableStateFlow<List<Player>> = MutableStateFlow(value = emptyList())
    override val loggedInPlayerListStateFlow: StateFlow<List<Player>> = _loggedInPlayerListStateFlow.asStateFlow()

    private val _loggedInDeclaredShotListStateFlow: MutableStateFlow<List<DeclaredShot>> = MutableStateFlow(value = emptyList())
    override val loggedInDeclaredShotListStateFlow: StateFlow<List<DeclaredShot>> = _loggedInDeclaredShotListStateFlow.asStateFlow()

    private val hasLoggedInSuccessfulMutableSharedFlow = MutableSharedFlow<Boolean>(extraBufferCapacity = Channel.UNLIMITED)
    override val hasLoggedInSuccessfulFlow: Flow<Boolean> = hasLoggedInSuccessfulMutableSharedFlow

    private val hasNoPlayersMutableSharedFlow = MutableSharedFlow<Boolean>(extraBufferCapacity = Channel.UNLIMITED)
    override val hasNoPlayersFlow: Flow<Boolean> = hasNoPlayersMutableSharedFlow

    override fun logout() {
        scope.launch {
            navigator.progress(progressAction = Progress())

            existingUserFirebase.logout()
            createSharedPreferences.createShouldShowTermsAndConditionsPreference(value = false)
            createSharedPreferences.createHasAuthenticatedAccount(value = false)
            clearOutDatabase()

            delay(Constants.DELAY_IN_MILLISECONDS_TO_SHOW_PROGRESS_MASK_ON_LOG_OUT)

            navigator.progress(progressAction = null)
            navigator.navigate(navigationAction = NavigationActions.DrawerScreen.logout())
        }
    }

    override fun login(email: String, password: String) {
        navigator.progress(progressAction = Progress())

        scope.launch {
            existingUserFirebase.loginFlow(
                email = email,
                password = password
            ).collectLatest { isSuccessful ->
                if (isSuccessful) {
                    createSharedPreferences.createShouldShowTermsAndConditionsPreference(value = false)
                    readFirebaseUserInfo.getAccountInfoFlow()
                        .collectLatest { accountInfoRealtimeResponse ->
                            accountInfoRealtimeResponse?.let { accountInfo ->
                                updateActiveUserFromLoggedInUser(email = accountInfo.email, username = accountInfo.userName)
                            } ?: disableProgressAndShowUnableToLoginAlert(isLoggedIn = true)
                        }
                } else {
                    disableProgressAndShowUnableToLoginAlert()
                }
            }
        }
    }

    override fun checkIfWeNeedToLogoutOnLaunch() {
        if (existingUserFirebase.isLoggedIn()) {
            scope.launch {
                existingUserFirebase.logout()
                clearOutDatabase()
            }
        }
    }

    override fun updateLoggedInDeclaredShotFlow(declaredShots: List<DeclaredShot>) {
        _loggedInDeclaredShotListStateFlow.value = declaredShots
    }

    internal suspend fun clearOutDatabase() {
        activeUserRepository.deleteActiveUser()
        playerRepository.deleteAllPlayers()
        pendingPlayerRepository.deleteAllPendingPlayers()
        userRepository.deleteAllUsers()
        individualPlayerReportRepository.deleteAllReports()
    }

    override fun deleteAllPendingShotsAndPlayers() {
        deleteAllPendingPlayers()
        deleteAllPendingShotsFromPlayers()
    }

    internal fun deleteAllPendingPlayers() = scope.launch { pendingPlayerRepository.deleteAllPendingPlayers() }

    internal fun deleteAllPendingShotsFromPlayers() {
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

    internal suspend fun checkForActiveUserAndPlayers() {
        if (activeUserRepository.fetchActiveUser() != null) {
            activeUserRepository.deleteActiveUser()
        }
        if (playerRepository.fetchAllPlayers().isNotEmpty()) {
            playerRepository.deleteAllPlayers()
        }
    }

    internal suspend fun updateActiveUserFromLoggedInUser(email: String, username: String) {
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
                createSharedPreferences.createShouldUpdateLoggedInDeclaredShotListPreference(value = true)
                declaredShotRepository.createDeclaredShots()

                _loggedInDeclaredShotListStateFlow.value = declaredShotRepository.fetchAllDeclaredShots()

                collectPlayerInfoList()
            } ?: disableProgressAndShowUnableToLoginAlert(isLoggedIn = true)
        }
    }

    internal suspend fun collectPlayerInfoList() {
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

                    createSharedPreferences.createShouldUpdateLoggedInPlayerListPreference(value = true)
                    _loggedInPlayerListStateFlow.value = playerList
                    playerRepository.createListOfPlayers(playerList = playerList)
                    hasNoPlayersMutableSharedFlow.tryEmit(value = false)
                    collectReportList()
                } else {
                    hasNoPlayersMutableSharedFlow.tryEmit(value = true)
                    collectReportList()
                }
            }
    }

    // todo -> Nick come back and test this
    internal suspend fun collectReportList() {
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
                    hasLoggedInSuccessfulMutableSharedFlow.tryEmit(value = true)
                } else {
                    hasLoggedInSuccessfulMutableSharedFlow.tryEmit(value = true)
                }

                createSharedPreferences.createHasAuthenticatedAccount(value = false)
                disableProcessAndNavigateToPlayersList()
            }
    }

    private fun disableProcessAndNavigateToPlayersList() {
        navigator.progress(progressAction = null)
        navigator.navigate(navigationAction = NavigationActions.DrawerScreen.playersList())
    }

    suspend fun disableProgressAndShowUnableToLoginAlert(isLoggedIn: Boolean = false) {
        if (isLoggedIn) {
            existingUserFirebase.logout()
            clearOutDatabase()
        }
        navigator.progress(progressAction = null)
        navigator.alert(alertAction = null)
        navigator.alert(alertAction = unableToLoginToAccountAlert())
        hasLoggedInSuccessfulMutableSharedFlow.tryEmit(value = false)
    }

    internal fun unableToLoginToAccountAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.unableToLoginToAccount),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.gotIt)
            ),
            description = application.getString(StringsIds.havingTroubleLoggingIntoYourAccountPleaseTryAgainAndEnsureCredentialsExistAndAreValid)
        )
    }
}
