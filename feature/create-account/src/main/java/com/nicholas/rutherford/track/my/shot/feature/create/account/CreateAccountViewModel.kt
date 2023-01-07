package com.nicholas.rutherford.track.my.shot.feature.create.account

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nicholas.rutherford.track.my.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.my.shot.data.shared.alert.AlertConfirmAndDismissButton
import com.nicholas.rutherford.track.my.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.my.shot.feature.splash.StringsIds
import com.nicholas.rutherford.track.my.shot.firebase.create.CreateFirebaseUserInfo
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CreateAccountViewModel(
    private val navigation: CreateAccountNavigation,
    private val application: Application,
    private val createFirebaseUserInfo: CreateFirebaseUserInfo
) : ViewModel() {

    internal var isUsernameEmptyOrNull: Boolean = false
    internal var isEmailEmptyOrNull: Boolean = false
    internal var isPasswordEmptyOrNull: Boolean = false
    internal var isTwoOrMoreFieldsEmptyOrNull: Boolean = false

    private val createAccountMutableStateFlow = MutableStateFlow(
        value = CreateAccountState(
            username = null,
            email = null,
            password = null
        )
    )
    val createAccountStateFlow = createAccountMutableStateFlow.asStateFlow()

    fun onBackButtonClicked() = navigation.pop()

    fun onCreateAccountButtonClicked() {
        val createAccountState = createAccountMutableStateFlow.value

        setIsUsernameEmptyOrNull(username = createAccountState.username)
        setIsEmailEmptyOrNull(email = createAccountState.email)
        setIsPasswordEmptyOrNull(password = createAccountState.password)
        setIsTwoOrMoreFieldsEmptyOrNull()

        validateFields()
    }

    internal fun setIsUsernameEmptyOrNull(username: String?) {
        username?.let { value ->
            isUsernameEmptyOrNull = value.isEmpty()
        } ?: run {
            isUsernameEmptyOrNull = true
        }
    }

    internal fun setIsEmailEmptyOrNull(email: String?) {
        email?.let { value ->
            isEmailEmptyOrNull = value.isEmpty()
        } ?: run {
            isEmailEmptyOrNull = true
        }
    }

    internal fun setIsPasswordEmptyOrNull(password: String?) {
        password?.let { value ->
            isPasswordEmptyOrNull = value.isEmpty()
        } ?: run {
            isPasswordEmptyOrNull = true
        }
    }

    internal fun setIsTwoOrMoreFieldsEmptyOrNull() {
        var counter = 0

        if (isUsernameEmptyOrNull) {
            counter += 1
        }
        if (isEmailEmptyOrNull) {
            counter += 1
        }
        if (isPasswordEmptyOrNull) {
            counter += 1
        }

        isTwoOrMoreFieldsEmptyOrNull = counter >= 2
    }

    internal fun validateFields() {
        val defaultAlert = Alert(
            onDismissClicked = {},
            title = application.getString(StringsIds.empty),
            dismissButton = AlertConfirmAndDismissButton(
                onButtonClicked = {},
                buttonText = application.getString(StringsIds.gotIt)
            )
        )

        if (isTwoOrMoreFieldsEmptyOrNull) {
            navigation.alert(
                alert = defaultAlert.copy(
                    title = application.getString(StringsIds.emptyFields),
                    description = application.getString(StringsIds.multipleFieldsAreRequiredThatAreNotEnteredPleaseEnterAllFields)
                )
            )
        } else if (isUsernameEmptyOrNull) {
            navigation.alert(
                alert = defaultAlert.copy(
                    title = application.getString(StringsIds.emptyField),
                    description = application.getString(StringsIds.usernameIsRequiredPleaseEnterAUsernameToCreateAAccount)
                )
            )
        } else if (isEmailEmptyOrNull) {
            navigation.alert(
                alert = defaultAlert.copy(
                    title = application.getString(StringsIds.emptyField),
                    description = application.getString(
                        StringsIds.emailIsRequiredPleaseEnterAEmailToCreateAAccount
                    )
                )
            )
        } else if (isPasswordEmptyOrNull) {
            navigation.alert(
                alert = defaultAlert.copy(
                    title = application.getString(StringsIds.emptyField),
                    description = application.getString(StringsIds.passwordIsRequiredPleaseEnterAPasswordToCreateAAccount)
                )
            )
        } else {
            // test code not final
            viewModelScope.launch {
                navigation.enableProgress(
                    progress = Progress(
                        onDismissClicked = {}
                    )
                )

                delay(timeMillis = 4000L)
                navigation.disableProgress()
            }
        }
    }

    internal fun onUsernameValueChanged(newUsername: String) {
        createAccountMutableStateFlow.value = createAccountStateFlow.value.copy(username = newUsername)
    }

    internal fun onEmailValueChanged(newEmail: String) {
        createAccountMutableStateFlow.value = createAccountStateFlow.value.copy(email = newEmail)
    }

    internal fun onPasswordValueChanged(newPassword: String) {
        createAccountMutableStateFlow.value = createAccountStateFlow.value.copy(password = newPassword)
    }
}
