package com.nicholas.rutherford.track.your.shot.helper.account

import com.nicholas.rutherford.track.your.shot.data.room.repository.ActiveUserRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.UserRepository
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.your.shot.firebase.util.existinguser.ExistingUserFirebase
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import com.nicholas.rutherford.track.your.shot.navigation.NavigationActions
import com.nicholas.rutherford.track.your.shot.navigation.Navigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AccountAuthManagerImpl(
    private val scope: CoroutineScope,
    private val navigator: Navigator,
    private val activeUserRepository: ActiveUserRepository,
    private val playerRepository: PlayerRepository,
    private val userRepository: UserRepository,
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

    override fun login() {
        TODO("Not yet implemented")
    }
}
