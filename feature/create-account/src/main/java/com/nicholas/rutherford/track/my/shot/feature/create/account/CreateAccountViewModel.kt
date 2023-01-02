package com.nicholas.rutherford.track.my.shot.feature.create.account

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CreateAccountViewModel(private val navigation: CreateAccountNavigation) : ViewModel() {

    internal var isUsernameEmptyOrNull: Boolean = false
    internal var isEmailEmptyOrNull: Boolean = false
    internal var isPasswordEmptyOrNull: Boolean = false

    private val createAccountMutableStateFlow = MutableStateFlow(
        value = CreateAccountState(
            username = null,
            email = null,
            password = null,
            alert = null
        )
    )
    val createAccountStateFlow = createAccountMutableStateFlow.asStateFlow()

    fun onBackButtonClicked() = navigation.pop()

    fun onCreateAccountButtonClicked() {
        val createAccountState = createAccountMutableStateFlow.value

        setIsUsernameEmptyOrNull(username = createAccountState.username)
        setIsEmailEmptyOrNull(email = createAccountState.email)
        setIsPasswordEmptyOrNull(password = createAccountState.password)
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

    fun onUsernameValueChanged(newUsername: String) {
        createAccountMutableStateFlow.value = createAccountStateFlow.value.copy(username = newUsername)
    }

    fun onEmailValueChanged(newEmail: String) {
        createAccountMutableStateFlow.value = createAccountStateFlow.value.copy(email = newEmail)
    }

    fun onPasswordValueChanged(newPassword: String) {
        createAccountMutableStateFlow.value = createAccountStateFlow.value.copy(password = newPassword)
    }
}
