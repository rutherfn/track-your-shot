package com.nicholas.rutherford.track.my.shot.feature.home

import androidx.lifecycle.ViewModel
import com.nicholas.rutherford.track.my.shot.navigation.Navigator

class HomeViewModel(private val navigator: Navigator) : ViewModel() {

    fun navigateToSplash() = navigator.navigateTo(Navigator.NavTarget.SPLASH)
}
