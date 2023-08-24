package com.nicholas.rutherford.track.my.shot.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nicholas.rutherford.track.my.shot.data.room.repository.activeuser.ActiveUserRepository
import com.nicholas.rutherford.track.my.shot.firebase.util.existinguser.ExistingUserFirebase
import kotlinx.coroutines.launch

class HomeViewModel(
    private val navigation: HomeNavigation,
    private val existingUserFirebase: ExistingUserFirebase,
    private val activeUserRepository: ActiveUserRepository
) : ViewModel() {
    fun navigateTest() {
        viewModelScope.launch { activeUserRepository.deleteActiveUser() }

        existingUserFirebase.logOut()
        navigation.navigateToLogin()
    }
}
