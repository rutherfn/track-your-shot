package com.nicholas.rutherford.track.your.shot.helper.account

import android.app.Application
import com.nicholas.rutherford.track.your.shot.data.room.converters.PlayerPositionsConverter
import com.nicholas.rutherford.track.your.shot.data.room.repository.ActiveUserRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.UserRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.ActiveUser
import com.nicholas.rutherford.track.your.shot.data.room.response.Player
import com.nicholas.rutherford.track.your.shot.data.room.response.PlayerPositions
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.alert.AlertConfirmAndDismissButton
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.your.shot.feature.splash.StringsIds
import com.nicholas.rutherford.track.your.shot.firebase.core.read.ReadFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.firebase.util.existinguser.ExistingUserFirebase
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import com.nicholas.rutherford.track.your.shot.navigation.NavigationActions
import com.nicholas.rutherford.track.your.shot.navigation.Navigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AccountAuthManagerImpl(
    private val scope: CoroutineScope,
    private val application: Application,
    private val navigator: Navigator,
    private val activeUserRepository: ActiveUserRepository,
    private val playerRepository: PlayerRepository,
    private val userRepository: UserRepository,
    private val readFirebaseUserInfo: ReadFirebaseUserInfo,
    private val existingUserFirebase: ExistingUserFirebase
) : AccountAuthManager {

    override fun logout() {
        scope.launch {
            navigator.progress(progressAction = Progress())
            existingUserFirebase.logout()
            clearOutDatabase()

            delay(Constants.DELAY_IN_MILLISECONDS_BEFORE_LOGGING_OUT)
            navigator.progress(progressAction = null)
            navigator.navigate(navigationAction = NavigationActions.DrawerScreen.logout())
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

    internal suspend fun updateActiveUserFromLoggedInUser(email: String, username: String) {
        readFirebaseUserInfo.getAccountInfoKeyFlowByEmail(email).collectLatest { key ->
            key?.let { firebaseAccountInfoKey ->
                if (activeUserRepository.fetchActiveUser() == null) {
                    activeUserRepository.createActiveUser(
                        activeUser = ActiveUser(
                            id = Constants.ACTIVE_USER_ID,
                            accountHasBeenCreated = true,
                            username = username,
                            email = email,
                            firebaseAccountInfoKey = firebaseAccountInfoKey
                        )
                    )
                    readFirebaseUserInfo.getPlayerInfoList(accountKey = firebaseAccountInfoKey)
                        .collectLatest { playerInfoRealtimeWithKeyResponseList ->
                            if (playerInfoRealtimeWithKeyResponseList.isNotEmpty()) {
                                if (playerRepository.fetchAllPlayers().isEmpty()) {
                                    val playerList =
                                        playerInfoRealtimeWithKeyResponseList.map {
                                                    Player(
                                                        firstName = it.playerInfo.firstName,
                                                        lastName = it.playerInfo.lastName,
                                                        position = PlayerPositions.fromValue(it.playerInfo.positionValue),
                                                        imageUrl = it.playerInfo.imageUrl
                                                    )
                                        }

                                    playerRepository.createListOfPlayers(playerList = playerList)
                                }
                            }
                            disableProcessAndNavigateToPlayersList()
                        }
                } else {
                    disableProgressAndShowUnableToLoginAlert(isLoggedIn = true)
                }
            } ?: disableProgressAndShowUnableToLoginAlert(isLoggedIn = true)
        }
    }

    internal fun disableProcessAndNavigateToPlayersList() {
        navigator.progress(progressAction = null)
        navigator.navigate(navigationAction =NavigationActions.DrawerScreen.playersList())
    }

    internal fun disableProgressAndShowUnableToLoginAlert(isLoggedIn: Boolean = false) {
        if (isLoggedIn) {
            scope.launch {
                existingUserFirebase.logout()
                clearOutDatabase()
            }
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
