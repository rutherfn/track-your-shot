package com.nicholas.rutherford.track.my.shot.feature.home

import androidx.lifecycle.ViewModel
import com.nicholas.rutherford.track.my.shot.navigation.NavigationActions
import com.nicholas.rutherford.track.my.shot.navigation.Navigator

class HomeViewModel(private val navigator: Navigator) : ViewModel() {
    fun navigateTest() = navigator.navigate(NavigationActions.HomeScreen.splash())
}
