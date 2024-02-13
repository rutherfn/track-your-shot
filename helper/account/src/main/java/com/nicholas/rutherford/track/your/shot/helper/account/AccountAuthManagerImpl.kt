package com.nicholas.rutherford.track.your.shot.helper.account

import android.app.Application
import com.nicholas.rutherford.track.your.shot.data.room.repository.ActiveUserRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.DeclaredShotRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.UserRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.ActiveUser
import com.nicholas.rutherford.track.your.shot.data.room.response.DeclaredShot
import com.nicholas.rutherford.track.your.shot.data.room.response.Player
import com.nicholas.rutherford.track.your.shot.data.room.response.PlayerPositions
import com.nicholas.rutherford.track.your.shot.data.room.response.ShotLogged
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.alert.AlertConfirmAndDismissButton
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.your.shot.feature.splash.StringsIds
import com.nicholas.rutherford.track.your.shot.firebase.core.read.ReadFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.firebase.util.existinguser.ExistingUserFirebase
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import com.nicholas.rutherford.track.your.shot.navigation.NavigationActions
import com.nicholas.rutherford.track.your.shot.navigation.Navigator
import com.nicholas.rutherford.track.your.shot.shared.preference.create.CreateSharedPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AccountAuthManagerImpl(
    private val scope: CoroutineScope,
    private val application: Application,
    private val navigator: Navigator,
    private val activeUserRepository: ActiveUserRepository,
    private val declaredShotRepository: DeclaredShotRepository,
    private val playerRepository: PlayerRepository,
    private val userRepository: UserRepository,
    private val readFirebaseUserInfo: ReadFirebaseUserInfo,
    private val existingUserFirebase: ExistingUserFirebase,
    private val createSharedPreferences: CreateSharedPreferences
) : AccountAuthManager {

    private val _loggedInPlayerListStateFlow: MutableStateFlow<List<Player>> = MutableStateFlow(value = emptyList())
    override val loggedInPlayerListStateFlow: StateFlow<List<Player>> = _loggedInPlayerListStateFlow.asStateFlow()

    private val _loggedInDeclaredShotListStateFlow: MutableStateFlow<List<DeclaredShot>> = MutableStateFlow(value = emptyList())
    override val loggedInDeclaredShotListStateFlow: StateFlow<List<DeclaredShot>> = _loggedInDeclaredShotListStateFlow.asStateFlow()
    override fun logout() {
        scope.launch {
            navigator.progress(progressAction = Progress())

            delay(Constants.DELAY_IN_MILLISECONDS_TO_SHOW_PROGRESS_MASK_ON_LOG_OUT)

            navigator.progress(progressAction = null)
            navigator.navigate(navigationAction = NavigationActions.DrawerScreen.logout())

            delay(Constants.DELAY_IN_MILLISECONDS_BEFORE_LOGGING_OUT)

            existingUserFirebase.logout()

            clearOutDatabase()
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

    internal suspend fun clearOutDatabase() {
        activeUserRepository.deleteActiveUser()
        playerRepository.deleteAllPlayers()
        userRepository.deleteAllUsers()
    }

    override fun login(email: String, password: String) {
        navigator.progress(progressAction = Progress())

        scope.launch {
            existingUserFirebase.loginFlow(
                email = email,
                password = password
            ).collectLatest { isSuccessful ->
                if (isSuccessful) {
                    readFirebaseUserInfo.getAccountInfoFlowByEmail(email = email)
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

    internal suspend fun checkForActiveUserAndPlayers() {
        if (activeUserRepository.fetchActiveUser() != null) {
            activeUserRepository.deleteActiveUser()
        }
        if (playerRepository.fetchAllPlayers().isNotEmpty()) {
            playerRepository.deleteAllPlayers()
        }
    }

    internal suspend fun updateActiveUserFromLoggedInUser(email: String, username: String) {
        readFirebaseUserInfo.getAccountInfoKeyFlowByEmail(email).collectLatest { key ->
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

                collectPlayerInfoList(firebaseAccountInfoKey = firebaseAccountInfoKey)
            } ?: disableProgressAndShowUnableToLoginAlert(isLoggedIn = true)
        }
    }

    internal suspend fun collectPlayerInfoList(firebaseAccountInfoKey: String) {
        readFirebaseUserInfo.getPlayerInfoList(accountKey = firebaseAccountInfoKey)
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

                    disableProcessAndNavigateToPlayersList()
                } else {
                    disableProcessAndNavigateToPlayersList()
                }
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
