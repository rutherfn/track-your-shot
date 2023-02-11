package com.nicholas.rutherford.track.my.shot.feature.create.account

import android.app.Application
import com.nicholas.rutherford.track.my.shot.feature.create.account.authentication.AuthenticationNavigation
import com.nicholas.rutherford.track.my.shot.feature.create.account.authentication.AuthenticationViewModel
import com.nicholas.rutherford.track.my.shot.firebase.read.ReadFirebaseUserInfo
import com.nicholas.rutherford.track.my.shot.firebase.util.AuthenticationFirebase
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AuthenticationViewModelTest {

    lateinit var viewModel: AuthenticationViewModel

    private var readFirebaseUserInfo = mockk<ReadFirebaseUserInfo>(relaxed = true)
    private var navigation = mockk<AuthenticationNavigation>(relaxed = true)

    private val application = mockk<Application>(relaxed = true)

    private val authenticationFirebase = mockk<AuthenticationFirebase>(relaxed = true)

    @BeforeEach
    fun beforeEach() {
        viewModel = AuthenticationViewModel(
            readFirebaseUserInfo = readFirebaseUserInfo,
            navigation = navigation,
            application = application,
            authenticationFirebase = authenticationFirebase
        )
    }

    @Test
    fun `on navigate close should call navigation alert`() {
        viewModel.onNavigateClose()

        verify { navigation.alert(alert = any()) }
    }

    @Test
    fun `on alert button clicked should call navigation finish`() {
        viewModel.onAlertConfirmButtonClicked()

        verify { navigation.finish() }
    }
}
