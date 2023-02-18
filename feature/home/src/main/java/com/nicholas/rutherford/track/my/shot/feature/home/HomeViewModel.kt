package com.nicholas.rutherford.track.my.shot.feature.home

import androidx.lifecycle.ViewModel
import com.nicholas.rutherford.track.my.shot.firebase.util.existinguser.ExistingUserFirebase

class HomeViewModel(
    private val navigation: HomeNavigation,
    private val existingUserFirebase: ExistingUserFirebase
) : ViewModel() {
    fun navigateTest() {
        existingUserFirebase.logOut()
        navigation.navigateToLogin()
    }
}
