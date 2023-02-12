package com.nicholas.rutherford.track.my.shot.feature.home

import androidx.lifecycle.ViewModel
import com.nicholas.rutherford.track.my.shot.navigation.Navigator
import com.nicholas.rutherford.track.my.shot.shared.preference.create.CreateSharedPreferences

class HomeViewModel(private val navigator: Navigator, private val createSharedPreferences: CreateSharedPreferences) : ViewModel() {

    init {
        createSharedPreferences.createAccountHasBeenCreatedPreference(value = true)
    }
    fun navigateTest() {
        // todo
    }
}
