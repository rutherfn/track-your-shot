package com.nicholas.rutherford.track.your.shot.feature.settings.accountinfo

import android.app.Application
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import com.nicholas.rutherford.track.your.shot.data.room.repository.ActiveUserRepository
import com.nicholas.rutherford.track.your.shot.data.test.room.TestActiveUser
import com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer.RESET_SCREEN_DELAY_IN_MILLIS
import com.nicholas.rutherford.track.your.shot.feature.splash.StringsIds
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class AccountInfoViewModelTest {

    private lateinit var viewModel: AccountInfoViewModel

    private var application = mockk<Application>(relaxed = true)

    private var activeUserRepository = mockk<ActiveUserRepository>(relaxed = true)
    private var navigation = mockk<AccountInfoNavigation>(relaxed = true)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    private val scope = CoroutineScope(SupervisorJob() + testDispatcher)

    @BeforeEach
    fun beforeEach() {
        viewModel = AccountInfoViewModel(
            application = application,
            activeUserRepository = activeUserRepository,
            navigation = navigation,
            scope = scope
        )
    }

    @Nested
    inner class UpdateInitialState {

        @Test
        fun `when fetch active user returns back null should not update state`() = runTest {
            viewModel.activeUser = null

            coEvery { activeUserRepository.fetchActiveUser() } returns null

            viewModel.updateInitialState()

            Assertions.assertEquals(viewModel.activeUser, null)
            Assertions.assertEquals(
                viewModel.accountInfoMutableStateFlow.value,
                AccountInfoState()
            )
        }

        @Test
        fun `when fetch active user returns back a user should update state`() = runTest {
            val activeUser = TestActiveUser().create()

            viewModel.activeUser = null

            coEvery { activeUserRepository.fetchActiveUser() } returns activeUser

            viewModel.updateInitialState()

            Assertions.assertEquals(viewModel.activeUser, activeUser)
            Assertions.assertEquals(
                viewModel.accountInfoMutableStateFlow.value,
                AccountInfoState(email = activeUser.email, username = activeUser.username)
            )
        }
    }

    @Test
    fun `on toolbar icon button clicked`() = runTest {
        viewModel.onToolbarIconButtonClicked()

        verify { navigation.pop() }

        delay(RESET_SCREEN_DELAY_IN_MILLIS)

        Assertions.assertEquals(
            viewModel.accountInfoMutableStateFlow.value,
            AccountInfoState(shouldEditAccountInfoDetails = false)
        )
    }

    @Test
    fun `on new email value changed`() {
        val email = "emailTest"

        viewModel.onNewEmailValueChanged(email = email)

        Assertions.assertEquals(
            viewModel.accountInfoMutableStateFlow.value,
            AccountInfoState(newEmail = email)
        )
    }

    @Test
    fun `on confirm new email value changed`() {
        val email = "emailTest"

        viewModel.onConfirmNewEmailValueChanged(email = email)

        Assertions.assertEquals(
            viewModel.accountInfoMutableStateFlow.value,
            AccountInfoState(confirmNewEmail = email)
        )
    }

    @Test
    fun `on new username value changed`() {
        val username = "usernameTest12121"

        viewModel.onNewUsernameValueChanged(username = username)

        Assertions.assertEquals(
            viewModel.accountInfoMutableStateFlow.value,
            AccountInfoState(newUsername = username)
        )
    }

    @Test
    fun `on confirm new username value changed`() {
        val username = "usernameTest12121"

        viewModel.onConfirmNewUsernameValueChanged(username = username)

        Assertions.assertEquals(
            viewModel.accountInfoMutableStateFlow.value,
            AccountInfoState(confirmNewUsername = username)
        )
    }

    @Nested
    inner class OnToolbarSecondaryIconButtonClicked {

        @Test
        fun `when shouldEditAccountInfoDetails returns false should update state`() {
            viewModel.accountInfoMutableStateFlow.value = AccountInfoState(shouldEditAccountInfoDetails = false)

            viewModel.onToolbarSecondaryIconButtonClicked()

            Assertions.assertEquals(
                viewModel.accountInfoMutableStateFlow.value,
                AccountInfoState(
                    shouldEditAccountInfoDetails = true,
                    toolbarSecondaryImageVector = Icons.Filled.Save,
                    toolbarTitleId = StringsIds.editAccountInfo
                )
            )
        }
    }
}
