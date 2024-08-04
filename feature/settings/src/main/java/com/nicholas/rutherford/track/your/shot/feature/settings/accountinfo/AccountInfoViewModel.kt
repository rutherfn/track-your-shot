package com.nicholas.rutherford.track.your.shot.feature.settings.accountinfo

import android.app.Application
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.lifecycle.ViewModel
import com.nicholas.rutherford.track.your.shot.data.room.repository.ActiveUserRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.ActiveUser
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.alert.AlertConfirmAndDismissButton
import com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer.RESET_SCREEN_DELAY_IN_MILLIS
import com.nicholas.rutherford.track.your.shot.feature.splash.StringsIds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AccountInfoViewModel(
    private val application: Application,
    private val activeUserRepository: ActiveUserRepository,
    private val navigation: AccountInfoNavigation,
    private val scope: CoroutineScope
) : ViewModel() {

    internal val accountInfoMutableStateFlow = MutableStateFlow(value = AccountInfoState())
    val accountInfoStateFlow = accountInfoMutableStateFlow.asStateFlow()

    internal var activeUser: ActiveUser? = null

    init {
        scope.launch { updateInitialState() }
    }

    internal suspend fun updateInitialState() {
        activeUserRepository.fetchActiveUser()?.let { user ->
            activeUser = user
            accountInfoMutableStateFlow.update { state ->
                state.copy(
                    email = user.email,
                    username = user.username
                )
            }
        }
    }

    fun onToolbarIconButtonClicked() {
        navigation.pop()
        scope.launch {
            delay(RESET_SCREEN_DELAY_IN_MILLIS)
            accountInfoMutableStateFlow.update { state ->
                state.copy(shouldEditAccountInfoDetails = false)
            }
        }
    }

    fun onNewEmailValueChanged(email: String) {
        accountInfoMutableStateFlow.update { state ->
            state.copy(newEmail = email)
        }
    }

    fun onConfirmNewEmailValueChanged(email: String) {
        accountInfoMutableStateFlow.update { state ->
            state.copy(confirmNewEmail = email)
        }
    }

    fun onNewUsernameValueChanged(username: String) {
        accountInfoMutableStateFlow.update { state ->
            state.copy(newUsername = username)
        }
    }

    fun onConfirmNewUsernameValueChanged(username: String) {
        accountInfoMutableStateFlow.update { state ->
            state.copy(confirmNewUsername = username)
        }
    }

    private fun onToolbarSecondaryClickedFromView() {
        accountInfoMutableStateFlow.update { state ->
            state.copy(
                shouldEditAccountInfoDetails = true,
                toolbarSecondaryImageVector = Icons.Filled.Save,
                toolbarTitleId = StringsIds.editAccountInfo
            )
        }
    }

    internal fun alert(description: String): Alert {
        return Alert(
            title = application.getString(StringsIds.errorEditingAccountInfo),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.gotIt)
            ),
            description = description
        )
    }

    internal fun buildAlertDescription(state: AccountInfoState): String? {
        when {
            state.newEmail.isEmpty() && state.email.isEmpty() && state.newUsername.isEmpty() && state.confirmNewUsername.isEmpty() -> {
                return "We have no changes"
            }
            state.newEmail.isNotEmpty() && state.email.isNotEmpty() && state.newUsername.isNotEmpty() && state.confirmNewUsername.isNotEmpty() -> {
                // we need to check each here
            }
        }
    }

    private fun onToolbarSecondaryClickedFromEdit() {
        val state = accountInfoMutableStateFlow.value

        navigation.enableProgress()

        if
    }

    fun onToolbarSecondaryIconButtonClicked() {
        if (!accountInfoMutableStateFlow.value.shouldEditAccountInfoDetails) {
            onToolbarSecondaryClickedFromView()
        } else {
            onToolbarSecondaryClickedFromEdit()
        }
    }
}
