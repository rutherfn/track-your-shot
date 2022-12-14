package com.nicholas.rutherford.track.my.shot.feature.create.account

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CreateAccountViewModel(private val navigation: CreateAccountNavigation) : ViewModel() {

    private val createAccountMutableStateFlow = MutableStateFlow(value = CreateAccountState(email = null))
    val createAccountStateFlow = createAccountMutableStateFlow.asStateFlow()

    fun onBackButtonClicked() = navigation.pop()
}
