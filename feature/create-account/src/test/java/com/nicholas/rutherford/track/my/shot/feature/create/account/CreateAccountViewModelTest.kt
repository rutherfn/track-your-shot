package com.nicholas.rutherford.track.my.shot.feature.create.account

import android.app.Application
import com.nicholas.rutherford.track.my.shot.firebase.create.CreateFirebaseUserInfo
import com.nicholas.rutherford.track.my.shot.helper.network.Network
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class CreateAccountViewModelTest {

    private lateinit var viewModel: CreateAccountViewModel

    private var navigation = mockk<CreateAccountNavigation>(relaxed = true)
    private val application = mockk<Application>(relaxed = true)

    private val network = mockk<Network>(relaxed = true)

    private val createFirebaseUserInfo = mockk<CreateFirebaseUserInfo>(relaxed = true)

    private val state = CreateAccountState(username = null, email = null, password = null)

    @BeforeEach
    fun beforeEach() {
        viewModel = CreateAccountViewModel(navigation = navigation, application = application, network = network, createFirebaseUserInfo = createFirebaseUserInfo)
    }

    @Test
    fun initializeCreateAccountState() {
        Assertions.assertEquals(
            viewModel.createAccountStateFlow.value, state
        )
    }

    @Test fun `on back button clicked should pop`() {
        viewModel.onBackButtonClicked()

        verify { navigation.pop() }
    }

    @Nested
    inner class SetIsUsernameEmptyOrNull {

        @Test fun `when username is null should set isUsernameEmptyOrNull to true`() {
            viewModel.isUsernameEmptyOrNull = false

            viewModel.setIsUsernameEmptyOrNull(username = null)

            Assertions.assertEquals(
                viewModel.isUsernameEmptyOrNull,
                true
            )
        }

        @Test fun `when username is not null with a actual value should set isUsernameEmptyOrNull to false`() {
            viewModel.isUsernameEmptyOrNull = true

            viewModel.setIsUsernameEmptyOrNull(username = "username")

            Assertions.assertEquals(
                viewModel.isUsernameEmptyOrNull,
                false
            )
        }

        @Test fun `when username is not null and empty should set isUsernameEmptyOrNull to true`() {
            viewModel.isUsernameEmptyOrNull = false

            viewModel.setIsUsernameEmptyOrNull(username = "")

            Assertions.assertEquals(
                viewModel.isUsernameEmptyOrNull,
                true
            )
        }
    }

    @Nested
    inner class SetIsEmailEmptyOrNull {

        @Test fun `when email is null should set isEmailEmptyOrNull to true`() {
            viewModel.isEmailEmptyOrNull = false

            viewModel.setIsEmailEmptyOrNull(email = null)

            Assertions.assertEquals(
                viewModel.isEmailEmptyOrNull,
                true
            )
        }

        @Test fun `when email is not null with a actual value should set isEmailEmptyOrNull to false`() {
            viewModel.isEmailEmptyOrNull = true

            viewModel.setIsEmailEmptyOrNull(email = "email@gmail.com")

            Assertions.assertEquals(
                viewModel.isEmailEmptyOrNull,
                false
            )
        }

        @Test fun `when email is not null and empty should set isEmailEmptyOrNull to true`() {
            viewModel.isEmailEmptyOrNull = false

            viewModel.setIsEmailEmptyOrNull(email = "")

            Assertions.assertEquals(
                viewModel.isEmailEmptyOrNull,
                true
            )
        }
    }

    @Nested
    inner class SetIsPasswordEmptyOrNull {

        @Test fun `when password is null should set isPasswordEmptyOrNull is true`() {
            viewModel.isPasswordEmptyOrNull = false

            viewModel.setIsPasswordEmptyOrNull(password = null)

            Assertions.assertEquals(
                viewModel.isPasswordEmptyOrNull,
                true
            )
        }

        @Test fun `when password is not null with a actual value should set isPasswordEmptyOrNull to false`() {
            viewModel.isPasswordEmptyOrNull = true

            viewModel.setIsPasswordEmptyOrNull(password = "password")

            Assertions.assertEquals(
                viewModel.isPasswordEmptyOrNull,
                false
            )
        }

        @Test fun `when password is not null and empty should set isPasswordEmptyOrNull to true`() {
            viewModel.isPasswordEmptyOrNull = false

            viewModel.setIsPasswordEmptyOrNull(password = "")

            Assertions.assertEquals(
                viewModel.isPasswordEmptyOrNull,
                true
            )
        }
    }

    @Nested
    inner class SetIsTwoOrMoreFieldsEmptyOrNull {

        @Test fun `when none of the fields are empty or null isTwoMoreFieldsEmptyOrNull should be set to false`() {
            viewModel.isUsernameEmptyOrNull = false
            viewModel.isEmailEmptyOrNull = false
            viewModel.isPasswordEmptyOrNull = false
            viewModel.isTwoOrMoreFieldsEmptyOrNull = true

            viewModel.setIsTwoOrMoreFieldsEmptyOrNull()

            Assertions.assertEquals(
                viewModel.isTwoOrMoreFieldsEmptyOrNull,
                false
            )
        }

        @Test fun `when 1 of the fields are empty or null isTwoMoreFieldsEmptyOrNull should be set to false`() {
            viewModel.isUsernameEmptyOrNull = true
            viewModel.isEmailEmptyOrNull = false
            viewModel.isPasswordEmptyOrNull = false
            viewModel.isTwoOrMoreFieldsEmptyOrNull = true

            viewModel.setIsTwoOrMoreFieldsEmptyOrNull()

            Assertions.assertEquals(
                viewModel.isTwoOrMoreFieldsEmptyOrNull,
                false
            )
        }

        @Test fun `when 2 of the fields are empty or null isTwoMoreFieldsEmptyOrNull should be set to true`() {
            viewModel.isUsernameEmptyOrNull = true
            viewModel.isEmailEmptyOrNull = true
            viewModel.isPasswordEmptyOrNull = false
            viewModel.isTwoOrMoreFieldsEmptyOrNull = false

            viewModel.setIsTwoOrMoreFieldsEmptyOrNull()

            Assertions.assertEquals(
                viewModel.isTwoOrMoreFieldsEmptyOrNull,
                true
            )
        }

        @Test fun `when all of the fields are empty or null isTwoMoreFieldsEmptyOrNull should be set to true`() {
            viewModel.isUsernameEmptyOrNull = true
            viewModel.isEmailEmptyOrNull = true
            viewModel.isPasswordEmptyOrNull = true
            viewModel.isTwoOrMoreFieldsEmptyOrNull = false

            viewModel.setIsTwoOrMoreFieldsEmptyOrNull()

            Assertions.assertEquals(
                viewModel.isTwoOrMoreFieldsEmptyOrNull,
                true
            )
        }
    }

    @Nested
    inner class ValidateFields {

        @Test
        fun `when isDeviceConnectedToInternet is set to true should navigate to show alert`() {
            every { network.isDeviceConnectedToInternet() } returns false

            viewModel.validateFields()

            verify { navigation.alert(alert = any()) }
        }

        @Test
        fun `when isTwoOrMoreFieldsEmptyOrNull is set to true should navigate to show alert`() {
            every { network.isDeviceConnectedToInternet() } returns true

            viewModel.isTwoOrMoreFieldsEmptyOrNull = false
            viewModel.isUsernameEmptyOrNull = false
            viewModel.isEmailEmptyOrNull = false
            viewModel.isPasswordEmptyOrNull = false
            viewModel.isTwoOrMoreFieldsEmptyOrNull = true

            viewModel.validateFields()

            verify { navigation.alert(alert = any()) }
        }

        @Test
        fun `when isUsernamEmptyOrNull is set to true should navigate to show alert`() {
            every { network.isDeviceConnectedToInternet() } returns true

            viewModel.isTwoOrMoreFieldsEmptyOrNull = false
            viewModel.isUsernameEmptyOrNull = true
            viewModel.isEmailEmptyOrNull = false
            viewModel.isPasswordEmptyOrNull = false

            viewModel.validateFields()

            verify { navigation.alert(alert = any()) }
        }

        @Test
        fun `when isEmailEmptyOrNull is set to true should navigate to show alert`() {
            every { network.isDeviceConnectedToInternet() } returns true

            viewModel.isTwoOrMoreFieldsEmptyOrNull = false
            viewModel.isUsernameEmptyOrNull = false
            viewModel.isEmailEmptyOrNull = true
            viewModel.isPasswordEmptyOrNull = false

            viewModel.validateFields()

            verify { navigation.alert(alert = any()) }
        }

        @Test
        fun `when isPasswordEmptyOrNull is set to true should navigate to show alert`() {
            every { network.isDeviceConnectedToInternet() } returns true

            viewModel.isTwoOrMoreFieldsEmptyOrNull = false
            viewModel.isUsernameEmptyOrNull = false
            viewModel.isEmailEmptyOrNull = false
            viewModel.isPasswordEmptyOrNull = true

            viewModel.validateFields()

            verify { navigation.alert(alert = any()) }
        }

        @Test
        fun `when all validation fields are set to false should not navigate to show alert`() {
            every { network.isDeviceConnectedToInternet() } returns true

            viewModel.isTwoOrMoreFieldsEmptyOrNull = false
            viewModel.isUsernameEmptyOrNull = false
            viewModel.isEmailEmptyOrNull = false
            viewModel.isPasswordEmptyOrNull = false

            viewModel.validateFields()

            verify(exactly = 0) { navigation.alert(alert = any()) }
        }
    }

    @Test fun `on user name value changed should update username state value`() {
        val usernameTest = "user name 1"

        viewModel.onUsernameValueChanged(newUsername = usernameTest)

        Assertions.assertEquals(
            viewModel.createAccountStateFlow.value,
            state.copy(username = usernameTest)
        )
    }

    @Test fun `on email value changed should update email state value`() {
        val emailTest = "new email"

        viewModel.onEmailValueChanged(newEmail = emailTest)

        Assertions.assertEquals(
            viewModel.createAccountStateFlow.value,
            state.copy(email = emailTest)
        )
    }

    @Test fun `on password value changed should update password state value`() {
        val passwordTest = "new password"

        viewModel.onPasswordValueChanged(newPassword = passwordTest)

        Assertions.assertEquals(
            viewModel.createAccountStateFlow.value,
            state.copy(password = passwordTest)
        )
    }
}
